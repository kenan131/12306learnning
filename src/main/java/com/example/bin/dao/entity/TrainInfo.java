package com.example.bin.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 15:26
 **/
@Data
public class TrainInfo {
    Integer id;
    String name;
    String code;
    int pathSize;
    String pathInfo;
    String timeInfo;
    int carriage;//车厢数
    int carriageNum;//车厢载人数
}
