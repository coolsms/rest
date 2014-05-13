/*
 *  BALACNE (잔액정보 가져오기) 예제 입니다.
 */
public class BalanceExample {

	public static void main(String[] args) {
		Coolsms coolsms = new Coolsms(); 
		
		BalanceResult result = coolsms.balance(); // 잔액정보 가져오기

		if (result.getErrorString() == null) {
			System.out.println("잔액정보 불러오기 성공");
			System.out.println(result.getCash()); // 잔여 캐쉬
			System.out.println(result.getPoint()); // 잔여 포인트
		} else {
			System.out.println("실패");
			System.out.println(result.getErrorString()); // 오류 메시지
		}		
	}

}
