package ua.tsisar.abetka.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ua.tsisar.abetka.R;

public class GridAdapter extends ArrayAdapter<String> {
    private static final String FONT = "fonts/font.ttf";

    private final Context context;
    private final String[] values;
    private Typeface typeface;

    public GridAdapter(Context context, String[] values) {
        super(context, R.layout.item_grid_view, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        int size = getRowSize();
        ListHolder holder;

        if(row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(R.layout.item_grid_view, parent, false);
            ViewGroup.LayoutParams layoutParams = row.getLayoutParams();

            layoutParams.height = size; //this is in pixels
            layoutParams.width = size;
            row.setLayoutParams(layoutParams);

            holder = new ListHolder();

            holder.image = row.findViewById(R.id.grid_image_view);
            holder.letter =  row.findViewById(R.id.grid_text_view);
            typeface = Typeface.createFromAsset(context.getAssets(), FONT);

            row.setTag(holder);
        } else {
            holder = (ListHolder)row.getTag();
        }

        // Відступи для букв
        RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lParams.addRule(RelativeLayout.ALIGN_TOP);
        lParams.addRule(RelativeLayout.ALIGN_LEFT);
        lParams.setMargins(size/16, size/32, 0, 0);
        holder.letter.setLayoutParams(lParams);

        // Десь тут потрібно встановити розмір букви
        holder.letter.setTextSize(pixelsToSp(size/3));
        // Малюэмо букву
        holder.letter.setText(values[position]);
        // Встановлюємо колір букви
        holder.letter.setTextColor(context.getResources()
                .obtainTypedArray(R.array.letters_color_array).getColor(position, 0xFF000000));
        holder.letter.setTypeface(typeface);
        // Малюнок до букви
        holder.image.setImageDrawable(context.getResources()
                .obtainTypedArray(R.array.lite_mipmap_id_array).getDrawable(position));

        return row;
    }

    // Розраховуємо ширину ітема
    private int getRowSize(){
        int dimension = (int) context.getResources().getDimension(R.dimen.activity_horizontal_padding);
        Point size = getMyScreenSize();
        if(size.x < size.y) {
            return (size.x - convertDpToPixels(dimension)*4)/3;
        }else {
            return (size.x - convertDpToPixels(dimension)*4)/5;
        }
    }

    private Point getMyScreenSize() {
        Point size = new Point();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }

    private int convertDpToPixels(float dp){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private float pixelsToSp(float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    private static class ListHolder{
        ImageView image;
        TextView letter;
    }
}