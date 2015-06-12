program set_default_example;

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
    coolsms.setApiKey('NCS52B122858C04F', '8BAAE5A5926C9AE081920A085BFB835A');

    // data 설정
    data := TStringList.create;
    data.Values['handle_key'] := 'SID55793FC12DDA2'; // register 호출 후 리턴받은 핸들값

    jsonObject := coolsms.postRequest('set_default', data, 'senderid');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
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
