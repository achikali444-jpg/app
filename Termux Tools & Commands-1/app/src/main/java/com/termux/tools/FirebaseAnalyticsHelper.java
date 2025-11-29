package com.termux.tools;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsHelper {
	private static FirebaseAnalytics mFirebaseAnalytics;
	
	public static void initialize(Context context) {
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
	}
	
	// Track screen views
	public static void trackScreenView(String screenName) {
		if (mFirebaseAnalytics != null) {
			Bundle bundle = new Bundle();
			bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
			bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
			mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
		}
	}
	
	// Track tab selections
	public static void trackTabSelected(String tabName) {
		if (mFirebaseAnalytics != null) {
			Bundle bundle = new Bundle();
			bundle.putString("tab_name", tabName);
			mFirebaseAnalytics.logEvent("tab_selected", bundle);
		}
	}
	
	// Track ad events
	public static void trackAdEvent(String adType, String event) {
		if (mFirebaseAnalytics != null) {
			Bundle bundle = new Bundle();
			bundle.putString("ad_type", adType);
			bundle.putString("ad_event", event);
			mFirebaseAnalytics.logEvent("ad_interaction", bundle);
		}
	}
	
	// Track custom events - ADD THIS METHOD
	public static void trackCustomEvent(String eventName, Bundle params) {
		if (mFirebaseAnalytics != null) {
			mFirebaseAnalytics.logEvent(eventName, params);
		}
	}
}