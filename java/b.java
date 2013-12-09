import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.*;
import javax.crypto.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec; import java.security.SignatureException; 
public class b {

	public static void main(String[] args)
	{
		try{
			String charset = "UTF-8";
			String api_key = "NCS52A57F48C3D32";
			String api_secret = "5AC44E03CE8E7212D9D1AD9091FA9966";
			String to = "01048597580";
			String from = "029302266";
			String text = "안뇽하세요";
			
			SecretKeySpec keySpec = new SecretKeySpec(api_secret.getBytes(), "HmacMD5");
			           
		    Mac mac = Mac.getInstance("HmacMD5");
		    mac.init(keySpec);
			byte[] result = mac.doFinal(to.getBytes());

			char[] hexArray = "0123456789ABCDEF".toCharArray();
			char[] hexChars = new char[result.length * 2];

			System.out.println(result.length);
			for(int i = 0; i < result.length; i++) {
				int positive = result[i] & 0xff;
				hexChars[i * 2] = hexArray[positive >>> 4];
				hexChars[i * 2 + 1] = hexArray[positive & 0x0F];
			}

			
			String signature = new String(hexChars);
			System.out.println(signature);
			
			String data = URLEncoder.encode("api_key", charset) + "=" + URLEncoder.encode(api_key, charset);
			data += "&" + URLEncoder.encode("signature", charset) + "=" + URLEncoder.encode(signature, charset);
			data += "&" + URLEncoder.encode("to", charset) + "=" + URLEncoder.encode(to, charset);
			data += "&" + URLEncoder.encode("from", charset) + "=" + URLEncoder.encode(from, charset);
			data += "&" + URLEncoder.encode("text", charset) + "=" + URLEncoder.encode(text, charset);

			URL url = new URL("http://apitest.coolsms.co.kr:2000/1/send");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			System.out.println("test-2");
			System.out.println(conn.getAllowUserInteraction());
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
