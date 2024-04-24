package com.example.bin;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bin.dao.*;
import com.example.bin.dao.dto.TicketDto;
import com.example.bin.dao.dto.TicketMessage;
import com.example.bin.dao.entity.SeatInfo;
import com.example.bin.dao.entity.TicketInfo;
import com.example.bin.dao.entity.TrainInfo;
import com.example.bin.service.SeatService;
import com.example.bin.service.SellService;
import com.example.bin.service.TestService;
import com.example.bin.service.TicketService;
import com.example.bin.util.Message;
import com.example.bin.util.MyMessageQueue;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class ApplicationTests {

    @Autowired(required = false)
    UserInfoMapper userInfoMapper;

    @Autowired(required = false)
    TicketInfoMapper ticketInfoMapper;

    @Autowired(required = false)
    TrainInfoMapper trainInfoMapper;

    @Autowired(required = false)
    SeatInfoMapper seatInfoMapper;

    @Autowired(required = false)
    SeatService seatService;

    @Autowired(required = false)
    SellService sellService;

    @Autowired(required = false)
    TicketService ticketService;


    @Test
    void sellTest() throws InterruptedException {
        for(int i=0;i<=100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sellTicket();
                }
            }).start();
        }
        Thread.sleep(30000);
    }
    void sellTicket(){
        Random random = new Random();
        for(int i=0;i<1000;i++){
            int num = random.nextInt(8);
            if(num==0 || num ==5)
                continue;
            sellService.sellTicket(2, 1,num, 4);
        }
    }

    @Test
    void testTicket(){
        List<TicketInfo> ticketInfos = ticketInfoMapper.selectList(new QueryWrapper<>());
        Map<Integer, List<TicketInfo>> collect = ticketInfos.stream().collect(Collectors.groupingBy(TicketInfo::getSeatId));
        for(Map.Entry<Integer,List<TicketInfo>> entry : collect.entrySet()){
            List<TicketInfo> value = entry.getValue();
            int temp = 0;
            for(TicketInfo ticketInfo : value){
                int status = ticketInfo.getPathStatus();
                if((temp&status)!=0){
                    System.out.println("有重复购票:"+ticketInfo.getSeatId());
                    return ;
                }
                temp|=status;
            }
        }
        System.out.println("测试成功！");
    }


    @Test
    void testTicket2(){
        HashMap<Integer, Integer> map = new HashMap<>();
        List<TicketInfo> ticketInfos = ticketInfoMapper.selectList(new QueryWrapper<>()); // 查询所有的车票信息
        List<SeatInfo> seatInfos = seatInfoMapper.selectList(new QueryWrapper<>());// 查询所有的座位信息
        Map<Integer, List<TicketInfo>> collect = ticketInfos.stream().collect(Collectors.groupingBy(TicketInfo::getSeatId));
        for(Map.Entry<Integer,List<TicketInfo>> entry : collect.entrySet()){
            Integer seatId = entry.getKey();
            int len = entry.getValue().size();
            map.put(seatId,len); // 根据座位id 映射 有几张票
        }
        for(SeatInfo seatInfo : seatInfos){
            if(seatInfo.getState() != map.get(seatInfo.getId())){
                System.out.println("座位的版本号 和 票据信息 不相符"); // 因目前只有购票记录,所以 只有购票成功了 才会更改座位的版本.
            }
        }
        System.out.println("测试2成功!");
    }

    @Test
    void train(){
        TrainInfo trainInfo = new TrainInfo();
        trainInfo.setId(1);
        trainInfo.setCode("testCode");
        trainInfo.setName("复兴号");
        trainInfo.setCarriage(5);
        trainInfo.setCarriageNum(128);
        trainInfo.setPathInfo("北京-上海-长沙-深圳");
        trainInfo.setPathSize(4);
        trainInfo.setTimeInfo("2024-03-06 12:00#2024-03-06 14:00#2024-03-06 16:00#2024-03-06 18:00");
        int insert = trainInfoMapper.insert(trainInfo);
        System.out.println(insert);
    }

    @Autowired
    TestService testService;



    @Test
    void seatTest() throws InterruptedException {
        seatService.initSeat();
        ticketService.delTicket();

//        System.out.println(ticketService.selectTicketInfo(2, 8072));
    }
    @Test
    void queryTicket() {
        List<TicketDto> ticketDtos = ticketService.selectTicketInfo(2, 9333);
        for (TicketDto ticketDto : ticketDtos){
            System.out.println(ticketDto.toString());
        }
//        TicketDto(userId=2, userName=小蒋, pathInfo=上海-深圳, trainName=复兴号, carriageNum=4, seatNum=95, startTime=2024-03-06 14:00, endTime=2024-03-06 18:00)
//        TicketDto(userId=2, userName=小蒋, pathInfo=北京-上海, trainName=复兴号, carriageNum=4, seatNum=95, startTime=2024-03-06 12:00, endTime=2024-03-06 14:00)
    }



}
