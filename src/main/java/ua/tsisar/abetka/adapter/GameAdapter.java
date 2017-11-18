package ua.tsisar.abetka.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.tsisar.abetka.AlphabetItem;
import ua.tsisar.abetka.R;

public class GameAdapter extends ArrayAdapter<Integer> {
    private static final String FONT = "fonts/font.ttf";

    private final Context context;
    private final int layoutResourceId;
    private Typeface typeface;
    private ArrayList<Integer> data = new ArrayList<>();

    public GameAdapter(Context context, int layoutResourceId, ArrayList<Integer> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ListHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();

            holder.letter = row.findViewById(R.id.game_gv_textView);
            typeface = Typeface.createFromAsset(context.getAssets(), FONT);

            row.setTag(holder);
        } else {
            holder = (ListHolder) row.getTag();
        }

        AlphabetItem alphabetItem = new AlphabetItem(context.getResources(), data.get(position), 0);

        holder.letter.setText(alphabetItem.getLetter());
        holder.letter.setTextColor(alphabetItem.getColor());
        holder.letter.setTypeface(typeface);

        return row;
    }

    static class ListHolder {
        TextView letter;
    }
}
