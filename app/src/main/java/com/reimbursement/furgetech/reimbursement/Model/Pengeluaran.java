package com.reimbursement.furgetech.reimbursement.Model;

/**
 * Created by Furgetech on 27/02/2018.
 */

public class Pengeluaran {
    String pengeluaranID;
    String pengeluaranTanggal;
    String pengeluaranJenisPengualaran;
    String pengeluaranBiaya;
    String pengeluaranKeterangan;
    String pengeluaranStatus;

    public Pengeluaran(){

    }

    public Pengeluaran(String pengeluaranID, String pengeluaranTanggal, String pengeluaranJenisPengualaran, String pengeluaranBiaya, String pengeluaranKeterangan, String pengeluaranStatus) {
        this.pengeluaranID = pengeluaranID;
        this.pengeluaranTanggal = pengeluaranTanggal;
        this.pengeluaranJenisPengualaran = pengeluaranJenisPengualaran;
        this.pengeluaranBiaya = pengeluaranBiaya;
        this.pengeluaranKeterangan = pengeluaranKeterangan;
        this.pengeluaranStatus = pengeluaranStatus;
    }

    public String getPengeluaranID() {
        return pengeluaranID;
    }

    public String getPengeluaranTanggal() {
        return pengeluaranTanggal;
    }

    public String getPengeluaranJenisPengualaran() {
        return pengeluaranJenisPengualaran;
    }

    public String getPengeluaranBiaya() {
        return pengeluaranBiaya;
    }

    public String getPengeluaranKeterangan() {
        return pengeluaranKeterangan;
    }

    public String getPengeluaranStatus() {
        return pengeluaranStatus;
    }
}

