package studydemo.test;

import java.util.concurrent.CountDownLatch;

public class ThreadTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch=new CountDownLatch(1);

    }
    public class NumTool extends Thread{
        @Override
        public void run() {
            super.run();
            for(int i=0;i<5;i++){
                System.out.println(i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
