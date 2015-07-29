program set_default_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in '..\..\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  data: TStringList;
  jsonObject: TJSONObject;

begin
  try
    // http://www.coolsms.co.kr/SenderID_API#POSTset_default 참조

    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('2CS35588FB7DE511A', '2FB5FF8B9XB7D0E0AEB840D403DE0F74');

    // parameters
    data := TStringList.create;
    data.Values['handle_key'] := 'SID55793FC12DDA2'; // register 호출 후 리턴받은 핸들값

    // default 발신번호등록 요청
    jsonObject := coolsms.postRequest('set_default', data, 'senderid');
    if strToBool(jsonObject.GetValue('status').ToString) = TRUE then
    begin
      Writeln('성공');
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
