
public class SendResult {
	private String group_id;
	private String result_code;
	private String result_message;
	private String errorString;
	private String errorCount;
	private String successCount;
	
	public String getGroup_id() {
		return group_id; 
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_message() {
		return result_message;
	}
	public void setResult_message(String result_message) {
		this.result_message = result_message;
	}
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	public String getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}
	public String getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(String successCount) {
		this.successCount = successCount;
	}
}
