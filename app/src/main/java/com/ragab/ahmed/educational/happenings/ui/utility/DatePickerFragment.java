package com.ragab.ahmed.educational.happenings.ui.utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ragabov on 6/14/2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog with maxDate and return it
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.getDatePicker().setMaxDate(new Date().getTime());
        return datePicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Do something with the date chosen by the user
        DialogFragment newFragment2 = TimePickerFragment.getInstance(year, month, day);
        newFragment2.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
}