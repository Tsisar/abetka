package ua.tsisar.abetka.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Random;

import ua.tsisar.abetka.R;
import ua.tsisar.abetka.fragment.PageFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private static final String POSITION = "position";
    private static final String OFFSET = "offset";

    private int count = 100;
    private boolean randomOffset;
    private FragmentActivity activity;

    public PagerAdapter(FragmentActivity activity, int count, boolean randomOffset) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
        this.count = count;
        this.randomOffset = randomOffset;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putInt(OFFSET, randomOffset ? genOffset(position % count) : 0);

        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(bundle);

        randomOffset = true;

        return pageFragment;
    }

    @Override
    public int getCount() {
        return count * 20;
    }

    private int genOffset(int position){
        return new Random().nextInt(
                activity.getResources().getIntArray(
                        activity.getResources().obtainTypedArray(R.array.illustration_array)
                                .getResourceId(position, R.array.illustration_array_000)).length);
    }
}
