package com.reimbursement.furgetech.reimbursement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reimbursement.furgetech.reimbursement.Model.Pengeluaran;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahReimbursementActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databasePengeluaran;
    EditText editTextTanggal, editTextJenisPengeluaran, editTextBiaya, editTextKeterangan, editTextStatus;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_reimbursement);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tambah Data Pengeluaran");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        editTextTanggal = (EditText) findViewById(R.id.editTextTanggal);
        editTextJenisPengeluaran = (EditText) findViewById(R.id.editTextJenisPengeluaran);
        editTextBiaya = (EditText) findViewById(R.id.editTextBiaya);
        editTextKeterangan = (EditText) findViewById(R.id.editTextKeterangan);
        editTextStatus = (EditText) findViewById(R.id.editTextStatus);

        dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US);
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String date = sdf1.format(c1.getTime());
        editTextTanggal.setText(date);

        Button buttonAdd = (Button) findViewById(R.id.buttonTambahPengeluaran);

        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();
        databasePengeluaran = FirebaseDatabase.getInstance().getReference("reimbursement").child(id);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPengeluaran();
            }
        });

        Button buttonDate = (Button) findViewById(R.id.buttonOpenDatePicker);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        editTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                editTextTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addPengeluaran(){
        String tanggal = editTextTanggal.getText().toString().trim();
        String jenis_pengeluaran = editTextJenisPengeluaran.getText().toString().trim();
        String biaya = editTextBiaya.getText().toString().trim();
        String keterangan = editTextKeterangan.getText().toString().trim();
        String status = editTextStatus.getText().toString().trim();

        if(!TextUtils.isEmpty(tanggal)){

            String id = databasePengeluaran.push().getKey();
            Pengeluaran pengeluaran1 = new Pengeluaran(id, tanggal, jenis_pengeluaran, biaya, keterangan, status);
            databasePengeluaran.child(id).setValue(pengeluaran1);

            Toast.makeText(this, "Pengeluaran added", Toast.LENGTH_LONG).show();
            editTextTanggal.setText("");
            editTextJenisPengeluaran.setText("");
            editTextBiaya.setText("");
            editTextKeterangan.setText("");
            editTextStatus.setText("");
            editTextTanggal.setHint("Enter Tanggal");
            editTextJenisPengeluaran.setHint("Enter Title");
            editTextBiaya.setHint("Enter Biaya");
            editTextKeterangan.setHint("Enter Keterangan");
            editTextStatus.setHint("Enter Status");
            editTextTanggal.requestFocus();
            Intent intent = getIntent();
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "You should enter a new", Toast.LENGTH_LONG).show();
        }
    }
}

