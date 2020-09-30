package Commands;

import java.io.*;
import java.net.Socket;

public class ThreadClient extends Thread{
    private Socket socket;
    private ObjectOutputStream OUT;
    private ObjectInputStream IN;
    private CommandClient commandClient;

    public ThreadClient(int port) throws IOException {
        socket = new Socket("localhost", port);
        IN = new ObjectInputStream(socket.getInputStream());
        OUT = new ObjectOutputStream(socket.getOutputStream());
        commandClient = new CommandClient(port, socket);
        start();
    }

    public void run() {
        try {
            CheckCommandClient checkCommandClient = new CheckCommandClient();
            checkCommandClient.check(IN, OUT, socket, commandClient);
        }
        catch (IOException e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        catch (ClassNotFoundException e1){
            System.out.println("Класс коллекции не существует. Работа невозможна");
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }
}
