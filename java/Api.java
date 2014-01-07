//import com.adobe.serialization.json.JSON;

import java.lang.reflect.Type; // ArrayList나 Collection 이용시 필요
import java.util.ArrayList;

import com.google.gson.Gson; // gson
import com.google.gson.reflect.TypeToken; // gson 

import java.sql.Time;
import java.util.*;

public class Api {

	private static String charset;

	public static void main(String[] args)
	{

		Set setSend = new Set();
		
		String api_key = "NCS52A57F48C3D32";
		String api_secret = "5AC44E03CE8E7212D9D1AD9091FA9966";

		Controller ab = new Controller(api_key, api_secret);

		setSend.image = "test.jpg";
		setSend.from = "029302266";
		setSend.to = new String[] {"01090683469"};
		setSend.text = "안뇽";

		//ab.send(setSend);

		Set setSent = new Set();
		setSent.mid="M52B2AAECDDF2C";
		//setSent.gid="";

		//ab.sent(setSent);

		Set setCancel = new Set();
		setCancel.mid="M52B2AAECDDF2C";
		//setCancel.gid="";

		//ab.cancel(setCancel);

		String balance = ab.balance();

		System.out.println("balance!!");
		System.out.println(balance);


		 // JSON decoding
		  int [] decodeInts = gson.fromJson(jsonStrInts, int[].class);
		  int index = 0;
		  for(int var : decodeInts){
		   System.out.println("decodeInts["+index+++"] : "+var);
		  }
		  
		  Type arrayListType = new TypeToken<ArrayList<String>>(){}.getType();
		  ArrayList<String> decodeStrings = gson.fromJson(jsonStrStrings2, arrayListType);
		  String tmp;
		  while(!decodeStrings.isEmpty()){
		   System.out.println(decodeStrings.get(0));
		   decodeStrings.remove(0);
		  }
}


/*
		JSONObject obj = new JSONObject("{interests : [{interestKey:Dogs}, {interestKey:Cats}]}");

		List<String> list = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("interests");
		for(int i = 0 ; i < array.length() ; i++){
			list.add(array.getJSONObject(i).getString("interestKey"));
		}
	}
*/

/*
		HashMap<String,String> res = remain();
        int cash = Integer.parseInt(res.get("CASH"));
        int point = Integer.parseInt(res.get("POINT"));
        int credits = Integer.parseInt(res.get("CREDITS"));
        String resultCode = res.get("RESULT-CODE");
        String resultMessage = res.get("RESULT-MESSAGE");
*/

/*
		String jsonString = "{\"stat\": { \"sdr\": \"aa:bb:cc:dd:ee:ff\", \"rcv\": \"aa:bb:cc:dd:ee:ff\", \"time\": \"UTC in millis\", \"type\": 1, \"subt\": 1, \"argv\": [{\"type\": 1, \"val\":\"stackoverflow\"}]}}";
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject newJSON = jsonObject.getJSONObject("stat");
        System.out.println(newJSON.toString());
        jsonObject = new JSONObject(newJSON.toString());
        System.out.println(jsonObject.getString("rcv"));
		System.out.println(jsonObject.getJSONArray("argv"));
*/

}
