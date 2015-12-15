package tweetreduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;


public class TwitterStreamConsumer extends Thread {

	// private static String CONSUMER_KEY = "0bQkZcnF0VGHrvJv6y55Yhuc7";//"xpRa8FuS4TvLdIuQmEZXaae6N";
	// private static String CONSUMER_SECRET = "JfIkF3wNWul9SVpIb8cKpeKZt2TKrLiDgigVLIhYolACvo5TAm";//"ccE6lFnzirvSjBttgzofyHyOnVkwF5aKQwK29qJBgpo0IoZHgz";

	private static String CONSUMER_KEY = "pSrw50yhAIus7xBVTgDx3HQxP";
	private static String CONSUMER_SECRET = "Q1lG6jN0mkv4zL8FWFglPu0WKGDEirplNBuCoPSOAfiZHBwf7F";


	private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";

    private String searchTerms;

    private long maxtime;

    public TwitterStreamConsumer(String searchTerms, long maxtime) {
        this.searchTerms = searchTerms;
        this.maxtime = maxtime;
    }

    public void run(){
		try{
			System.out.println("Starting Twitter public stream consumer thread.");

			// Enter your consumer key and secret below
			OAuthService service = new ServiceBuilder()
			                       .provider(TwitterApi.class)
			                       .apiKey(CONSUMER_KEY)
			                       .apiSecret(CONSUMER_SECRET)
			                       .build();

			// Set your access token
			Token accessToken = new Token(OurCredentials.ACCESS_TOKEN, OurCredentials.ACCESS_TOKEN_SECRET);

			// Let's generate the request
			System.out.println("Connecting to Twitter Public Stream");
			OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
			request.addHeader("version", "HTTP/1.1");
			request.addHeader("host", "stream.twitter.com");
			request.setConnectionKeepAlive(true);
			request.addHeader("user-agent", "Twitter Stream Reader");
            // request.addBodyParameter("track", "happy,satisfied,joyful,joyous,chherful,contented,delighted,ecstatic,depressed,disturbed,sad,sadness,upset,unhappy,troubled,disappointed"); // Set keywords you'd like to track here
//            request.addBodyParameter("track", "chocolate,cat,girl"); // Set keywords you'd like to track here
            request.addBodyParameter("track", this.searchTerms); // Set keywords you'd like to track here
			service.signRequest(accessToken, request);
			Response response = request.send();

			// Create a reader to read Twitter's stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));

			String line;

			// Création du fichier texte pour le programme
			File fichierTexte = new File ("res.txt");
			// Création de "l'écrivain"
			FileWriter ecrireFichier;
			// Instanciation de l'objet ecrireFichier qui va écrire dans fichierTexte.txt
			ecrireFichier = new FileWriter(fichierTexte);

            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;

			while ((line = reader.readLine()) != null && elapsedTime < this.maxtime) {
                ArrayList<String> information = new ArrayList<String>(Arrays.asList(line.split(",")));
                if(information != null && information.size() > 3){
                    String textInfo = information.get(3);
                    // System.out.println("Text info " + textInfo);

                    String tweetText = textInfo.substring(8, textInfo.length());
    				ecrireFichier.write(tweetText+ "\n");

                    elapsedTime = System.currentTimeMillis() - startTime;

                }

			}

		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
}
