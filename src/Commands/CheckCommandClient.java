package Commands;

import Music.Coordinates;
import Music.MusicBand;
import Music.MusicGenre;
import Music.Person;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class CheckCommandClient {
    private static String comanda;
    ObjectInputStream INclient;
    ObjectOutputStream OUTclient;
    Socket socket;

    public void check(ObjectInputStream IN, ObjectOutputStream OUT, Socket socket, CommandClient commandClient) throws IOException, ClassNotFoundException {
        INclient = IN;
        OUTclient = OUT;
        this.socket = socket;

        String [] user = {"", ""};
        boolean flag = true, flag2 = true, flag3 = true;

        while (flag3) {
            try {
                System.out.println("Войдите или зарегистрируйтесь, используя соответсвенно команды \"enter\" и \"reg\"");
                String comanda = CommandClient.getTerminalNextLine();
                if (comanda.equals("enter") || comanda.equals("reg")) {
                    OUTclient.writeObject( new Command(comanda, user) );
                    if (comanda.equals("enter")) {
                        System.out.println("Введите логин и пароль через пробел");
                        String log = CommandClient.getTerminalNextLine();
                        user = log.split(" ");
                        OUTclient.writeObject( new Command("enter", user) );
                        flag = true;
                    }
                    else {
                        while (flag2) {
                            System.out.println("Введите логин и пароль для регистрации.\nЛогин и/или пароль не должны содержать знаки пробела");
                            String log = CommandClient.getTerminalNextLine();
                            user = log.split(" ");
                            if (user.length == 2) {
                                OUTclient.writeObject( new Command("enter", user) );
                                flag = true;
                                flag2 = false;
                            } else {
                                System.out.println("Некорректный ввод");
                                flag2 = commandClient.getExit();
                                if (flag2 == false)
                                    OUTclient.writeObject(new Command( "exit" , user));
                                flag = false;
                            }
                        }
                    }
                    if (flag) {
                        Command command = (Command)INclient.readObject();
                        if (command.getName().equals("0")) {
                            System.out.println("Вы вошли");
                            flag3 = false;
                        } else if (command.getName().equals("1")) {
                            System.out.println("Вы зарегестрирвоались и вошли");
                            flag3 = false;
                        } else {
                            System.out.println("Неверный логин и/или пароль");
                            flag3 = commandClient.getExit();
                            flag = false;
                        }
                    }
                }
                else {
                    System.out.println("Некорректный ввод");
                    flag3 = commandClient.getExit();
                    flag = false;
                }
            }
            catch (SocketException e){
                commandClient.getLoss(this);
            }
        }

        if(flag == false)
            OUTclient.writeObject(new Command("exit", user));
        else
            OUTclient.writeObject(new Command("ready", user));

        if(flag) {
            try {
                OUTclient.writeObject( new Command("upload", user));
                System.out.println(((Command)INclient.readObject()).getName());
            }
            catch (SocketException e) {
                System.out.println("Сервер временно недоступен. Коллекция не загружена.");
                commandClient.getLoss(this);
            }
            catch (NullPointerException e1) {
                System.out.println("Произошла ошибка: " + e1.getMessage());
            }
            while (flag) {
                System.out.println("Введите команду");
                System.out.println("Введите \"help\" чтобы увидеть список команд");
                try {
                    comanda = CommandClient.getTerminalNextLine();
                }
                catch (Exception e) {
                    try {
                        System.exit(-1);
                    } catch (Exception e1) {
                        e1.getMessage();
                    }
                    break;
                }
                switch (comanda) {
                    case "info": {
                        try {
                            OUTclient.writeObject(new Command("info", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (NullPointerException e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                        }
                        break;
                    }
                    case "upload": {
                        try {
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (NullPointerException e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                        }
                        break;
                    }
                    case "clear": {
                        try {
                            OUTclient.writeObject(new Command("clear", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (NullPointerException e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                        }
                        break;
                    }
                    case "remove_last": {
                        try {
                            OUTclient.writeObject(new Command("remove_last", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (NullPointerException e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                        }
                        break;
                    }
                    case "show": {
                        try {
                            OUTclient.writeObject(new Command("show", user));
                            Command com = (Command) INclient.readObject();
                            ArrayList<MusicBand> bands = com.getBands();
                            if (bands.size() > 0) {
                                for (MusicBand mb : bands) {
                                    System.out.println(
                                            "{name: " + mb.getName() +
                                                    ", coordinates: " + mb.getCoordinates() +
                                                    ", size: " + mb.getNumberOfParticipants() +
                                                    ", albums: " + mb.getAlbumsCount() +
                                                    ", date: " + mb.getDate() +
                                                    ", genre: " + mb.getGenre() +
                                                    ", front person: " + mb.getPerson() + "}"
                                    );
                                }
                            } else
                                System.out.println("Коллекция пуста");
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (Exception e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                            e1.printStackTrace();
                        }
                        break;
                    }
                    case "exit": {
                        flag = false;
                        try {
                            OUTclient.writeObject(new Command("exit", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (SocketException e) {
                            System.out.println("Сервер временно недоступен");
                            commandClient.getLoss(this);
                            OUTclient.writeObject(new Command("upload", user));
                            System.out.println(((Command) INclient.readObject()).getName());
                        } catch (NullPointerException e1) {
                            System.out.println("Произошла ошибка: " + e1.getMessage());
                        }
                        break;
                    }
                    case "remove": {
                        OUTclient.writeObject(new Command("remove", user));
                        while (flag) {
                            System.out.println("Введите парметры объекта для создания в формате json:");
                            System.out.println("Подсказка: для данной коллекции ввод должен выглядеть так:\n{name: String, coordinates: X-Y, size: int, albums: int, date: YYYY-MM-DD, genre: RAP/SOUL/BLUES/POST_ROCK, front person: String}");
                            try {
                                String str = CommandClient.getTerminalNextLine();
                                JSONObject jsonObject = new JSONObject(str);
                                String coords[] = jsonObject.getString("coordinates").split("-");
                                ArrayList<MusicBand> bands = new ArrayList<MusicBand>();
                                bands.add(
                                        new MusicBand(
                                                jsonObject.getString("name"),
                                                new Coordinates(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])),
                                                jsonObject.getInt("size"),
                                                jsonObject.getInt("albums"),
                                                java.time.LocalDate.parse(jsonObject.getString("date")),// xxxx-xx-xx
                                                MusicGenre.valueOf(jsonObject.getString("genre")),
                                                new Person(jsonObject.getString("front person"))
                                        )
                                );
                                OUTclient.writeObject(new Command("", bands, user));
                                str = ((Command) INclient.readObject()).getName();
                                if (str.equals("0")) {
                                    System.out.println("Элемент удален");
                                    flag = false;
                                    OUTclient.writeObject(new Command("false", user));
                                } else if (str.equals("1")) {
                                    System.out.println("Некорректный ввод.");
                                    flag = commandClient.getExit();
                                    OUTclient.writeObject(new Command(String.valueOf(flag), user));
                                } else if (str.equals("2")) {
                                    System.out.println("Не существует такого элемента");
                                    flag = commandClient.getExit();
                                    OUTclient.writeObject(new Command(String.valueOf(flag), user));
                                } else {
                                    System.out.println("Ошибка авторизации");
                                }
                            } catch (SocketException e) {
                                System.out.println("Сервер временно недоступен");
                                commandClient.getLoss(this);
                                OUTclient.writeObject(new Command("upload", user));
                                System.out.println(((Command) INclient.readObject()).getName());
                                flag = false;
                            } catch (Exception e1) {
                                System.out.println("Некорректный ввод");
                                OUTclient.writeObject(new Command("kek", user));
                                flag = commandClient.getExit();
                                OUTclient.writeObject(new Command(String.valueOf(flag), user));
                            }
                        }
                        flag = true;
                        break;
                    }
                    case "add": {
                        OUTclient.writeObject(new Command("add", user));
                        while (flag) {
                            System.out.println("Введите парметры объекта для создания в формате json:");//
                            System.out.println("Подсказка: для данной коллекции ввод должен выглядеть так:\n{name: String, coordinates: X-Y, size: int, albums: int, date: YYYY-MM-DD, genre: RAP/SOUL/BLUES/POST_ROCK, front person: String}");
                            try {
                                String str = CommandClient.getTerminalNextLine();
                                JSONObject jsonObject = new JSONObject(str);
                                String coords[] = jsonObject.getString("coordinates").split("-");
                                ArrayList<MusicBand> bands = new ArrayList<MusicBand>();
                                bands.add(
                                        new MusicBand(
                                                jsonObject.getString("name"),
                                                new Coordinates(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])),
                                                jsonObject.getInt("size"),
                                                jsonObject.getInt("albums"),
                                                java.time.LocalDate.parse(jsonObject.getString("date")),// xxxx-xx-xx
                                                MusicGenre.valueOf(jsonObject.getString("genre")),
                                                new Person(jsonObject.getString("front person"))
                                        )
                                );
                                OUTclient.writeObject(new Command("", bands, user));
                                str = ((Command) INclient.readObject()).getName();
                                if (str.equals("0")) {
                                    System.out.println("Элемент добавлен");
                                    flag = false;
                                    OUTclient.writeObject(new Command("false", user));
                                } else if (str.equals("1")) {
                                    System.out.println("Некорректный ввод.");
                                    flag = commandClient.getExit();
                                    OUTclient.writeObject(new Command(String.valueOf(flag), user));
                                } else  if (str.equals("2")){
                                    System.out.println("Такой элемент уже существует");
                                    flag = commandClient.getExit();
                                    OUTclient.writeObject(new Command(String.valueOf(flag), user));
                                } else {
                                    System.out.println("Ошибка авторизации");
                                }
                            } catch (SocketException e) {
                                System.out.println("Сервер временно недоступен");
                                commandClient.getLoss(this);
                                OUTclient.writeObject(new Command("upload", user));
                                System.out.println(((Command) INclient.readObject()).getName());
                                flag = false;
                            } catch (Exception e1) {
                                System.out.println("Некорректный ввод");
                                OUTclient.writeObject(new Command("kek", user));
                                flag = commandClient.getExit();
                                OUTclient.writeObject(new Command(String.valueOf(flag), user));
                            }
                        }
                        flag = true;
                        break;
                    }
                    case "help": {
                        System.out.println(commandClient.help());
                        break;
                    }
                    default: {
                        System.out.println("Неверная команда. Попробуйте еще раз");
                    }
                }
            }
        }
    }
}
