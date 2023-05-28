package com.aplikasipmo.myapk;

public class data_mahasiswa {
    private String nim,nama,fakultas,prodi,gol_darah,gender, tgl_lahir,hp,email,ipk,alamat,gambar,key;

    public data_mahasiswa() {
    }

    public data_mahasiswa(String nim, String nama, String fakultas, String prodi, String gol_darah, String gender, String tgl_lahir, String hp, String email, String ipk, String alamat, String gambar) {
        this.nim = nim;
        this.nama = nama;
        this.fakultas = fakultas;
        this.prodi = prodi;
        this.gol_darah = gol_darah;
        this.gender = gender;
        this.tgl_lahir = tgl_lahir;
        this.hp = hp;
        this.email = email;
        this.ipk = ipk;
        this.alamat = alamat;
        this.gambar = gambar;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getGol_darah() {
        return gol_darah;
    }

    public void setGol_darah(String gol_darah) {
        this.gol_darah = gol_darah;
    }

    public String getGender() {
        return gender;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
