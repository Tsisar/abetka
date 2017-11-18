package ua.tsisar.abetka.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import ua.tsisar.abetka.R;


public class StrokeTextView extends AppCompatTextView {
    private int strokeColor= Color.TRANSPARENT;
    private int strokeWidth = 2;

    public StrokeTextView(Context context){
        super(context);
    }

    public StrokeTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        strokeColor = a.getColor(R.styleable.StrokeTextView_strokeColor, strokeColor);
        strokeWidth = a.getDimensionPixelSize(R.styleable.StrokeTextView_strokeWidth, strokeWidth);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas canvas){
        final ColorStateList textColor = getTextColors();

        TextPaint paint = this.getPaint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeMiter(10);
        this.setTextColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);

        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);

        setTextColor(textColor);
        super.onDraw(canvas);
    }
}
