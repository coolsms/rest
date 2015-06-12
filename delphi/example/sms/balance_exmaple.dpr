program balance_exmaple;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils;

var
  coolsms: handler;
  jsonObject: TJSONObject;

begin
  try
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52B122858C04F', '8BAAE5A5926C9AE081920A085BFB835A');

    // http://www.coolsms.co.kr/SMS_API#GETbalance 참조
    jsonObject := coolsms.request('balance', Nil, 'sms');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
      Writeln('cash : ' + jsonObject.Get('cash').JsonValue.ToString);
      Writeln('point : ' + jsonObject.Get('point').JsonValue.ToString);
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
