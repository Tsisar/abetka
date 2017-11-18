package ua.tsisar.abetka.content;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import ua.tsisar.abetka.AlphabetItem;
import ua.tsisar.abetka.R;

public class IncorrectAnswerContent {
    private static final String FONT = "fonts/font.ttf";

    private View content;
    private Activity activity;
    private TextView letter;
    private TextView body;


    public IncorrectAnswerContent(Activity activity){
        super();
        this.activity = activity;
        this.content = activity.findViewById(R.id.content_incorrect_answer);
        this.letter = activity.findViewById(R.id.incorrect_answer_letter);
        this.body = activity.findViewById(R.id.incorrect_answer_body);
    }

    public void show(AlphabetItem alphabetItem){
        content.setVisibility(View.VISIBLE);
        if(alphabetItem != null){
            letter.setText(alphabetItem.getLetter());
            letter.setTextColor(alphabetItem.getColor());
            letter.setTypeface(Typeface.createFromAsset(activity.getAssets(), FONT));
            body.setText(alphabetItem.getName());
        }
    }

    public void hide(){
        content.setVisibility(View.GONE);
    }
}
