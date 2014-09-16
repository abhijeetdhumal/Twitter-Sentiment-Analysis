package com.twitter.sentiment.rest;

import java.util.List;

import com.twitter.analytics.TweetWithSentiment;

public class Result {
	private String keyword;
	private List <TweetWithSentiment> sentiments;
	
	public Result(){
		
	}
	public Result(String keyword, List<TweetWithSentiment> sentiments) {
		super();
		this.keyword = keyword;
		this.sentiments = sentiments;
	}
	public String getKeyword() {
		return keyword;
	}
	public List<TweetWithSentiment> getSentiments() {
		return sentiments;
	}
	
	
}
