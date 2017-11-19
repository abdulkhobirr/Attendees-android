package com.example.iqbalzauqul.attendees;

/**
 * Created by iqbalzauqul on 18/11/17.
 */

public class listItem {

    private String lblKelas;
    private String lblPengabsen;
    private int imgKelas;

    public listItem(String lblKelas, String lblPengabsen, int imgKelas) {
        this.lblKelas = lblKelas;
        this.lblPengabsen = lblPengabsen;
        this.imgKelas = imgKelas;
    }

    public void setLblKelas(String lblKelas) {
        this.lblKelas = lblKelas;
    }

    public void setLblPengabsen(String lblPengabsen) {
        this.lblPengabsen = lblPengabsen;
    }

    public void setImgKelas(int imgKelas) {
        this.imgKelas = imgKelas;
    }

    public String getLblKelas() {
        return lblKelas;
    }

    public String getLblPengabsen() {
        return lblPengabsen;
    }

    public int getImgKelas() {
        return imgKelas;
    }
}
