program send_example;

{$APPTYPE CONSOLE}

{$R *.res}

uses
  System.Json, coolsms in 'C:\Users\Administrator\IdeaProjects\coolsms.pas', System.SysUtils, Classes;

var
  coolsms: handler;
  jsonObject: TJSONObject;
  data: TStringList;
  extensionObject: TJSONObject;
  extensionArray: TJSONArray;

begin
  try
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52A122858B04F', '8B2AE5A6926C9AE081920A085BFB835A');

    data := TStringList.create;
    data.Values['to'] := '01090683469'; // 받는 번호
    data.Values['from'] := '029302266'; // 보내는 번호
    data.Values['text'] := 'test'; // 문자내용
    data.Values['type'] := 'sms';  // 문자타입 sms, mms, lms
    //data.Values['image'] := 'C:\Users\Administrator\sdk\new\rest\java\images\test.jpg'; // IMAGE 파일 (MMS일경우)

    // 그외 parameters, http://www.coolsms.co.kr/SMS_API#POSTsend 참조
    {
      image_encoding	이미지 인코딩 방식 binary(Default), base64
      refname	참조내용(이름)
      country
      한국: 82, 일본: 81, 중국: 86, 미국: 1, 기타 등등 (기본 한국)
      http://countrycode.org 참고
      datetime	예약시간을 YYYYMMDDHHMISS 포맷으로 입력 (입력 없거나 지난날짜를 입력하면 바로 전송) 예) 20131216090510 (2013년 12월 16일 9시 5분 10초에 발송되도록 예약)
      subject	LMS, MMS 일때 제목 (40바이트)
      charset	한글 인코딩 방식 지정 유니코드 UTF-8 일 경우 utf8 완성형 한글(EUC-KR) 일 경우 euckr 으로 입력 입력 없으면 utf8가 기본
      srk	솔루션 제공 수수료를 정산받을 솔루션 등록키
      mode	test로 입력할 경우 CARRIER 시뮬레이터로 시뮬레이션됨 수신번호를 반드시 01000000000 으로 테스트하세요. 예약필드 datetime는 무시됨 결과값은 60 잔액에서 실제 차감되며 다음날 새벽에 재충전됨
      delay	0~20 사이의 값으로 전송지연 시간을 줄 수 있음, 1은 약 1초 (기본값은 0)
      force_sms
      누리고푸시를 사용하더라도 SMS로 발송되도록 강제
      true 혹은 false(기본)
      os_platform
      클라이언트의 OS 및 플랫폼 버전) CentOS 6.6
      (v1.5에서 추가됨)
      dev_lang
      개발 프로그래밍 언어 예) PHP 5.3.3
      (v1.5에서 추가됨)
      sdk_version
      SDK 버전 예) PHP SDK 1.5
      (v1.5에서 추가됨)
      app_version
      어플리케이션 버전 예) Purplebook 4.1
      (v1.5에서 추가됨)
      extension	JSON 포맷의 개별 메시지를 담을 수 있음
    }

    {
      // 개별 메시지 보내기
      extensionObject := TJSONObject.Create;
      extensionArray := TJSONArray.Create;

      extensionObject.AddPair('type', 'sms');
      extensionObject.AddPair('to', '01000000003');
      extensionObject.AddPair('text', 'sms_test');
      extensionArray.Add(extensionObject);

      extensionObject := TJSONObject.Create;
      extensionObject.AddPair('type', 'sms');
      extensionObject.AddPair('to', '01000000000, 010000000001');
      extensionObject.AddPair('text', 'sms_test2');
      extensionArray.Add(extensionObject);
      data.Values['extension'] := extensionArray.ToString;
    }

    jsonObject := coolsms.postRequest('send', data, 'sms');

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
      Writeln('group_id : ' + jsonObject.Get('group_id').JsonValue.ToString);
      Writeln('success_count : ' + jsonObject.Get('success_count').JsonValue.ToString);
      Writeln('error_count : ' + jsonObject.Get('error_count').JsonValue.ToString);
      Writeln('result_code : ' + jsonObject.Get('result_code').JsonValue.ToString);
      Writeln('result_message : ' + jsonObject.Get('result_message').JsonValue.ToString);
    end
    else
    begin
      Writeln('실패');
      Writeln('code : ' + jsonObject.Get('code').JsonValue.ToString);

      if jsonObject.Get('message').Equals(Nil) = FALSE then
        Writeln('message : ' + jsonObject.Get('message').JsonValue.ToString);
    end;

    jsonObject.Free;
    extensionObject.Free;
    extensionArray.Free;

    Writeln('-----------------------------------------');
    Writeln('Press <enter> to quit...');
    Readln;
  except
    on E: Exception do
      Writeln(E.ClassName, ': ', E.Message);
  end;
end.
