package Music;

import Previous.Color;
import Previous.Location;

import java.io.Serializable;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Строка не может быть пустой, Длина строки должна быть не меньше 7, Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null
    public Person(String name, String passportID, Color hairColor, Location location){
        this.name=name;
        this.passportID=passportID;
        this.hairColor=hairColor;
        this.location=location;
    }
    public Person(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return  "       Имя: "+ name +"\n" +
                "       Паспорт: "+passportID+"\n"+
                "       Цвет волос: "+hairColor+"\n"+
                "       Адрес: "+location.toString()+"\n";
    }
    public String getName(){
        return name;
    }
}
