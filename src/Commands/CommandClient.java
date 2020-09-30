package Commands;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CommandClient {
    private int port;
    private Socket socket;
    public CommandClient(int port, Socket socket){
        this.port = port;
        this.socket = socket;
    }
    /**
     *Считывает следующее слово с консоли/терминала
     */
    public static String getTerminalNext(){
        System.out.print(">>>");
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }
    /**
     *Считывает строку с консоли/терминала
     */
    public static String getTerminalNextLine(){
        System.out.print(">>>");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    /**
     *Выводит название и краткое описание большинства методов
     */
    String help(){
        return (
                "info - информация о коллекции\n" +
                        //"save - сохранение коллекции\n" +
                        "upload - загрузка коллекции из файла\n" +
                        "clear - очистка коллекции\n" +
                        "remove_last - удалить последний элемент\n" +
                        "remove - удалить элемент\n" +
                        "add - добавить элемент\n" +
                        "show - показать коллекцию\n" +
                        "exit - выход"
        );
    }
    /**
     *Выводит информацию о получении ошибки и спрашивает повторный ввод
     */
    boolean getExit(){
        boolean flag = true;
        System.out.println("Введите любое значение для повторного ввода объекта или введите команду \"exit\" для выхода");
        if (CommandClient.getTerminalNext().equals("exit"))
            flag = false;
        return flag;
    }
    /**
     *Перподключается при потере связи с сервером
     */
    void getLoss(CheckCommandClient checkCommandClient) throws IOException {
        boolean flag = true;
        while (flag) {
            checkCommandClient.socket.close();
            System.out.println("Повторная попытка подключиться через...");
            for (int i = 5; i > 0; i--) {
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                checkCommandClient.socket = new Socket("localhost", port);
                checkCommandClient.INclient = new ObjectInputStream(socket.getInputStream());
                checkCommandClient.OUTclient = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Сокет подключения: " + checkCommandClient.socket);
                flag = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
