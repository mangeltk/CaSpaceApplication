package com.example.caspaceapplication.Owner.BookingTransactions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.caspaceapplication.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    private final ArrayList<Integer> badgeCounts = new ArrayList<>();
    private final Context context;

    public ViewPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }


    public void addFragment(Fragment fragment, String title, int badgeCount){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
        badgeCounts.add(badgeCount);
    }


    private int getPendingBTCount() {
        return badgeCounts.get(0); // Get the badge count for the "Pending" tab
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = fragmentTitle.get(position);
        if (position == 0) {
            int badgeCount = getPendingBTCount();
            if (badgeCount > 0) {
                // Create a SpannableString with the fragment title and badge count
                String titleWithBadge = title + " (" + badgeCount + ")";
                SpannableString spannableString = new SpannableString(titleWithBadge);
                Drawable badgeDrawable = ContextCompat.getDrawable(context, R.drawable.backgroundnistephanie_ayawhilabti); // Replace with your badge drawable
                badgeDrawable.setBounds(0, 0, badgeDrawable.getIntrinsicWidth(), badgeDrawable.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(badgeDrawable, ImageSpan.ALIGN_BOTTOM);
                spannableString.setSpan(imageSpan, title.length() + 1, titleWithBadge.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }
        }
        return title;
    }
}