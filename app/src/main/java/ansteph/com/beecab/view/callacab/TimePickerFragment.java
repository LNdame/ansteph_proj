package ansteph.com.beecab.view.callacab;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import java.util.Calendar;

import ansteph.com.beecab.R;

/**
 * Created by loicStephan on 15/09/16.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //edtTime
        EditText edtTime = (EditText) getActivity().findViewById(R.id.edtTime);

        edtTime.setText(  String.valueOf(hourOfDay)+ " : "+ String.valueOf(minute));

    }
}
