program register_example;

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
    // http://www.coolsms.co.kr/SenderID_API#POSTregister 참조

    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    // data 설정
    data := TStringList.create;
    data.Values['phone'] := '01090683469'; // 발신번호로 등록할 전화번호를 입력해주세요.

    jsonObject := coolsms.postRequest('register', data, 'senderid');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
      Writeln('handle_key : ' + jsonObject.Get('handle_key').JsonValue.ToString);
      Writeln('ars_number : ' + jsonObject.Get('ars_number').JsonValue.ToString);
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
