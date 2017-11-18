package ua.tsisar.abetka.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ua.tsisar.abetka.db.DBHelper;
import ua.tsisar.abetka.game.GameResult;
import ua.tsisar.abetka.preference.LoadPreference;
import ua.tsisar.abetka.R;

import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_DATE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_STARS;

public class DialogStatistics extends DialogFragment {

    public DialogStatisticsListener listener;

    public interface DialogStatisticsListener{
        void onClearStatistics();
    }

    public void onDialogStatisticsListener(DialogStatisticsListener dialogStatisticsListener) {
        this.listener = dialogStatisticsListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogStatisticsListener) {
            this.listener = (DialogStatisticsListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<GameResult> gameResults = new DBHelper(getActivity()).getHeightResults();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(gameResults.size() >= 1) {
            final ViewGroup nullParent = null;
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View dialog_layout = inflater.inflate(R.layout.dialog_statistics, nullParent, false);
            drawBestResult(dialog_layout, gameResults);
            builder.setView(dialog_layout);
            builder.setPositiveButton(getString(R.string.clear), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    new DBHelper(getActivity()).deleteHeightResults();
                    listener.onClearStatistics();
                }
            });
        }else {
            builder.setMessage(R.string.dialog_null_row);
        }

        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setTitle(R.string.pref_title_info_statistics);
        builder.setCancelable(true);

        return builder.create();
    }

    private void drawBestResult(View view, ArrayList<GameResult> gameResults){
        int itemHeight = getListPreferredItemHeight();

        LinearLayout headLinearLayout = view.findViewById(R.id.statistics_head_linearLayout);
        LinearLayout bodyLinearLayout = view.findViewById(R.id.statistics_body_linearLayout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, itemHeight);
        layoutParams.weight = 1;

        LinearLayout.LayoutParams layoutParamsDate = new LinearLayout.LayoutParams(0, itemHeight);
        layoutParamsDate.weight = 2;

        addViews(headLinearLayout,
                imageView(R.mipmap.game_ok, layoutParams),
                imageView(R.mipmap.game_star, layoutParams),
                imageView(R.mipmap.ic_calendar, layoutParamsDate));

        for (GameResult gameResult : gameResults) {
            ImageView line = new ImageView(getActivity());
            line.setImageDrawable(getLine());
            bodyLinearLayout.addView(line);

            LinearLayout listLinearLayoutList = new LinearLayout(getActivity());
            listLinearLayoutList.setOrientation(LinearLayout.HORIZONTAL);

            addViews(listLinearLayoutList,
                    textView(String.valueOf(gameResult.getAnswer()), getColor(R.color.colorGreen), layoutParams),
                    textView(String.valueOf(gameResult.getStars()), getColor(R.color.colorBlackStar), layoutParams),
                    textView(gameResult.getDate(), getColor(R.color.colorBlack), layoutParamsDate));

            bodyLinearLayout.addView(listLinearLayoutList);
        }
    }

    private ImageView imageView(int resourceId, LinearLayout.LayoutParams layoutParams){
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(resourceId);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    private TextView textView(String text, int color, LinearLayout.LayoutParams layoutParams){
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(color);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void addViews(LinearLayout layout, View answer, View stars, View date){
        String orderBy = new LoadPreference(getActivity()).getStatisticsOrder();

        switch (orderBy){
            case VALUE_ORDER_STARS:
                layout.addView(stars);
                layout.addView(answer);
                layout.addView(date);
                break;

            case VALUE_ORDER_DATE:
                layout.addView(date);
                layout.addView(answer);
                layout.addView(stars);
                break;

            default:
                layout.addView(answer);
                layout.addView(stars);
                layout.addView(date);
                break;
        }
    }

    private int getListPreferredItemHeight(){
        android.util.TypedValue value = new android.util.TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) value.getDimension(metrics)/2;
    }

    @SuppressWarnings("deprecation")
    private Drawable getLine(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(R.drawable.line, getActivity().getTheme());
        } else {
            return getResources().getDrawable(R.drawable.line);
        }
    }

    @ColorInt
    @SuppressWarnings("deprecation")
    private int getColor(@ColorRes int resId) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(resId, getActivity().getTheme());
        }else {
            return getActivity().getResources().getColor(resId);
        }
    }
}