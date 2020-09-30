package Commands;

import Music.MusicBand;


import java.io.*;
import java.math.BigInteger;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckCommandServer {
    ObjectInputStream INserver;
    ObjectOutputStream OUTserver;
    public void check(ObjectInputStream IN, ObjectOutputStream OUT, CommandServer commandServer, int number, Connection connection) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        INserver = IN;
        OUTserver = OUT;
        MessageDigest MD = MessageDigest.getInstance("SHA-256");



        boolean flag = true, flag2 = true;
        String [] user = {"", ""};

        while (flag2){
            Command command = (Command)INserver.readObject();
            if (command.getName().equals("exit")) {
                flag2 = false;
                flag = false;
            }
            else if (command.getName().equals("ready")){
                flag2 = false;
            }
            else if (command.getName().equals("enter")){
                command = (Command)INserver.readObject();
                user = command.getUser();
                try {
                    if (commandServer.checkUser(connection, user, MD)){
                        OUTserver.writeObject(new Command("0"));
                    }
                    else {
                        OUTserver.writeObject(new Command("2"));
                    }
                }
                catch (Exception e){
                    OUTserver.writeObject(new Command(e.getMessage()));
                }
            }
            else if (command.getName().equals("reg")){
                command = (Command)INserver.readObject();
                try {
                    if (!command.getName().equals("exit")) {
                        user = command.getUser();
                        Statement statement = connection.createStatement();
                        String before = user[1];
                        byte[] hash = MD.digest(before.getBytes(StandardCharsets.UTF_8));
                        String pass = String.format("%064x", new BigInteger(1, hash));
                        statement.executeUpdate("INSERT INTO FIRST (name, pass) VALUES ('" + user[0] + "', '" + pass + "')");
                        OUTserver.writeObject(new Command("1"));
                    }
                }
                catch (Exception e){
                    OUTserver.writeObject(new Command("2"));
                }
            }
            else{
                OUTserver.writeObject(new Command("КАВО БЛУЭТ"));
            }
        }
        while (flag) {
            Command command;
            String comanda;
            try {
                command = (Command)INserver.readObject();
                comanda = command.getName();
                user = command.getUser();
            }
            catch (SocketException | ClassNotFoundException e){
                System.out.println(e.getMessage());
                break;
            }
            if(commandServer.checkUser(connection, user, MD)) {
                switch (comanda) {
                    case "info": {
                        try {
                            OUTserver.writeObject(commandServer.info());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case "save": {
                        commandServer.save(connection);
                        break;
                    }
                    case "upload": {
                        if (commandServer.upload(connection))
                            OUTserver.writeObject(new Command("Файл загружен"));
                        else
                            OUTserver.writeObject(new Command(commandServer.ErrorMessage));
                        break;
                    }
                    case "clear": {
                        OUTserver.writeObject(commandServer.clear());
                        break;
                    }
                    case "remove_last": {
                        OUTserver.writeObject(commandServer.remove_last());
                        break;
                    }
                    case "show": {
                        OUTserver.writeObject(commandServer.show());
                        break;
                    }
                    case "exit": {
                        OUTserver.writeObject(new Command("Вы вышли"));
                        flag = false;
                        break;
                    }
                    case "remove": {
                        try {
                            while (flag) {
                                try {
                                    MusicBand mb = ((Command) INserver.readObject()).getBands().get(0);
                                    OUTserver.writeObject(commandServer.remove(mb));
                                } catch (Exception e) {
                                }
                                flag = Boolean.parseBoolean(((Command) INserver.readObject()).getName());
                            }
                            flag = true;
                        } catch (SocketException | ClassNotFoundException e) {
                            System.out.println("Произошла ошибка: " + e.getMessage());
                        }
                        break;
                    }
                    case "add": {
                        try {
                            while (flag) {
                                try {
                                    MusicBand mb = ((Command) INserver.readObject()).getBands().get(0);
                                    OUTserver.writeObject(commandServer.add(mb));
                                } catch (Exception e) {
                                }
                                flag = Boolean.parseBoolean(((Command) INserver.readObject()).getName());
                            }
                            flag = true;
                        } catch (SocketException | ClassNotFoundException e) {
                            System.out.println("Произошла ошибка: " + e.getMessage());
                        }
                        break;
                    }
                    default: {
                        System.out.println("ПРОИЗОШЛО ЧТО-ТО СТРАННОЕ: " + comanda);
                    }
                }
            }
            else {
                OUTserver.writeObject(new Command("Ошибка авторизации"));
            }
        }
    }
}
