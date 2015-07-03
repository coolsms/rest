import java.util.HashMap;
import org.json.simple.JSONObject;

/*
 *  CANCEL(예약취소) 예제 입니다.
 */
public class CancelExample {
	public static void main(String[] args) {
		/*
		 * 서버에서 받은 API_KEY, API_SECRET를 입력해주세요.
		 */
		String api_key = "CS588FB7DE511A";
		String api_secret = "B5FF8B9AB7D0E0AEB840D403DE0F74";
		Coolsms coolsms = new Coolsms(api_key, api_secret);

		HashMap<String, String> set = new HashMap<String, String>();
		set.put("mid", "R1M559395C5939E5"); // message_id
		//set.put("gid", "R2G52F9A0CDB76F8"); //group_id
		
		//message_id나 group_id중 하나는 반드시 입력하셔야 됩니다.
		JSONObject result = coolsms.cancel(set);
		if ((Boolean) result.get("status") == true) {
			System.out.println("예약취소 성공");
		} else {
			System.out.println("예약취소 실패");
			System.out.println(result.get("code")); // REST API 에러코드
			System.out.println(result.get("message")); // 에러메시지
		}
	}
}
