package ua.tsisar.abetka.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ua.tsisar.abetka.R;

public class DialogExit extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_exit_title));  // заголовок
        builder.setMessage(getString(R.string.dialog_exit_msg)); // сообщение
        builder.setNegativeButton(getString(R.string.dialog_cancel), null);
        builder.setPositiveButton(getString(R.string.dialog_exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                (getActivity()).finish();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
