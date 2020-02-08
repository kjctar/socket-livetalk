package studydemo.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

public class UDPProvider {
    public static void main(String[] args) throws IOException {
        String sn= UUID.randomUUID().toString();
        Provider provider =new Provider(sn);
        provider.start();
        System.in.read();
        provider.exit();

    }
    private static class Provider extends Thread{
        private  final  String sn;
        private boolean done = false;
        private DatagramSocket ds=null;
        public Provider(String sn){
            super();
            this.sn=sn;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("UDPProvide Started.");

            try {
                ds = new DatagramSocket(2000);

            while(!done){

                final byte[] buf=new byte[512];
                DatagramPacket receivePack =new DatagramPacket(buf,buf.length);
                ds.receive(receivePack);

                String ip=receivePack.getAddress().getHostAddress();
                int port =receivePack.getPort();
                int dataLen=receivePack.getLength();
                String data=new String(receivePack.getData(),0,dataLen);
                System.out.println("UDPPrivider receivr from ip:"+ip+"\tport:"+port+"\tdata:"+data);

                int responsePort=MassageCreator.parsePort(data);

                System.out.println("没有阻塞");

                if(responsePort!=-1){
                    //构建回送数据
                    String responseData=MassageCreator.buildWithSn(sn);
                    byte[] responeseDatabytes=responseData.getBytes();
                    DatagramPacket responsePacket =new DatagramPacket(responeseDatabytes,
                            responeseDatabytes.length,
                            receivePack.getAddress(),
                            responsePort);

                        ds.send(responsePacket);

                }

                System.out.println("UDPPrivider Finished");

            }
            } catch (IOException ignored) {
                //e.printStackTrace();
            } finally {
                close();
            }
        }
        private void close(){
            if(ds!=null){
                ds.close();
            }
        }
        void exit(){
            done=true;
            close();
        }
    }
}
