package com.example.bin.controller;

import com.example.bin.dao.dto.TicketDto;
import com.example.bin.service.SellService;
import com.example.bin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: bin.jiang
 * @date: 2024/3/4 15:46
 **/
@RestController
public class SellController {

    @Autowired
    private SellService sellService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/sellTicket")
    public String sell(int userId,int trainId,int status,int pathSize){
        String res = "";
        try {
            res = sellService.sellTicket(userId,trainId,status,pathSize);
        }catch (Exception e){
            System.out.println(e.getMessage());
            res = "购票出错，请稍后重试！";
        }
        return res;
    }
    @GetMapping("/getTicket")
    public Object getTicket(int userId,int seatId){
        List<TicketDto> ticketDtos = ticketService.selectTicketInfo(userId, seatId);
        return ticketDtos;
    }

    @GetMapping("/initSeat")
    public String initSeat(){
        sellService.initSeat();
        return "重置成功！";
    }

    @GetMapping("/initTicket")
    public String initTicket(){
        sellService.delTicket();
        return "重置成功！";
    }
}
