package com.example.bin.service;

import com.alibaba.fastjson.JSON;
import com.example.bin.dao.SeatInfoMapper;
import com.example.bin.dao.dto.TicketMessage;
import com.example.bin.dao.entity.SeatInfo;
import com.example.bin.util.Message;
import com.example.bin.util.MyMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 15:50
 **/
@Service
public class SellService {

    @Autowired(required = false)
    private SeatInfoMapper seatInfoMapper;
    @Autowired(required = false)
    private SeatService seatService;

    @Autowired(required = false)
    private TicketService ticketService;

    public String sellTicket(int userId,int trainId,int status,int pathSize){
        List<SeatInfo> seatInfos = selectSeat(trainId, status, pathSize);
        if(seatInfos.isEmpty()){
            //车票已抢完！
            return "车票已抢完";
        }
        Random random = new Random();
        int index = random.nextInt(seatInfos.size());
        SeatInfo seatInfo = seatInfos.get(index);
        boolean res = seatInfoMapper.updateStatus(seatInfo.getId(), seatInfo.getStatus() | status,seatInfo.getState());
        if(res){
            TicketMessage ticketMessage = new TicketMessage(userId, seatInfo.getId() ,status,trainId);
            Message message = new Message("insertTicket", JSON.toJSONString(ticketMessage));
            MyMessageQueue.sendMessage(message);
            return "请求成功,等待出票!";
        }
        return "购票失败";
    }

    public List<SeatInfo> selectSeat(int trainId, int status, int pathSize){
        int temp = 0;
        for(int i=0;i<pathSize-1;i++){
            int flag = (status>>i&1);
            if(flag == 0){
                temp|=1<<i;
            }
        }
        //查只剩这一段路程的票
        List<SeatInfo> seatInfos = seatInfoMapper.selectByStatus(trainId, temp);
        if(seatInfos.isEmpty()){
            //查能买这一路程的票
            seatInfos = seatInfoMapper.selectSeat(trainId,status);
        }
        return seatInfos;
    }

    public void initSeat(){
        seatService.initSeat();
    }

    public void delTicket(){
        ticketService.delTicket();
    }


}
