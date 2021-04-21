package com.cui.juc;

/*
线程 操作 资源类、高内聚低耦合
1、判断
2、干活
3、通知

面试题：两个线程，一个线程打印1-52，另一个打印字母A-Z打印顺序为12A34B...5152Z，要求用线程间通信
使用线程间定制化调用通信+标志位实现
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThread1 {

    public static void main(String[] args) {

        MyResource1 mr = new MyResource1();   //线程操作的资源类

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
class MyResource1{

    private boolean flag = true;   //为真打印数字，为假打印字母

    private int num = 1;    //记录数字

    private Lock lock = new ReentrantLock();
    private Condition ct1 = lock.newCondition();
    private Condition ct2 = lock.newCondition();


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
    public void printNumber() throws InterruptedException {

        lock.lock();
        try {
            //1、判断
            if(!flag){
                ct1.await();
            }

            //2、干活
            System.out.print(num++);
            System.out.print(num++);

            //3、通知
            flag = false;
            ct2.signal();
        } catch (Exception e) {

            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * 打印字母的方法
     * @throws InterruptedException
     */
    public void printzimu() throws InterruptedException {

        lock.lock();
        try {
            //1、判断
            if(flag){
                ct2.await();
            }

            //2、干活
            System.out.print(zimu[num / 2 -1]);

            //3、通知
            flag = true;
            ct1.signal();
        } catch (Exception e) {

            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
}
