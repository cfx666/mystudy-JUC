package com.cui.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SaleTicket {


    public static void main(String[] args) {

        Ticket1 ticket = new Ticket1();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"CC").start();


    }
}

class Ticket1{

    private int number = 30;


    public synchronized void sale(){

        if(number > 0){
            System.out.println(Thread.currentThread().getName() + "卖出"+ number-- +"\t 还剩："+number+"张票");
        }
    }
}

class Ticket{

    private int number = 30;

    private Lock look = new ReentrantLock();

    public void sale(){
        look.lock();

        try {

            if(number > 0){
                System.out.println(Thread.currentThread().getName() + "卖出"+ number-- +"\t 还剩："+number+"张票");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }finally{

            look.unlock();
        }

    }
}