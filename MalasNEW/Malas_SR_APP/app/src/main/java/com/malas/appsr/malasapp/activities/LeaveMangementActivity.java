package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.fragment.CompoffRequestFragment;
import com.malas.appsr.malasapp.fragment.LeaveRequestfragment;
import com.malas.appsr.malasapp.fragment.RequestStatusListFragment;
import com.malas.appsr.malasapp.fragment.RevertLeaveRequestFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public final class LeaveMangementActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private ViewPager viewPager;

    private MenuItem prevMenuItem;



    @SuppressLint("NonConstantResourceId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leave);
        getSupportActionBar().hide();
        viewPager = (ViewPager)this.findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener((OnNavigationItemSelectedListener)(item -> {

            switch(item.getItemId()) {
                case R.id.menu_leave_request:
                   viewPager.setCurrentItem(0);
                    break;
                case  R.id.menu_compoff_request:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.menu_revert_leave_request:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.menu_status_of_request:
                    viewPager.setCurrentItem(3);
            }
            return false;
        }));
        viewPager.addOnPageChangeListener((OnPageChangeListener)(new OnPageChangeListener() {
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
                prevMenuItem=bottomNavigationView.getMenu().getItem(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        }));
        setupViewPager(viewPager);
    }

    public final void setupViewPager(@NotNull ViewPager viewPager) {
       ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LeaveRequestfragment());
        adapter.addFragment(new CompoffRequestFragment());
        adapter.addFragment(new RevertLeaveRequestFragment());
        adapter.addFragment(new RequestStatusListFragment());
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
            return (Fragment)this.mFragmentList.get(position);
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
