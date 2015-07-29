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
    // http://www.coolsms.co.kr/SenderID_API#POSTset_default ����

    // api_key, api_secret ����
    coolsms := handler.Create;
    coolsms.setApiKey('2CS35588FB7DE511A', '2FB5FF8B9XB7D0E0AEB840D403DE0F74');

    // parameters
    data := TStringList.create;
    data.Values['handle_key'] := 'SID55793FC12DDA2'; // register ȣ�� �� ���Ϲ��� �ڵ鰪

    // default �߽Ź�ȣ��� ��û
    jsonObject := coolsms.postRequest('set_default', data, 'senderid');
    if strToBool(jsonObject.GetValue('status').ToString) = TRUE then
    begin
      Writeln('����');
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
