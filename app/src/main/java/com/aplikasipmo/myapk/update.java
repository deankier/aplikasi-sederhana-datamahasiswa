package com.aplikasipmo.myapk;

import static android.text.TextUtils.isEmpty;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class update extends AppCompatActivity {

    AutoCompleteTextView DwnFakultas, DwnProdi;
    private ProgressBar UpdateProgres;
    private TextInputEditText NIM, Nama, TglLahir, NoHp, Email, IPK, Alamat;
    DatePickerDialog datePickerDialog;
    private RadioGroup rbGender, rbGolDarah;
    private RadioButton rbPria, rbWanita, rbGolA, rbGolB, rbGolAB, rbGolO;
    private Button BtnUpdate, getFoto;
    private ImageButton BtnBack;
    private String getNim, getNama, getTgl, getHp, getEmail, getIPK, getFakulas, getProdi, getGender, getGolDarah, getAlamat, getGambar;

    private ImageView ImageContainer;


    DatabaseReference database;
    StorageReference storageReference;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    String[] fakultas = new String[]{"Ilmu Komputer", "Ilmu Sosial dan Bisnis"};
    String[] prodi = new String[]{"Informatika", "Sistem Informasi", "Teknolgi Informasi", "Ilmu Komunikasi", "Bisnis Digital"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ArrayAdapter<String> adapter_fakultas = new ArrayAdapter<>(
                this, R.layout.drop_down_item,fakultas
        );

        ArrayAdapter<String> adapter_prodi = new ArrayAdapter<>(
                this, R.layout.drop_down_item,prodi
        );

        DwnFakultas = findViewById(R.id.dwn_fakultas_update);
        DwnProdi = findViewById(R.id.dwn_prodi_update);
        DwnProdi.setAdapter(adapter_prodi);
        DwnFakultas.setAdapter(adapter_fakultas);

        BtnUpdate = findViewById(R.id.btn_update);
        BtnBack = findViewById(R.id.btn_back_update);
        getFoto = findViewById(R.id.btn_pilih_foto_update);

        UpdateProgres = findViewById(R.id.progress_update);
        ImageContainer = findViewById(R.id.foto_container_update);

        //Edit Text
        Nama = findViewById(R.id.input_nama_update);
        NIM = findViewById(R.id.input_nim_update);
        TglLahir = findViewById(R.id.input_tgl_update);
        NoHp = findViewById(R.id.input_hp_update);
        Email = findViewById(R.id.input_email_update);
        IPK = findViewById(R.id.input_ipk_update);
        Alamat = findViewById(R.id.input_alamat_update);

        //Radio Button
        rbGender = findViewById(R.id.gender_update);
        rbPria = findViewById(R.id.pria_update);
        rbWanita = findViewById(R.id.wanita_update);
        rbGolDarah = findViewById(R.id.gol_darah_update);
        rbGolA = findViewById(R.id.gol_a_update);
        rbGolB = findViewById(R.id.gol_b_update);
        rbGolAB = findViewById(R.id.gol_ab_update);
        rbGolO = findViewById(R.id.gol_o_update);

        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();

        getData();

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mendapatkan Data Mahasiswa yang akan dicek
                getNim = NIM.getText().toString();
                getNama = Nama.getText().toString();
                getFakulas = DwnFakultas.getText().toString();
                getProdi = DwnProdi.getText().toString();
                getHp = NoHp.getText().toString();
                getEmail = Email.getText().toString();
                getIPK = IPK.getText().toString();
                getAlamat = Alamat.getText().toString();
                getTgl = TglLahir.getText().toString();
                RbGenderPicker();
                RbGolDarahPicker();

                //mengecek agar tidak ada data yang kosong saat proses update
                if (isEmpty(getNim)||isEmpty(getNama)||isEmpty(getFakulas)||isEmpty(getProdi)||isEmpty(getHp)||isEmpty(getEmail)
                        ||isEmpty(getIPK)||isEmpty(getAlamat)||isEmpty(getTgl)||isEmpty(getGender)||isEmpty(getGolDarah)){
                    Toast.makeText(update.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {

                    UpdateProgres.setVisibility(View.VISIBLE);
                    //mendapatkan data dari Image View sebagai Bytes
                    ImageContainer.setDrawingCacheEnabled(true);
                    ImageContainer.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    //mengkompress Bitmap menjadi JPG
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] bytes = stream.toByteArray();

                    //Lokasi Lengkap dimana gambar disimpan
                    String namaFile = UUID.randomUUID()+".jpg";
                    final String pathImage = "foto/"+namaFile;
                    UploadTask uploadTask = storageReference.child(pathImage).putBytes(bytes);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    /*
                                    Menjalankan proses update data
                                    Method Setter digunakan untuk mendapatkan data baru yang diinputkan user */

                                    data_mahasiswa setMahasiswa = new data_mahasiswa();
                                    setMahasiswa.setNim(NIM.getText().toString());
                                    setMahasiswa.setNama(Nama.getText().toString());
                                    setMahasiswa.setFakultas(DwnFakultas.getText().toString());
                                    setMahasiswa.setProdi(DwnProdi.getText().toString());
                                    setMahasiswa.setHp(NoHp.getText().toString());
                                    setMahasiswa.setEmail(Email.getText().toString());
                                    setMahasiswa.setIpk(IPK.getText().toString());
                                    setMahasiswa.setAlamat(Alamat.getText().toString());
                                    setMahasiswa.setGol_darah(getGolDarah);
                                    setMahasiswa.setGender(getGender);
                                    setMahasiswa.setTgl_lahir(TglLahir.getText().toString());
                                    setMahasiswa.setGambar(uri.toString().trim());

                                    updateMahasiswa(setMahasiswa);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(update.this, "Update Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            UpdateProgres.setVisibility(View.VISIBLE);
                            double progress = (100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                            UpdateProgres.setProgress((int) progress);
                        }
                    });



                }
            }
        });
    }

    private void updateMahasiswa(data_mahasiswa setMahasiswa) {
        UpdateProgres.setVisibility(View.GONE);
        String getKey = getIntent().getExtras().getString("dataPrimaryKey");
        database.child("Admin")
                .child("Mahasiswa")
                .child(getKey)
                .setValue(setMahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        NIM.setText("");
                        Nama.setText("");
                        NoHp.setText("");
                        Email.setText("");
                        IPK.setText("");
                        Alamat.setText("");
                        TglLahir.setText("");
                        DwnFakultas.setText("");
                        DwnProdi.setText("");
                        rbGender.clearCheck();
                        rbGolDarah.clearCheck();
                        ImageContainer.setImageResource(R.drawable.ic_image);
                        ImageContainer.setPadding(3,3,3,3);
                        Toast.makeText(update.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void getImage() {
        Intent imageIntenGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntenGallery, REQUEST_CODE_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode==RESULT_OK){
                    ImageContainer.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    ImageContainer.setImageURI(uri);
                    ImageContainer.setPadding(5,5,5,5);
                }
                break;
        }
    }

    private void getData() {
            //Menampilkan data dari item yang dipilih sebelumnya
            final String getNIM = getIntent().getExtras().getString("dataNIM");
            final String getNama = getIntent().getExtras().getString("dataNama");
            final String getFakultas = getIntent().getExtras().getString("dataFakultas");
            final String getNohp = getIntent().getExtras().getString("dataNohp");
            final String getEmail = getIntent().getExtras().getString("dataEmail");
            final String getIpk = getIntent().getExtras().getString("dataIpk");
            final String getAlamat = getIntent().getExtras().getString("dataAlamat");
            final String getProdi = getIntent().getExtras().getString("dataProdi");
            final String getGolDar = getIntent().getExtras().getString("dataGolDarah");
            final String getGenderr = getIntent().getExtras().getString("dataGender");
            final String getTgl = getIntent().getExtras().getString("dataTanggal");
            final String getGambar = getIntent().getExtras().getString("dataGambar");

            //mengatur tampilan gambar
            if (isEmpty(getGambar)) {
                //foto lama = nama imageview
                ImageContainer.setImageResource(R.drawable.ic_image);
            } else {
                Glide.with(update.this)
                        .load(getGambar)
                        .into(ImageContainer);
            }


            //Mengatur DatePicker
            TglLahir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog();
                }
            });

            if (getGolDar.equalsIgnoreCase("A")){
                rbGolA.setChecked(true);
            } else if (getGolDar.equalsIgnoreCase("B")) {
                rbGolB.setChecked(true);
            }else if (getGolDar.equalsIgnoreCase("AB")) {
                rbGolAB.setChecked(true);
            }else if (getGolDar.equalsIgnoreCase("O")) {
                rbGolO.setChecked(true);
            }

            if (getGenderr.equalsIgnoreCase("Pria")){
                rbPria.setChecked(true);
            } else if (getGenderr.equalsIgnoreCase("Wanita")) {
                rbWanita.setChecked(true);
            }

            DwnProdi.setText(getProdi);
            DwnFakultas.setText(getFakultas);
            NIM.setText(getNIM);
            Nama.setText(getNama);
            NoHp.setText(getNohp);
            Email.setText(getEmail);
            IPK.setText(getIpk);
            Alamat.setText(getAlamat);
            TglLahir.setText(getTgl);
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                TglLahir.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void RbGolDarahPicker(){
        if (rbGolA.isChecked()){
            getGolDarah = "A";
        } else if (rbGolB.isChecked()) {
            getGolDarah = "B";
        }else if (rbGolAB.isChecked()) {
            getGolDarah = "AB";
        }else if (rbGolO.isChecked()) {
            getGolDarah = "O";
        }
    }

    private void RbGenderPicker(){
        if (rbPria.isChecked()){
            getGender = "Pria";
        } else if (rbWanita.isChecked()) {
            getGender = "Wanita";
        }
    }
}
