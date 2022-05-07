package com.example.cookking;

public class MaterialData {

    private int plusbutton;
    private String material;
    private String expiredate;

    public MaterialData(int plusbutton, String material, String expiredate){
        this.plusbutton = plusbutton;
        this.material = material;
        this.expiredate = expiredate;
    }

    public int getPlusbutton() {return this.plusbutton;}

    public String getMaterial()
    {
        return this.material;
    }

    public String getExpiredate() {return this.expiredate; }

}
