package com.example.zivototekrug;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Elderly_person extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private TextView txtSaatOd, txtSaatDo, txtDatum, txtAdresa;
    private EditText editTextAktivnost, editTextOpisAktivnost;
    private RadioGroup radioGroup;
    private int saat, minuti;
    private Spinner spinerDenovi, spinner;
    private ArrayAdapter<CharSequence> adapter;

    private static final int REQ_CODE = 123;

    private double Lat,Log = 0;
    private String Address = "";

    private List<String> list = Arrays.asList("Понеделник", "Вторник", "Среда", "Четврток", "Петок","Сабота","Недела");
    private ImageButton signOut;
    private TextView save, activ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_person);

        txtSaatOd = (TextView) findViewById(R.id.txtSaatOd);
        txtSaatDo = (TextView) findViewById(R.id.txtSaatDo);
        txtDatum = (TextView) findViewById(R.id.txtDatum);
        txtAdresa = (TextView) findViewById(R.id.txtLokacija);
        editTextAktivnost = (EditText) findViewById(R.id.aktivnost);
        editTextOpisAktivnost = (EditText) findViewById(R.id.opis);
        spinerDenovi = (Spinner) findViewById(R.id.spinnerDenovi);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        spinner = (Spinner) findViewById(R.id.itnost);
        adapter = ArrayAdapter.createFromResource(this, R.array.itnost, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinerDenovi.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinerDenovi.setAdapter(adapter);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        activ = (Button) findViewById(R.id.activ);
        activ.setOnClickListener(this);
        signOut = (ImageButton) findViewById(R.id.signOut);
        signOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                zacuvaj();
                break;
            case R.id.activ:
                startActivity(new Intent(Elderly_person.this, Elderly_person_activities.class));
                break;
            case R.id.signOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Одјави се.");
                builder.setMessage("Дали сте сигурни?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Elderly_person.this, MainActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                });

                builder.setNegativeButton("Не", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if (requestCode == REQ_CODE) {
                Lat = data.getDoubleExtra("latitude", 0);
                Log = data.getDoubleExtra("longitude", 0);
                Address = data.getStringExtra("address");
                txtAdresa.setText(Address);
            }
        }
    }
    public void SelectTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(Elderly_person.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view1, int hourOfDay, int minute) {

                saat = hourOfDay;
                minuti = minute;

                String vreme = saat + ":" + minuti;
                SimpleDateFormat format24 = new SimpleDateFormat("HH:mm");
                try {
                    Date date = format24.parse(vreme);

                    if(view.getId() == txtSaatOd.getId()) {
                        txtSaatOd.setText(format24.format(date));
                    } else if (view.getId() == txtSaatDo.getId()) {
                        txtSaatDo.setText(format24.format(date));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },24,0,true);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(saat, minuti);
        timePickerDialog.show();
    }
    public void SelectDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Elderly_person.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        txtDatum.setText(date);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    public void Frekfencija(View view) {
        int ID = radioGroup.getCheckedRadioButtonId();

        if(R.id.povtorliva == ID) {
            spinerDenovi.setVisibility(View.VISIBLE);
            txtDatum.setVisibility(View.INVISIBLE);
        } else {
            spinerDenovi.setVisibility(View.INVISIBLE);
            txtDatum.setVisibility(View.VISIBLE);
        }
    }
    public void SelectLocation(View view) {
        Intent intent = new Intent(this, GoogleMaps.class);
        startActivityForResult(intent, REQ_CODE);
    }
    private void zacuvaj() {
        String Aktivnost = editTextAktivnost.getText().toString().trim();
        String OpisAktivnost = editTextOpisAktivnost.getText().toString().trim();
        String VremeOd = txtSaatOd.getText().toString().trim();
        String VremeDo = txtSaatDo.getText().toString().trim();
        String Datum = "";
        String Denovi = "";

        int ID = radioGroup.getCheckedRadioButtonId();

        if(R.id.povtorliva == ID) {
            Denovi = spinerDenovi.getSelectedItem().toString();
        } else {
            Datum = txtDatum.getText().toString().trim();
        }

        if(Aktivnost.equals("")) {
            editTextAktivnost.setError("Задолжително поле!");
            editTextAktivnost.requestFocus();
            return;
        }

        if(OpisAktivnost.equals("")) {
            editTextOpisAktivnost.setError("Задолжително поле!");
            editTextOpisAktivnost.requestFocus();
            return;
        }

        if(VremeOd.equals("")) {
            txtSaatOd.setError("Задолжително поле!");
            txtSaatOd.requestFocus();
            return;
        } else {
            txtSaatOd.setError(null);
        }

        if(VremeDo.equals("")) {
            txtSaatDo.setError("Задолжително поле!");
            txtSaatDo.requestFocus();
            return;
        } else {
            txtSaatDo.setError(null);
        }

        if(R.id.ednokratna == ID) {
            if(Datum.equals("")) {
                txtDatum.setError("Задолжително поле!");
                txtDatum.requestFocus();
                return;
            } else {
                txtDatum.setError(null);
            }
        }

        if(Address.equals("")) {
            txtAdresa.setError("Задолжително поле!");
            txtAdresa.requestFocus();
            return;
        } else {
            txtAdresa.setError(null);
        }

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Baranje baranje = new Baranje(UID, "", Aktivnost, OpisAktivnost, VremeOd, VremeDo, Datum, Denovi, Address, "Активно", Log, Lat, "", "", 0, 0, "", "" );
        FirebaseDatabase.getInstance().getReference("Baranja")
                .child(UUID.randomUUID().toString()).setValue(baranje).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Elderly_person.this, "Успешно поднесено барање за помош!", Toast.LENGTH_SHORT).show();
                    deleteData();
                } else {
                    Toast.makeText(Elderly_person.this, "Настана грешка.Обидете се повторно!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteData() {
        editTextAktivnost.setText("");
        editTextOpisAktivnost.setText("");
        radioGroup.check(R.id.ednokratna);
        txtSaatOd.setText("");
        txtSaatDo.setText("");
        txtDatum.setText("");
        txtAdresa.setText("");
        spinerDenovi.setVisibility(View.INVISIBLE);
        txtDatum.setVisibility(View.VISIBLE);
        spinerDenovi.setSelection(0);
    }
}