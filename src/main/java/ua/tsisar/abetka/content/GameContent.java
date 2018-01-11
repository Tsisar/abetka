package ua.tsisar.abetka.content;

import android.app.Activity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import ua.tsisar.abetka.AlphabetItem;
import ua.tsisar.abetka.game.GameListener;
import ua.tsisar.abetka.R;
import ua.tsisar.abetka.adapter.GameAdapter;

public class GameContent implements AdapterView.OnItemClickListener{
    private Activity activity;
    private View content;
    private ImageView illustration;
    private TextView label;
    private GridView gridView;
    private GameContentListener listener;

    public interface GameContentListener{
        void onItemClick(int position);
    }

    public GameContent(Activity activity){
        super();
        this.activity = activity;
        if (activity instanceof GameListener) {
            this.listener = (GameContentListener) activity;
        }
        this.content = activity.findViewById(R.id.content_game);
        this.illustration = activity.findViewById(R.id.game_illustration);
        this.label = activity.findViewById(R.id.game_label);
        this.gridView = activity.findViewById(R.id.game_grid_view);
        this.gridView.setOnItemClickListener(this);
    }

    public void show(){
        content.setVisibility(View.VISIBLE);
    }

    public void hide(){
        content.setVisibility(View.GONE);
    }

    public void drawCurrentLetter(AlphabetItem alphabetItem, GameAdapter gameAdapter){
        String name = ".." + alphabetItem.getName();
        this.label.setText(name);
        illustration.setImageResource(alphabetItem.getIllustration());
        gridView.setAdapter(gameAdapter);
    }

    public void secondHint(int position){
        int p1 = new Random().nextInt(4);

        while (p1 == position){
            p1 = new Random().nextInt(4);
        }

        int p2 = new Random().nextInt(4);

        while (p2 == p1 || p2 == position){
            p2 = new Random().nextInt(4);
        }
        gridView.getChildAt(p1).setVisibility(View.INVISIBLE);
        gridView.getChildAt(p2).setVisibility(View.INVISIBLE);
    }

    public void thirdHint(int position) {
        View view = gridView.getChildAt(position);
        view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_hint));
        view.setBackground(activity.getResources().getDrawable(R.drawable.background_red_stroke));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        listener.onItemClick(position);
    }
}
