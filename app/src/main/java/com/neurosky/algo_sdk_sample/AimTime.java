package com.neurosky.algo_sdk_sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class AimTime extends DialogFragment {
    private static final int MAX_HOUR = 24;
    private static final int MIN_HOUR = 0;
    private static final int MAX_MINUTE = 59;
    private static final int MIN_MINUTE = 0;
    private TimePickerDialog.OnTimeSetListener listener;

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    Button btnOk;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.activity_aimtime, null);

        btnOk = dialog.findViewById(R.id.btn_ok);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.hourpick);
        final NumberPicker minPicker = (NumberPicker) dialog.findViewById(R.id.minpick);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AimTime.this.getDialog().cancel(); //취소버튼 누르면 취소되게.
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() { //시간선택시
            @Override
            public void onClick(View v) {

                AimTime.this.getDialog();
                {
                    Intent intent = new Intent(AimTime.this.getActivity(), GraphActivity.class);
                    intent.putExtra("pick","cn");

                    int hours = hourPicker.getValue();
                    int mins = minPicker.getValue();
                    String data = "목표시간 " + hours + "시간 " + mins + "분";

                    //데이터 가지고 이동
                    intent.putExtra("data", data);
                    intent.putExtra("hours", hours);

                    intent.putExtra("mins", mins);

                    startActivity(intent);
                }

            }
        });
        hourPicker.setMinValue(MIN_HOUR);
        hourPicker.setMaxValue(MAX_HOUR);
        minPicker.setMinValue(MIN_MINUTE);
        minPicker.setMaxValue(MAX_MINUTE);

        builder.setView(dialog);
        return builder.create();

    }

}
