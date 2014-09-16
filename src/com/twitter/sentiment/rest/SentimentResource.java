package com.twitter.sentiment.rest;

import javax.ws.rs.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import twitter4j.Status;

import com.twitter.analytics.SentimentAnalysis;
import com.twitter.analytics.TweetWithSentiment;
import com.twitter.analytics.TwitterAnalytics;

;

@Path("setiments")
public class SentimentResource {

	@Inject
	private SentimentAnalysis sentimentAnalysis;

	@Inject
	private TwitterAnalytics twitterAnalytics;

	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<Result> sentiments(
			@QueryParam("searchKeywords") String searchKeywords) {
		List<Result> results = new ArrayList<>();

		if (searchKeywords == null || searchKeywords.length() == 0) {
			return results;
		}

		Set<String> keywords = new HashSet<String>();
		for (String keyword : searchKeywords.split("'")) {
			keywords.add(keyword.trim().toLowerCase());
		}

		if (keywords.size() > 3) {
			keywords = new HashSet<>(new ArrayList<>(keywords).subList(0, 3));
		}

		for (String keyword : keywords) {
			List<Status> statuses = twitterAnalytics.searchTweets(keyword);
			System.out.println("...... Found Statuses.... " + statuses.size());

			List<TweetWithSentiment> sentiments = new ArrayList<>();

			for (Status status : statuses) {
				TweetWithSentiment tweetWithSentiment = sentimentAnalysis
						.findSentiment(status.getText());

				if (tweetWithSentiment != null) {
					sentiments.add(tweetWithSentiment);
				}
			}
			Result result = new Result(keyword, sentiments);
			results.add(result);
		}

		return results;

	}

	@GET
	@Path("/text")
	@Produces(value = MediaType.APPLICATION_JSON)
	public TweetWithSentiment findNer(@QueryParam("text") String text) {
		if (text == null || text.length() == 0) {
			return null;
		}
		TweetWithSentiment tweetWithSentiment = sentimentAnalysis
				.findSentiment(text);
		return tweetWithSentiment;
	}

}
