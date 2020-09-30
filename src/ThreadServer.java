import Commands.CheckCommandServer;
import Commands.CommandServer;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

public class ThreadServer extends Thread{
    private Socket socket;
    private ObjectInputStream IN;
    private ObjectOutputStream OUT;
    private int number;
    private Connection connection;

    CommandServer commandServer;

    public ThreadServer(Socket socket, int number, Connection connection) throws IOException {
        this.socket = socket;
        this.OUT = new ObjectOutputStream(socket.getOutputStream());
        this.IN = new ObjectInputStream(socket.getInputStream());
        this.number = number;
        this.connection = connection;

        commandServer = new CommandServer(OUT);

        start(); // вызываем run()
    }

    public void run() {
        try {
            CheckCommandServer checkCommandServer = new CheckCommandServer();
            checkCommandServer.check(IN, OUT, commandServer, number, connection);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (NoSuchAlgorithmException e1) {
            System.out.println("Нет такого алгоритма хеширования");
        }
        catch (ClassNotFoundException e2) {
            System.out.println("Некоторые классы не добавлены");
        }
        finally {
            try {
                MainServer.number--;
                socket.close();
            } catch (IOException e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }
}
