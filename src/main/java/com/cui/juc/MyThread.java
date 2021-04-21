package com.cui.juc;

/*
线程 操作 资源类、高内聚低耦合
1、判断
2、干活
3、通知

面试题：两个线程，一个线程打印1-52，另一个打印字母A-Z打印顺序为12A34B...5152Z，要求用线程间通信
使用synchronized+标志位实现
 */

import java.util.Collections;
import java.util.List;

public class MyThread {

    public static void main(String[] args) {

        MyResource mr = new MyResource();   //线程操作的资源类

        new Thread(()->{    //打印数字的线程，因为每次打印需要打印两个数字，因此52个数字，循环次数为52 / 2 = 26
            for (int i = 0; i < 26; i++) {

                try {
                    mr.printNumber();   //AA线程操作资源打印数字
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(()->{    //打印字母的线程，因为打印字母每次打印1个，因此循环次数为26
            for (int i = 0; i < 26; i++) {

                try {
                    mr.printzimu();     //BB线程操作资源打印字母
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();
    }


}


/**
 * 资源类MyResource
 */
class MyResource{

    private boolean flag = true;   //为真打印数字，为假打印字母

    private int num = 1;    //记录数字

    private char[] zimu = new char[]{'A','B','C','D',       //全部字母
                                     'E','F','G','H',
                                     'I','J','K','L',
                                     'M','N','O','P',
                                     'Q','R','S','T',
                                     'U','V','W','X',
                                     'Y','Z'};

    /**
     * 打印数字的方法
     * @throws InterruptedException
     */
    public synchronized void printNumber() throws InterruptedException {

        //1、判断
        if(!flag){
            this.wait();
        }

        //2、干活
        System.out.print(num++);
        System.out.print(num++);

        //3、通知
        flag = false;
        this.notify();
    }

    /**
     * 打印字母的方法
     * @throws InterruptedException
     */
    public synchronized void printzimu() throws InterruptedException {

        //1、判断
        if(flag){
            this.wait();
        }

        //2、干活
        System.out.print(zimu[num / 2 -1]);     //打印数字是一次循环打印两个，所以除2，因为num从1开始，但是字母数组从0开始，再减1

        //3、通知
        flag = true;
        this.notify();


    }
}
