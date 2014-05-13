import java.io.*;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 *  RestApi JAVA 
 *  v0.1 BETA  
 */
 
public class Coolsms {
	private String api_key;
	private String api_secret;	
	private String timestamp;
	private String base_url;

	/*
	 *  Set RestApi Config
	 */
	public Coolsms() {
		SetBase base = new SetBase();		
		this.api_key = base.getApiKey();
		this.api_secret = base.getApiSecret();
		this.base_url = base.getBaseUrl();		
	}
	
	/*
	 *  POST  Send Message
	 *  @param set : Send Message Parameters
	 */
	public SendResult send(Set set) {
		String response = "";
		SendResult send = null;
		String to = "";
		
		try {
			// get SendNumber 
			if (set.getTo() != null) {
				to = arrayJoin(",", set.getTo());				
			}		
			
			String salt = salt();
			String signature = getSignature(this.api_secret, salt); // getSignature
			URL url = new URL(base_url+"send");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); // connect
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			if (set.getImage() == null) {
				/*
				 * Send SMS, LMS
				 */
				String data = URLEncoder.encode("api_key", set.getCharset())
						+ "="
						+ URLEncoder.encode(this.api_key, set.getCharset());
				data += "&" + URLEncoder.encode("signature", set.getCharset())
						+ "=" + URLEncoder.encode(signature, set.getCharset());
				data += "&" + URLEncoder.encode("salt", set.getCharset()) + "="
						+ URLEncoder.encode(salt, set.getCharset());
				data += "&" + URLEncoder.encode("timestamp", set.getCharset())
						+ "="
						+ URLEncoder.encode(this.timestamp, set.getCharset());
				
				if (set.getText() != null){
					data += "&"
							+ URLEncoder.encode("text", set.getCharset())
							+ "="
							+ URLEncoder
									.encode(set.getText(), set.getCharset());
				}				
				if (set.getTo() != null){
					data += "&"
							+ URLEncoder.encode("to", set.getCharset())
							+ "="
							+ URLEncoder
									.encode(to, set.getCharset());
				}				
				if (set.getFrom() != null){
					data += "&"
							+ URLEncoder.encode("from", set.getCharset())
							+ "="
							+ URLEncoder
									.encode(set.getFrom(), set.getCharset());
				}
				if (set.getType() != null){
					data += "&"
							+ URLEncoder.encode("type", set.getCharset())
							+ "="
							+ URLEncoder
									.encode(set.getType(), set.getCharset());
				}
				if (set.getRefname() != null){
					data += "&"
							+ URLEncoder.encode("refname", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getRefname(),
									set.getCharset());
				}
				if (set.getCountry() != null){
					data += "&"
							+ URLEncoder.encode("country", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getCountry(),
									set.getCharset());
				}
				if (set.getDatetime() != null){
					data += "&"
							+ URLEncoder.encode("datetime", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getDatetime(),
									set.getCharset());
				}
				if (set.getSrk() != null){
					data += "&" + URLEncoder.encode("srk", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getSrk(), set.getCharset());
				}
				if (set.getMode() != null){
					data += "&" + URLEncoder.encode("mode", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getMode(), set.getCharset());
				}
				if (set.getSubject() != null){
					data += "&"
							+ URLEncoder.encode("subject", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getSubject(),
									set.getCharset());
				}
				if (set.getCharset() != null){
					data += "&"
							+ URLEncoder.encode("charset", set.getCharset())
							+ "="
							+ URLEncoder.encode(set.getCharset(),
									set.getCharset());
				}		
				if (set.getExtension() != null){					
					data += "&"
							+ URLEncoder.encode("extension", set.getCharset())
							+ "="
							+ set.getExtension();
				}
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
				wr.close();
			} else {
				/*
				 * SEND MMS
				 */
				String boundary = "^***********^" + salt;
				String delimiter = "\r\n--" + boundary + "\r\n";

				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setUseCaches(false);

				StringBuffer postDataBuilder = new StringBuffer();
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("charset", set.getCharset()));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("api_key", this.api_key));
				postDataBuilder.append(delimiter);				
				postDataBuilder.append(setValue("salt", salt));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("signature", signature));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("timestamp", this.timestamp));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("type", "mms"));
				postDataBuilder.append(delimiter);

				if(set.getText() != null){
					postDataBuilder.append(setValue("text", set.getText()));
					postDataBuilder.append(delimiter);
				}				
				if(to != null){
					postDataBuilder.append(setValue("to", to));
					postDataBuilder.append(delimiter);
				}
				if (set.getFrom() != null) {
					postDataBuilder.append(setValue("from", set.getFrom()));
					postDataBuilder.append(delimiter);
				}
				if (set.getType() != null) {
					postDataBuilder.append(setValue("type", set.getType()));
					postDataBuilder.append(delimiter);
				}
				if (set.getRefname() != null) {
					postDataBuilder.append(setValue("refname", set.getRefname()));
					postDataBuilder.append(delimiter);
				}
				if (set.getCountry() != null) {
					postDataBuilder.append(setValue("country", set.getCountry()));
					postDataBuilder.append(delimiter);
				}
				if (set.getDatetime() != null) {
					postDataBuilder.append(setValue("datetime", set.getDatetime()));
					postDataBuilder.append(delimiter);
				}
				if (set.getSrk() != null) {
					postDataBuilder.append(setValue("srk", set.getSrk()));
					postDataBuilder.append(delimiter);
				}
				if (set.getMode() != null) {
					postDataBuilder.append(setValue("mode", set.getMode()));
					postDataBuilder.append(delimiter);
				}
				if (set.getSubject() != null) {
					postDataBuilder.append(setValue("subject", set.getSubject()));
					postDataBuilder.append(delimiter);
				}
				if (set.getCharset() != null) {
					postDataBuilder.append(setValue("charset", set.getCharset()));
					postDataBuilder.append(delimiter);
				}
					
				postDataBuilder.append(setFile("image", set.getImage()));
				postDataBuilder.append("\r\n");
				String filePath = set.getImagePath() + set.getImage();
				FileInputStream fs = new FileInputStream(filePath);
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				out.writeUTF(postDataBuilder.toString());

				// 파일 복사 작업 시작
				int maxBufferSize = 1024;
				int bufferSize = Math.min(fs.available(), maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				// 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다.
				int byteRead = fs.read(buffer, 0, bufferSize);
				// 전송
				while (byteRead > 0) {
					out.write(buffer);
					bufferSize = Math.min(fs.available(), maxBufferSize);
					byteRead = fs.read(buffer, 0, bufferSize);
				}
				out.writeBytes(delimiter); 
				out.flush();
				out.close();
				fs.close();
			}

			String inputLine; // 서버로 부터 받은 response를 받을 변수
			int responseCode = conn.getResponseCode();

			if (responseCode != 200) // 오류발생시
			{
				response = getErrorString(responseCode);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}
			}

			if (response != null) {
				JSONObject obj = (JSONObject) JSONValue.parse(response);
				if (obj != null) {					
					send = new SendResult();					
					send.setGroup_id(obj.get("group_id") + "");					
					send.setResult_code(obj.get("result_code") + "");
					send.setResult_message(obj.get("result_message") + "");
					send.setSuccessCount(obj.get("success_count") + "");
					send.setErrorCount(obj.get("error_count") + "");
				} else {
					send = new SendResult();
					send.setErrorString(response);
				}
			} else {
				send = new SendResult();
				send.setErrorString("서버로부터 받은 값이 없습니다.");
			}
		} catch (Exception e) {
			send = new SendResult();
			send.setErrorString(e.getMessage());
		}
		return send;
	}
	
	/*
	 * SentMessage
	 */
	public SentResult[] sent(Set set) {
		SentResult sent[] = null;

		try {
			String charset = "UTF8";
			String salt = salt();
			String signature = getSignature(this.api_secret, salt); // getSignature

			String data = URLEncoder.encode("api_key", charset) + "="
					+ URLEncoder.encode(this.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "="
					+ URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("salt", charset) + "="
					+ URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "="
					+ URLEncoder.encode(this.timestamp, charset);

			if (set.getMid() != null) {
				data += "&" + URLEncoder.encode("mid", charset) + "="
						+ URLEncoder.encode(set.getMid(), charset);
			}
			if (set.getGid() != null) {
				data += "&" + URLEncoder.encode("gid", charset) + "="
						+ URLEncoder.encode(set.getGid(), charset);
			}
			if (set.getCount() != null) {
				data += "&" + URLEncoder.encode("count", charset) + "="
						+ URLEncoder.encode(set.getCount(), charset);
			}
			if (set.getPage() != null) {
				data += "&" + URLEncoder.encode("page", charset) + "="
						+ URLEncoder.encode(set.getPage(), charset);
			}
			if (set.getSRcpt() != null) {
				data += "&" + URLEncoder.encode("s_rcpt", charset) + "="
						+ URLEncoder.encode(set.getSRcpt(), charset);
			}
			if (set.getSStart() != null) {
				data += "&" + URLEncoder.encode("s_start", charset) + "="
						+ URLEncoder.encode(set.getSStart(), charset);
			}
			if (set.getSEnd() != null) {
				data += "&" + URLEncoder.encode("s_end", charset) + "="
						+ URLEncoder.encode(set.getSEnd(), charset);
			}

			String sent_url = base_url+"sent" + "?"+ data;
			String response = null;			
			response = transmit_get(sent_url);			
		
			if (response != null) {
				JSONObject obj = (JSONObject) JSONValue.parse(response);
				if (obj != null) {
					JSONArray array = (JSONArray) obj.get("data");
					sent = new SentResult[array.size()];
					for (int i = 0; array.size() > i; i++) {
						sent[i] = new SentResult();
						sent[i].setTotal_count(obj.get("total_count") + "");
						sent[i].setList_count(obj.get("list_count") + "");
						sent[i].setPage(obj.get("page") + "");

						JSONObject obj2 = (JSONObject) array.get(i);
						sent[i].setResult_message(obj2.get("result_message")+ "");
						sent[i].setResult_code(obj2.get("result_code") + "");
						sent[i].setText(obj2.get("text") + "");
						sent[i].setStatus(obj2.get("status") + "");
						sent[i].setAccepted_time(obj2.get("accepted_time") + "");
						sent[i].setSent_time(obj2.get("sent_time") + "");
						sent[i].setGroup_id(obj2.get("group_id") + "");
						sent[i].setMessage_id(obj2.get("message_id") + "");
						sent[i].setType(obj2.get("type") + "");
						sent[i].setRecipient_number(obj2.get("recipient_number") + "");
					}
				} else {
					sent = new SentResult[1];
					sent[0] = new SentResult();
					sent[0].setErrorString(response);
				}
			} else {
				sent = new SentResult[1];
				sent[0] = new SentResult();
				sent[0].setErrorString("서버로부터 받은 값이 없습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sent = new SentResult[1];
			sent[0] = new SentResult();
			sent[0].setErrorString(e.getMessage());
		}
		return sent;
	}

	public CancelResult cancel(Set set) {
		CancelResult cancel = null;
		String response = null;
		
		if (set.getMid() == null && set.getGid() == null) {
			cancel = new CancelResult();
			cancel.setErrorString("Mid나 Gid중 하나는 반드시 들어가야 됩니다.");
			return cancel;
		}

		try {
			String charset = "UTF8";
			String salt = salt();

			String signature = getSignature(this.api_secret, salt); // getSignature

			String data = URLEncoder.encode("api_key", charset) + "="
					+ URLEncoder.encode(this.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "="
					+ URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("salt", charset) + "="
					+ URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "="
					+ URLEncoder.encode(this.timestamp, charset);

			if (set.getMid() != null) {
				data += "&" + URLEncoder.encode("mid", charset) + "="
						+ URLEncoder.encode(set.getMid(), charset);
			}
			if (set.getGid() != null) {
				data += "&" + URLEncoder.encode("gid", charset) + "="
						+ URLEncoder.encode(set.getGid(), charset);
			}

			URL url = new URL(base_url+"cancel");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); // connect
			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();

			String inputLine; // 서버로 부터 받은 response를 받을 변수

			int responseCode = conn.getResponseCode();

			if (responseCode != 200) // 오류발생시
			{
				response = getErrorString(responseCode);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}

				if (response != null) {
					JSONObject obj = (JSONObject) JSONValue.parse(response);
					if (obj != null) {
						cancel = new CancelResult();
					} else {
						cancel = new CancelResult();
						cancel.setErrorString(response);
					}
				} else {
					cancel = new CancelResult();
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
			cancel = new CancelResult();
			cancel.setErrorString(e.getMessage());
		}
		
		return cancel;
	}

	
	/*
	 * BalanceInfo
	 */
	public BalanceResult balance() {
		String response = "";
		String charset = "UTF8";
		String salt = salt();
		String signature = getSignature(this.api_secret, salt); // getSignature
		String data = null;

		BalanceResult balance = new BalanceResult();
		
		try {
			data = URLEncoder.encode("api_key", charset) + "="
					+ URLEncoder.encode(this.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "="
					+ URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("salt", charset) + "="
					+ URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "="
					+ URLEncoder.encode(this.timestamp, charset);
		} catch (UnsupportedEncodingException e) {			
			balance.setErrorString(e.toString());
			return balance;
		}

		String sent_url = base_url+"balance" + "?" + data;		
		response = transmit_get(sent_url); // GET방식 전송	

		if (response != null) {
			JSONObject obj = (JSONObject) JSONValue.parse(response);
			if (obj == null) {
				balance.setErrorString(response);
			} else {
				balance.setCash(obj.get("cash"));
				balance.setPoint(obj.get("point"));
			}
		} else {
			balance.setErrorString("서버로부터 받은 값이 없습니다.");
		}

		return balance;
	}
	
	public String transmit_get(String sent_url) {
		String response = "";
		String inputLine; // 서버로 부터 받은 response를 받을 변수
		
		try {
			URL url = new URL(sent_url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); // connect			
			
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();			
			if (responseCode != 200) // 오류발생시
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}
				response = getErrorString(responseCode);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}			
			}			
		} catch (Exception e) {
			response = e.getMessage();
		}

		return response;
	}


	private String getErrorString(int responseCode) {		
		String errorString = "알수없는 오류";

		if (responseCode == 403) {
			errorString = "API_KEY가 잘못 되었습니다.";
		} else if (responseCode == 402) {
			errorString = "잔액이 부족합니다";
		} else if (responseCode == 404) {
			errorString = "해당 메시지가 없습니다.";
		} else if (responseCode == 500) {
			errorString = "서버내부오류.";
		} else if (responseCode == 400) {
			errorString = "리소스에 접근 METHOD 가 잘못 되었습니다.";
		}else if (responseCode == 413) {
			errorString = "이미지파일 사이즈 300KB 초과.";
		}

		return errorString;
	}

	public String getSignature(String api_secret, String salt) {
		long timestamp_long = System.currentTimeMillis() / 1000;
		this.timestamp = Long.toString(timestamp_long);
		String signature = "";
		
		try {
			String temp = timestamp + salt;
			SecretKeySpec keySpec = new SecretKeySpec(api_secret.getBytes(), "HmacMD5");
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(keySpec);

			byte[] result = mac.doFinal(temp.getBytes());
			char[] hexArray = "0123456789ABCDEF".toCharArray();
			char[] hexChars = new char[result.length * 2];

			for (int i = 0; i < result.length; i++) {
				int positive = result[i] & 0xff;
				hexChars[i * 2] = hexArray[positive >>> 4];
				hexChars[i * 2 + 1] = hexArray[positive & 0x0F];
			}
			signature = new String(hexChars);
		} catch (Exception e) {
			signature = e.getMessage();
		}

		return signature;
	}

	public String salt() {
		String uniqId = "";
		
		Random randomGenerator = new Random();

		// length - set the unique Id length
		for (int length = 1; length <= 10; ++length) {
			int randomInt = randomGenerator.nextInt(10); // digit range from 0 - 9
			uniqId += randomInt + "";
		}
		
		return uniqId;
	}
	
	/*
	 * array를 ,로 구분한 문자열로
	 */
	public String arrayJoin(String glue, String array[]) 
	{
		String result = "";
		for(int i = 0; i < array.length; i++) 
		{
			result += array[i];
			if (i < array.length - 1) result += glue;
		}

		return result;
	}

	/*
     * Map 형식으로 Key와 Value를 셋팅한다.
     * @param key : 서버에서 사용할 변수명
     * @param value : 변수명에 해당하는 실제 값
     * @return
     */
	public String setValue(String key, String value) {
		return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n"+ value;
	}
 
    /*
     * 업로드할 파일에 대한 메타 데이터를 설정한다.
     * @param key : 서버에서 사용할 파일 변수명
     * @param fileName : 서버에서 저장될 파일명
     * @return
     */
	public String setFile(String key, String fileName) {
		return "Content-Disposition: form-data; name=\"" + key
				+ "\";filename=\"" + fileName
				+ "\"\r\nContent-type: image/jpeg;\r\n";
	}
}


