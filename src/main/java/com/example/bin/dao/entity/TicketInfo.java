package com.example.bin.dao.entity;

import lombok.Data;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 15:25
 **/
@Data
public class TicketInfo {
    Integer id;
    String code;
    int userId;
    int seatId;
    int trainId;
    int pathStatus;

    public TicketInfo() {
    }

    public TicketInfo(String code, int userId, int seatId, int trainId, int pathStatus) {
        this.code = code;
        this.userId = userId;
        this.seatId = seatId;
        this.trainId = trainId;
        this.pathStatus = pathStatus;
    }
}
