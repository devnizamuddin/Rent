package com.example.rent.screen.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.rent.R;
import com.example.rent.screen.home.hotel.HotelListFragment;
import com.example.rent.screen.home.house.HouseListFragment;
import com.example.rent.screen.createads.CreateAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton create_ad;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        create_ad = view.findViewById(R.id.create_ad);

        create_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateAds.class));
            }
        });

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("House"));
        tabLayout.addTab(tabLayout.newTab().setText("Hotel"));

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int tabCount;

        public ViewPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    //House
                    HouseListFragment houseListFragment = new HouseListFragment();
                    return houseListFragment;
                case 1:
                    //Hotel
                    HotelListFragment hotelListFragment = new HotelListFragment();
                    return hotelListFragment;

                default:
                    return new HouseListFragment();
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


}
