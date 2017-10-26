package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class HelpAbout extends AppCompatActivity {

    private SectionPageAdapter hSectionPageAdapter;
    private ViewPager hViewPager;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_about);

        hSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        hViewPager = (ViewPager) findViewById(R.id.container);

        createViewPager(hViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(hViewPager);
    }

    private void createViewPager (ViewPager viewPager) {
        SectionPageAdapter pageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        pageAdapter.addFrag("About", new AboutTabFragment());
        pageAdapter.addFrag("Help", new HelpTabFragment());
        pageAdapter.addFrag("Legal", new LegalTabFragment());
        viewPager.setAdapter(pageAdapter);
    }
}
