program get_default_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  data: TStringList;
  jsonObject: TJSONObject;

begin
  try
    // http://www.coolsms.co.kr/SenderID_API#POSTset_default 참조

    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    jsonObject := coolsms.request('get_default', Nil, 'senderid');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
      Writeln('handle_key : ' + jsonObject.Get('handle_key').JsonValue.ToString);
      Writeln('phone_number : ' + jsonObject.Get('phone_number').JsonValue.ToString);
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
