package com.example.bin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bin.dao.SeatInfoMapper;
import com.example.bin.dao.TrainInfoMapper;
import com.example.bin.dao.entity.SeatInfo;
import com.example.bin.dao.entity.TrainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: bin.jiang
 * @date: 2024/3/5 11:42
 **/
@Service
public class SeatService {

    @Autowired(required = false)
    private SeatInfoMapper seatInfoMapper;

    @Autowired(required = false)
    private TrainInfoMapper trainInfoMapper;

    public void initSeat(){
        delSeat();
        addSeat();
    }

    public void addSeat(){
        ArrayList<SeatInfo> seatInfos = new ArrayList<>();
        TrainInfo trainInfo = trainInfoMapper.selectById(1);
        for(int i=1;i<=trainInfo.getCarriage();i++){
            for(int j=1;j<=trainInfo.getCarriageNum();j++){
                SeatInfo seatInfo = new SeatInfo();
                seatInfo.setNum(j);
                seatInfo.setCarriageNum(i);
                seatInfo.setStatus(0);
                seatInfo.setTrainId(trainInfo.getId());
                seatInfo.setState(0);
                seatInfos.add(seatInfo);
            }
        }
        // 批量插入
        if(!seatInfos.isEmpty()){
            seatInfoMapper.insertBatch(seatInfos);
        }
    }
    public void delSeat(){
        List<SeatInfo> seatInfos = seatInfoMapper.selectList(new QueryWrapper<>());
        List<Integer> ids = seatInfos.stream().mapToInt(SeatInfo::getId).boxed().collect(Collectors.toList());
        if(!ids.isEmpty()){
            // 删除所有id
            seatInfoMapper.deleteBatchIds(ids);
        }
    }
    public void selectSeat(){

    }

}
