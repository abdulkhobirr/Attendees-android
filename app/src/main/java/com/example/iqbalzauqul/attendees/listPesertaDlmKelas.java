package com.example.iqbalzauqul.attendees;

/**
 * Created by iqbalzauqul on 19/11/17.
 */

public class listPesertaDlmKelas {

    private String lblnamaPeserta;
    private String lblNIM;
    private int imgPeserta;
    private int imgProgressKehadiran;

    public listPesertaDlmKelas(String lblnamaPeserta, String lblNIM, int imgPeserta, int imgProgressKehadiran) {
        this.lblnamaPeserta = lblnamaPeserta;
        this.lblNIM = lblNIM;
        this.imgPeserta = imgPeserta;
        this.imgProgressKehadiran = imgProgressKehadiran;
    }

    public String getLblnamaPeserta() {
        return lblnamaPeserta;
    }

    public void setLblnamaPeserta(String lblnamaPeserta) {
        this.lblnamaPeserta = lblnamaPeserta;
    }

    public String getLblNIM() {
        return lblNIM;
    }

    public void setLblNIM(String lblNIM) {
        this.lblNIM = lblNIM;
    }

    public int getImgPeserta() {
        return imgPeserta;
    }

    public void setImgPeserta(int imgPeserta) {
        this.imgPeserta = imgPeserta;
    }

    public int getImgProgressKehadiran() {
        return imgProgressKehadiran;
    }

    public void setImgProgressKehadiran(int imgProgressKehadiran) {
        this.imgProgressKehadiran = imgProgressKehadiran;
    }
}
