package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aplikasipmo.myapk.ui.DataMhs.DataMhsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


public class CrudActivity extends AppCompatActivity {

    AutoCompleteTextView DwnFakultas, DwnProdi;
    TextInputLayout DwnLayFakultas;
    PopupMenu.OnMenuItemClickListener listener;

    //Inisialisasi Crud
    private ProgressBar CrudProgres;
    private TextInputEditText NIM, Nama, TglLahir, NoHp, Email, IPK, Alamat;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    private RadioGroup Gender, GolDarah;
    private RadioButton rbPria, rbWanita, rbGolA, rbGolB, rbGolAB, rbGolO;
    private Button BtnSimpan, getFoto;
    private ImageButton BtnBack;
    private String getNim, getNama, getTgl, getHp, getEmail, getIPK, getFakulas, getProdi, getGender, getGolDarah, getAlamat, getGambar;
    public Uri imageUrl,uri;
    public Bitmap bitmap;
    private StorageReference reference;
    private ImageView ImageContainer;

    DatabaseReference getReference;
    FirebaseStorage storage;
    DatabaseReference database;
    StorageReference storageReference;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    String[] fakultas = new String[]{"Ilmu Komputer", "Ilmu Sosial dan Bisnis"};
    String[] prodi = new String[]{"Informatika", "Sistem Informasi", "Teknolgi Informasi", "Ilmu Komunikasi", "Bisnis Digital"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        ArrayAdapter<String> adapter_fakultas = new ArrayAdapter<>(
                this, R.layout.drop_down_item,fakultas
        );

        ArrayAdapter<String> adapter_prodi = new ArrayAdapter<>(
                this, R.layout.drop_down_item,prodi
        );

        // Spinner
        DwnFakultas = findViewById(R.id.dwn_fakultas);
        DwnProdi = findViewById(R.id.dwn_prodi);
        DwnProdi.setAdapter(adapter_prodi);
        DwnFakultas.setAdapter(adapter_fakultas);

        //Btn
        ImageButton showPopUp = findViewById(R.id.btn_popup_mn);
        BtnSimpan = findViewById(R.id.btn_simpan_crud);
        BtnBack = findViewById(R.id.btn_back_crud);
        getFoto = findViewById(R.id.btn_pilih_foto);

        CrudProgres = findViewById(R.id.progress_crud);
        ImageContainer = findViewById(R.id.foto_container);

        //Edit Text
        Nama = findViewById(R.id.input_nama);
        NIM = findViewById(R.id.input_nim);
        TglLahir = findViewById(R.id.input_tgl);
        NoHp = findViewById(R.id.input_hp);
        Email = findViewById(R.id.input_email);
        IPK = findViewById(R.id.input_ipk);
        Alamat = findViewById(R.id.input_alamat);

        //Radio Button
        Gender = findViewById(R.id.gender);
        rbPria = findViewById(R.id.pria);
        rbWanita = findViewById(R.id.wanita);
        GolDarah = findViewById(R.id.gol_darah);
        rbGolA = findViewById(R.id.gol_a);
        rbGolB = findViewById(R.id.gol_b);
        rbGolAB = findViewById(R.id.gol_ab);
        rbGolO = findViewById(R.id.gol_o);

        reference = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dateFormat = new SimpleDateFormat("dd MM yyyy");

        TglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateDialog();
            }
        });

        BtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNama = Nama.getText().toString();
                getNim = NIM.getText().toString();
                getNama = Nama.getText().toString();
                getTgl = TglLahir.getText().toString();
                getEmail = Email.getText().toString();
                getHp = NoHp.getText().toString();
                getIPK = IPK.getText().toString();
                getFakulas = DwnFakultas.getText().toString();
                getProdi = DwnProdi.getText().toString();
                RbGenderPicker();
                RbGolDarahPicker();
                getGambar = ImageContainer.toString().trim();
                getAlamat = Alamat.getText().toString();

                checkUser();
            }
        });

        getFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        listener = new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btn_logout_popup:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(CrudActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CrudActivity.this, LoginActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        };

        showPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CrudActivity.this, v);
                popup.setOnMenuItemClickListener(listener);
                popup.inflate(R.menu.option_menu);
                popup.show();
            }
        });

        DwnFakultas.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(
                        getResources(), R.drawable.bg_dropdown,null
                )
        );
        DwnProdi.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(
                        getResources(), R.drawable.bg_dropdown,null
                )
        );

    }

    private void checkUser(){
        if (TextUtils.isEmpty(getNama) || TextUtils.isEmpty(getNim) || TextUtils.isEmpty(getTgl) || TextUtils.isEmpty(getEmail) ||
                TextUtils.isEmpty(getHp) || TextUtils.isEmpty(getIPK) || TextUtils.isEmpty(getFakulas) || TextUtils.isEmpty(getProdi) ||
                TextUtils.isEmpty(getGender) || TextUtils.isEmpty(getGolDarah) || TextUtils.isEmpty(getAlamat) || uri==null){
            Toast.makeText(this, "Data Tidak Boleh Ada Yang Kosong", Toast.LENGTH_SHORT).show();
        }else {
            ImageContainer.setDrawingCacheEnabled(true);
            ImageContainer.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes = stream.toByteArray();

            String namaFile = UUID.randomUUID()+".jpg";
            final String pathImage = "gambar/"+namaFile;
            UploadTask uploadTask = reference.child(pathImage).putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getReference.child("Admin").child("Mahasiswa").push().setValue(new data_mahasiswa(
                                    getNim, getNama, getFakulas, getProdi, getGolDarah, getGender, getTgl, getHp, getEmail, getIPK, getAlamat, uri.toString().trim()
                            )).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(CrudActivity.this);
                                    alert.setTitle("Data berhasil tersimpan!");
                                    alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Nama.setText("");
                                            NIM.setText("");
                                            TglLahir.setText("");
                                            Email.setText("");
                                            NoHp.setText("");
                                            IPK.setText("");
                                            DwnFakultas.setText("");
                                            DwnProdi.setText("");
                                            Gender.clearCheck();
                                            GolDarah.clearCheck();
                                            Alamat.setText("");
                                            ImageContainer.setImageResource(R.drawable.ic_image);
                                            ImageContainer.setPadding(3,3,3,3);
                                            CrudProgres.setVisibility(View.GONE);
//                                            startActivity(new Intent(CrudActivity.this, DataMhsFragment.class));
                                            onBackPressed();
                                            finish();
                                        }
                                    });
                                    alert.create();
                                    alert.show();

                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CrudProgres.setVisibility(View.GONE);
                    Toast.makeText(CrudActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    CrudProgres.setVisibility(View.VISIBLE);
                    double progres = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    CrudProgres.setProgress((int) progres);
                }
            });
        }
    }

    private void getImage() {
        Intent imageIntentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGalery, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (resultCode==RESULT_OK){
                    ImageContainer.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageContainer.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode==RESULT_OK){
                    ImageContainer.setVisibility(View.VISIBLE);
                    uri = data.getData();
                    ImageContainer.setImageURI(uri);
                    ImageContainer.setPadding(5,5,5,5);
                }
                break;
        }
    }

    private void ShowDateDialog() {
        Calendar calendar = Calendar.getInstance();

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