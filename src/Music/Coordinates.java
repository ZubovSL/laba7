package Music;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Double x; //Значение поля должно быть больше -110, Поле не может быть null
    private Double y; //Поле не может быть null
    public Coordinates(double x, double y){
        this.x=x;
        this.y=y;
    }
    public Coordinates(String coord){
        String [] coords = coord.split("-");
        this.x = Double.parseDouble( coords[0] );
        this.y = Double.parseDouble( coords[1] );
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    @Override
    public String toString(){
        return  x.intValue() + "-" + y.intValue();
    }
}
