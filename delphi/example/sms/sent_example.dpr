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
    // api_key, api_secret 설정
    coolsms := handler.Create;
    coolsms.setApiKey('NCS52B122858C04F', '8BAAE5A5926C9AE081920A085BFB835A');

    // data 설정 mid나 gid 둘 중 하나는 들어가야 합니다.
    data := TStringList.create;
    data.Values['mid'] := 'R2M547004D2DD1EE'; // 메시지ID

    // 그외 parameters, http://www.coolsms.co.kr/SMS_API#GETsent 참조
    {
    	count	기본값 20이며 20개의 목록을 받을 수 있음. 40입력시 40개의 목록이 리턴
      page	1부터 시작하는 페이지값
      rcpt	수신번호로 검색
      start	검색 시작일시 접수 날짜와 시간으로 검색 YYYY-MM-DD HH:MI:SS 포맷의 날짜와 시간
      end	검색 종료일시 접수 날짜와 시간으로 검색 YYYY-MM-DD HH:MI:SS 포맷의 날짜와 시간
      status	메시지 상태 값으로 검색
      resultcode	전송결과 값으로 검색
      notin_resultcode	입력된 전송결과 값 이외의 건들만 조회
      gid	그룹ID
    }

    jsonObject := coolsms.request('sent', data, 'sms');
    Writeln(jsonObject.Get('code').Equals(Nil));

    if jsonObject.Get('code').Equals(Nil) = TRUE then
    begin
      Writeln('성공');
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
