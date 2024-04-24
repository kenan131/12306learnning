package com.example.bin.dao.entity;

import lombok.Data;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 15:27
 **/
@Data
public class SeatInfo {
    Integer id;
    int num;
    int carriageNum;
    int trainId;
    int status;
    int state;

    public SeatInfo() {
    }

    public SeatInfo(int num, int carriageNum, int trainId, int status, int state) {
        this.num = num;
        this.carriageNum = carriageNum;
        this.trainId = trainId;
        this.status = status;
        this.state = state;
    }
}
