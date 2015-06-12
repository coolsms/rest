program sent_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  data: TStringList;
  jsonObject: TJSONObject;
  jsonArray: TJSONArray;
  jsonObjectData: TJSONObject;
  i: integer;

begin
  try
    // api_key, api_secret ����
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52B122858C04F', '8BAAE5A5926C9AE081920A085BFB835A');

    // data ���� mid�� gid �� �� �ϳ��� ���� �մϴ�.
    data := TStringList.create;
    data.Values['mid'] := 'R2M547004D2DD1EE'; // �޽���ID

    // �׿� parameters, http://www.coolsms.co.kr/SMS_API#GETsent ����
    {
    	count	�⺻�� 20�̸� 20���� ����� ���� �� ����. 40�Է½� 40���� ����� ����
      page	1���� �����ϴ� ��������
      rcpt	���Ź�ȣ�� �˻�
      start	�˻� �����Ͻ� ���� ��¥�� �ð����� �˻� YYYY-MM-DD HH:MI:SS ������ ��¥�� �ð�
      end	�˻� �����Ͻ� ���� ��¥�� �ð����� �˻� YYYY-MM-DD HH:MI:SS ������ ��¥�� �ð�
      status	�޽��� ���� ������ �˻�
      resultcode	���۰�� ������ �˻�
      notin_resultcode	�Էµ� ���۰�� �� �̿��� �ǵ鸸 ��ȸ
      gid	�׷�ID
    }

    jsonObject := coolsms.request('sent', data, 'sms');
    Writeln(jsonObject.Get('code').Equals(Nil));

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('����');
      jsonArray := JsonObject.Get('data').JsonValue as TJSONArray;

      Writeln('total_count : ' + jsonObject.Get('total_count').JsonValue.ToString);
      Writeln('list_count : ' + jsonObject.Get('list_count').JsonValue.ToString);
      Writeln('page : ' + jsonObject.Get('page').JsonValue.ToString);

      for i := 0 to jsonArray.Size - 1 do
      begin
          jsonObjectData := TJSONObject.Create;
          jsonObjectData := jsonArray.Get(i) as TJSONObject;

          Writeln('-----------------------------------------');
          Writeln('type : ' + jsonObjectData.Get('type').JsonValue.ToString);
          Writeln('recipient_number : ' + jsonObjectData.Get('recipient_number').JsonValue.ToString);
          Writeln('text : ' + jsonObjectData.Get('text').JsonValue.ToString);
          Writeln('carrier : ' + jsonObjectData.Get('carrier').JsonValue.ToString);
          Writeln('status : ' + jsonObjectData.Get('status').JsonValue.ToString);
          Writeln('result_code : ' + jsonObjectData.Get('result_code').JsonValue.ToString);
          Writeln('result_message : ' + jsonObjectData.Get('result_message').JsonValue.ToString);
          Writeln('message_id : ' + jsonObjectData.Get('message_id').JsonValue.ToString);
          Writeln('group_id : ' + jsonObjectData.Get('group_id').JsonValue.ToString);
          Writeln('sent_time : ' + jsonObjectData.Get('sent_time').JsonValue.ToString);
          Writeln('accepted_time : ' + jsonObjectData.Get('accepted_time').JsonValue.ToString);
      end;
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
