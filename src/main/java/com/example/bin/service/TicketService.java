package com.example.bin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bin.dao.SeatInfoMapper;
import com.example.bin.dao.TicketInfoMapper;
import com.example.bin.dao.TrainInfoMapper;
import com.example.bin.dao.UserInfoMapper;
import com.example.bin.dao.dto.TicketDto;
import com.example.bin.dao.dto.TicketMessage;
import com.example.bin.dao.entity.SeatInfo;
import com.example.bin.dao.entity.TicketInfo;
import com.example.bin.dao.entity.TrainInfo;
import com.example.bin.dao.entity.UserInfo;
import com.example.bin.util.MyMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author: bin.jiang
 * @date: 2024/3/5 11:33
 **/
@Service
public class TicketService {
    @Autowired(required = false)
    private TicketInfoMapper ticketInfoMapper;

    @Autowired(required = false)
    private TrainInfoMapper trainInfoMapper;

    @Autowired(required = false)
    private UserInfoMapper userInfoMapper;
    @Autowired(required = false)
    private SeatInfoMapper seatInfoMapper;

    @PostConstruct
    public void init(){
        MyMessageQueue.addProcessType("insertTicket",(message)->{
            System.out.println("消费消息！");
            TicketMessage ticketMessage = JSON.parseObject(message.getValue(), TicketMessage.class);
            System.out.println("消息内容：" + message.getValue());
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setPathStatus(ticketMessage.getStatus());
            ticketInfo.setUserId(ticketMessage.getUserId());
            ticketInfo.setSeatId(ticketMessage.getSeatId());
            ticketInfo.setTrainId(ticketMessage.getTrainId());
            ticketInfo.setCode(UUID.randomUUID().toString());
            int res = ticketInfoMapper.insert(ticketInfo);
            if(res == 1){
                System.out.println("消费成功！");
            }
            else{
                System.out.println("消费失败！");
                // TODO 发送到fail队列中，还是存到失败消息数据库表中。
            }
        });
    }

    public void delTicket(){
        List<TicketInfo> ticketInfos = ticketInfoMapper.selectList(new QueryWrapper<>());
        if(ticketInfos.isEmpty())
            return ;
        List<Integer> ids = ticketInfos.stream().mapToInt(TicketInfo::getId).boxed().collect(Collectors.toList());
        if(!ids.isEmpty()){
            ticketInfoMapper.deleteBatchIds(ids);
        }
    }

    public List<TicketDto> selectTicketInfo(int userId,int seatId){
        List<TicketInfo> tickets = ticketInfoMapper.selectList(new QueryWrapper<TicketInfo>().eq("seat_id", seatId)
                .eq("user_id",userId));
        ArrayList<TicketDto> res = new ArrayList<>();
        if(!tickets.isEmpty()){
            TrainInfo trainInfo = trainInfoMapper.selectById(tickets.get(0).getTrainId());
            UserInfo userInfo = userInfoMapper.selectById(userId);
            if(trainInfo==null || userInfo == null){
                return null;
            }
            String[] pathInfo = trainInfo.getPathInfo().split("-");
            String[] timeInfo = trainInfo.getTimeInfo().split("#");
            for(TicketInfo ticketInfo : tickets){
                int status = ticketInfo.getPathStatus();//车票状态 一张票只会存在连续的1。状态在前端得控制好
                int l=-1,r=-1;
                for(int i=0;i<pathInfo.length;i++){
                    if((status>>i&1)==1){
                        if(l==-1){
                            l=i;
                        }
                        r=i;
                    }else if(l!=-1){
                        break;
                    }
                }
                SeatInfo seatInfo = seatInfoMapper.selectById(ticketInfo.getSeatId());
                TicketDto ticketDto = new TicketDto();
                ticketDto.setUserId(userInfo.getId());
                ticketDto.setUserName(userInfo.getName());
                ticketDto.setPathInfo(pathInfo[l]+"-"+pathInfo[r+1]);
                ticketDto.setCarriageNum(seatInfo.getCarriageNum());
                ticketDto.setSeatNum(seatInfo.getNum());
                ticketDto.setStartTime(timeInfo[l]);
                ticketDto.setEndTime(timeInfo[r+1]);
                ticketDto.setTrainName(trainInfo.getName());
                res.add(ticketDto);
            }
        }
        return res;
    }

}
