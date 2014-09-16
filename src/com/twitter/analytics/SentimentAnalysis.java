package com.twitter.analytics;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;

public class SentimentAnalysis {
	public TweetWithSentiment findSentiment(String line) {
		Properties prop = new Properties();
		prop.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

		StanfordCoreNLP pipeLine = new StanfordCoreNLP(prop);

		int mainSentiment = 0;

		if (line != null && line.length() > 0) {
			int longest = 0;
			Annotation annotation = pipeLine.process(line);

			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}
			}

		}
		if (mainSentiment == 2 | mainSentiment > 4 || mainSentiment < 0)
			return null;

		TweetWithSentiment tweetSentiment = new TweetWithSentiment(line,
				toCss(mainSentiment));
		return tweetSentiment;

	}

	private String toCss(int mainSentiment) {
		// TODO Auto-generated method stub
		switch (mainSentiment) {
		case 0:
			return "very negative";

		case 1:
			return "negative";

		case 2:
			return "Normal";

		case 3:
			return "Positive";

		case 4:
			return "very positive";
		}
		return null;
	}

	public static void main(String[] args) {
		SentimentAnalysis sentiment = new SentimentAnalysis();
		TweetWithSentiment tweetSentiment = sentiment
				.findSentiment("Modi");

		System.out.println(tweetSentiment);

	}
}
