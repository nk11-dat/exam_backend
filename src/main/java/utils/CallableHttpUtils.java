package utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class CallableHttpUtils implements Callable<String>
{

    String url;
    public CallableHttpUtils(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception
    {
        URL apiURL = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) apiURL.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", "server");

        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
            jsonStr = scan.nextLine();
        }
        scan.close();
        return jsonStr;
    }
}
