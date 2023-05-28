package com.aplikasipmo.myapk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listdata extends AppCompatActivity implements RecyclerViewAdapter.dataListener{

    //deklarasi var utk recycleview
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //Deklarasi varibel database
    private DatabaseReference reference;
    private ArrayList<data_mahasiswa> dataMahasiswa;
    ProgressBar listProgres;
    private FloatingActionButton fab,home;

    //Deklarasi variabel untuk searc view
    private EditText SearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        ImageButton BtnBack = findViewById(R.id.btn_back_list);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        recyclerView = findViewById(R.id.datalist);
        fab = findViewById(R.id.fab);
        home = findViewById(R.id.home);
        listProgres = findViewById(R.id.progress_list);
        SearchView = findViewById(R.id.etSearch);

        listProgres.setVisibility(View.VISIBLE);

        ImageButton BtnBack = findViewById(R.id.btn_back_list);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    GetData(s.toString());
                } else {
                    adapter.getFilter().filter(s);
                }
            }
        });

        MyRecycleView();

        GetData("");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(listdata.this, CrudActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(listdata.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void GetData(String data) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Mahasiswa").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataMahasiswa = new ArrayList<>();
                        dataMahasiswa.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            data_mahasiswa mahasiswa = snapshot.getValue(data_mahasiswa.class);
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);
                        }
                        adapter = new RecyclerViewAdapter(dataMahasiswa, listdata.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        listProgres.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("ListActivity", error.getDetails() + " " + error.getMessage());
                    }
                });
    }
    private void MyRecycleView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        listProgres.setVisibility(View.GONE);

        DividerItemDecoration ItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        ItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_divider));
        recyclerView.addItemDecoration(ItemDecoration);
    }

    @Override
    public void onDeleteData(data_mahasiswa data, int position) {
        if (reference != null){
            reference.child("Admin")
                    .child("Mahasiswa")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(listdata.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
