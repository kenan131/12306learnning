package com.example.bin.dao.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author: bin.jiang
 * @date: 2024/3/6 13:42
 **/
@Data
public class TicketDto {
    int userId;
    String userName;
    String pathInfo;
    String trainName;
    int carriageNum;
    int seatNum;
    String startTime; // 发车时间
    String endTime; //到站时间
}
