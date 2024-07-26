package com.project1;

public class Order {

    @Step(id=1,description = "减库存")
    public String kuCun(){
        return "减库存";
    }

    @Step(id=2,description = "支付宝加钱")
    public String zhibubaoAdd(){
        return "支付宝加钱";
    }

    @Step(id=3,description = "支付宝--")
    public String zhibubaoSub(){
        return "支付宝--";
    }

    @Step(id=4,description = "发货")
    public void faHuo(){
        System.out.println("发货");;
    }
}
