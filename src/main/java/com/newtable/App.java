package com.newtable;

/**
 * 启动的时候对于已经存在的类进行建表操作
 */

public class App {
    public static void main(String[] args) throws Exception {
        NewTable.createTable();
    }
}
