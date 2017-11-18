package ua.tsisar.abetka.preference;

import android.content.Context;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;

public class SwitchPreferenceExt extends SwitchPreferenceCompat {

    public SwitchPreferenceExt(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SwitchPreferenceExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwitchPreferenceExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchPreferenceExt(Context context) {
        super(context);
    }

    public void performClick(){
        if (!isEnabled()) {
            return;
        }
        onClick();
    }

}
