package com.malas.appsr.malasapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.fragment.TourPlanFragment;
import com.malas.appsr.malasapp.fragment.TourPlanHistoryFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class TourPlan extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private ViewPager viewPager;

    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_plan);
getSupportActionBar().hide();

        viewPager = findViewById(R.id.tour_viewpager);
        bottomNavigationView = findViewById(R.id.tour_navigation);

        bottomNavigationView.inflateMenu(R.menu.tour);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() ==
                    R.id.menu_tour_plan) {
                viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.menu_tour_history) {
                viewPager.setCurrentItem(1);
            }

            return false;
        });
        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) (new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                Log.d("page", "" + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        }));

        setupViewPager(viewPager);
    }

    public final void setupViewPager(@NotNull ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TourPlanFragment());
        adapter.addFragment(new TourPlanHistoryFragment());
        viewPager.setAdapter(adapter);
    }


    private static final class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mFragmentList = new ArrayList<>();
        }

        @NonNull
        public Fragment getItem(int position) {
            return (Fragment) this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(@NotNull Fragment fragment) {
            Intrinsics.checkNotNullParameter(fragment, "fragment");
            mFragmentList.add(fragment);
        }
    }
}