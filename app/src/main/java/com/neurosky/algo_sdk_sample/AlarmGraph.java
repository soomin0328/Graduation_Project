package com.neurosky.algo_sdk_sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class AlarmGraph extends DialogFragment {
    Handler mHandler = new Handler();

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.alarm_graph, null);

        builder.setView(dialog);

        return builder.create();
    }

}
