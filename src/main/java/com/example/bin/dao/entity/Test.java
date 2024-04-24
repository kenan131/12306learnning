package com.example.bin.dao.entity;

import lombok.Data;

/**
 * @author: bin.jiang
 * @date: 2024/3/22 15:55
 **/
@Data
public class Test {
    int id;
    String name;
    int code;

    public Test(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
