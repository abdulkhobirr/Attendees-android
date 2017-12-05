package com.example.iqbalzauqul.attendees;

/**
 * Created by iqbalzauqul on 05/12/17.
 */

public class PesertaList {

    private String nama;
    private String noID;
    private String avatar;
    private int progress;

    public PesertaList(String nama, String noID, String avatar, int progress) {
        this.nama = nama;
        this.noID = noID;
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

    public String getNoID() {
        return noID;
    }

    public void setNoID(String noID) {
        this.noID = noID;
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
