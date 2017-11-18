package ua.tsisar.abetka.dialog;


import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;

import ua.tsisar.abetka.BuildConfig;
import ua.tsisar.abetka.R;

public class DialogAbout extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getString(R.string.about);
        String copyright = getString(R.string.copyright);
        //dp -> pixels
        int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                getActivity().getResources().getDisplayMetrics()));

        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.setPadding(0, 4, 0, 0);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(padding, padding, padding, padding);

        ImageView logo = new ImageView(getActivity());
        logo.setImageResource(R.mipmap.logo);

        TextView version = new TextView(getActivity());
        version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
        version.setGravity(Gravity.END);

        DocumentView documentView = new DocumentView(getActivity(), DocumentView.PLAIN_TEXT);  // Support plain text
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        documentView.setText(message); // Set to `true` to enable justification
//        documentView.getDocumentLayoutParams().setOffsetX(convertDpToPixels(32));

        TextView copyrightTextView = new TextView(getActivity());
        copyrightTextView.setText(copyright);
        copyrightTextView.setTextSize(14);
        copyrightTextView.setTextColor(getColor(R.color.colorBlack));

        Linkify.addLinks(copyrightTextView, Linkify.ALL);

        linearLayout.addView(logo);
        linearLayout.addView(version);
        linearLayout.addView(documentView);
        linearLayout.addView(copyrightTextView);

        scrollView.addView(linearLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(scrollView); // сообщение
        //builder.setTitle(R.string.pref_title_info_about);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setCancelable(true);

        return builder.create();
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
