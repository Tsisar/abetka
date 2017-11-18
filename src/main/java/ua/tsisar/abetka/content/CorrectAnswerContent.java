package ua.tsisar.abetka.content;

import android.app.Activity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ua.tsisar.abetka.R;

public class CorrectAnswerContent {
    private View content;
    private ImageView[] stars = new ImageView[3];
    private Activity activity;

    public CorrectAnswerContent(Activity activity){
        super();
        this.activity = activity;
        this.content = activity.findViewById(R.id.content_correct_answer);
        this.stars[0] = activity.findViewById(R.id.correct_answer_star_1_img);
        this.stars[1] = activity.findViewById(R.id.correct_answer_star_2_img);
        this.stars[2] = activity.findViewById(R.id.correct_answer_star_3_img);
    }

    public void show(int star){
        int anim[] = {R.anim.anim_star_visible_3, R.anim.anim_star_visible_2,
                R.anim.anim_star_visible_1};

        content.setVisibility(View.VISIBLE);

        stars[0].setVisibility(View.GONE);
        stars[1].setVisibility(View.GONE);
        stars[2].setVisibility(View.GONE);

        for (int i = 0; i < star; i++){
            stars[i].setVisibility(View.VISIBLE);
            stars[i].startAnimation(
                    AnimationUtils.loadAnimation(activity, anim[i]));
        }
    }

    public void hide(){
        content.setVisibility(View.GONE);
    }
}
