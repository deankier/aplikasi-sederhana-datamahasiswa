package com.aplikasipmo.myapk;

import static android.text.TextUtils.isEmpty;

import static java.nio.file.Files.delete;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<data_mahasiswa> listMahasiswa;
    listdata context;
    ArrayList<data_mahasiswa> listMahasiswaSearch;

    public interface dataListener{
        void onDeleteData(data_mahasiswa data, int position);
    }
    dataListener listener;

    Filter setSearch = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<data_mahasiswa> filterMahasiswa = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterMahasiswa.addAll(listMahasiswaSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (data_mahasiswa item : listMahasiswaSearch) {
                    if (item.getNama().toLowerCase().contains(filterPattern)) {
                        filterMahasiswa.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterMahasiswa;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listMahasiswa.clear();
            listMahasiswa.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public RecyclerViewAdapter(ArrayList<data_mahasiswa> listMahasiswa, listdata context) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        listener = (listdata)context;
        this.listMahasiswaSearch = listMahasiswa;
    }



    @Override
    public Filter getFilter() {
        return setSearch;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        data_mahasiswa dm = listMahasiswa.get(position);

        final String NIM = listMahasiswa.get(position).getNim();
        final String Nama = listMahasiswa.get(position).getNama();
        final String Fakultas = listMahasiswa.get(position).getFakultas();
        final String Prodi = listMahasiswa.get(position).getProdi();
        final String Goldar = listMahasiswa.get(position).getGol_darah();
        final String JenisKelamin = listMahasiswa.get(position).getGender();
        final String Tgllahir = listMahasiswa.get(position).getTgl_lahir();
        final String Nohp = listMahasiswa.get(position).getHp();
        final String Crudemail = listMahasiswa.get(position).getEmail();
        final String Ipk = listMahasiswa.get(position).getIpk();
        final String Alamat = listMahasiswa.get(position).getAlamat();
        final String Gambar = listMahasiswa.get(position).getGambar();

        holder.NIM.setText(": " + NIM);
        holder.Nama.setText(": " + Nama);
        holder.Fakultas.setText(": " + Fakultas);
        holder.Prodi.setText(": " + Prodi);
        holder.Goldar.setText(": " + Goldar);
        holder.JenisKelamin.setText(": " + JenisKelamin);
        holder.Tgllahir.setText(": " + Tgllahir);
        holder.Nohp.setText(": " + Nohp);
        holder.Crudemail.setText(": " + Crudemail);
        holder.Ipk.setText(": " + Ipk);
        holder.Alamat.setText(": " + Alamat);

        if (isEmpty(Gambar)) {
            holder.Gambar.setImageResource(R.drawable.ic_account);
        } else {
            Glide.with(holder.itemView.getContext()).load(Gambar.trim()).into(holder.Gambar);
            holder.Gambar.setPadding(20,20,20,20);
        }

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Apa yang akan anda pilih?");
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                                bundle.putString("dataNama", listMahasiswa.get(position).getNama());
                                bundle.putString("dataFakultas", listMahasiswa.get(position).getFakultas());
                                bundle.putString("dataGolDarah", listMahasiswa.get(position).getGol_darah());
                                bundle.putString("dataGender", listMahasiswa.get(position).getGender());
                                bundle.putString("dataTanggal", listMahasiswa.get(position).getTgl_lahir());
                                bundle.putString("dataNohp", listMahasiswa.get(position).getHp());
                                bundle.putString("dataEmail", listMahasiswa.get(position).getEmail());
                                bundle.putString("dataIpk", listMahasiswa.get(position).getIpk());
                                bundle.putString("dataAlamat", listMahasiswa.get(position).getAlamat());
                                bundle.putString("dataPrimaryKey", listMahasiswa.get(position).getKey());
                                bundle.putString("dataProdi", listMahasiswa.get(position).getProdi());
                                bundle.putString("dataGambar", listMahasiswa.get(position).getGambar());

                                Intent intent = new Intent(v.getContext(), update.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;

                            case 1:
                                AlertDialog.Builder alertt = new AlertDialog.Builder(v.getContext());
                                alertt.setTitle("Apakah anda yakin menghapus data ini?");
                                alertt.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        listener.onDeleteData(listMahasiswa.get(position),position);
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                alertt.create();
                                alertt.show();

                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView NIM, Nama, Prodi, Fakultas, Goldar, JenisKelamin, Tgllahir, Nohp, Crudemail, Alamat, Ipk;
        public ImageView Gambar;
        private LinearLayout ListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NIM = itemView.findViewById(R.id.view_nim);
            Nama = itemView.findViewById(R.id.view_nama);
            Fakultas = itemView.findViewById(R.id.view_fakultas);
            Prodi = itemView.findViewById(R.id.view_prodi);
            Goldar = itemView.findViewById(R.id.view_gol_darah);
            JenisKelamin = itemView.findViewById(R.id.view_gender);
            Tgllahir = itemView.findViewById(R.id.view_tgl);
            Nohp = itemView.findViewById(R.id.view_no_hp);
            Crudemail = itemView.findViewById(R.id.view_email);
            Ipk = itemView.findViewById(R.id.view_ipk);
            Alamat = itemView.findViewById(R.id.view_alamat);
            ListItem = itemView.findViewById(R.id.list_item);
            Gambar = itemView.findViewById(R.id.view_gambar);
        }
    }
}
