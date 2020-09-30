package Commands;

import Music.Coordinates;
import Music.MusicBand;
import Music.MusicGenre;
import Music.Person;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CommandServerSelf {

    private ArrayList<MusicBand> bands = new ArrayList<>();
    private static Date date = new Date();
    Connection connection;

    public CommandServerSelf(Connection connection){
        this.connection = connection;
    }
    /**
     *Сохраняет коллекцию в файл CSV
     */
    public boolean save(boolean notInside) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM BANDS");
            for (int i = 0; i < bands.size(); i++)
                statement.executeUpdate("INSERT INTO BANDS (name, coordinates, size, albums, date, genre, frontPerson) VALUES ('" + bands.get(i).getName() + "', '" + bands.get(i).getCoordinates() + "', " + bands.get(i).getNumberOfParticipants() + ", '" + bands.get(i).getAlbumsCount() + "', '" + bands.get(i).getDate() + "', '" + bands.get(i).getGenre() + "', '" + bands.get(i).getPerson() + "')");
            if(notInside)
                System.out.println("Файл сохранен");
            statement.close();
            return true;
        }
        catch (Exception e) {
            System.out.println("Файл не сохранен: " + e.getMessage());
            return false;
        }
    }
    /**
     *Выводит в консоль/терминал информацию о типе, дате инициализации и размере коллекции
     */
    void info(){
        System.out.println("Тип: " + MusicBand.class + "; Дата инициализации: " + date + "; Количество элементов: " + bands.size());
    }
    /**
     *Загружает коллекцию из CSV файла
     */
    synchronized void upload(){
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
            System.out.println("Коллекция загружена");
        }
        catch (SQLException e) {
            System.out.println("Нечего загружать(((");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e1) {
            System.out.println( e1.getMessage() );
            e1.printStackTrace();
        }
    }
    /**
     *Очищает коллекцию
     */
    void clear(){
        try {
            bands.clear();
            save(false);
            System.out.println("Коллекция очищена");
        } catch (Exception e) {
            System.out.println("Коллекция не очищена: " + e.getMessage());
        }
    }
    /**
     *Удаляет последний элемент коллекции, если таковой имеется
     */
    void remove_last() {
        if (bands.size() > 0){
            bands.remove(bands.size() - 1);
            if(save(false))
                System.out.println("Последний элемент удален");
        }
        else
            System.out.println("Коллекция и так пуста");
    }
    /**
     *Удаляет элемент
     */
    boolean remove(MusicBand band){
        if(bands.contains(band)){
            bands.remove(band);
            if(save(false)) {
                System.out.println("Элемент удален");
                return false;
            }
            else
                return true;
        }
        else {
            System.out.println("Не существует такого элемента");
            return true;
        }
    }
    /**
     *Добавляет элемент
     */
    boolean add(MusicBand band){
        if(!bands.contains(band)){
            bands.add(band);
            if(save(false)) {
                System.out.println("Элемент добавлен");
                return false;
            }
            else
                return true;
        }
        else{
            System.out.println("Такой элемент уже существует");
            return true;
        }
    }
    /**
     *Показывает информацию обо всех элементах коллекции
     */
    void show(){
        String s = "";
        if (bands.size()>0) {
            for (int i = 0; i < bands.size(); i++) {
                s = "{name: " + bands.get(i).getName() + ", coordinates: " + bands.get(i).getCoordinates() + ", size: " + bands.get(i).getNumberOfParticipants() + ", albums: " + bands.get(i).getAlbumsCount() + ", date: " + bands.get(i).getDate() + ", genre: " + bands.get(i).getGenre() + ", front person: " + bands.get(i).getPerson() + "}";
                System.out.println(s);
            }
        }
        else
            System.out.println("Коллекция пуста");
    }
    /**
     *Считывает строку с консоли/терминала
     */
    public String getTerminalNextLine(){
        System.out.print(">>>");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
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
}
