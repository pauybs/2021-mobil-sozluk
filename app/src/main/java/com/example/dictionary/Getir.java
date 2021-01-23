package com.example.dictionary;

public class Getir {
    private String kelime;
    private String anlam;
    private String a1;

  /*  public String getA1() {
        return a1;
    }
    public void setA1(String a1) {
        this.a1 = a1;
    }*/
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
