import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * SEND(문자전송) 예제입니다.
 */
public class SendExtensionExample {
	public static void main(String[] args) {	
		/*
		 * 서버에서 받은 API_KEY, API_SECRET를 입력해주세요.
		 */
		String api_key = "CS3588FB7DE511A";
		String api_secret = "FB5FF8B9AB7D0E0AEB840D403DE0F74";
		Coolsms coolsms = new Coolsms(api_key, api_secret);
	
		/*
		 * Parameters
		 * 관련정보 : http://www.coolsms.co.kr/SDK_Java_API_Reference_ko#toc-0
		 */
		HashMap<String, String> set = new HashMap<String, String>(); 
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();
		
		obj.put("type", "sms"); // 문자타입
		obj.put("to", "01000000000, 01000000001"); // 받는사람번호
		obj.put("text", "Test Message"); // 문자내용
			
		list.add(obj); // 원하는 만큼 obj를 넣어주면 됩니다.		
		set.put("extension", list.toString()); // set extension
		
		JSONObject result = coolsms.send(set); // 보내기&전송결과받기
		if ((Boolean) result.get("status") == true) {
			// 메시지 보내기 성공 및 전송결과 출력
			System.out.println("성공");			
			System.out.println(result.get("group_id")); // 그룹아이디
			System.out.println(result.get("result_code")); // 결과코드
			System.out.println(result.get("result_message"));  // 결과메시지
			System.out.println(result.get("success_count")); // 성공갯수
			System.out.println(result.get("error_count"));  // 발송실패 메시지 수
		} else {
			// 메시지 보내기 실패
			System.out.println("실패");
			System.out.println(result.get("code")); // REST API 에러코드
			System.out.println(result.get("message")); // 에러메시지
		}		
	}	
}
