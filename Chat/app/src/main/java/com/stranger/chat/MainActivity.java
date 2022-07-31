package com.stranger.chat;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ViewPager mainScreen;
    TabLayout tabs;
    TextView title;
    String[] titleText = {"Chat", "Call"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        tabs = findViewById(R.id.tabBar);
        mainScreen = findViewById(R.id.mainScreen);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), this, tabs.getTabCount());
        mainScreen.setAdapter(adapter);
tabs.setupWithViewPager(mainScreen);        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainScreen.setCurrentItem(tab.getPosition());
                title.setText(titleText[tab.getPosition()]);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}

class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int tabCount;

    public TabAdapter(@NonNull FragmentManager fm, Context context, int tabCount) {
        super(fm);
        this.context = context;
        this.tabCount = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ChatFragment();
        }
        return new CallFragment();
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}