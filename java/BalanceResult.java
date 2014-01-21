public class BalanceResult {
	private String point;
	private String cash;
	private String errorString;
	
	public String getPoint() {
		return point;
	}
	public void setPoint(Object object) {
		this.point = (String) object;
	}
	
	public String getCash() {
		return cash;
	}
	public void setCash(Object object) {
		this.cash = (String) object;
	}
	
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	
}
