package com.hakkum.ebygarage.customclasses;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.util.Log;

public class ServerApi {
	public static ArrayList<Guitar_Video> PostDataWithXml(String feedUrl)
			throws MalformedURLException, ParserConfigurationException {

		URL url = new URL(feedUrl);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(new InputSource(url.openStream()));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		ArrayList<Guitar_Video> finalArrayList = new ArrayList<Guitar_Video>();
		NodeList nodeList = doc.getElementsByTagName("entry");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Guitar_Video video = new Guitar_Video();
				video.publishDate = getValue(node, "published");
				video.songName = getValue(node, "title");
				video.videoUrl = getId(getAttribute(node, "media:content").get(
						0));
				video.postURl = getAttribute(node, "media:content").get(0);
				Log.v("postURl","postURl"+video.postURl);
				video.imageName = getAttribute(node, "media:thumbnail").get(0);
				finalArrayList.add(video);
			}
		}
		return finalArrayList;
	}

	public static String getId(String getId) {
		int start = getId.indexOf("/v/") + 3;
		int end = start + 11;
		String tempString = (getId.substring(start, end));
		return tempString;
	}

	public static ArrayList<String> getAttribute(Node n, String mainTag) {

		ArrayList<String> tempArray = new ArrayList<String>();
		Node currentAttributeNode = ((org.w3c.dom.Element) n)
				.getElementsByTagName(mainTag).item(0);
		NamedNodeMap attrs = currentAttributeNode.getAttributes();
		for (int a = 0; a < attrs.getLength(); a++) {
			Node theAttribute = attrs.item(a);
			tempArray.add(theAttribute.getNodeValue());
		}
		return tempArray;
	}

	public static String getValue(Node item, String str) {
		NodeList n = ((org.w3c.dom.Element) item).getElementsByTagName(str);
		return getElementValue(n.item(0));
	}

	public final static String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	// GET ALL PRODUCTS
	public static ArrayList<Guitar_Video> getAllProducts(String feedUrl) {
		ArrayList<Guitar_Video> guitarVideos = new ArrayList<Guitar_Video>();
		try {
			guitarVideos = PostDataWithXml(feedUrl);
			// Log.v("tag","GuitarVideos"+guitarVideos);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return guitarVideos;
	}
	
	

	public static String getResponse(String urlRequest) throws URISyntaxException {

		ServerResponse sr = new ServerResponse();
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String finalResult = null;

		try {
			HttpGet request = new HttpGet();
			URI url = new URI(urlRequest);
			request.setURI(url);
			response = client.execute(request);

			sr.setSuccess(false);
			sr.setErrorMessage(null);
			sr.setResponseString(null);

			Log.d("serverapi", "" + response.getStatusLine().getStatusCode());
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				finalResult = EntityUtils.toString(responseEntity);
				Log.d("serverapi", "Response: " + finalResult);
				if (response.getStatusLine().getStatusCode() == 200) {
					sr.setSuccess(true);
					sr.setResponseString(finalResult);
				} else {
					sr.setErrorMessage(finalResult);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sr.getResponseString();
	}

	public static ArrayList<SoundCloud> getSoundCloudMp3Stream()
			throws URISyntaxException {
		ArrayList<SoundCloud> soundStream = null;
		if (soundStream == null)
			soundStream = parseJsonObjects(getResponse(Utility.SOUNDCLOUD_URL));
		return soundStream;
	}

	public static ArrayList<Flickr> getFlickr() throws URISyntaxException {
		ArrayList<Flickr> flickrFeeds = null; 
		if (flickrFeeds == null)
		flickrFeeds = parseFlickrJsonObjects(getResponse(Utility.URL_FLICKR));
		return flickrFeeds;
	}
	
	public static ArrayList<SoundCloud> parseJsonObjects(String response) {
		ArrayList<SoundCloud> soundStream = new ArrayList<SoundCloud>();
		try {
			JSONArray jArray = new JSONArray(response);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jObj = jArray.getJSONObject(i);
				SoundCloud mp3 = new SoundCloud();
				mp3.title = jObj.getString("title").toString();
				mp3.description = jObj.getString("description").toString();
				mp3.mp3Url = "https://api.soundcloud.com/tracks/"
						+ jObj.getString("id")
						+ "/stream?client_id=a0511f550ed4110af9098ccba7d139c0";
				mp3.image = jObj.getJSONObject("user").getString("avatar_url");
				soundStream.add(mp3);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return soundStream;
	}
	
	public static ArrayList<Flickr> parseFlickrJsonObjects(String response) {
		ArrayList<Flickr> flickr = new ArrayList<Flickr>();
		try {
			JSONObject jArray = new JSONObject(response);
			JSONArray jsonArray = jArray.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObj = jsonArray.getJSONObject(i);
				Flickr flikcrObject = new Flickr();
				flikcrObject.title = jObj.getString("title").toString();
				flikcrObject.image = jObj.getJSONObject("media").getString("m").replace("_m", "_z");
				flikcrObject.thumbImages = jObj.getJSONObject("media").getString("m").replace("_m", "_q");;
				Log.v("log", "Image -- >"+flikcrObject.image);
				flickr.add(flikcrObject);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flickr;
	}
	
	public static ArrayList<Twitter_Item> getTwitterFeeds()
	{
		List<Status> statuses = null;
		ArrayList<Twitter_Item>arrayList = new ArrayList<Twitter_Item>();
		Twitter_Item listItem;
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("hY19Kcp9nfylMEOMfzIPNw")
                .setOAuthConsumerSecret("2tCouAesVKiFP53M2SPrETBgEmzmaM6lWEXKsgEM")
                .setOAuthAccessToken("226109854-ciUJk1zhQHSq2vc3jtaewsMEosBwasu2IZ3L5cyj")
                .setOAuthAccessTokenSecret("Bs3brVVEUumF2dbwI7u1MiKrvNb3kL0Vo92xayX0RSRDA");
        
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            String user;
            user = "@ibanezebenezer";
            statuses = twitter.getUserTimeline(user);  
            for(Status status : statuses)
            {
            listItem = new Twitter_Item();
            listItem.image = status.getUser().getProfileImageURL();
            listItem.title =  status.getText().toString();
            listItem.name = status.getUser().getName();
            listItem.screenName = status.getUser().getScreenName();
         //   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          //  Format formatter = new SimpleDateFormat("yyyy-MM-dd");
          //  listItem.publishDate =formatter.format( status.getUser().getCreatedAt());
            arrayList.add(listItem);
            }
           
        } catch (TwitterException te) {
            te.printStackTrace();
        }
		return arrayList;
	}
}
