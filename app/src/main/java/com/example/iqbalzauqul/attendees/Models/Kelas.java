package com.example.iqbalzauqul.attendees.Models;

/**
 * Created by iqbalzauqul on 18/11/17.
 */

public class Kelas {

    private String nama;
    private String desc;
    private String image;

    public Kelas(String nama, String desc, String image) {
        this.nama = nama;
        this.desc = desc;
        this.image = image;
    }

    Kelas() {
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public void setdesc(String desc) {
        this.desc = desc;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getnama() {
        return nama;
    }

    public String getdesc() {
        return desc;
    }

    public String getimage() {
        return image;
    }
}
