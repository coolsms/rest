import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * Coolsms Class
 * RestApi JAVA 
 * v0.1 BETA  
 * POST?GET REQUEST
 */
public class Coolsms extends Https {
	private String api_key;
	private String api_secret;	
	private String timestamp;
	private Https https = new Https();

	/*
	 * Set RestApi config
	 */
	public Coolsms(String api_key, String api_secret) {
		this.api_key = api_key;
		this.api_secret = api_secret;
	}
	
	/*
	 * Send messages
	 * @param set : HashMap<String, String>
	 */
	public JSONObject send(HashMap<String, String> set) {
		JSONObject response = new JSONObject();
		String to = "";

		try {
			String salt = salt();
			String signature = getSignature(this.api_secret, salt); // getSignature
			String boundary = "^***********^2193901290219219878wewe891283";
			String delimiter = "\r\n--" + boundary + "\r\n";
			
			// 기본정보 입력
			set.put("api_key", this.api_key);
			set.put("salt", salt);
			set.put("signature", signature);
			set.put("timestamp", timestamp);
			
			StringBuffer postDataBuilder = new StringBuffer();
			postDataBuilder.append(delimiter);
			
			String image = "";
			String image_path = "";
			for (Entry<String, String> entry : set.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				
				if (key == "image") {
					image = value;
					continue;
				}				
				if (key == "image_path") {
					image_path = value;
					continue;
				}				
				postDataBuilder = setPostData(postDataBuilder, key, value, delimiter);				
			}
			
			if (image != "") {
				/*
				 * SEND MMS
				 */
				response = https.postRequest("send", postDataBuilder, image, image_path);				
			} else {
				/*
				 * SEND SMS or LMS 
				 */
				response = https.postRequest("send", postDataBuilder, "", "");
			}
		} catch (Exception e) {
			response.put("Code", "request error : " + e.getMessage());
		}
		return response;
	}
	
	/*
	 * Sent messages
	 * @param set : HashMap<String, String>
	 */
	public JSONObject sent(HashMap<String, String> set) {
		JSONObject obj = new JSONObject();

		try {
			String charset = "UTF8";
			String salt = salt();
			String signature = getSignature(this.api_secret, salt); // getSignature

			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(this.api_key, charset);
			data = setGetData(data, "signature", signature, charset);
			data = setGetData(data, "salt", salt, charset);
			data = setGetData(data, "timestamp", this.timestamp, charset);

			for (Entry<String, String> entry : set.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				data = setGetData(data, key, value, charset);
			}
			
			String response = null;	
			data = "sent" + "?"+ data;
			response = https.request(data); // GET방식 전송	
		
			if (response != null) {
				obj = (JSONObject) JSONValue.parse(response);
			} else {
				obj.put("code", "서버로부터 받은 값이 없습니다");
			}
		} catch (Exception e) {
			obj.put("code", e.getMessage());
		}
		return obj;
	}

	/*
	 * Reserve message cancel 
	 * @param set : HashMap<String, String>
	 */
	public JSONObject cancel(HashMap<String, String> set) {
		JSONObject obj = new JSONObject();
		Https https = new Https();

		if (set.get("mid") == null && set.get("gid") == null) {
			obj.put("code", "Mid나 Gid중 하나는 반드시 들어가야 됩니다.");
			return obj;
		}

		try {
			String salt = salt();
			String signature = getSignature(this.api_secret, salt); // getSignature
			String boundary = "^***********^2193901290219219878wewe891283";
			String delimiter = "\r\n--" + boundary + "\r\n";

			// 기본정보 입력
			set.put("api_key", this.api_key);
			set.put("salt", salt);
			set.put("signature", signature);
			set.put("timestamp", this.timestamp);
			set.put("charset", "utf8");
			
			StringBuffer postDataBuilder = new StringBuffer();
			postDataBuilder.append(delimiter);

			for (Entry<String, String> entry : set.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				postDataBuilder = setPostData(postDataBuilder, key, value, delimiter);
			}
			obj = https.postRequest("cancel", postDataBuilder, "", "");	
		} catch (Exception e) {
			obj.put("code", e.getMessage());
		}
		return obj;
	}

	/*
	 * Balance info
	 */
	public JSONObject balance() {
		String charset = "utf8";
		String salt = salt();
		String signature = getSignature(this.api_secret, salt); // getSignature
		String data = null;

		JSONObject response = new JSONObject();
		
		try {
			data = URLEncoder.encode("api_key", charset) + "="
					+ URLEncoder.encode(this.api_key, charset);
			data = setGetData(data, "signature", signature, charset);
			data = setGetData(data, "salt", salt, charset);
			data = setGetData(data, "timestamp", timestamp, charset);

			String response_string = null;
			data = "balance" + "?" + data;		
			response_string = https.request(data); // GET방식 전송	

			if (response_string != null) {
				JSONObject obj = (JSONObject) JSONValue.parse(response_string);
				if (obj.get("code") == null) {
					response.put("cash", obj.get("cash"));
					response.put("point", obj.get("point"));
				} else {
					response.put("code", response_string);
				}
			} else {
				response.put("code", "서버로부터 받은 값이 없습니다.");
			}
		} catch (UnsupportedEncodingException e) {			
			response.put("code", e.getMessage());
		}

		return response;
	}
	
	/*
	 * Get signature
	 */
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

	/*
	 * Get salt
	 */
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
	public String arrayJoin(String glue, String array[]) {
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
     */
	public String setValue(String key, String value) {
		return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n"+ value;
	}
 
	/*
	 * String을 GET 방식으로 변경
	 */
	public String setGetData(String data, String key, String value, String charSet) {
		try {
			data += "&"
				+ URLEncoder.encode(key, charSet)
				+ "="
				+ URLEncoder.encode(value, charSet);
		} catch(Exception e) {
			System.out.println("GET data encoding error");
		}

		return data;
	}

	/*
	 * String을 POST 형식에 맞게 Input 
	 */
	public StringBuffer setPostData(StringBuffer builder, String key, String value, String delimiter) {
		try {
			builder.append(setValue(key, value));
			builder.append(delimiter);
		} catch(Exception e) {
			System.out.println("POST data append error");
		}

		return builder;
	}
}
