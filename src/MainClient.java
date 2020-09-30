import Commands.CommandClient;
import Commands.ThreadClient;

import java.io.*;

public class MainClient {
    static int port;
    public static void main(String[] args){
        boolean flag = true;
        while (flag) {
            System.out.println("Введите порт");
            try{
                port = Integer.parseInt(CommandClient.getTerminalNext());
                new ThreadClient(port);
                flag = false;
            }
            catch (NumberFormatException e){
                System.out.println("Некорректный ввод порта. Введите корректное значение");
            }
            catch (IOException e1){
                System.out.println(e1.getMessage());
            }
            catch (Exception e2){
                System.out.println("Не удается подключиться к выбранному порту. Введите корректный порт");
            }
        }
    }
}
