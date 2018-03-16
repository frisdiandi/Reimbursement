package com.reimbursement.furgetech.reimbursement;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reimbursement.furgetech.reimbursement.Adapter.PengeluaranList;
import com.reimbursement.furgetech.reimbursement.Model.Pengeluaran;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    boolean bool=true;

    TextView textView,textView1,textView2, textViewJudul;
    ImageView imageView;

    FirebaseAuth mAuth;

    ListView listViewPengeluaran;

    List<Pengeluaran> pengeluaranList;
    DatabaseReference databasePengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initView();
        initToolbar();
        initDrawer();
        initNavigationViewListener();
        //loadUserInformation();

        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();
        //textViewJudul.setText(id);
        databasePengeluaran = FirebaseDatabase.getInstance().getReference("reimbursement").child(id);
        list();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        listViewPengeluaran = (ListView) findViewById(R.id.listViewPengeluaran);
        pengeluaranList = new ArrayList<>();

        listViewPengeluaran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pengeluaran pengeluaran = pengeluaranList.get(position);
                showUpadteDialog(pengeluaran.getPengeluaranID(), pengeluaran.getPengeluaranTanggal(), pengeluaran.getPengeluaranJenisPengualaran(), pengeluaran.getPengeluaranBiaya(), pengeluaran.getPengeluaranKeterangan(), pengeluaran.getPengeluaranStatus());
            }
        });
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TambahReimbursementActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //load();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void list() {
        databasePengeluaran.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pengeluaranList.clear();

                for(DataSnapshot pengeluaranSnapshoot : dataSnapshot.getChildren())    {
                    Pengeluaran pengeluaran1 = pengeluaranSnapshoot.getValue(Pengeluaran.class);
                    pengeluaranList.add(pengeluaran1);
                }
                PengeluaranList adapter = new PengeluaranList(MainActivity.this, pengeluaranList);
                listViewPengeluaran.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void load(){
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getUid() != null) {
                textViewJudul.setText(user.getUid());
            }
        }
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                textViewJudul.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private  void showUpadteDialog(final String pengeluaranID, final String pengeluaranTanggal, final String pengeluaranJenisPengeluaran, final String pengeluaranBiaya, final String pengeluaranKeterangan, final String pengeluaranStatus) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reimbursement_dialog, null);
        dialogBuilder.setView(dialogView);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdatePengeluaran);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeletePengeluaran);

        dialogBuilder.setTitle("Pengeluaran Dialog");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateReimbursementActivity.class);
                intent.putExtra("pengeluaranID",pengeluaranID);
                intent.putExtra("pengeluaranTanggal",pengeluaranTanggal);
                intent.putExtra("pengeluaranJenisPengeluaran",pengeluaranJenisPengeluaran);
                intent.putExtra("pengeluaranBiaya",pengeluaranBiaya);
                intent.putExtra("pengeluaranKeterangan",pengeluaranKeterangan);
                intent.putExtra("pengeluaranStatus",pengeluaranStatus);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePengeluaran(pengeluaranID);
                alertDialog.dismiss();
            }
        });
    }

    private void deletePengeluaran(String pengeluaranId) {
        FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();
        DatabaseReference dPengeluaran = FirebaseDatabase.getInstance().getReference("reimbursement").child(id).child(pengeluaranId);

        dPengeluaran.removeValue();

        Toast.makeText(this, "Pengeluaran is deleted", Toast.LENGTH_LONG).show();
    }

    ////////////////////////////////////

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                textView2.setText(user.getDisplayName());
            }

            if (user.getEmail() != null) {
                textView1.setText(user.getEmail());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    ///////////////////////////////////////////

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.img_avatar);
        textView1 = (TextView) findViewById(R.id.tv_name);
        textView2 = (TextView) findViewById(R.id.tv_email);
        //textView = (TextView) findViewById(R.id.tv_verify);
        //tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void initNavigationViewListener() {
        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = getIntent();
                                switch (menuItem.getItemId()){
                                    case R.id.navigation_item_1:
                                        list();
                                        menuItem.isChecked();
                                        break;
                                    case R.id.navigation_item_2:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Contact");
                                        builder.setMessage("---------------------------------------------------------\n\nMore Info...\nHubungi Dandi : 085765530135.");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Log.e("info", "OK");
                                                    }
                                                });
                                        builder.show();
                                        break;
                                    case R.id.navigation_item_3:
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                        builder2.setTitle("About");
                                        builder2.setMessage("---------------------------------------------------------\n\nTeknologi memudahkan setiap orang dalam menjalani aktivitas sehari-hari. Mekanisme reimbursement misalnya, jika sebelumnya dilakukan secara manual, sekarang dengan teknologi kegiatan administrasi itu bisa lebih mudah dilakukan lewat ponsel pintar.\n" +
                                                "\n" +
                                                "Lewat aplikasi Reimbursement V.1.0, segala keribetan manual reimbursement kini dipermudah. Karyawan hanya perlu memfoto bukti transaksi, memilih kategori reimbursement lalu melaporkannya ke bagian keuangan secara real-time lewat ponsel pintarnya.");
                                        builder2.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Log.e("info", "OK");
                                                    }
                                                });
                                        builder2.show();
                                        break;
                                    case R.id.navigation_item_4:
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        break;
                                }
                            }
                        }, 325);

                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}