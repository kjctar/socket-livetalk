package studydemo.ptp2.client;


import studydemo.ptp2.client.bean.ServerInfo;

import java.io.IOException;

/**
 *  客户端获取到服务器的tcp端口信息建立连接
 */
public class Client {
    public static void main(String[] args) {
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if (info != null) {
            try {
                TCPClient.linkWith(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
