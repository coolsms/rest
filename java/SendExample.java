/*
 * SEND 예제 입니다.
 */
public class SendExample {
	
	public static void main(String[] args) {	
		SetBase base = new SetBase();
		Coolsms coolsms = new Coolsms(base.getApiKey(), base.getApiSecret());
		
		// 반드시 있어야 되는 정보
		Set set = new Set();
		set.setTo("01090683469"); // 받는사람 번호
		// setSend.setTo(new String[] { "01040683469", "01041683469", "01052683469", "01043683469" }); // 받는사람 번호 여러개 입력시
		set.setText("안뇽"); // 문자내용 SMS(80바이트), LMS(장문 2,000바이트), MMS(장문+이미지)
										   // 한글:2바이트, 영어:1byte, 띄어쓰기:1byte
		set.setFrom("029302266"); // 보내는 사람 번호		
	
		// 부가적인 정보
		/*
		set.setImagePath("./"); // image file path 이미지 파일 경로 설정 (기본 "./")
		set.setImage("test.jpg"); // image file (지원형식 : 200KB 이하의 JPEG)
		set.setFrom("029302266"); // 보내는 사람 번호
		set.setDatetime("201401151230"); // 예약전송시 날짜 설정
		set.setType("SMS"); // 메시지 타입
		set.setRefname("참조내용"); // 참조내용
		set.setCountry("KR"); // 국가코드 한국:KR 일본:JP 미국:US 중국:CN
		set.setMid("R9182"); // 메시지 아이디 (없으면 자동으로 지정)
		set.setGid("R19992"); // 그룹 아이디 (없으면 자동으로 지정)
		set.setSubject("제목"); // LMS, MMS 일때 제목
		set.setCharset("utf8"); // 인코딩 방식
		*/

		SendResult result = coolsms.send(set);

		if (result.getErrorString() == null) {
			/*
			 *  메시지 보내기 성공 및 전송결과 출력
			 */
			System.out.println("성공");
			System.out.println(result.getRecipient_number()); // 받는사람 번호
			System.out.println(result.getGroup_id()); // 그룹아이디
			System.out.println(result.getMessage_id()); // 메시지아이디
			System.out.println(result.getResult_code()); // 결과코드
			System.out.println(result.getResult_message());  // 결과 메시지
			System.out.println(result.getErrorCount());  // 여러개 보낼시 오류난 메시지 수
		} else {
			/*
			 * 메시지 보내기 실패
			 */
			System.out.println("실패");
			System.out.println(result.getErrorString()); // 에러 메시지
		}		
	}
	
}
