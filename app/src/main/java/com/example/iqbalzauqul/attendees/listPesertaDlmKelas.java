package com.example.iqbalzauqul.attendees;

/**
 * Created by iqbalzauqul on 19/11/17.
 */

public class listPesertaDlmKelas {

    private String lblnamaPeserta;
    private String lblNIM;
    private String imgPeserta;
    private String imgProgressKehadiran;

    public listPesertaDlmKelas(String lblnamaPeserta, String lblNIM, String imgPeserta, String imgProgressKehadiran) {
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

    public String getImgPeserta() {
        return imgPeserta;
    }

    public void setImgPeserta(String imgPeserta) {
        this.imgPeserta = imgPeserta;
    }

    public String getImgProgressKehadiran() {
        return imgProgressKehadiran;
    }

    public void setImgProgressKehadiran(String imgProgressKehadiran) {
        this.imgProgressKehadiran = imgProgressKehadiran;
    }
}
