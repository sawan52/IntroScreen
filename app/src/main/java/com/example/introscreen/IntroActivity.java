package com.example.introscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {


    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button nextButton;
    int position = 0;
    Button getStartedButton;
    Animation buttonAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when the activity is about to launch we need to check if it's opened before or not

        if (restorePrefData()){

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);

        // hide the action bar

        getSupportActionBar().hide();

        // init view
        tabIndicator = findViewById(R.id.tab_indicator);
        nextButton = findViewById(R.id.button_next);
        getStartedButton = findViewById(R.id.button_get_started);
        buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        // fill list screen
        final List<ScreenItem> mList = new ArrayList<>();

        mList.add(new ScreenItem("Superman", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.superman ));
        mList.add(new ScreenItem("Cycling", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.mountain_cycling));
        mList.add(new ScreenItem("Ice Skating", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.ice_skating));
        mList.add(new ScreenItem("Fresh Food", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.fresh_food));
        mList.add(new ScreenItem("Fast Delivery", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.fast_delivery ));
        mList.add(new ScreenItem("Easy Payment", "This color palette comprises primary and accent colors that can be used for illustration or to develop your brand colors. They’ve been designed to work harmoniously with each other.", R.drawable.easy_payment));

        // setup viewPager
        screenPager = findViewById(R.id.screen_view_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tabLayout with View pager

        tabIndicator.setupWithViewPager(screenPager);

        // next button click listener

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);
                }
                // when we reach to the last screen
                if (position == mList.size() - 1){

                    // show the GET STARTED button and hide the indicator & next button

                    loadLastScreen();

                }

            }
        });

        // tabLayout add change Listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1){

                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if (tab.getPosition() < mList.size()){

                    loadPreviousScreen();
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // get Started Button click listener

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open main Activity

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);

                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // we are going to use the Shared Preference for that

                savePrefData();
                finish();
            }
        });

    }


    private boolean restorePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("my_pref", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    // show the GET STARTED button and hide the indicator & next button
    private void loadLastScreen() {

        nextButton.setVisibility(View.INVISIBLE);
        getStartedButton.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        // add an animation in the getStartedButton
        // setup animation

        getStartedButton.setAnimation(buttonAnimation);

    }

    private void loadPreviousScreen(){

        nextButton.setVisibility(View.VISIBLE);
        getStartedButton.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);

    }
}
