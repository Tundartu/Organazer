package com.example.org2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends AppCompatActivity {

    private Button deleteButton;
    private EditText eventNameEditText;
    private EditText eventDescriptionEditText;
    private TextView eventDateTextView;
    private TextView eventTimeTextView;
    private CheckBox notificationCheckBox;
    private Button saveButton;
    private Button cancelButton;
    private Button selectDateButton;
    private Button selectTimeButton;

    private Event event;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> deleteEvent());
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventTimeTextView = findViewById(R.id.eventTimeTextView);
        notificationCheckBox = findViewById(R.id.notificationCheckBox);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);

        if (getIntent().hasExtra("event")) {
            event = (Event) getIntent().getSerializableExtra("event");
            fillEventDetails(event);
        } else {
            event = new Event();
        }

        selectDateButton.setOnClickListener(v -> showDatePicker());
        selectTimeButton.setOnClickListener(v -> showTimePicker());

        saveButton.setOnClickListener(v -> saveEvent());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void deleteEvent() {
        if (event.getId() != null) {
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.deleteEvent(event.getId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EventDetailActivity.this, "Событие удалено", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish(); // Закрываем активность после успешного удаления
                    } else {
                        Toast.makeText(EventDetailActivity.this, "Ошибка удаления события", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EventDetailActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Не удалось удалить событие", Toast.LENGTH_SHORT).show();
        }
    }
    private void fillEventDetails(Event event) {

        eventNameEditText.setText(event.getName());
        eventDescriptionEditText.setText(event.getDescription());
        notificationCheckBox.setChecked(event.isNotification());
        SimpleDateFormat datestring = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        try {
             Date  date = datestring.parse(event.getEventDate());
             event.setOutDate(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (event.getEventDate() != null) {
            calendar.setTime(event.getOutDate());
        } else {
            // Установите текущее время как значение по умолчанию
            System.out.println(event.getEventDate());
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        updateDateTimeTextViews();

    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeTextViews();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateDateTimeTextViews();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void updateDateTimeTextViews() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        eventDateTextView.setText("Дата: " + dateFormat.format(calendar.getTime()));
        eventTimeTextView.setText("Время: " + timeFormat.format(calendar.getTime()));
    }

    private void saveEvent() {
        String overTime;
        event.setName(eventNameEditText.getText().toString());
        event.setDescription(eventDescriptionEditText.getText().toString());
        event.setNotification(notificationCheckBox.isChecked());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat datestring = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = calendar.getTime();
        String overDate = dateFormat.format(calendar.getTime());
        String overTime2 = timeFormat.format(calendar.getTime());
        overTime = datestring.format(date);
        System.out.println(overTime);
        event.setEventDate(overTime);
        event.setDate(overDate);
        event.setTime(overTime2);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Event> call;

        if (event.getId() != null) {
            call = apiService.updateEvent(event.getId(), event);
        } else {
            call = apiService.createEvent(event);
        }

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EventDetailActivity.this, "Событие сохранено", Toast.LENGTH_SHORT).show();
                    if (event.isNotification()) {
                        Calendar eventTime = Calendar.getInstance();
                        eventTime.setTime(date);
                        EventNotificationScheduler.scheduleNotification(EventDetailActivity.this, event.getName(), event.getDescription(), eventTime, event.getId().intValue());
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EventDetailActivity.this, "Ошибка сохранения события", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(EventDetailActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
