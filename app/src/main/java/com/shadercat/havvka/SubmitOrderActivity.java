package com.shadercat.havvka;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SubmitOrderActivity extends AppCompatActivity {

    TextView numItems;
    TextView sum;
    TextView orderData;
    TextView orderTime;
    TextView changeDate;
    TextView changeTime;
    ImageView back_arrow;
    ImageView waitIc;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<Proposition> propositions = new ArrayList<>();
    Button button;
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();
    int proposition = 0;
    Animation animation;
    String[] array;
    Calendar calendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        numItems = (TextView) findViewById(R.id.num_of_items);
        sum = (TextView) findViewById(R.id.sum_of_items);
        orderData = (TextView) findViewById(R.id.order_date_set);
        orderTime = (TextView) findViewById(R.id.order_time_set);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.submit_btn);
        changeDate = (TextView) findViewById(R.id.changeDate);
        changeTime = (TextView) findViewById(R.id.changeTime);
        back_arrow = (ImageView) findViewById(R.id.back_arrow_order);
        waitIc = (ImageView) findViewById(R.id.ic_wait);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);


        parallelThread = new mWorkingThread("submitorderactivity");
        parallelThread.start();
        parallelThread.prepareHandler();

        Runnable propTask = new Runnable() {
            @Override
            public void run() {
                propositions = DataAdapter.GetProposition(getApplicationContext());
                array = new String[propositions.size()];
                for (int i = 0; i < propositions.size(); i++) {
                    array[i] = propositions.get(i).getName();
                }
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });
            }
        };
        parallelThread.postTask(propTask);

        setDate();
        numItems.setText(String.valueOf(CartHelper.list.size()));
        sum.setText(String.format(Locale.getDefault(), "%.2f UAH", CartHelper.GetSumPrice()));
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(false);
                                waitIc.setVisibility(View.VISIBLE);
                                waitIc.startAnimation(animation);
                            }
                        });
                        DataAdapter.SetOrder(getApplicationContext(), calendar, proposition, DataAdapter.ORDER_CASH_MODE);
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                waitIc.clearAnimation();
                                waitIc.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), getString(R.string.sendSubmit), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                parallelThread.postTask(task);
            }
        });
    }

    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();
            }
        }, year, month, day);
        dialog.show();
    }

    private void showTimeDialog() {
        final Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, // Context
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        setDate();
                    }
                },
                hourOfDay,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void setDate() {
        orderData.setText(String.format(Locale.getDefault(), "%d.%d.%d", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
        orderTime.setText(String.format(Locale.getDefault(), "%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

    }

    @Override
    protected void onDestroy() {
        if (parallelThread != null) {
            parallelThread.quit();
        }
        super.onDestroy();
    }
}
