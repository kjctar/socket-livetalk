package studydemo.ptp1.constants;

public class UDPConstants {
    // 公用头部
    public static byte[] HEADER = new byte[]{7, 7, 7, 7, 7, 7, 7, 7};
    // 服务器固化UDP接收端口(服务器端接收和发送udp数据的端口)
    public static int PORT_SERVER = 30201;
    // 客户端回送端口 （客户端广播和接收服务器回送数据的端口）
    public static int PORT_CLIENT_RESPONSE = 30202;
}
