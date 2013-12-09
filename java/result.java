import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.*;
import javax.crypto.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec; import java.security.SignatureException; 
public class result {

	public static void main(String[] args)
	{
		try{
			String charset = "UTF-8";
			String api_key = "NCS52A027B408F5E";
			String api_secret = "E03D85D35A48F399B2C8E1E84D2337F3";
			String mid = "M52A56FFB77E98";
			
			SecretKeySpec keySpec = new SecretKeySpec(api_secret.getBytes(), "HmacMD5");
			           
		    Mac mac = Mac.getInstance("HmacMD5");
		    mac.init(keySpec);
			byte[] result = mac.doFinal(mid.getBytes());

			char[] hexArray = "0123456789ABCDEF".toCharArray();
			char[] hexChars = new char[result.length * 2];

			System.out.println(result.length);
			for(int i = 0; i < result.length; i++) {
				int positive = result[i] & 0xff;
				hexChars[i * 2] = hexArray[positive >>> 4];
				hexChars[i * 2 + 1] = hexArray[positive & 0x0F];
			}

			
			String signature = new String(hexChars);
			System.out.println("signature : " + signature);
			
			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "=" + URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("mid", charset) + "=" + URLEncoder.encode("M52A55479DB68A", charset);

			URL url = new URL("http://apitest.coolsms.co.kr:2000/1/sent");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
										
			String inputLine;
			while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
			in.close();
		}
		catch(Exception e){
			System.out.println("Error : " + e.getMessage());
		}
	}
}
