package com.devapps.igor.Screens.DiceRoller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devapps.igor.R;

public class NewRollDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    NewRollDialogListener mListener;

    private EditText _typeInput;
    private EditText _numberInput;
    private EditText _modifierInput;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_roll_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        _typeInput = (EditText) view.findViewById(R.id.roll_dialog_type_input);
        _numberInput = (EditText) view.findViewById(R.id.roll_dialog_number_input);
        _modifierInput = (EditText) view.findViewById(R.id.roll_dialog_modifier_input);

        builder.setTitle(R.string.roll_dialog_title)
                // Add action buttons
                .setPositiveButton(R.string.confirm_roll, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (Integer.parseInt(_typeInput.getText().toString()) < 1) {
                            Toast.makeText(getActivity(), R.string.incorrect_dice_type, Toast.LENGTH_SHORT).show();
                        } else {
                            // Make de roll
                            mListener.onDialogPositiveClick(Integer.parseInt(_typeInput.getText().toString()),
                                    Integer.parseInt(_numberInput.getText().toString()),
                                    Integer.parseInt(_modifierInput.getText().toString()));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_roll, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewRollDialogFragment.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewRollDialogListener {
        public void onDialogPositiveClick(int diceType, int diceNumber, int diceModifier);
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewRollDialogListener) {
            mListener = (NewRollDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NewRollDialogListener ");
        }
    }
}
