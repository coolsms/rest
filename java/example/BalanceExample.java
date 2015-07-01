import java.util.HashMap;
import org.json.simple.JSONObject;

/*
 *  BALACNE (잔액정보 가져오기) 예제 입니다.
 */
public class BalanceExample {
	public static void main(String[] args) {
		/*
		 * 서버에서 받은 API_KEY, API_SECRET를 입력해주세요.
		 */
		String api_key = "1CS5588FB7DE511A";
		String api_secret = "4FB5FF8B9AB7D0E0AEB840D404DE0F4";
		Coolsms coolsms = new Coolsms(api_key, api_secret);
		
		JSONObject result = coolsms.balance(); // 잔액정보 가져오기
		if ((Boolean) result.get("status") == true) {
			System.out.println("잔액정보 불러오기 성공");
			System.out.println(result.get("cash")); // 잔여 캐쉬
			System.out.println(result.get("point")); // 잔여 포인트
		} else {
			System.out.println("실패");
			System.out.println(result.get("code")); // REST API 에러코드
			System.out.println(result.get("message")); // 에러메시지
		}		
	}
}
