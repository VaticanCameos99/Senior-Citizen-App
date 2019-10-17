package com.example.SeniorCitizenCare;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ContactRelationDialog extends DialogFragment {

    public String[] options = {"Caregiver", "Father", "Mother", "Son", "Daughter", "Spouse", "Friend"};
    DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Relation")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onClick(options[i]);
                    }
                });

        return builder.create();
    }

    public interface DialogListener {
        void onClick(String relation);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (DialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogListener.");
        }
    }
}
