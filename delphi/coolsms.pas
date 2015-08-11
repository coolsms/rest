unit coolsms;

{ handler class interface�� ����� �Ʒ� implementation���� ���� }
interface
  uses
    System.Json, IdHash, IdHMACMD5, IDGlobal, DateUtils, IdException, IdHTTP, IdSSLOpenSSL, IdMultipartFormData, Classes, System.SysUtils;

  type
    handler = class
    private
     class var api_key : String;
     class var api_secret : String;
    public
	   { set api_key and api_secret }
     class procedure setApiKey(key: String; secret: String);

     { request(post) }
     class function postRequest(method: String; data: TStringList; api_type: String): TJSONObject;
     { request(get) }
     class function request(method: String; data: TStringList; api_type: String): TJSONObject;
   end;

implementation
  { api request�� ���� Adata  ���� md5�� encrypt }
  function hashHmacMd5(const AData, AKey: string): string;
  var
     HMACMD5: TIdHMACMD5;
     KeyBytes: TIdBytes;
     DataBytes: TIdBytes;
     ResBytes: TIdBytes;
     i: integer;

  begin
     HMACMD5 := TIdHMACMD5.Create;

     try
       HMACMD5.Key := ToBytes(AKey);
       ResBytes := HMACMD5.HashValue(ToBytes(AData));

       Result := '';
       for i := low(ResBytes) to high(ResBytes) do
         Result := Result + inttohex(ResBytes[i], 2);

     finally
       HMACMD5.Free;
     end;
  end;

  { get random string }
  function randomString(): string;
  var
    str: string;
  begin
    Randomize;
    //string with all possible chars
    str := 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
    Result := '';
    repeat
      Result := Result + str[Random(Length(str)) + 1];
    until (Length(Result) = 10)
  end;

  { image�� ������ string data ���� data�� delimiter�� �и��ؼ� �ִ´� }
  function split(delimiter: Char; input: string; data: TIdMultiPartFormDataStream): TIdMultiPartFormDataStream ;
  var
    strings: TStringList;
  begin
    strings := TSTringList.Create;
    strings.Delimiter := delimiter;
    strings.DelimitedText := input;

    // �ش� string data�� image�� �ƴϸ� data�� add
    if strings[0] <> 'image' then
    begin
      data.AddFormField(strings[0], strings[1]);
    end;

    strings.Clear;
    Result := data;
  end;

  { request(POST) }
  class function handler.postRequest(method: String; data: TStringList; api_type: String): TJSONObject;
  var
    params: TStringList;
    http: TIdHTTP;
    idSSLIOHandlerSocketOpenSSL: TIdSSLIOHandlerSocketOpenSSL;
    jsonObject: TJSONObject;
    signature: String;
    salt: String;
    response: String;
    timestamp: String;
    url: String;
    multi_data: TIdMultiPartFormDataStream;
    data_string: String;

  begin
    params := TStringList.Create;
    http := TIdHTTP.Create();
    salt := randomString();
    jsonObject := TJSonObject.Create;
    idSSLIOHandlerSocketOpenSSL := TIdSSLIOHandlerSocketOpenSSL.Create;
    url := 'https://rest1.coolsms.co.kr/';

    try
      // currrent unix time
      timestamp := IntToStr(DateTimetoUnix(IncHour(Now, -9)));
      signature := hashHmacMd5(timestamp + salt, handler.api_secret);

      data.Values['api_key'] := handler.api_key;
      data.Values['salt'] := salt;
      data.Values['signature'] := signature;
      data.Values['timestamp'] := timestamp;

      if method.Equals('send') = TRUE then
      begin
        // get Development Lang & version
        if CompilerVersion.ToString() = '29' then data.Values['dev_lang'] := 'XE8';
        if CompilerVersion.ToString() = '28' then data.Values['dev_lang'] := 'XE7';
        if CompilerVersion.ToString() = '27' then data.Values['dev_lang'] := 'XE6';
        if CompilerVersion.ToString() = '26' then data.Values['dev_lang'] := 'XE5';
        if CompilerVersion.ToString() = '25' then data.Values['dev_lang'] := 'XE4';
        data.Values['dev_lang'] := 'Delphi ' + data.Values['dev_lang'];

        // get OS Version
        if data.Values['os_platform'].IsEmpty = TRUE then
        begin
          data.Values['os_platform'] := IntToStr(Win32MajorVersion) + '.' + IntToStr(Win32MinorVersion);
          if data.Values['os_platform'] = '5.0' then data.Values['os_platform'] := '2000';
          if data.Values['os_platform'] = '5.1' then data.Values['os_platform'] := 'XP';
          if data.Values['os_platform'] = '5.2' then data.Values['os_platform'] := '2003 server or XP64';
          if data.Values['os_platform'] = '6.0' then data.Values['os_platform'] := 'Vista/2008 server';
          if data.Values['os_platform'] = '6.1' then data.Values['os_platform'] := '7/2008 server R2';
          data.Values['os_platform'] := 'Windows ' + data.Values['os_platform'];
        end;

        data.Values['sdk_version'] := 'Delphi SDK 1.0';
      end;

      // api_type�� ���� ��û url ����
      if (api_type = '') or (api_type = 'sms') then
        url := url + 'sms/1.5/' + method
      else
        url := url + api_type + '/1/' + method;

      http.IOHandler := idSSLIOHandlerSocketOpenSSL;
      http.Request.ContentType := 'application/x-www-form-urlencoded';
      http.Request.UserAgent := 'Delphi';

      // image�� ������ TStringList�� �ƴ� TIdMultiPartFormDataStream���� data�� �����Ѵ�.
      if data.Values['image'].IsEmpty = FALSE then
      begin
        multi_data := TIdMultiPartFormDataStream.Create;
        for data_string in data do
          multi_data := split('=', data_string, multi_data);

        multi_data.AddFile('image', data.Values['image']);
        response := http.Post(url, multi_data);
      end
      else
      begin
        response := http.Post(url, data);
      end;

      if response.Equals('') = FALSE then
      begin
        // response�� �ִٸ�
        jsonObject := TJSONObject.ParseJSONValue(response) as TJSONObject;
      end;
      jsonObject.AddPair(TJSONPair.Create('status', TJSONTrue.Create));
      Result := jsonObject;
    except
      // http exception ó��
      on E: EIdHTTPProtocolException  do
        begin
          jsonObject := TJSONObject.ParseJSONValue(E.ErrorMessage) as TJSONObject;
          jsonObject.AddPair(TJSONPair.Create('status', TJSONFalse.Create));
          Result := jsonObject;
        end;
      // �׿� exception ó��
      on E: Exception do
        begin
          jsonObject.AddPair(TJSONPair.Create('message', E.Message));
          jsonObject.AddPair(TJSONPair.Create('status', TJSONFalse.Create));
          Result := jsonObject;
        end;
    end;

    http.Free;
  end;

  { request(GET) }
  class function handler.request(method: String; data: TStringList; api_type: String): TJSONObject;
  var
  http: TIdHTTP;
  jsonObject: TJSONObject;
  url: String;
  signature: String;
  salt: String;
  timestamp: String;
  data_string: String;
  response: String;

  begin
    http := TIdHTTP.Create();
    url := 'https://rest1.coolsms.co.kr/';
    salt := randomString();
    jsonObject := TJSONObject.Create;

    try
      // currrent unix time
      timestamp := IntToStr(DateTimetoUnix(IncHour(Now, -9)));
      signature := hashHmacMd5(timestamp + salt, handler.api_secret);

      if data = nil then data := TStringList.Create;

      // �⺻����
      data.Values['api_key'] := handler.api_key;
      data.Values['salt'] := salt;
      data.Values['signature'] := signature;
      data.Values['timestamp'] := timestamp;

      // api_type�� ���� ��û url ����
      if (api_type = '') or (api_type = 'sms') then
        url := url + 'sms/1.5/' + method + '?'
      else
        url := url + api_type + '/1/' + method + '?';

      for data_string in data Do
        url := url + data_string + '&';

      delete(url, length(url), 1);
      response := http.get(url);
      jsonObject := TJSONObject.ParseJSONValue(response) as TJSONObject;
      jsonObject.AddPair(TJSONPair.Create('status', TJSONTrue.Create));
      Result := jsonObject;
    except
      on E: EIdHTTPProtocolException  do
        begin
          //jsonObject.AddPair(TJSONPair.Create('John','Doe'));
          jsonObject := TJSONObject.ParseJSONValue(E.ErrorMessage) as TJSONObject;
          jsonObject.AddPair(TJSONPair.Create('status', TJSONFalse.Create));
          Result := jsonObject;
        end;
      on E: Exception do
        begin
          jsonObject.AddPair(TJSONPair.Create('message', E.Message));
          jsonObject.AddPair(TJSONPair.Create('status', TJSONFalse.Create));
          Result := jsonObject;
        end;
    end;

    http.Free;
  end;

  { set api_key and api_secret }
  class procedure handler.setApiKey(key: String; secret: String);
  begin
    api_key := key;
    api_secret := secret;
  end;
end.
