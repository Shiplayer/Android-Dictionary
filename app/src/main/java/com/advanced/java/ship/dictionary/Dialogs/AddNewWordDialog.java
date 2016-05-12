package com.advanced.java.ship.dictionary.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.advanced.java.ship.dictionary.R;

/**
 * Created by Anton on 12.05.2016.
 */
public class AddNewWordDialog extends DialogFragment{

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private String words;
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog, null);


        builder.setView(inflater.inflate(R.layout.dialog, null)).setPositiveButton(R.string.add, (dialog, which) -> {
            EditText editText = (EditText)v.findViewById(R.id.edit_word);
            words = editText.getText().toString();
            Log.i("word", "test");
        }).setNegativeButton(R.string.cancel, (dialog, which)-> {
            dialog.dismiss();
        });
        return builder.create();
    }

}
