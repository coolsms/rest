import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 *  SENT(발송문자확인) 예제 입니다.
 */
public class SentExample {
	public static void main(String[] args) {
		/*
		 * 서버에서 받은 API_KEY, API_SECRET를 입력해주세요.
		 */
		String api_key = "CS588FB7DE511A";
		String api_secret = "B5FF8B9AB7D0E0AEB840D403DE0F74";
		Coolsms coolsms = new Coolsms(api_key, api_secret);
		
		/*
		 * Parameters
		 * 관련정보 : http://www.coolsms.co.kr/SDK_Java_API_Reference_ko#toc-1	
		 */
		HashMap<String, String> set = new HashMap<String, String>();
		set.put("mid", "R2M553DD58703AEC");  // message_id 
		//set.put("gid", "R1G55923A83639F0"); // group_id
		//set.put("count", "20"); // count
		//set.put("page", "1"); // page
		//set.put("s_rcpt", "01025555544"); // 수신번호
		//set.put("s_start", "2014-01-01 14:10:10"); // 검색 시작 날짜
		//set.put("s_end", "2014-02-01 14:10:10");	//검색 종료 날짜
		
		JSONObject result = coolsms.sent(set);

		if((Boolean) result.get("status") == true) {
			System.out.println("total_count is = " + result.get("total_count")); // Total Count
			System.out.println("list_count is = " + result.get("list_count")); // List Count
			System.out.println("page is = " + result.get("page")); // Page

			JSONArray data = (JSONArray) result.get("data");
			for (int i = 0; i < data.size(); i++) {
				JSONObject obj = (JSONObject) data.get(i);
				System.out.println("\n=======================================\n");
				System.out.println("SENT 성공");
				System.out.println(obj.get("result_message")); // 결과 메시지
				System.out.println(obj.get("result_code")); // 결과 코드
				System.out.println(obj.get("text")); // 문자내용
				System.out.println(obj.get("status")); // 상태 0:대기중 1:전송완료 2:이통사로부터 리포트 도착
				System.out.println(obj.get("accepted_time")); // 접수시간
				System.out.println(obj.get("sent_time")); // 기지국에서 받는사람한테 보낸시간
				System.out.println(obj.get("group_id")); // Group ID
				System.out.println(obj.get("message_id")); // Message ID
				System.out.println(obj.get("type")); // 메시지 타입 SMS, LMS, MMS
				System.out.println(obj.get("recipient_number")); // 받는사람 번호
			}
		} else {
			System.out.println("SENT 실패");
			System.out.println(result.get("code")); // REST API 에러코드
			System.out.println(result.get("message")); // 에러메시지
		}
	}
}
