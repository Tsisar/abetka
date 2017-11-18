package ua.tsisar.abetka.content;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import ua.tsisar.abetka.R;

public class PauseContent {
    private View content;
    private TextView answerLabel;
    private TextView starLabel;

    public PauseContent(Activity activity){
        super();
        this.content = activity.findViewById(R.id.content_pause);
        this.answerLabel =  activity.findViewById(R.id.pause_answer_label);
        this.starLabel = activity.findViewById(R.id.pause_star_label);
    }

    public void show(int answer, int star){
        String a = Integer.toString(answer);
        String s = Integer.toString(star);

        content.setVisibility(View.VISIBLE);
        answerLabel.setText(a);
        starLabel.setText(s);
    }

    public void hide(){
        content.setVisibility(View.GONE);
    }
}
