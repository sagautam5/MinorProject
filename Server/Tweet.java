package latNlog;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class Tweet {

		ArrayList<String> tweetList = new ArrayList<String>(); 
		public static void geocoding() throws Exception
		{
		    // build a URL
		    String s = "http://localhost/tweets/phps/javaRetrieve.php";
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
		    JSONArray res = obj.getJSONArray("tweet");
		    System.out.println(res.length());
		    ArrayList<String> tweetList = new ArrayList<String>();     
		    JSONArray jsonArray = (JSONArray) res; 
		    if (jsonArray != null) { 
		       int len = jsonArray.length();
		       for (int i=0;i<len;i++){ 
		        tweetList.add(jsonArray.get(i).toString());
		       } 
		    }
		    
		    for (int i=0;i<tweetList.size();i++)
		    {
		    	System.out.printf("%s",tweetList.get(i));
		    	System.out.println("\n\n");
		    }
		    
		    
		    /*JSONObject loc =
		        res.getJSONObject("phone");
		    System.out.println("Address: " + loc.getString("mobile") +
		                        ", Contact: " + loc.getString("home"));*/
		}

}
