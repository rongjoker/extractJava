package org.rongjoker.codeeye;

import jdk.internal.vm.annotation.Contended;

public class VolatileTest3 {


    // b使用volatile修饰
    public static volatile long b = 0;

    //消除缓存行的影响
    // c不使用volatile修饰
    @Contended
    public static long c = 0;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while (c == 0) {
                //long x = b;
            }
            System.out.println("c=" + c);
        }).start();

        Thread.sleep(1000);

        b = 1;
        c = 1;
    }

}
