package com.twitter.analytics;
import java.util.List;

import twitter4j.*;

public class TwitterAnalytics {
	public static void main(String []args) {
		String searchKeyword = "modi";
		searchTweets(searchKeyword);
	}
	public static List<Status> searchTweets(String searchKeyword) {
		Twitter twit = new TwitterFactory().getInstance();
		List <Status> tweets = null;
		SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
		TweetWithSentiment tweetWithSentiment;
		try {
			Query query = new Query (searchKeyword + " -filter:retweets -filter:links -filter:replies -filter:images");
			query.setLang("en");
			QueryResult result;
			
			do {
				result = twit.search(query);
				tweets = result.getTweets();
				
				for (Status tweet : tweets){
					
					System.out.println("@" + tweet.getUser().getScreenName() + ":-> "+ tweet.getText() );
					tweetWithSentiment = sentimentAnalysis.findSentiment(tweet.getText());
					System.out.println("Tweet Sentiment: " + tweetWithSentiment);
				}
			}while((query = result.nextQuery())!= null);
		}catch( TwitterException e){
			e.printStackTrace();
			System.out.println("Failed to search tweets");
		}
		return tweets;
		
	}
}
