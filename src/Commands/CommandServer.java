package Commands;

import Music.Coordinates;
import Music.MusicBand;
import Music.MusicGenre;
import Music.Person;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.*;

public class CommandServer {
    public String ErrorMessage;
    private ArrayList<MusicBand> bands = new ArrayList<>();
    private static Date date = new Date();

    public CommandServer(ObjectOutputStream OUT){}
    /**
     *Сохраняет коллекцию в файл CSV
     */
    void save(Connection connection) {
//        try (FileOutputStream fops = new FileOutputStream(file, false)) {
//            for (int i = 0; i< bands.size(); i++){
//                fops.write((bands.get(i).getName() +";").getBytes());
//                fops.write((bands.get(i).getCoordinates() +";").getBytes());
//                fops.write((bands.get(i).getNumberOfParticipants()+";").getBytes());
//                fops.write((bands.get(i).getAlbumsCount()+";").getBytes());
//                fops.write((bands.get(i).getDate()+";").getBytes());
//                fops.write((bands.get(i).getGenre()+";").getBytes());
//                fops.write((bands.get(i).getPerson() +";\n").getBytes());
//            }
//            System.out.println("Файл сохранен");
//        } catch (IOException e) {
//            System.out.println("Файл не сохранен: " + e.getMessage());
//        }
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM BANDS");
            for (int i = 0; i < bands.size(); i++)
                statement.executeUpdate("INSERT INTO BANDS (name, coordinates, size, albums, date, genre, frontPerson) VALUES ('" + bands.get(i).getName() + "', '" + bands.get(i).getCoordinates() + "', " + bands.get(i).getNumberOfParticipants() + ", '" + bands.get(i).getAlbumsCount() + "', '" + bands.get(i).getDate() + "', '" + bands.get(i).getGenre() + "', '" + bands.get(i).getPerson() + "')");
            System.out.println("Файл сохранен");
            statement.close();
        }
        catch (SQLException e) {
            System.out.println("Файл не сохранен: " + e.getMessage());
        }
    }
    /**
     *Выводит в консоль/терминал информацию о типе, дате инициализации и размере коллекции
     */
    Command info() {
        return new Command("Тип: " + MusicBand.class + "; Дата инициализации: " + date + "; Количество элементов: " + bands.size());
    }
    /**
     *Загружает коллекцию из CSV файла
     */
    synchronized boolean upload(Connection connection) {
        try {
            bands.clear();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM BANDS");
            while(resultSet.next()){
                if(resultSet.getRow() != 0) {
                    String[] coords = resultSet.getString(2).split("-");
                    bands.add(
                            new MusicBand(
                                    resultSet.getString(1),
                                    new Coordinates(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])),
                                    resultSet.getInt(3),
                                    resultSet.getInt(4),
                                    java.time.LocalDate.parse(resultSet.getString(5)),
                                    MusicGenre.valueOf(resultSet.getString(6)),
                                    new Person(resultSet.getString(7))
                            )
                    );
                }
            }
            return true;
        }
        catch (SQLException e) {
            ErrorMessage = e.getMessage();
            return false;
        }
    }
    /**
     *Очищает коллекцию
     */
    Command clear() {
        if(bands.size() == 0)
            return new Command("Коллекция и так пуста");
        else {
            bands.clear();
            return new Command("Коллекция очищена");
        }
    }
    /**
     *Удаляет последний элемент коллекции, если таковой имеется
     */
    Command remove_last() {
        if (bands.size() > 0){
            bands.remove(bands.size() - 1);
            return new Command("Последний элемент удален");
        }
        else
            return new Command("Коллекция и так пуста");
    }
    /**
     *Удаляет элемент
     */
    Command remove(MusicBand band) {
        if(bands.contains(band)){
            bands.remove(band);
            return new Command("0");
        }
        else {
            return new Command("2");
        }
    }
    /**
     *Добавляет элемент
     */
    Command add(MusicBand band) {
        if(!bands.contains(band)){
            bands.add(band);
            bands = sort(bands);
            return new Command("0");
        }
        else{
            return new Command("2");
        }
    }
    /**
     *Показывает информацию обо всех элементах коллекции
     */
    Command show() {
        if (bands.size()>0) {
            ArrayList<MusicBand> bnds = (ArrayList<MusicBand>) bands.clone();
            bnds = sort(bnds);
            return new Command("", bnds);
        }
        else
            return new Command("Коллекция пуста");
    }
    ArrayList<MusicBand> sort(ArrayList<MusicBand> mbs) {
        Comparator<MusicBand> comp = (MusicBand mb1, MusicBand mb2) -> {
            Coordinates a = mb1.getClassCoordinates();
            Coordinates b = mb2.getClassCoordinates();
            if(a.getX() > b.getX())
                return -1;
            else if(a.getX() > b.getX())
                return 1;
            else {
                if(a.getY() > b.getY())
                    return -1;
                else if(a.getY() < b.getY())
                    return 1;
                else
                    return 0;
            }
        };
        Collections.sort(mbs, comp);

        return mbs;
    }
    boolean checkUser(Connection connection, String [] user, MessageDigest MD) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT pass FROM FIRST WHERE name ='" + user[0] + "'");
            String before = user[1];
            byte[] hash = MD.digest(before.getBytes(StandardCharsets.UTF_8));
            String pass = String.format("%064x", new BigInteger(1, hash));
            resultSet.next();
            if (resultSet.getString(1).equals(pass)){
                return true;
            }
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}
