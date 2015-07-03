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
 * Https.class
 * Https request, response를 관리하는 class 입니다.
 */
public class Https
{
	private String url = "https://api.coolsms.co.kr/sms/1.5/";

	/*
	 * postRequest (POST)
	 * @param StringBuffer : data 
	 * @param String : image
	 */
	public JSONObject postRequest(String resource, HashMap<String, String> params) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", false);

			String salt = salt();
			String timestamp = getTimestamp();
			String signature = getSignature(params.get("api_secret"), salt, timestamp); 
			String boundary = salt + timestamp;
			String delimiter = "\r\n--" + boundary + "\r\n";

			params.put("salt", salt);
			params.put("signature", signature);
			params.put("timestamp", timestamp);

			// data 생성 및 데이터 구분을 위한 delimiter 설정
			StringBuffer postDataBuilder = new StringBuffer();
			postDataBuilder.append(delimiter);

			// params에 image가 있으면 변수에 담아 request를 다르게 보낸다
			String image = null;
			String image_path = null;
			for (Entry<String, String> entry : params.entrySet()) {
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
				if(postDataBuilder == null) {
					obj.put("message", "postRequest data build fail");
				   	return obj;
				}
			}
			
			URL url = new URL(this.url + resource);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); // connect
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			connection.setUseCaches(false);
			DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			outputStream.writeUTF(postDataBuilder.toString());

			// image data set
			if(image != null) {
				// MMS Setting
				if(image_path == null) image_path = "./";
				
				// image file set
				postDataBuilder.append(setFile("image", image));				
				postDataBuilder.append("\r\n");
				FileInputStream fileStream = new FileInputStream(image_path + image);				
				
				// 파일전송 작업 시작
				int maxBufferSize = 1024;
				int bufferSize = Math.min(fileStream.available(), maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				// 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다
				int byteRead = fileStream.read(buffer, 0, bufferSize);
				// 전송
				while (byteRead > 0) {
					outputStream.write(buffer);
					bufferSize = Math.min(fileStream.available(), maxBufferSize);
					byteRead = fileStream.read(buffer, 0, bufferSize);
				}
				fileStream.close();
			} 
			outputStream.writeBytes(delimiter); 
			outputStream.flush();
			outputStream.close();

			String response = null;
			String inputLine; 
			int response_code = connection.getResponseCode();

			obj = checkResponseCode(obj, response_code);
			if((Boolean) obj.get("status") == false) {
				return obj;
			}

			BufferedReader in = null;
			// response 담기 
			if (response_code != 200) {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}

			while ((inputLine = in.readLine()) != null) {					
				response = inputLine;
			}

			if (response != null) {
				obj = (JSONObject) JSONValue.parse(response);
				obj.put("status", true);
				if (obj.get("code") != null) {
					obj.put("status", false);
				}
			} else {
				if (resource == "cancel") {
					obj.put("status", true);
				   	return obj;
				}
				obj.put("status", false);
				obj.put("message", "response is empty");
			}
		} catch (Exception e) {
			obj.put("status", false);
			obj.put("message", e.toString());
		}
		return obj;
	}

	/*
	 * https request (GET)
	 */
	public JSONObject request(String resource, HashMap<String, String> params) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", true);
			String charset = "UTF8";
			String salt = salt();
			String timestamp = getTimestamp();
			String signature = getSignature(params.get("api_secret"), salt, timestamp); // getSignature

			String data = resource + "?";
			data = data + URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(params.get("api_key"), charset);
			data = setGetData(data, "signature", signature, charset);
			data = setGetData(data, "salt", salt, charset);
			data = setGetData(data, "timestamp", timestamp, charset);

			params.remove("api_secret");

			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				data = setGetData(data, key, value, charset);
				if(data == null) {
					obj.put("status", false);
					obj.put("message", "request data build fail");
				   	return obj;
				}
			}

			URL url = new URL(this.url + data);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); // connect			
			connection.setRequestMethod("GET");
			
			BufferedReader in = null;

			int response_code = connection.getResponseCode();			
			obj = checkResponseCode(obj, response_code);
			if((Boolean) obj.get("status") == false) {
				return obj;
			}

			if (response_code != 200) // 오류발생시
			{
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}

			String response = null;
			String inputLine; // 서버로 부터 받은 response를 받을 변수
			while ((inputLine = in.readLine()) != null) {
				response = inputLine;
			}

			if (response != null) {
				obj = (JSONObject) JSONValue.parse(response);
				obj.put("status", true);
				if (obj.get("code") != null) {
					obj.put("status", false);
				}
			} else {
				obj.put("status", false);
				obj.put("message", "response is empty");
			}
		} catch (Exception e) {
			obj.put("status", false);
			obj.put("message", e.toString());
		}
		return obj;
	}

	/*
     * 업로드할 파일에 대한 메타 데이터를 설정한다.
     * @param key : 서버에서 사용할 파일 변수명
     * @param fileName : 서버에서 저장될 파일명
     */
	public String setFile(String key, String fileName) {
		return "Content-Disposition: form-data; name=\"" + key
				+ "\";filename=\"" + fileName
				+ "\"\r\nContent-type: image/jpeg;\r\n";
	}

	/*
	 * String을 POST 형식에 맞게 Input 
	 */
	public StringBuffer setPostData(StringBuffer builder, String key, String value, String delimiter) {
		try {
			builder.append(setValue(key, value));
			builder.append(delimiter);
		} catch(Exception e) {
			return null;
		}

		return builder;
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
			return null;
		}

		return data;
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
	 * Get signature
	 */
	public String getSignature(String api_secret, String salt, String timestamp) {
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
	 * Get timestamp
	 */
	public String getTimestamp() {
		long timestamp_long = System.currentTimeMillis() / 1000;
		String timestamp = Long.toString(timestamp_long);
		return timestamp;
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
	 * Https ResponseCode Check
	 */
	private JSONObject checkResponseCode(JSONObject obj, int response_code) {
		if(obj.get("status") == null) {
			obj.put("status", false);
		}
		//http connection check
		switch (response_code) {
			case HttpsURLConnection.HTTP_OK:
				obj.put("status", true);
				break;
			case HttpsURLConnection.HTTP_FORBIDDEN:
				obj.put("status", true);
				break;
			case HttpsURLConnection.HTTP_GATEWAY_TIMEOUT:
				obj.put("message", "gateway timeout");
				break;
			case HttpsURLConnection.HTTP_UNAVAILABLE:
				obj.put("message", "unavailable");
				break;
			default:
				obj.put("message", "unknown response code : " + response_code);
				break;
		}
		return obj;
	}
}
