package ua.tsisar.abetka.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.View;
import android.view.animation.AnimationUtils;

import ua.tsisar.abetka.preference.LoadPreference;
import ua.tsisar.abetka.R;
import ua.tsisar.abetka.sound.SoundHelper;
import ua.tsisar.abetka.sound.SoundManager;
import ua.tsisar.abetka.adapter.PagerAdapter;
import ua.tsisar.abetka.fragment.PageFragment;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, PageFragment.OnMapChangedListener{
    private static final String POSITION = "position";
    private static final String RANDOM_OFFSET = "random_offset";

    private ViewPager viewPager;

    private SparseIntArray sparseIntArray = new SparseIntArray();
    private SoundHelper soundHelper;
    private SoundManager soundManager;

    private boolean playLetter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadPreference loadPreference = new LoadPreference(this);

        // Якщо було передано позицію
        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITION, 0);
        // TODO Розібратися з відступом, змінити за замовчуванням на TRUE
        boolean randomOffset = intent.getBooleanExtra(RANDOM_OFFSET, false);

        // Кількість букв
        int count = getResources().obtainTypedArray(R.array.letters_array).length();

//        sparseIntArray = new SparseIntArray();
        soundHelper = new SoundHelper(this);
        soundManager = new SoundManager(this);

        PagerAdapter pagerAdapter = new PagerAdapter(this, count, randomOffset);

        if(!loadPreference.isButtonsVisible()) {
            findViewById(R.id.cursor_left).setVisibility(View.GONE);
            findViewById(R.id.cursor_right).setVisibility(View.GONE);
        }

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(position + count * 10);

    }

    @Override
    protected void onDestroy(){
        soundManager.release();
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        soundManager.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        soundManager.stop();
        soundManager.play(soundHelper.getMainSounds(position, sparseIntArray.get(position)));
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    public void onClickLeft(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_on_click));
    }

    public void onClickRight(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_on_click));
    }

    public void onClickIllustration(View view){
        int currentItem = viewPager.getCurrentItem();
        soundManager.play(soundHelper.getName(currentItem, sparseIntArray.get(currentItem)));
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_on_click));
    }

    public void onClickLetter(View view){
        soundManager.play(playLetter ? soundHelper.getLetter(viewPager.getCurrentItem())
            :soundHelper.getNoise(viewPager.getCurrentItem()));
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_on_click));
        playLetter=!playLetter;
    }

    @Override
    public void onMapChanged(int position, int offset) {
        if(sparseIntArray != null) {
            sparseIntArray.put(position, offset);
//            Log.d("onMapChanged", sparseIntArray.toString());
        }
    }
}
