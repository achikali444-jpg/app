package com.termux.tools.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.termux.tools.fragments.HomeFragment;
import com.termux.tools.fragments.OfflineResourcesFragment;
import com.termux.tools.fragments.OnlineResourcesFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new OnlineResourcesFragment();  // Web at position 1
            case 2:
                return new OfflineResourcesFragment(); // Tools at position 2
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}