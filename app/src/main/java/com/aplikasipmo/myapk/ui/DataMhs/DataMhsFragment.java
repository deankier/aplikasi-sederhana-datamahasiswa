package com.aplikasipmo.myapk.ui.DataMhs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aplikasipmo.myapk.CrudActivity;
import com.aplikasipmo.myapk.ListDataActivity;
import com.aplikasipmo.myapk.R;
import com.aplikasipmo.myapk.listdata;
//import com.aplikasipmo.myapk.databinding.FragmentDataMhsBinding;

public class DataMhsFragment extends Fragment implements View.OnClickListener {

    LinearLayout BtnTambah, BtnLihat;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data_mhs, container, false);
        BtnTambah = view.findViewById(R.id.btn_tambah);
        BtnLihat = view.findViewById(R.id.btn_lihat);
        BtnTambah.setOnClickListener(this);
        BtnLihat.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tambah:
                getActivity().startActivity(new Intent(getActivity(), CrudActivity.class));
                break;
            case R.id.btn_lihat:
                getActivity().startActivity(new Intent(getActivity(), listdata.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}