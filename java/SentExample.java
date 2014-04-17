/*
 *  SENT(발송문자확인) 예제 입니다.
 */
public class SentExample {

	public static void main(String[] args) {
		Coolsms coolsms = new Coolsms();
				
		Set set = new Set();
		 
		// 부가적인 정보		
		set.setMid("R1M334262297FE38");  // message_id 
		//set.setGid("R2G532FE991BFD40"); // group_id
		//set.setCount("20"); // count
		//set.setPage("1"); // page
		//set.setSRcpt("01025555544"); // 수신번호
		//set.setSStart("2014-01-01 14:10:10"); // 검색 시작 날짜
		//set.setSEnd("2014-02-01 14:10:10");	//검색 종료 날짜
		
		SentResult result[] = coolsms.sent(set);
			
		if (result[0].getErrorString() == null) {
			for (int i = 0; i < result.length; i++) {
				System.out.println("SENT 성공");
				System.out.println(result[i].getTotal_count()); // Total Count
				System.out.println(result[i].getList_count()); // List Count
				System.out.println(result[i].getPage()); // Page
				System.out.println(result[i].getResult_message()); // 결과 메시지
				System.out.println(result[i].getResult_code()); // 결과 코드
				System.out.println(result[i].getText()); // 문자내용
				System.out.println(result[i].getStatus()); // 상태 0:대기중 1:전송완료 2:이통사로부터 리포트 도착
				System.out.println(result[i].getAccepted_time()); // 접수시간
				System.out.println(result[i].getSent_time()); // 기지국에서 받는사람한테 보낸시간
				System.out.println(result[i].getGroup_id()); // Group ID
				System.out.println(result[i].getMessage_id()); // Message ID
				System.out.println(result[i].getType()); // 메시지 타입 SMS, LMS, MMS
				System.out.println(result[i].getRecipient_number()); // 받는사람 번호
			}
		} else {
			System.out.println("SENT  실패");
			System.out.println(result[0].getErrorString()); // 오류 메시지
		}
	}
}
