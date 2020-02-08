package studydemo.udp;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UDPSearcher {
    private static final  int LISTEN_PORT=3000;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("UDPSearcher Started.");

        System.out.println("开始监听要接受搜索反馈的端口");
        Listener listener=Listen();
        System.out.println("在2000端口发送广播");
        sendBroadcast();


       System.in.read();
        System.out.println("搜到设备如下：");
       List <Device> devices=listener.getDeviceAndClose();
        for (Device device : devices) {
            System.out.println(device.toString());
        }


    }
    public static void sendBroadcast(){


        DatagramSocket ds = null;//搜索方无需指定，交给系统分配
        try {
            ds = new DatagramSocket();


        //构建回送数据
        String requestData=MassageCreator.buildWithPort(LISTEN_PORT);
        byte[] responeseDatabytes=requestData.getBytes();
        DatagramPacket requestPacket =new DatagramPacket(responeseDatabytes,
                responeseDatabytes.length);
        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        requestPacket.setPort(2000);
        ds.send(requestPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ds.close();
        }
    }

    public static Listener Listen() throws InterruptedException {


        CountDownLatch countDownLatch=new CountDownLatch(1);
        Listener listener=new Listener(LISTEN_PORT,countDownLatch);
        listener.start();

        countDownLatch.await();;
        return listener;


    }
    private static class Device{

        final int port;
        final  String ip;
        final  String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }
    private static class Listener  extends Thread{
        private final  int listenPort;
        private final CountDownLatch countDownLatch;
        private final List <Device>devices = new ArrayList<>();
        private  boolean done=false;
        private  DatagramSocket ds=null;


        public Listener(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void run() {
            super.run();
            countDownLatch.countDown();

            try {
                ds=new DatagramSocket(listenPort);
                while (!done){
                    final byte[] buf=new byte[512];
                    DatagramPacket receivePack =new DatagramPacket(buf,buf.length);


                    ds.receive(receivePack);
                    String ip=receivePack.getAddress().getHostAddress();
                    int port =receivePack.getPort();
                    int dataLen=receivePack.getLength();
                    String data=new String(receivePack.getData(),0,dataLen);
                    System.out.println("UDPSearcher receivr from ip:"+ip+"\tport:"+port+"\tdata:"+data);

                    String sn=MassageCreator.buildWithSn(data);
                    if(sn!=null){
                        Device device=new Device(port,ip,sn);
                        devices.add(device);
                    }

                    System.out.println("UDPPrivider Finished");
                    ds.close();
                }

            }catch (Exception e){

            }finally {

                close();
            }
        }
        private void  close(){
            if(ds!=null){
                ds.close();
                ds=null;
            }
        }
        List<Device> getDeviceAndClose(){
            done=true;
            close();
            return devices;
        }
    }
}
