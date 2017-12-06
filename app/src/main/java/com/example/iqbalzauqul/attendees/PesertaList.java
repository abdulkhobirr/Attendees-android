package com.example.iqbalzauqul.attendees;

/**
 * Created by iqbalzauqul on 05/12/17.
 */

public class PesertaList {

    private String nama;
    private String nomorIdentitas;
    private String avatar;
    private int progress;

    public PesertaList(String nama, String nomorIdentitas, String avatar, int progress) {
        this.nama = nama;
        this.nomorIdentitas = nomorIdentitas;
        this.avatar = avatar;
        this.progress = progress;
    }

    PesertaList() {

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorIdentitas() {
        return nomorIdentitas;
    }

    public void setNomorIdentitas(String nomorIdentitas) {
        this.nomorIdentitas = nomorIdentitas;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
