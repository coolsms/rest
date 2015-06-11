program cancel_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  jsonObject: TJSONObject;
  data: TStringList;

begin
  try
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    // http://www.coolsms.co.kr/SMS_API#POSTcancel 참조
    data := TStringList.create;
    data.Values['mid'] := 'R2M557004D2DD1EE'; // 메시지ID
    // data.Values['gid'] := ''; // 그룹ID

    jsonObject := coolsms.postRequest('cancel', data, 'sms');
    Writeln(jsonObject.Get('code').Equals(Nil));

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

