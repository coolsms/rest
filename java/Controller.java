import java.io.*;
/*
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
*/
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec; 
import java.util.Random;

/*
import javax.crypto.Mac;
import java.security.SignatureException; 
*/

public class Controller {

	//private static String charset;
	private static String api_key;
	private static String api_secret;
	private static String salt;
	private static String signature;
	private static String timestamp;

	public Controller(String api_key, String api_secret)
	{
		//Controller.charset = "UTF-8";
		Controller.api_key = api_key;
		Controller.api_secret = api_secret;
		Controller.salt = salt();
		Controller.signature = getSignature(api_secret, salt);
	}
	
	public static void main(String[] args)
	{
	}


	public static void send(Set set)
	{
		System.out.println("in send message");
		try{
			String nums = "";
			if(set.to.length >= 1) nums = arrayJoin(",", set.to);

			String to = nums;
			String salt = salt();
			String signature = getSignature(Controller.api_secret, salt); // getSignature

			URL url = new URL("http://apitest.coolsms.co.kr:2000/1/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // connect
		
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			//String datetime = "20131224050510";
			//nums, type, image, refname, country, datetime, mid, gid, subject, charset)	
			if(set.image == null)
			{
				String data = URLEncoder.encode("api_key", set.charset) + "=" + URLEncoder.encode(Controller.api_key, set.charset);
				data += "&" + URLEncoder.encode("signature", set.charset) + "=" + URLEncoder.encode(signature, set.charset);
				data += "&" + URLEncoder.encode("to", set.charset) + "=" + URLEncoder.encode(to, set.charset);
				data += "&" + URLEncoder.encode("from", set.charset) + "=" + URLEncoder.encode(set.from, set.charset);
				data += "&" + URLEncoder.encode("text", set.charset) + "=" + URLEncoder.encode(set.text, set.charset);
				data += "&" + URLEncoder.encode("salt", set.charset) + "=" + URLEncoder.encode(salt, set.charset);
				data += "&" + URLEncoder.encode("timestamp", set.charset) + "=" + URLEncoder.encode(Controller.timestamp, set.charset);

				if(set.type != null) data += "&" + URLEncoder.encode("type", set.charset) + "=" + URLEncoder.encode(set.type, set.charset);
				if(set.refname != null) data += "&" + URLEncoder.encode("refname", set.charset) + "=" + URLEncoder.encode(set.refname, set.charset);
				if(set.country != null) data += "&" + URLEncoder.encode("country", set.charset) + "=" + URLEncoder.encode(set.country, set.charset);
				if(set.datetime != null) data += "&" + URLEncoder.encode("datetime", set.charset) + "=" + URLEncoder.encode(set.datetime, set.charset);
				if(set.mid != null) data += "&" + URLEncoder.encode("mid", set.charset) + "=" + URLEncoder.encode(set.mid, set.charset);
				if(set.gid != null) data += "&" + URLEncoder.encode("gid", set.charset) + "=" + URLEncoder.encode(set.gid, set.charset);
				if(set.subject != null) data += "&" + URLEncoder.encode("subject", set.charset) + "=" + URLEncoder.encode(set.subject, set.charset);
				if(set.charset != null) data += "&" + URLEncoder.encode("charset", set.charset) + "=" + URLEncoder.encode(set.charset, set.charset);

				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
				wr.close();
			}
			else
			{
				String boundary = "^***********^";
				String delimiter = "\r\n--" + boundary + "\r\n";

				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setUseCaches(false);

				StringBuffer postDataBuilder = new StringBuffer();

				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("charset", set.charset));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("api_key", Controller.api_key));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("to", to));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("from", set.from));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("text", set.text));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("salt", salt));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("signature", signature));
				postDataBuilder.append(delimiter);
				postDataBuilder.append(setValue("timestamp", Controller.timestamp));
				postDataBuilder.append(delimiter);

				postDataBuilder.append(setValue("type", "mms"));
				postDataBuilder.append(delimiter);
				
				//nums, type, image, refname, country, datetime, mid, gid, subject, charset)

				postDataBuilder.append(setFile("image", set.image));
				postDataBuilder.append("\r\n");
				String filePath = set.file_path + set.image;

				FileInputStream an = new FileInputStream(filePath);
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
				out.writeUTF(postDataBuilder.toString());

				// 파일 복사 작업 시작
				int maxBufferSize = 1024;
				int bufferSize = Math.min(an.available(), maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				// 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다.
				int byteRead = an.read(buffer, 0, bufferSize);
				// 전송
				while (byteRead > 0) 
				{
					out.write(buffer);
									bufferSize = Math.min(an.available(), maxBufferSize);
					byteRead = an.read(buffer, 0, bufferSize);
				}

				out.writeBytes(delimiter); // 반드시 작성해야 한다.
				out.flush();
				out.close();
				an.close();
			}

			String inputLine; // 서버로 부터 받은 request를 받을 변수

			int responseCode = conn.getResponseCode();
			if(responseCode != 200) // 오류발생시 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

				System.out.println("signature4");
				while((inputLine = in.readLine()) != null) 
				{
					System.out.println(inputLine);
				}
			}
			else
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				//StringBuffer response = new StringBuffer();
				
				System.out.println("signature5");

				while((inputLine = in.readLine()) != null) 
				{
					System.out.println(inputLine);
				}
			}

		}
		catch(Exception e){
			System.out.println("Error : " + e);
		}
	}

	

	public static void sent(Set set)
	{
		System.out.println("in sent");
		try{
			String charset = "UTF8";
			String salt = salt();
			
			String signature = getSignature(Controller.api_secret, salt); // getSignature
			
			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(Controller.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "=" + URLEncoder.encode(signature, charset);
			if(set.mid != null)
			{
				data += "&" + URLEncoder.encode("mid", charset) + "=" + URLEncoder.encode(set.mid, charset);
			}
			if(set.gid != null)
			{
				data += "&" + URLEncoder.encode("gid", charset) + "=" + URLEncoder.encode(set.gid, charset);
			}
			data += "&" + URLEncoder.encode("salt", charset) + "=" + URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "=" + URLEncoder.encode(Controller.timestamp, charset);

			String sent_url = "http://apitest.coolsms.co.kr:2000/1/sent" + "?" + data;
			URL url = new URL(sent_url);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // connect

			conn.setRequestMethod("GET");

			String inputLine; // 서버로 부터 받은 request를 받을 변수

			int responseCode = conn.getResponseCode();
			if(responseCode != 200) // 오류발생시 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

				while((inputLine = in.readLine()) != null)
				{
					System.out.println(inputLine);
				}
			}
			else
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				while((inputLine = in.readLine()) != null)
				{
					System.out.println(inputLine);
				}

				
			}
			//conn.disconnect();
		}
		catch(Exception e){
			System.out.println("Error : " + e);
		}
	}

	public static void cancel(Set set)
	{
		System.out.println("in cancel");
		try{
			String charset = "UTF8";
			String salt = salt();
			
			String signature = getSignature(Controller.api_secret, salt); // getSignature
			
			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(Controller.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "=" + URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("salt", charset) + "=" + URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "=" + URLEncoder.encode(Controller.timestamp, charset);

			if(set.mid != null)
			{
				data += "&" + URLEncoder.encode("mid", charset) + "=" + URLEncoder.encode(set.mid, charset);
			}
			if(set.gid != null)
			{
				data += "&" + URLEncoder.encode("gid", charset) + "=" + URLEncoder.encode(set.gid, charset);
			}


			URL url = new URL("http://apitest.coolsms.co.kr:2000/1/cancel");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // connect

			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();

			String inputLine; // 서버로 부터 받은 request를 받을 변수

			int responseCode = conn.getResponseCode();

			if(responseCode != 200) // 오류발생시 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

				System.out.println("signature4");
				while((inputLine = in.readLine()) != null) 
				{
					System.out.println(inputLine);
				}
			}
			else
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				//StringBuffer response = new StringBuffer();
				
				System.out.println("signature5");

				while((inputLine = in.readLine()) != null) 
				{
					System.out.println(inputLine);
				}
			}
		}
		catch(Exception e){
			System.out.println("Error : " + e);
		}
	}

	public static String balance()
	{
		String request = "";
		System.out.println("in balance");
		try{
			String charset = "UTF8";
			String salt = salt();
			
			String signature = getSignature(Controller.api_secret, salt); // getSignature
			
			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(Controller.api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "=" + URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("salt", charset) + "=" + URLEncoder.encode(salt, charset);
			data += "&" + URLEncoder.encode("timestamp", charset) + "=" + URLEncoder.encode(Controller.timestamp, charset);

			String sent_url = "http://apitest.coolsms.co.kr:2000/1/balance" + "?" + data;
			URL url = new URL(sent_url);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // connect

			conn.setRequestMethod("GET");

			String inputLine; // 서버로 부터 받은 request를 받을 변수

			int responseCode = conn.getResponseCode();
			if(responseCode != 200) // 오류발생시 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

				System.out.println("signature4");
				while((inputLine = in.readLine()) != null)
				{
					request = inputLine;
					System.out.println(inputLine);
				}
			}
			else
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				//StringBuffer response = new StringBuffer();
				System.out.println("inputLine+!@+@!+");

				while((inputLine = in.readLine()) != null)
				{
					request = inputLine;
					System.out.println(inputLine);
				}
			}
			//conn.disconnect();
		}
		catch(Exception e){
			System.out.println("Error : " + e);
			String error = "Error : "  + e;
			request = error;
		}

		return request;
	}


	public static String getSignature(String api_secret, String salt)
	{
		long timestamp_long = System.currentTimeMillis()/1000;
		Controller.timestamp = Long.toString(timestamp_long);

		String signature = "";
		try
		{
			String temp = timestamp + salt;
			
			SecretKeySpec keySpec = new SecretKeySpec(api_secret.getBytes(), "HmacMD5");
					   
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(keySpec);
			
			byte[] result = mac.doFinal(temp.getBytes());

			char[] hexArray = "0123456789ABCDEF".toCharArray();
			char[] hexChars = new char[result.length * 2];

			for(int i = 0; i < result.length; i++) 
			{
				int positive = result[i] & 0xff;
				hexChars[i * 2] = hexArray[positive >>> 4];
				hexChars[i * 2 + 1] = hexArray[positive & 0x0F];
			}

			signature = new String(hexChars);

			System.out.println("salt = " + salt);
			System.out.println("temp = " + temp);
			System.out.println("api_secret = " + api_secret);
			System.out.println("timestamp = " + timestamp);
			System.out.println("signature = " + signature);
		}
		catch(Exception e){
			System.out.println("Error : " + e);
		}

		return signature;
	}

	public static String salt()
	{
		String uniqId = "";

		Random randomGenerator = new Random();

		//length - set the unique Id length
		for(int length = 1; length <= 10; ++length) 
		{
			int randomInt = randomGenerator.nextInt(10); //digit range from 0 - 9
			uniqId += randomInt + "";
		}

		return uniqId;	
	}
	
	/*
	 * array를 ,로 구분한 문자열로
	 */
	public static String arrayJoin(String glue, String array[]) 
	{
		String result = "";

		for(int i = 0; i < array.length; i++) 
		{
			result += array[i];
			if (i < array.length - 1) result += glue;
		}

		return result;
	}

	/**
     * Map 형식으로 Key와 Value를 셋팅한다.
     * @param key : 서버에서 사용할 변수명
     * @param value : 변수명에 해당하는 실제 값
     * @return
     */
    public static String setValue(String key, String value) 
	{
        return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n"+ value;
    }
 
    /**
     * 업로드할 파일에 대한 메타 데이터를 설정한다.
     * @param key : 서버에서 사용할 파일 변수명
     * @param fileName : 서버에서 저장될 파일명
     * @return
     */
    public static String setFile(String key, String fileName) 
	{
        return "Content-Disposition: form-data; name=\"" + key
                + "\";filename=\"" + fileName + "\"\r\nContent-type: image/jpeg;\r\n";
    }

/*
	public static String getRequest(String resquest)
	{
		
	
	}
*/
}


