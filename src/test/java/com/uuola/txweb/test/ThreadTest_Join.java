/*
 * @(#)ThreadTest_Join.java 2013-6-22
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.test;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-6-22
 * </pre>
 */
public class ThreadTest_Join {

    public static void main(String...args) throws InterruptedException{
        Thread t = new Thread(new MyTask());
        t.start();
        t.join(); // 这里将导致线程任务执行完毕后再返回继续执行
        System.out.println("main thread is finished...");
    }
    
   static class MyTask implements Runnable {
        public void run() {
            System.out.println("I'm started..."+System.currentTimeMillis());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("I'm interrupted... message: " + e.getMessage());
            }
            System.out.println("I'm finished..."+System.currentTimeMillis());
        }
    }
}
