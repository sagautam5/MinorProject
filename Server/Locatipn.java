package latNlog;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Locatipn {

	public static void geocoding(String addr) throws Exception
	{
	    // build a URL
	    String s = "http://api.androidhive.info/contacts/";
	    //s += URLEncoder.encode(addr, "UTF-8");
	    URL url = new URL(s);
	 
	    // read from the URL
	    Scanner scan = new Scanner(url.openStream());
	    String str = new String();
	    while (scan.hasNext())
	        str += scan.nextLine();
	    scan.close();
	 
	    // build a JSON object
	    JSONObject obj = new JSONObject(str);
	    /*if (! obj.getString("name").equals("OK"))
	        return;*/
	 
	    // get the first result
	    JSONObject res = obj.getJSONArray("contacts").getJSONObject(0);
	    System.out.println(res.getString("name"));
	    JSONObject loc =
	        res.getJSONObject("phone");
	    System.out.println("Address: " + loc.getString("mobile") +
	                        ", Contact: " + loc.getString("home"));
	}
}
