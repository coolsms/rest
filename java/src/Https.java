import java.io.*;
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
 * Http.class
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
	public JSONObject postRequest(String request_url, StringBuffer data, String image, String image_path) {
		JSONObject obj = new JSONObject();
		try {
			String boundary = "^***********^2193901290219219878wewe891283";
			String delimiter = "\r\n--" + boundary + "\r\n";
			
			URL url = new URL(this.url+request_url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); // connect
			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setUseCaches(false);
			
			// data set
			if(image != "") {
				/*
				 * MMS Setting
				 */
				if(image_path == "") image_path = "./";
				
				// image file set
				data.append(setFile("image", image));				
				data.append("\r\n");
				FileInputStream fs = new FileInputStream(image_path + image);				
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				
				out.writeUTF(data.toString());
				// 파일복사 작업 시작
				int maxBufferSize = 1024;
				int bufferSize = Math.min(fs.available(), maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				// 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다
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
			} else {
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				out.writeUTF(data.toString());
				out.writeBytes(delimiter); 
				out.flush();
				out.close();
			}

			String response = "";
			String inputLine; 
			int responseCode = conn.getResponseCode();
			
			// 오류발생시
			if (responseCode != 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((inputLine = in.readLine()) != null) {					
					response = inputLine;
				}
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}
			}

			if (response != null) {
				obj = (JSONObject) JSONValue.parse(response);
				if (request_url == "cancel") obj.put("code", null);
			} else {
				obj.put("code", "no response");
			}
		} catch (Exception e) {
			obj.put("code", e.getMessage());
		}
		return obj;
	}

	/*
	 * https request (GET)
	 */
	public String request(String data) {
		String response = "";
		String inputLine; // 서버로 부터 받은 response를 받을 변수
		
		try {
			URL url = new URL(this.url + data);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); // connect			
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();			
			if (responseCode != 200) // 오류발생시
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((inputLine = in.readLine()) != null) {
					response = inputLine;
				}
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
}
