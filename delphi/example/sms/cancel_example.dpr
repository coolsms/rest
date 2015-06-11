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
    // api_key, api_secret ����
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    // http://www.coolsms.co.kr/SMS_API#POSTcancel ����
    data := TStringList.create;
    data.Values['mid'] := 'R2M557004D2DD1EE'; // �޽���ID
    // data.Values['gid'] := ''; // �׷�ID

    jsonObject := coolsms.postRequest('cancel', data, 'sms');
    Writeln(jsonObject.Get('code').Equals(Nil));

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('����');
    end
    else
    begin
      Writeln('����');
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

