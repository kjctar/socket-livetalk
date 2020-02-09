package studydemo.ptp1.constants;

public class TCPConstants {
    // 服务器固化UDP接收端口
    // （客户端事先知道服务器开放的udp端口 PORT_SERVER，通过广播的方式向向 PORT_SERVER 端口发送数据，其中数据包含客户端自己要接收回送数据的 PORT_CLIENT_RESPONSE 端口信息，
    // 服务器监听到广播后，解析出广播信息中的 PORT_CLIENT_RESPONSE 端口，并将服务端的tcp PORT_SERVER 端口封装在回送信息中发给客户端）


    public static int PORT_SERVER = 30401;
}
