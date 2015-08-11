program send_example;

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
    // api_key, api_secret ����
    coolsms := handler.Create;
    coolsms.setApiKey('NCS55882FB7DE511A', '4FB5FF82B9AB7D0E0AEB840D403DE0F74');

    // parameters
    data := TStringList.create;
    data.Values['to'] := '01000000000'; // �޴� ��ȣ
    data.Values['from'] := '029302266'; // ������ ��ȣ
    data.Values['text'] := 'test'; // ���ڳ���
    data.Values['type'] := 'mms';  // ����Ÿ�� sms, mms, lms
    data.Values['image'] := '..\..\test.jpg'; // IMAGE ���� (MMS�ϰ��)

    // �׿� parameters, http://www.coolsms.co.kr/SMS_API#POSTsend ����
    {
      image_encoding	�̹��� ���ڵ� ��� binary(Default), base64
      refname	��������(�̸�)
      country
      �ѱ�: 82, �Ϻ�: 81, �߱�: 86, �̱�: 1, ��Ÿ ��� (�⺻ �ѱ�)
      http://countrycode.org ����
      datetime	����ð��� YYYYMMDDHHMISS �������� �Է� (�Է� ���ų� ������¥�� �Է��ϸ� �ٷ� ����) ��) 20131216090510 (2013�� 12�� 16�� 9�� 5�� 10�ʿ� �߼۵ǵ��� ����)
      subject	LMS, MMS �϶� ���� (40����Ʈ)
      charset	�ѱ� ���ڵ� ��� ���� �����ڵ� UTF-8 �� ��� utf8 �ϼ��� �ѱ�(EUC-KR) �� ��� euckr ���� �Է� �Է� ������ utf8�� �⺻
      srk	�ַ�� ���� �����Ḧ ������� �ַ�� ���Ű
      mode	test�� �Է��� ��� CARRIER �ùķ����ͷ� �ùķ��̼ǵ� ���Ź�ȣ�� �ݵ�� 01000000000 ���� �׽�Ʈ�ϼ���. �����ʵ� datetime�� ���õ� ������� 60 �ܾ׿��� ���� �����Ǹ� ������ ������ ��������
      delay	0~20 ������ ������ �������� �ð��� �� �� ����, 1�� �� 1�� (�⺻���� 0)
      force_sms
      ������Ǫ�ø� ����ϴ��� SMS�� �߼۵ǵ��� ����
      true Ȥ�� false(�⺻)
      os_platform
      Ŭ���̾�Ʈ�� OS �� �÷��� ����) CentOS 6.6
      (v1.5���� �߰���)
      dev_lang
      ���� ���α׷��� ��� ��) PHP 5.3.3
      (v1.5���� �߰���)
      sdk_version
      SDK ���� ��) PHP SDK 1.5
      (v1.5���� �߰���)
      app_version
      ���ø����̼� ���� ��) Purplebook 4.1
      (v1.5���� �߰���)
      extension	JSON ������ ���� �޽����� ���� �� ����
    }

    // send Messages
    jsonObject := coolsms.postRequest('send', data, 'sms');
    if strToBool(jsonObject.GetValue('status').ToString) = TRUE then
    begin
      Writeln('����');
      Writeln('group_id : ' + jsonObject.Get('group_id').JsonValue.ToString);
      Writeln('success_count : ' + jsonObject.Get('success_count').JsonValue.ToString);
      Writeln('error_count : ' + jsonObject.Get('error_count').JsonValue.ToString);
      Writeln('result_code : ' + jsonObject.Get('result_code').JsonValue.ToString);
      Writeln('result_message : ' + jsonObject.Get('result_message').JsonValue.ToString);
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
