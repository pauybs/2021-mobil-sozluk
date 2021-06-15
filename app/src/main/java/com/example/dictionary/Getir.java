package com.example.dictionary;

public class Getir {
    private String kelime;
    private String anlam;

    public String getKelime() {
        return kelime;
    }
    public void setKelime(String kelime) {
        this.kelime = kelime;
    }
    public String getAnlam() {
        return anlam;
    }
    public void setAnlam(String anlam) {
        this.anlam = anlam;
    }
    public Getir(String kelime, String anlam)
    {
        this.kelime=kelime;
        this.anlam=anlam;
    }

}
