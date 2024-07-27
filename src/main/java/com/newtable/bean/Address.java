package com.newtable.bean;

import com.newtable.annotation.Field;
import com.newtable.annotation.PrimaryKey;
import com.newtable.annotation.Table;

@Table
public class Address {

    @PrimaryKey
    private Integer id;
    @Field(value = "heiheihei",foreignKey = "wqeqw-id")
    private Integer userId;    //用户id
    private String province;    //省
    private String city;        //市
    private String town;        //区
    private String notes;   //备注
}
