package com.example.bin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bin.dao.entity.SeatInfo;

import java.util.List;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 14:29
 **/

public interface SeatInfoMapper extends BaseMapper<SeatInfo> {

    public List<SeatInfo> selectByStatus(int train_id,int status);//查status相同值

    public List<SeatInfo> selectSeat(int train_id,int status);

    public boolean updateStatus(int seat_id,int status,int state);

    boolean insertBatch(List<SeatInfo> list);

}
