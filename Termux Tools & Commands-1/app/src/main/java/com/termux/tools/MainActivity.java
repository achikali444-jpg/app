package com.termux.tools;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.termux.tools.adapter.ViewPagerAdapter;
import com.termux.tools.dialogs.AboutUsDialog;
import com.termux.tools.dialogs.ContactUsDialog;
import com.termux.tools.dialogs.PrivacyPolicyDialog;
import com.termux.tools.dialogs.TermsOfServiceDialog;
import com.termux.tools.fragments.OnlineResourcesFragment;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
	
	private DrawerLayout drawerLayout;
	private ViewPager2 viewPager;
	private TabLayout tabLayout;
	private NavigationView navigationView;
	private RewardedAd rewardedAd;
	private InterstitialAd interstitialAd;
	private InterstitialAd threeMinuteInterstitialAd;
	private RewardedInterstitialAd rewardedInterstitialAd;
	private boolean isAdLoading = false;
	private boolean isInterstitialLoading = false;
	private boolean isThreeMinuteInterstitialLoading = false;
	private boolean isRewardedInterstitialLoading = false;
	private SharedPreferences prefs;
	private static final String PREFS_NAME = "AdPrefs";
	private final String REWARDED_AD_UNIT_ID = "ca-app-pub-5654505690672290/9529127387";
	private final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5654505690672290/6153005063";
	private final String THREE_MINUTE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5654505690672290/7984961481";
	private final String REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5654505690672290/5550369838";
	private long backPressedTime = 0;
	private int previousTab = 0;
	
	// Timer variables for multi-tier ad system
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;
	private long startTime = 0;
	private boolean isTimerRunning = false;
	private boolean canShowThreeMinuteInterstitial = false;
	private boolean canShowRewardedInterstitial = false;
	private boolean hasShownThreeMinuteInterstitial = false;
	private static final long THREE_MINUTES_IN_MILLIS = 3 * 60 * 1000; // 3 minutes
	private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000; // 10 minutes
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize Firebase Analytics
		FirebaseAnalyticsHelper.initialize(this);
		FirebaseAnalyticsHelper.trackScreenView("MainActivity");
		
		// Initialize SharedPreferences
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		
		// Remove the default action bar
		if (getSupportActionBar() != null) {
			getSupportActionBar().hide();
		}
		
		initializeViews();
		setupViewPager();
		setupTabIcons();
		setupNavigationDrawer();
		initializeAdMob();
		startMultiTierTimer();
	}
	
	private void initializeViews() {
		drawerLayout = findViewById(R.id.drawerLayout);
		viewPager = findViewById(R.id.viewPager);
		tabLayout = findViewById(R.id.tabLayout);
		navigationView = findViewById(R.id.navigationView);
	}
	
	private void initializeAdMob() {
		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
				loadRewardedAd();
				loadInterstitialAd();
				loadThreeMinuteInterstitialAd();
				loadRewardedInterstitialAd();
			}
		});
	}
	
	private void loadRewardedAd() {
		if (isAdLoading) return;
		
		isAdLoading = true;
		AdRequest adRequest = new AdRequest.Builder().build();
		
		RewardedAd.load(this, REWARDED_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull RewardedAd ad) {
				rewardedAd = ad;
				isAdLoading = false;
			}
			
			@Override
			public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
				rewardedAd = null;
				isAdLoading = false;
			}
		});
	}
	
	private void loadInterstitialAd() {
		if (isInterstitialLoading) return;
		
		isInterstitialLoading = true;
		AdRequest adRequest = new AdRequest.Builder().build();
		
		InterstitialAd.load(this, INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull InterstitialAd ad) {
				interstitialAd = ad;
				isInterstitialLoading = false;
			}
			
			@Override
			public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
				interstitialAd = null;
				isInterstitialLoading = false;
			}
		});
	}
	
	private void loadThreeMinuteInterstitialAd() {
		if (isThreeMinuteInterstitialLoading) return;
		
		isThreeMinuteInterstitialLoading = true;
		AdRequest adRequest = new AdRequest.Builder().build();
		
		InterstitialAd.load(this, THREE_MINUTE_INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull InterstitialAd ad) {
				threeMinuteInterstitialAd = ad;
				isThreeMinuteInterstitialLoading = false;
			}
			
			@Override
			public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
				threeMinuteInterstitialAd = null;
				isThreeMinuteInterstitialLoading = false;
			}
		});
	}
	
	private void loadRewardedInterstitialAd() {
		if (isRewardedInterstitialLoading) return;
		
		isRewardedInterstitialLoading = true;
		AdRequest adRequest = new AdRequest.Builder().build();
		
		RewardedInterstitialAd.load(this, REWARDED_INTERSTITIAL_AD_UNIT_ID, adRequest,
		new RewardedInterstitialAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
				rewardedInterstitialAd = ad;
				isRewardedInterstitialLoading = false;
			}
			
			@Override
			public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
				rewardedInterstitialAd = null;
				isRewardedInterstitialLoading = false;
			}
		});
	}
	
	private void showRewardedAd() {
		if (rewardedAd != null) {
			// Track ad show
			FirebaseAnalyticsHelper.trackAdEvent("rewarded", "show");
			
			rewardedAd.show(this, new OnUserEarnedRewardListener() {
				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
					// Track reward earned
					FirebaseAnalyticsHelper.trackAdEvent("rewarded", "reward_earned");
					
					Toast.makeText(MainActivity.this,
					"Thank you for watching the ad! You've earned " +
					rewardItem.getAmount() + " " + rewardItem.getType(),
					Toast.LENGTH_LONG).show();
					
					loadRewardedAd();
				}
			});
			} else {
			Toast.makeText(this, "Loading ad... Please wait a moment.", Toast.LENGTH_SHORT).show();
			loadRewardedAd();
		}
	}
	
	private void showInterstitialAd() {
		if (interstitialAd != null) {
			// Track interstitial ad show
			FirebaseAnalyticsHelper.trackAdEvent("interstitial", "show");
			
			interstitialAd.show(this);
			loadInterstitialAd();
			} else {
			loadInterstitialAd();
		}
	}
	
	private void showThreeMinuteInterstitialAd() {
		if (threeMinuteInterstitialAd != null && canShowThreeMinuteInterstitial && !hasShownThreeMinuteInterstitial) {
			// Track 3-minute interstitial ad show
			FirebaseAnalyticsHelper.trackAdEvent("3min_interstitial", "show");
			
			threeMinuteInterstitialAd.show(this);
			// Mark as shown so it only shows once per session
			hasShownThreeMinuteInterstitial = true;
			// Load new ad for potential future use
			loadThreeMinuteInterstitialAd();
		}
	}
	
	private void showRewardedInterstitialAd() {
		if (rewardedInterstitialAd != null && canShowRewardedInterstitial) {
			// Track rewarded interstitial ad show
			FirebaseAnalyticsHelper.trackAdEvent("rewarded_interstitial", "show");
			
			rewardedInterstitialAd.show(this, new OnUserEarnedRewardListener() {
				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
					// Track reward earned
					FirebaseAnalyticsHelper.trackAdEvent("rewarded_interstitial", "reward_earned");
					
					Toast.makeText(MainActivity.this,
					"Thanks for supporting the app! You've earned " +
					rewardItem.getAmount() + " " + rewardItem.getType() +
					" for using the app for 10+ minutes!",
					Toast.LENGTH_LONG).show();
					
					resetMultiTierTimer();
					loadRewardedInterstitialAd();
				}
			});
			} else {
			resetMultiTierTimer();
			loadRewardedInterstitialAd();
		}
	}
	
	private void startMultiTierTimer() {
		startTime = System.currentTimeMillis();
		isTimerRunning = true;
		canShowThreeMinuteInterstitial = false;
		canShowRewardedInterstitial = false;
		
		timerRunnable = new Runnable() {
			@Override
			public void run() {
				long elapsedTime = System.currentTimeMillis() - startTime;
				
				if (elapsedTime >= THREE_MINUTES_IN_MILLIS && elapsedTime < TEN_MINUTES_IN_MILLIS) {
					// 3 minutes have passed - user can now see 3-minute interstitial on back button
					canShowThreeMinuteInterstitial = true;
					canShowRewardedInterstitial = false;
					} else if (elapsedTime >= TEN_MINUTES_IN_MILLIS) {
					// 10 minutes have passed - user can now see rewarded interstitial on back button
					canShowThreeMinuteInterstitial = false;
					canShowRewardedInterstitial = true;
					isTimerRunning = false;
					} else {
					// Continue timer (under 3 minutes)
					canShowThreeMinuteInterstitial = false;
					canShowRewardedInterstitial = false;
				}
				
				// Continue timer if still running
				if (isTimerRunning) {
					timerHandler.postDelayed(this, 1000); // Check every second
				}
			}
		};
		
		timerHandler.postDelayed(timerRunnable, 1000);
	}
	
	private void resetMultiTierTimer() {
		// Stop current timer
		if (timerHandler != null && timerRunnable != null) {
			timerHandler.removeCallbacks(timerRunnable);
		}
		
		// Reset flags
		canShowThreeMinuteInterstitial = false;
		canShowRewardedInterstitial = false;
		isTimerRunning = false;
		
		// Start new timer
		startMultiTierTimer();
	}
	
	private void handleMultiTierAdsOnBackButton() {
		// Priority: 10-minute rewarded interstitial > 3-minute interstitial
		if (canShowRewardedInterstitial) {
			showRewardedInterstitialAd();
			} else if (canShowThreeMinuteInterstitial && !hasShownThreeMinuteInterstitial) {
			showThreeMinuteInterstitialAd();
		}
	}
	
	private void handleWebTabClick() {
		showRewardedAd();
	}
	
	private void handleInterstitialAd(int newPosition) {
		if (previousTab == 1 && newPosition == 0) {
			showInterstitialAd();
		}
		previousTab = newPosition;
	}
	
	private void setupViewPager() {
		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		viewPager.setAdapter(adapter);
		
		new TabLayoutMediator(tabLayout, viewPager,
		(tab, position) -> {
			switch (position) {
				case 0:
				tab.setText("Home");
				break;
				case 1:
				tab.setText("Web");
				break;
				case 2:
				tab.setText("Tools");
				break;
			}
		}
		).attach();
		
		viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				handleInterstitialAd(position);
				handleTabVisibility(position);
				
				if (position == 1) {
					handleWebTabClick();
				}
			}
		});
		
		viewPager.setUserInputEnabled(false);
		handleTabVisibility(0);
	}
	
	private void setupTabIcons() {
		tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
		tabLayout.getTabAt(1).setIcon(R.drawable.ic_web);
		tabLayout.getTabAt(2).setIcon(R.drawable.ic_tools);
		
		tabLayout.getTabAt(0).select();
		
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				String tabName = "";
				
				switch (position) {
					case 0:
					tabName = "Home";
					tab.setIcon(R.drawable.ic_home_selected);
					handleTabVisibility(0);
					break;
					case 1:
					tabName = "Web";
					tab.setIcon(R.drawable.ic_web_selected);
					handleTabVisibility(1);
					handleWebTabClick();
					break;
					case 2:
					tabName = "Tools";
					tab.setIcon(R.drawable.ic_tools_selected);
					handleTabVisibility(2);
					break;
				}
				
				// Track tab selection
				FirebaseAnalyticsHelper.trackTabSelected(tabName);
				handleInterstitialAd(position);
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				switch (position) {
					case 0:
					tab.setIcon(R.drawable.ic_home);
					break;
					case 1:
					tab.setIcon(R.drawable.ic_web);
					break;
					case 2:
					tab.setIcon(R.drawable.ic_tools);
					break;
				}
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				if (position == 1) {
					handleWebTabClick();
				}
			}
		});
	}
	
	private void setupNavigationDrawer() {
		ImageView btnMenu = findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(navigationView);
			}
		});
		
		navigationView.setNavigationItemSelectedListener(item -> {
			int itemId = item.getItemId();
			int currentPosition = viewPager.getCurrentItem();
			
			if (itemId == R.id.nav_home) {
				handleInterstitialAd(0);
				viewPager.setCurrentItem(0, false);
				handleTabVisibility(0);
				} else if (itemId == R.id.nav_tools) {
				viewPager.setCurrentItem(2, false);
				handleTabVisibility(2);
				} else if (itemId == R.id.nav_web) {
				viewPager.setCurrentItem(1, false);
				handleTabVisibility(1);
				handleWebTabClick();
				} else if (itemId == R.id.nav_about) {
				AboutUsDialog aboutDialog = new AboutUsDialog(MainActivity.this);
				aboutDialog.show();
				} else if (itemId == R.id.nav_contact) {
				ContactUsDialog contactDialog = new ContactUsDialog(MainActivity.this);
				contactDialog.show();
				} else if (itemId == R.id.nav_privacy) {
				PrivacyPolicyDialog privacyDialog = new PrivacyPolicyDialog(MainActivity.this);
				privacyDialog.show();
				} else if (itemId == R.id.nav_terms) {
				TermsOfServiceDialog termsDialog = new TermsOfServiceDialog(MainActivity.this);
				termsDialog.show();
			}
			
			drawerLayout.closeDrawer(navigationView);
			return true;
		});
	}
	
	private void handleTabVisibility(int position) {
		View tabLayout = findViewById(R.id.tabLayout);
		
		if (tabLayout != null) {
			if (position == 1) {
				tabLayout.setVisibility(View.GONE);
				if (drawerLayout != null) {
					drawerLayout.setPadding(0, 0, 0, 0);
				}
				} else {
				tabLayout.setVisibility(View.VISIBLE);
				if (drawerLayout != null) {
					drawerLayout.setPadding(0, 0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// Handle multi-tier ads first (3-minute interstitial & 10-minute rewarded interstitial)
		handleMultiTierAdsOnBackButton();
		
		// Check if we're in the Web tab
		if (viewPager.getCurrentItem() == 1) {
			OnlineResourcesFragment webFragment = getWebFragment();
			
			if (webFragment != null) {
				WebView webView = webFragment.getWebView();
				
				if (webView != null && webView.canGoBack()) {
					webView.goBack();
					return;
					} else {
					viewPager.setCurrentItem(0, true);
					return;
				}
			}
		}
		
		// If we're in Home tab or Tools tab
		if (viewPager.getCurrentItem() == 0 || viewPager.getCurrentItem() == 2) {
			if (backPressedTime + 2000 > System.currentTimeMillis()) {
				super.onBackPressed();
				finish();
				} else {
				Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
			}
			backPressedTime = System.currentTimeMillis();
			} else {
			super.onBackPressed();
		}
	}
	
	private OnlineResourcesFragment getWebFragment() {
		try {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());
			if (fragment instanceof OnlineResourcesFragment) {
				return (OnlineResourcesFragment) fragment;
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handleTabVisibility(viewPager.getCurrentItem());
		
		if (rewardedAd == null && !isAdLoading) {
			loadRewardedAd();
		}
		
		if (interstitialAd == null && !isInterstitialLoading) {
			loadInterstitialAd();
		}
		
		if (threeMinuteInterstitialAd == null && !isThreeMinuteInterstitialLoading) {
			loadThreeMinuteInterstitialAd();
		}
		
		if (rewardedInterstitialAd == null && !isRewardedInterstitialLoading) {
			loadRewardedInterstitialAd();
		}
		
		// Resume timer if it was running
		if (isTimerRunning) {
			startMultiTierTimer();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Stop timer when app goes to background
		if (timerHandler != null && timerRunnable != null) {
			timerHandler.removeCallbacks(timerRunnable);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Clean up timer
		if (timerHandler != null && timerRunnable != null) {
			timerHandler.removeCallbacks(timerRunnable);
		}
	}
}