package com.ragab.ahmed.educational.happenings.ui.utility;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.ragab.ahmed.educational.happenings.ui.MainActivity;

import java.util.Calendar;

/**
 * Created by Ragabov on 6/14/2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private static String YEAR = "year";
    private static String MONTH = "month";
    private static String DAY = "day";

    private Calendar selectedDate;

    public static TimePickerFragment getInstance(int year, int month, int day)
    {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putInt(DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        selectedDate = Calendar.getInstance();
        Bundle bundle = getArguments();
        int year = bundle.getInt(YEAR);
        int month = bundle.getInt(MONTH);
        int day = bundle.getInt(DAY);

        selectedDate.set(year, month, day, hourOfDay, minute);

        ((MainActivity) getActivity()).getSelectedDate(selectedDate);
    }

}