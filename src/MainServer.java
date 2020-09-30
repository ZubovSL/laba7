import Commands.ThreadServerSelf;
import Music.Coordinates;
import Music.MusicBand;
import Music.MusicGenre;
import Music.Person;
//import org.sqlite.JDBC;
//import org.postgresql.Driver;

import java.io.*;
import java.lang.String;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.*;

public class MainServer {
    public static ServerSocket server;
    public static int number = 0;
    static Connection connection = null;
    static final String DATABASE_URL = "jdbc:postgresql://pg:5432/studs";

    static final String USER = "s274807";
    static final String PASSWORD = "ipk430";

    public static ExecutorService CTPReader = Executors.newCachedThreadPool();
    public static ExecutorService CTPWorker = Executors.newCachedThreadPool();
    public static ForkJoinPool FJPReader = new ForkJoinPool();

    public static void main(String[] args) throws IOException {
        Statement statement = null;

        try {
            try {
                System.out.println("Регистрация JDBC драйвера...");
                Class.forName("org.postgresql.Driver");
            }
            catch (ClassNotFoundException | NullPointerException e) {
                throw new Exception("Could not find org.postgresql.Driver.");
            }

            System.out.println("Подключение к базе данных...");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            statement = connection.createStatement();

            System.out.println("Успешно завершено.");

            //.executeUpdate("DROP TABLE FIRST");
            //statement.executeUpdate("CREATE TABLE FIRST (name TEXT PRIMARY KEY, pass TEXT);");
            //statement.executeUpdate("CREATE TABLE BANDS (name TEXT, coordinates TEXT, size INTEGER, albums INTEGER, date TEXT, genre TEXT, frontPerson TEXT);");
            //statement.executeUpdate("INSERT INTO FIRST (name, pass) VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');");
            //statement.executeUpdate("INSERT INTO BANDS (name, coordinates, size, albums, date, genre, frontPerson) VALUES ('Anime2', '10-10', 5, 2, '2020-10-10', 'RAP', 'Animechnik');");
            //statement.executeUpdate("INSERT INTO BANDS (name, coordinates, size, albums, date, genre, frontPerson) VALUES ('Anime3', '10-10', 5, 2, '2020-10-10', 'RAP', 'Animechnik');");

            System.out.println("Введите номер порта для октрытия сервера");
            System.out.print(">>>");
            int port;
            while (true) {
                try {
                    port = new Scanner(System.in).nextInt();
                    server = new ServerSocket(port);
                    break;
                }
                catch (SocketException e1) {
                    System.out.println("Данный порт не доступен");
                }
                catch (Exception e) {
                    System.out.println("Некорректный ввод порта");
                }
            }
            System.out.println("Сервер открыт на порте  " + port);
            new ThreadServerSelf(connection);
            while (true) {
                Socket socket = server.accept();
                try {
                    new ThreadServer(socket, number, connection);
                    number++;
                } catch (IOException e) {
                    socket.close();
                }
            }

        }
        catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        finally {
            try {
                server.close();
                if(statement!=null){
                    statement.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }
            catch (Exception e1) {
                System.out.println("Сервер не будет открыт, так как данный порт недоступен");
            }
        }
    }
}
