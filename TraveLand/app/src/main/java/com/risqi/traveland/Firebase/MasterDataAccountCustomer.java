package com.risqi.traveland.Firebase;

import java.util.HashMap;

public class MasterDataAccountCustomer {
    String KataSandi;
    public  MasterDataAccountCustomer(){

    }

    public HashMap ChangePassword(String kataSandi){
        HashMap update = new HashMap();
        update.put("KataSandi", kataSandi);

        return update;
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
