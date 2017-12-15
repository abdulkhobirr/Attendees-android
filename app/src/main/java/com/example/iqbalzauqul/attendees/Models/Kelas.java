package com.example.iqbalzauqul.attendees.Models;


public class Kelas {

    private String nama;
    private String desc;
    private String image;
    private int persen;

    public Kelas(String nama, String desc, String image, int persen) {
        this.nama = nama;
        this.desc = desc;
        this.image = image;
        this.persen = persen;
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

    public void setPersen(int persen) {this.persen = persen; }

    public String getnama() {
        return nama;
    }

    public String getdesc() {
        return desc;
    }

    public String getimage() {
        return image;
    }

    public int getPersen() { return  persen; }
}
