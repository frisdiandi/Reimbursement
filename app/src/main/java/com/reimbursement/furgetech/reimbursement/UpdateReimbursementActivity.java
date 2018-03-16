package com.reimbursement.furgetech.reimbursement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class UpdateReimbursementActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databasePengeluaran;
    EditText editTextTanggal, editTextJenisPengeluaran, editTextBiaya, editTextKeterangan, editTextStatus;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reimbursement);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Update Data Pengeluaran");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("pengeluaranID");
        final String tanggal = intent.getStringExtra("pengeluaranTanggal");
        final String jenis = intent.getStringExtra("pengeluaranJenisPengeluaran");
        final String biaya = intent.getStringExtra("pengeluaranBiaya");
        final String keterangan = intent.getStringExtra("pengeluaranKeterangan");
        final String status = intent.getStringExtra("pengeluaranStatus");

        dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US);

        editTextTanggal = (EditText) findViewById(R.id.editTextTanggal);
        editTextJenisPengeluaran = (EditText) findViewById(R.id.editTextJenisPengeluaran);
        editTextBiaya = (EditText) findViewById(R.id.editTextBiaya);
        editTextKeterangan = (EditText) findViewById(R.id.editTextKeterangan);
        editTextStatus = (EditText) findViewById(R.id.editTextStatus);

        editTextTanggal.setText(tanggal);
        editTextJenisPengeluaran.setText(jenis);
        editTextBiaya.setText(biaya);
        editTextKeterangan.setText(keterangan);
        editTextStatus.setText(status);

        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdatePengeluaran);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tgl = editTextTanggal.getText().toString().trim();
                final String jen = editTextJenisPengeluaran.getText().toString().trim();
                final String biay = editTextBiaya.getText().toString().trim();
                final String ket = editTextKeterangan.getText().toString().trim();
                final String stat = editTextStatus.getText().toString().trim();
                updatePengeluaran(id, tgl, jen, biay, ket, stat);
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

    private boolean updatePengeluaran(String id, String tanggal, String jenis_pengeluaran, String biaya, String keterangan, String status){
        FirebaseUser user = mAuth.getCurrentUser();
        final String ids = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reimbursement").child(ids).child(id);
        Pengeluaran pengeluaran1 = new Pengeluaran(id, tanggal, jenis_pengeluaran, biaya, keterangan, status);
        databaseReference.setValue(pengeluaran1);
        Toast.makeText(this,"Pengeluaran Updated Successfully", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}

