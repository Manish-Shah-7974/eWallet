package com.ewallet.EWallet.Model;

public class User
{
    private int id;
    private String name;
    private String email;
    private String mobile;
    private int kyc_flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getKyc_flag() {
        return kyc_flag;
    }

    public void setKyc_flag(int kyc_flag) {
        this.kyc_flag = kyc_flag;
    }


}
