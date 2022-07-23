package com.risqi.traveland.Firebase;

public class MasterDataAccountCustomer {
    String KataSandi;
    public  MasterDataAccountCustomer(){

    }

    public MasterDataAccountCustomer(String kataSandi) {
        KataSandi = kataSandi;
    }

    public String getKataSandi() {
        return KataSandi;
    }

    public void setKataSandi(String kataSandi) {
        KataSandi = kataSandi;
    }
}
