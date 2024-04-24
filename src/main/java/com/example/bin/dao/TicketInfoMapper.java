package com.example.bin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bin.dao.entity.SeatInfo;
import com.example.bin.dao.entity.TicketInfo;

import java.util.ArrayList;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 14:25
 **/

public interface TicketInfoMapper extends BaseMapper<TicketInfo> {
    boolean batchInsert(ArrayList<TicketInfo> ticketInfos);
}
