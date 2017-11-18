package ua.tsisar.abetka.content;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ua.tsisar.abetka.R;

public class TopBarContent{
    private Activity activity;
    private TextView timeLabel;
    private ImageView timeProgressBar;
    private ImageView[] stars = new ImageView[3];

    public TopBarContent(Activity activity){
        super();
        this.activity = activity;
        this.timeLabel = activity.findViewById(R.id.game_time_label);
        this.timeProgressBar = activity.findViewById(R.id.game_time_progress_bar);
        this.stars[0] = activity.findViewById(R.id.game_star_1_img);
        this.stars[1] = activity.findViewById(R.id.game_star_2_img);
        this.stars[2] = activity.findViewById(R.id.game_star_3_img);
    }

    public void timeUpdate(int time){
        String timeString = Integer.toString(time);
        timeLabel.setText(timeString);

        circularImageBar(timeProgressBar, time);

        switch (time){
            case 10:
                stars[0].clearAnimation();
                stars[1].clearAnimation();
                stars[2].clearAnimation();
                break;
            case 8:
            case 5:
            case 2:
                startInvisibleAnimation(stars[time/3]);
                break;
        }
    }

    public void topBarStarsAnimation(int s){
        for(int i = 0; i < s; i++){
            startInvisibleAnimation(stars[i]);
        }
    }

    private void startInvisibleAnimation(View view){
        view.startAnimation(
                AnimationUtils.loadAnimation(activity, R.anim.anim_star_invisible));
    }

    private void circularImageBar(ImageView imageView, int i) {

        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(activity.getResources().getColor(R.color.colorWhite));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        oval.set(50,50,250,250);
        canvas.drawArc(oval, 270, ((i*360)/10), false, paint);
        imageView.setImageBitmap(bitmap);
    }
}
