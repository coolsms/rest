program send_extension_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in '..\..\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  jsonObject: TJSONObject;
  extensionObject: TJSONObject;
  extensionArray: TJSONArray;
  data: TStringList;

begin
  try
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS55882FB7DE511A', '4FB5FF82B9AB7D0E0AEB840D403DE0F74');

    // 개별 메시지 보내기
    extensionObject := TJSONObject.Create;
    extensionArray := TJSONArray.Create;

    // message 1 parameters
    extensionObject.AddPair('type', 'sms');
    extensionObject.AddPair('to', '01000000003');
    extensionObject.AddPair('text', 'sms_test');
    extensionArray.Add(extensionObject);

    // message 2 parameters
    extensionObject := TJSONObject.Create;
    extensionObject.AddPair('type', 'sms');
    extensionObject.AddPair('to', '01000000000, 010000000001');
    extensionObject.AddPair('text', 'sms_test2');
    extensionArray.Add(extensionObject);

    // input extension_data into string_list
    data := TStringList.create;
    data.Values['extension'] := extensionArray.ToString;

    // send Messages
    jsonObject := coolsms.postRequest('send', data, 'sms');
    if strToBool(jsonObject.GetValue('status').ToString) = TRUE then
    begin
      Writeln('성공');
      Writeln('group_id : ' + jsonObject.Get('group_id').JsonValue.ToString);
      Writeln('success_count : ' + jsonObject.Get('success_count').JsonValue.ToString);
      Writeln('error_count : ' + jsonObject.Get('error_count').JsonValue.ToString);
      Writeln('result_code : ' + jsonObject.Get('result_code').JsonValue.ToString);
      Writeln('result_message : ' + jsonObject.Get('result_message').JsonValue.ToString);
    end
    else
    begin
      Writeln('실패');
      if jsonObject.Get('code').Equals(Nil) = FALSE then Writeln('code : ' + jsonObject.Get('code').JsonValue.ToString);
      if jsonObject.Get('message').Equals(Nil) = FALSE then Writeln('message : ' + jsonObject.Get('message').JsonValue.ToString);
    end;

    jsonObject.Free;

    Writeln('-----------------------------------------');
    Writeln('Press <enter> to quit...');
    Readln;
  except
    on E: Exception do
      Writeln(E.ClassName, ': ', E.Message);
  end;
end.
