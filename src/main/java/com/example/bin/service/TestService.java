package com.example.bin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bin.dao.TestInfoMapper;
import com.example.bin.dao.TicketInfoMapper;
import com.example.bin.dao.entity.Test;
import com.example.bin.dao.entity.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static net.sf.jsqlparser.parser.feature.Feature.insert;

/**
 * @author: bin.jiang
 * @date: 2024/3/22 15:50
 **/
@Service
public class TestService {

    @Autowired(required = false)
    TestInfoMapper testInfoMapper;

    @Autowired(required = false)
    @Lazy
    TestService testService;

    @Autowired(required = false)
    TicketInfoMapper ticketInfoMapper;

    public void test() {
        Test test = new Test("2", 9);
        int res = testInfoMapper.insert(test);
//        int res = testInfoMapper.update(test, new QueryWrapper<Test>().eq("code", test.getCode()));
        System.out.println("test："+res);

        testService.test1();

        int a = 1/0;
//
//        try {
//            Thread.sleep(50000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test1(){
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setPathStatus(1);
        ticketInfo.setSeatId(1);
        ticketInfo.setTrainId(1);
        ticketInfo.setCode("1123");
        ticketInfo.setUserId(1);
        int insert = ticketInfoMapper.insert(ticketInfo);
        System.out.println("ticket："+insert);
    }



}
