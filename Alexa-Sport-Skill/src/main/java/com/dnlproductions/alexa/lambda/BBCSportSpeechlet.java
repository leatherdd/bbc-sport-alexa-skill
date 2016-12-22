package com.dnlproductions.alexa.lambda;

/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

 	http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
import java.util.List;
import java.util.Map;

import com.dnlproductions.alexa.RSSConnector;
import com.sun.syndication.feed.synd.SyndFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;


/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class BBCSportSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(com.dnlproductions.alexa.lambda.BBCSportSpeechlet.class);
	private static final String FEED_URL = "http://feeds.bbci.co.uk/sport/rss.xml?edition=int";

	private static final String WELCOME_RESPONSE = "Welcome to the BBC Sport App";
	private static final String POSSIBLE_QUESTIONS_RESPONSE = "You can ask the following questions: what's the latest sports news, or, give me the last 5 times";

	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		
		//session.setAttribute(k, v);
	}

	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
		} else if ("PossibleQuestionsIntent".equals(intentName)) {
			return getPossibleQuestionsResponse(intent, session);
		} else if ("AllSportsNewsIntent".equals(intentName)) {
			return getAllSportsNewsResponse(intent, session);
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		return getSpeechletResponse(WELCOME_RESPONSE, WELCOME_RESPONSE, true);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		return getSpeechletResponse(WELCOME_RESPONSE, WELCOME_RESPONSE, true);
	}
	

	/**
	 * Creates a {@code SpeechletResponse} for the cheat intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getPossibleQuestionsResponse(final Intent intent, final Session session) {
		return getSpeechletResponse(POSSIBLE_QUESTIONS_RESPONSE, POSSIBLE_QUESTIONS_RESPONSE, false);
	}
	
	/**
	 * Creates a {@code SpeechletResponse} for the gender intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getAllSportsNewsResponse(final Intent intent, final Session session) {

		Map<String, Slot> slots = intent.getSlots();

		/*
		// Get the slot from the list of slots.
		Slot genderSlot = slots.get(GENDER_SLOT);
		String name = (String) session.getAttribute(PERSON_ATTRIBUTE);
		*/

		SyndFeed feed = RSSConnector.getInstance().createFeed(FEED_URL);
		String speechText = RSSConnector.getInstance().getSetNumberOfItems(feed.getEntries(), 10);

		return getSpeechletResponse(speechText, speechText, true);
	}
	
	private SpeechletResponse getSpeechletResponse(String speechText, String repromptText, boolean isAskResponse) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("BBCSport");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
			// Create reprompt
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(repromptText);
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);

		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

}
