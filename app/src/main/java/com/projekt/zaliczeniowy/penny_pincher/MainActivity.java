package com.projekt.zaliczeniowy.penny_pincher;

import static com.projekt.zaliczeniowy.penny_pincher.ui.adapter.ViewPagerAdapter.ICONS;
import static com.projekt.zaliczeniowy.penny_pincher.ui.adapter.ViewPagerAdapter.TABS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.ViewPagerAdapter;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager2 vpViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tlTabs = findViewById(R.id.tabs);
        tlTabs.addOnTabSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vpViewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        vpViewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tlTabs, vpViewPager,
                (tab, position) -> {
                    tab.setText(TABS[position]);
                    tab.setIcon(ICONS[position]);
                }
        ).attach();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mnuCategory:
                intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                AnimationHelper.customType(MainActivity.this, "left-to-right");
                break;
            case R.id.mnuAbout:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                AnimationHelper.customType(MainActivity.this, "left-to-right");
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpViewPager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    public ViewPager2 getViewPager() {
        if (null == vpViewPager) {
            vpViewPager = findViewById(R.id.view_pager);
        }
        return vpViewPager;
    }

}