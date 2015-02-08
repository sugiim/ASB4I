package sugiim.sample.asb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * post AgileSamuraiBot contents for Idobata
 * @author sugiim
 *
 */
public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);
	static ArrayList<String> meigenList = new ArrayList<String>();
	static Map<String,String> meigenMap = new HashMap<String,String>();


	public static void main(String[] args) throws Exception{

		Main m = new Main();
		for(int i=1; i<15; i++){
			Thread.sleep(3000);
			m.getMengen("http://favotter.net/user/agile_s_jp_bot?page="+i);

		}
		List<String> meigenList = new ArrayList<String>(meigenMap.values());
		String text = meigenList.get(new Random().nextInt(meigenList.size()));

		logger.info("ListSize:"+meigenList.size());
		logger.info(text);

		//m.sendPost("https://idobata.io/hook/generic/8b530815-5b6b-479b-a493-390efdeb0aec", text);

	}

	public void getMengen(String url) throws Exception{
//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "8081");
		Document document = Jsoup.connect(url).get();
        Elements elements = document.select(".status_text");
        for (Element element : elements) {

        	meigenMap.put(element.text(),element.text());
        }

        logger.info("Got # "+url);
        return;

	}

	public int sendPost(String strUrl, String text) throws Exception{
		URL url = new URL(strUrl);
        HttpURLConnection connection = null;
        String line="";
        int resCode = 0;
//        System.setProperty("proxySet", "true");
//        System.setProperty("proxyHost", "localhost");
//        System.setProperty("proxyPort", "8081");
        try {

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),StandardCharsets.UTF_8));
                writer.write("source="+text);
                writer.write("\r\n");
                writer.flush();

                resCode = connection.getResponseCode();

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
        	logger.trace(e);
            //e.printStackTrace();
        }

        return resCode;
	}

}
