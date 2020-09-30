package Commands;

import Music.MusicBand;

import java.io.Serializable;
import java.util.ArrayList;

public class Command implements Serializable {
    private String name;
    private String [] user = new String[2];
    private ArrayList<MusicBand> bands = new ArrayList<>();

    public Command(String name){
        this.name = name;
    }
    public Command(String name, String [] user) {
        this(name);
        this.user = user;
    }
    public Command(String name, ArrayList<MusicBand> bands) {
        this(name);
        this.bands = bands;
    }
    public Command(String name, ArrayList<MusicBand> bands, String [] user) {
        this(name, bands);
        this.user = user;
    }
    public String getName() {
        return name;
    }
    public String [] getUser() {
        return user;
    }
    public ArrayList<MusicBand> getBands() {
        return bands;
    }
}
