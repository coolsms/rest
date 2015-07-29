program cancel_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in '..\..\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  jsonObject: TJSONObject;
  data: TStringList;

begin
  try
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS55882FB7DE511A', '4FB5FF82B9AB7D0E0AEB840D403DE0F74');

    // parameters, http://www.coolsms.co.kr/SMS_API#POSTcancel 참조
    data := TStringList.create;
    data.Values['mid'] := 'R2M557004D2DD1EE'; // 메시지ID
    // data.Values['gid'] := ''; // 그룹ID

    // 예약취소 요청
    jsonObject := coolsms.postRequest('cancel', data, 'sms');
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

