package com.example.bin.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: bin.jiang
 * @date: 2023/11/3 18:00
 **/
@Component
public class MyMessageQueue {

    //未处理队列
    public static LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    //失败队列
    public static LinkedBlockingQueue<Message> failQueue = new LinkedBlockingQueue<>();

    //根据任务类型，选择处理流程
    private static HashMap<String, ProcessFun> processMap = new HashMap<>();
    ThreadPoolExecutor threadPoolExecutor;

    public interface ProcessFun {
        void function(Message message);
    }

    public MyMessageQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handle();
            }
        }).start();
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
    }

    public static boolean sendMessage(Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            System.out.println("添加任务时发生异常");
            return false;
        }
        return true;
    }

    public void handle() {
        while (true) {
            Message message = null;
            try {
                message = queue.take();
                ProcessFun process = processMap.get(message.getType());
                if(process == null){
                    System.out.println("未找到相关类型的处理方法，已存入失败队列");
                    failQueue.put(message);
                }
                else{
                    Message finalMessage = message;
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            process.function(finalMessage);
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println("获取任务或任务执行时发生异常");
                failQueue.add(message);
                throw new RuntimeException(e);
            }
        }
    }

    //添加处理类型
    public static void addProcessType(String type,ProcessFun process){
        processMap.put(type,process);
    }

    //删除处理类型
    public void delProcessType(String type){
        processMap.remove(type);
    }

    //失败任务重新加入待执行队列
    public void reAddToQueue(){
        while (!failQueue.isEmpty()){
            try {
                queue.put(failQueue.poll());
            } catch (InterruptedException e) {
                System.out.println("任务重新添加报错");
                throw new RuntimeException(e);
            }
        }
    }
}
