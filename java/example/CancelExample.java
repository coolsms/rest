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
		String api_key = "NCS52A122851C04F";
		String api_secret = "8B2AE5A6923C9BE081920A085BFB835A";
		Coolsms coolsms = new Coolsms(api_key, api_secret);

		HashMap<String, String> set = new HashMap<String, String>();
		set.put("mid", "R2M5566B2C01922A"); // message_id
		//set.put("gid", "R2G52F9A0CDB76F8"); //group_id
		
		//message_id나 group_id중 하나는 반드시 입력하셔야 됩니다.
		JSONObject result = coolsms.cancel(set);
		if (result.get("code") == null) {
			System.out.println("예약취소 성공");
		} else {
			System.out.println("예약취소 실패");
			System.out.println(result.get("code"));			
		}
	}
}
