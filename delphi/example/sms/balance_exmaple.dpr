program balance_exmaple;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in '..\..\coolsms.pas', System.SysUtils;

var
  coolsms: handler;
  jsonObject: TJSONObject;

begin
  try
    // api_key, api_secret ����
    coolsms := handler.Create;
    coolsms.setApiKey('NCS55882FB7DE511A', '4FB5FF82B9AB7D0E0AEB840D403DE0F74');

    // �����ܾ� ��û, http://www.coolsms.co.kr/SMS_API#GETbalance ����
    jsonObject := coolsms.request('balance', Nil, 'sms');
    if strToBool(jsonObject.GetValue('status').ToString) = TRUE then
    begin
      Writeln('����');
      Writeln('cash : ' + jsonObject.Get('cash').JsonValue.ToString);
      Writeln('point : ' + jsonObject.Get('point').JsonValue.ToString);
    end
    else
    begin
      Writeln('����');
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
