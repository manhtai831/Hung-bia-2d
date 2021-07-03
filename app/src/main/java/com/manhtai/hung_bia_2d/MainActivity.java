package com.manhtai.hung_bia_2d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.manhtai.hung_bia_2d.common.StringConstant;

public class MainActivity extends AppCompatActivity {
    private TextView tvGamePlay;
    private TextView tvRank;
    private TextView tvSetting;
    private TextView tvAbout;
    private TextView tvExit;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        setEvents();
    }

    private void setEvents() {
        tvGamePlay.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,GamePlayActivity.class)));
        tvExit.setOnClickListener(v -> showDialogExit());
        tvSetting.setOnClickListener(v -> {
//            Interstitial ads
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                                mInterstitialAd.show(MainActivity.this);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                        }
                    });

        });

        tvAbout.setOnClickListener(v -> {
//            Native Ads
            AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd NativeAd) {
                            // Show the ad.

                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAds(new AdRequest.Builder().build(), 3);
        });
//        Reward ads
        tvRank.setOnClickListener(v -> {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.

                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            Activity activityContext = MainActivity.this;
                            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.

                                    int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();
                                }
                            });
                        }
                    });
        });
    }

    private void showDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(StringConstant.title);
        builder.setMessage(StringConstant.message);
        builder.setPositiveButton(StringConstant.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton(StringConstant.cancel, null);
        builder.show();
    }

    private void getView() {
        tvGamePlay = (TextView) findViewById(R.id.tv_game_play);
        tvRank = (TextView) findViewById(R.id.tv_rank);
        tvSetting = (TextView) findViewById(R.id.tv_setting);
        tvAbout = (TextView) findViewById(R.id.tv_about);
        tvExit = (TextView) findViewById(R.id.tv_exit);

    }
}