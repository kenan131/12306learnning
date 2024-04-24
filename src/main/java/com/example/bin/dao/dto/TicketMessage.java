package com.example.bin.dao.dto;

import lombok.Data;

/**
 * @author: bin.jiang
 * @date: 2024/3/5 11:11
 **/
@Data
public class TicketMessage {
    int userId;
    int seatId;
    int status;
    int trainId;

    public TicketMessage(int userId, int seatId, int status, int trainId) {
        this.userId = userId;
        this.seatId = seatId;
        this.status = status;
        this.trainId = trainId;
    }
}
