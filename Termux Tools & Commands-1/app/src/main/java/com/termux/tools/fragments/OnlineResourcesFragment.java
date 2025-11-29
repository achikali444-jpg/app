package com.termux.tools.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.termux.tools.R;
import com.termux.tools.FirebaseAnalyticsHelper;
import android.os.Handler;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class OnlineResourcesFragment extends Fragment {
	
	private WebView webView;
	private TextView internetMessage;
	private ProgressBar loadingProgressBar;
	private FrameLayout adContainer;
	private AdView bannerAdView;
	private boolean isFirstLoad = true;
	private boolean isAdShowing = false;
	private Handler scrollHandler = new Handler();
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-5654505690672290/3733056983";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online_resources, container, false);
		
		webView = view.findViewById(R.id.webView);
		internetMessage = view.findViewById(R.id.internetMessage);
		loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
		adContainer = view.findViewById(R.id.adContainer);
		
		// Remove any padding or margins that might be causing white space
		webView.setPadding(0, 0, 0, 0);
		
		setupWebView();
		setupScrollDetection();
		createBannerAd(); // Create ad but don't show yet
		
		return view;
	}
	
	private void createBannerAd() {
		// Create the banner ad view with standard BANNER size
		bannerAdView = new AdView(requireContext());
		bannerAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
		bannerAdView.setAdUnitId(BANNER_AD_UNIT_ID);
		
		// Set layout params for full width
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
		FrameLayout.LayoutParams.MATCH_PARENT,
		FrameLayout.LayoutParams.WRAP_CONTENT
		);
		bannerAdView.setLayoutParams(layoutParams);
		
		// Load the ad
		AdRequest adRequest = new AdRequest.Builder().build();
		bannerAdView.loadAd(adRequest);
		
		bannerAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
			@Override
			public void onAdLoaded() {
				// Track banner ad loaded
				FirebaseAnalyticsHelper.trackAdEvent("banner", "loaded");
			}
			
			@Override
			public void onAdFailedToLoad(LoadAdError adError) {
				// Track banner ad failed
				FirebaseAnalyticsHelper.trackAdEvent("banner", "failed");
			}
		});
	}
	
	private void setupWebView() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
				if (isFirstLoad) {
					showLoading();
				}
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				hideLoading();
				hideInternetMessage();
				isFirstLoad = false;
				
				// Track web page view
				android.os.Bundle bundle = new android.os.Bundle();
				bundle.putString("url", url);
				FirebaseAnalyticsHelper.trackCustomEvent("web_page_view", bundle);
				
				// Inject scroll detection after page loads
				injectScrollDetectionJS();
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				hideLoading();
				showInternetMessage();
				isFirstLoad = false;
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		
		showLoading();
		webView.loadUrl("https://app.achik.us");
	}
	
	private void setupScrollDetection() {
		webView.addJavascriptInterface(new Object() {
			@android.webkit.JavascriptInterface
			public void onBottomReached() {
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showBannerAd();
						}
					});
				}
			}
			
			@android.webkit.JavascriptInterface
			public void onScrolledAwayFromBottom() {
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							hideBannerAd();
						}
					});
				}
			}
		}, "Android");
	}
	
	private void injectScrollDetectionJS() {
		String javascript =
		"var isAtBottom = false;" +
		"function checkScroll() {" +
		"    var scrollTop = window.pageYOffset || document.documentElement.scrollTop;" +
		"    var windowHeight = window.innerHeight;" +
		"    var documentHeight = document.documentElement.scrollHeight;" +
		"    var currentIsAtBottom = (windowHeight + scrollTop) >= (documentHeight - 100);" +
		"" +
		"    if (currentIsAtBottom && !isAtBottom) {" +
		"        Android.onBottomReached();" +
		"        isAtBottom = true;" +
		"    } else if (!currentIsAtBottom && isAtBottom) {" +
		"        Android.onScrolledAwayFromBottom();" +
		"        isAtBottom = false;" +
		"    }" +
		"}" +
		"" +
		"window.addEventListener('scroll', checkScroll);" +
		"window.addEventListener('resize', checkScroll);" +
		"setInterval(checkScroll, 500);";
		
		webView.evaluateJavascript(javascript, null);
	}
	
	private void showBannerAd() {
		if (isAdShowing || bannerAdView == null) {
			return;
		}
		
		isAdShowing = true;
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Remove ad first to ensure clean state
				if (bannerAdView.getParent() != null) {
					((ViewGroup) bannerAdView.getParent()).removeView(bannerAdView);
				}
				
				// Add ad to container
				adContainer.addView(bannerAdView);
				adContainer.setVisibility(View.VISIBLE);
				
				// Track banner ad shown
				FirebaseAnalyticsHelper.trackAdEvent("banner", "shown");
				
				// Slide up animation
				bannerAdView.setTranslationY(bannerAdView.getHeight());
				bannerAdView.animate()
				.translationY(0)
				.setDuration(300)
				.start();
			}
		});
	}
	
	private void hideBannerAd() {
		if (!isAdShowing || bannerAdView == null) return;
		
		isAdShowing = false;
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Slide down animation
				bannerAdView.animate()
				.translationY(bannerAdView.getHeight())
				.setDuration(300)
				.withEndAction(new Runnable() {
					@Override
					public void run() {
						// Completely remove the ad from layout
						if (bannerAdView.getParent() != null) {
							((ViewGroup) bannerAdView.getParent()).removeView(bannerAdView);
						}
						adContainer.setVisibility(View.GONE);
					}
				})
				.start();
			}
		});
	}
	
	// ... rest of your existing methods (showLoading, hideLoading, etc.) remain the same
	private void showLoading() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadingProgressBar.setVisibility(View.VISIBLE);
				internetMessage.setVisibility(View.GONE);
				webView.setVisibility(View.GONE);
				hideBannerAd(); // Hide ad during loading
			}
		});
	}
	
	private void hideLoading() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadingProgressBar.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void showInternetMessage() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				internetMessage.setVisibility(View.VISIBLE);
				loadingProgressBar.setVisibility(View.GONE);
				webView.setVisibility(View.GONE);
				hideBannerAd(); // Hide ad when showing error
			}
		});
	}
	
	private void hideInternetMessage() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				internetMessage.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		hideBottomBar();
		if (webView != null) {
			webView.onResume();
		}
		if (bannerAdView != null) {
			bannerAdView.resume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		showBottomBar();
		if (webView != null) {
			webView.onPause();
		}
		if (bannerAdView != null) {
			bannerAdView.pause();
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		showBottomBar();
		if (webView != null) {
			webView.destroy();
			webView = null;
		}
		if (bannerAdView != null) {
			bannerAdView.destroy();
			bannerAdView = null;
		}
		if (scrollHandler != null) {
			scrollHandler.removeCallbacksAndMessages(null);
		}
	}
	
	private void hideBottomBar() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View tabLayout = getActivity().findViewById(R.id.tabLayout);
				if (tabLayout != null) {
					tabLayout.setVisibility(View.GONE);
				}
			}
		});
	}
	
	private void showBottomBar() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View tabLayout = getActivity().findViewById(R.id.tabLayout);
				if (tabLayout != null) {
					tabLayout.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public WebView getWebView() {
		return webView;
	}
}