import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by DW on 2017/3/7.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        int i = 0;
        while (i < 100) {
            ++i;
            request("192.168.137.90", 80);
        }

    }

    public static void request(String host, int port) {
        ObjectOutputStream output = null;
        Socket socket = null;
        try {
            socket = new Socket();
            SocketAddress address = new InetSocketAddress(host, port);
            socket.connect(address);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF("Hi, iam client");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
