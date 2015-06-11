program list_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  data: TStringList;
  jsonObject: TJSONObject;
  jsonArray: TJSONArray;
  jsonObjectData: TJSONObject;
  i: integer;

begin
  try
    // http://www.coolsms.co.kr/SenderID_API#GETlist 참조

    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    jsonObject := coolsms.request('list', Nil, 'senderid');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
      jsonArray := JsonObject.Get('data').JsonValue as TJSONArray;

      Writeln('total_count : ' + jsonObject.Get('total_count').JsonValue.ToString);
      Writeln('list_count : ' + jsonObject.Get('list_count').JsonValue.ToString);
      Writeln('page : ' + jsonObject.Get('page').JsonValue.ToString);

      for i := 0 to jsonArray.Size - 1 do
      begin
          jsonObjectData := TJSONObject.Create;
          jsonObjectData := jsonArray.Get(i) as TJSONObject;

          Writeln('-----------------------------------------');
          Writeln('idno : ' + jsonObjectData.Get('idno').JsonValue.ToString);
          Writeln('phone_number : ' + jsonObjectData.Get('phone_number').JsonValue.ToString);
          Writeln('flag_default : ' + jsonObjectData.Get('flag_default').JsonValue.ToString);
          Writeln('updatetime : ' + jsonObjectData.Get('updatetime').JsonValue.ToString);
          Writeln('regdate : ' + jsonObjectData.Get('regdate').JsonValue.ToString);
      end;
    end
    else
    begin
      Writeln('실패');
      Writeln('code : ' + jsonObject.Get('code').JsonValue.ToString);
      Writeln('message : ' + jsonObject.Get('message').JsonValue.ToString);
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
