package nhom7.fpoly.motoworld.Model;

import java.io.Serializable;

public class Hangxe implements Serializable {
    public int mahang;
    private String tenhang;

    public Hangxe() {
    }

    public Hangxe(int mahang, String tenhang) {
        this.mahang = mahang;
        this.tenhang = tenhang;
    }

    public int getMahang() {
        return mahang;
    }

    public void setMahang(int mahang) {
        this.mahang = mahang;
    }

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }
}
