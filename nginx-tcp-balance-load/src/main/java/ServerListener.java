import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DW on 2017/3/7.
 */
public class ServerListener {

    public static void main(String[] args) throws IOException {
        new ServerListener(9090);
        new ServerListener(9091);
    }

    /** 监听端口 */
    private int port;

    public ServerListener(int port) throws IOException {
        this.port = port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务端监听： " + port);
        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    System.out.println(port + ": 接收到客户端消息");
                }
            }).start();
        }
    }
}
