package com.example.hqb98.mj.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.example.hqb98.mj.activity.SettingActivity;
import com.example.hqb98.mj.callback.DatePickerListenner;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerListenner listenner;
    public DatePickerFragment(DatePickerListenner datePickerListenner){
        this.listenner = datePickerListenner;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listenner.getDate(year,month,dayOfMonth);
    }
}
