package Commands;

import Music.Coordinates;
import Music.MusicBand;
import Music.MusicGenre;
import Music.Person;
import org.json.JSONObject;
import java.io.IOException;

public class CheckCommandServerSelf {
    public void check(CommandServerSelf commandServerSelf) throws IOException {
        boolean flag = true;
        commandServerSelf.upload();

        while (flag) {
            System.out.println("Введите команду");
            System.out.println("Введите \"help\" чтобы увидеть список команд");
            String comanda="";
            try{
                comanda = commandServerSelf.getTerminalNextLine();
            }
            catch (Exception e){
                System.out.println("Произошла ошибка: " + e.getMessage());
                break;
            }
            switch (comanda) {
                case "info": {
                    commandServerSelf.info();
                    break;
                }
                case "save": {
                    commandServerSelf.save(true);
                    break;
                }
                case "upload": {
                    commandServerSelf.upload();
                    break;
                }
                case "clear": {
                    commandServerSelf.clear();
                    break;
                }
                case "remove_last": {
                    commandServerSelf.remove_last();
                    break;
                }
                case "show": {
                    commandServerSelf.show();
                    break;
                }
                case "exit": {
                    System.out.println("Вы вышли");
                    flag = false;
                    break;
                }
                case "remove": {
                    try {
                        while (flag) {
                            JSONObject jsonObject;
                            System.out.println("Введите парметры объекта для создания в формате json:");
                            System.out.println("Подсказка: для данной коллекции ввод должен выглядеть так:\n{name: String, coordinates: X-Y, size: int, albums: int, date: YYYY-MM-DD, genre: RAP/SOUL/BLUES/POST_ROCK, front person: String}");
                            try {
                                String str = commandServerSelf.getTerminalNextLine();
                                jsonObject = new JSONObject(str);
                                String coords [] = jsonObject.getString("coordinates").split("-");
                                flag = commandServerSelf.remove(
                                        new MusicBand(
                                                jsonObject.getString("name"),
                                                new Coordinates(Integer.parseInt( coords[0] ), Integer.parseInt(coords[1] )),
                                                jsonObject.getInt("size"),
                                                jsonObject.getInt("albums"),
                                                java.time.LocalDate.parse( jsonObject.getString("date") ),// xxxx-xx-xx
                                                MusicGenre.valueOf( jsonObject.getString("genre") ),
                                                new Person( jsonObject.getString("front person") )
                                        )
                                );
                                if(flag)
                                    flag = commandServerSelf.getExit();
                            } catch (Exception e) {
                                System.out.println("Некорректынй ввод");
                                flag = commandServerSelf.getExit();
                            }
                        }
                        flag = true;
                    }
                    catch (Exception e){
                        System.out.println("Произошла ошибка: " + e.getMessage());
                    }
                    break;
                }
                case "add": {
                    try {
                        while (flag) {
                            JSONObject jsonObject;
                            System.out.println("Введите парметры объекта для создания в формате json:");
                            System.out.println("Подсказка: для данной коллекции ввод должен выглядеть так:\n{name: String, coordinates: X-Y, size: int, albums: int, date: YYYY-MM-DD, genre: RAP/SOUL/BLUES/POST_ROCK, front person: String}");
                            try {
                                String str = commandServerSelf.getTerminalNextLine();
                                jsonObject = new JSONObject(str);
                                String coords [] = jsonObject.getString("coordinates").split("-");
                                flag = commandServerSelf.add(
                                        new MusicBand(
                                                jsonObject.getString("name"),
                                                new Coordinates(Integer.parseInt( coords[0] ), Integer.parseInt(coords[1] )),
                                                jsonObject.getInt("size"),
                                                jsonObject.getInt("albums"),
                                                java.time.LocalDate.parse( jsonObject.getString("date") ),// xxxx-xx-xx
                                                MusicGenre.valueOf( jsonObject.getString("genre") ),
                                                new Person( jsonObject.getString("front person") )
                                        )
                                );
                                if(flag)
                                    flag = commandServerSelf.getExit();
                            } catch (Exception e) {
                                System.out.println("Некорректынй ввод");
                                flag = commandServerSelf.getExit();
                            }
                        }
                        flag = true;
                    }
                    catch (Exception e){
                        System.out.println("Произошла ошибка: " + e.getMessage());
                    }
                    break;
                }
                case "help": {
                    System.out.println(
                            "info - информация о коллекции\n" +
                                    "save - сохранение коллекции\n" +
                                    "upload - загрузка коллекции из файла\n" +
                                    "clear - очистка коллекции\n" +
                                    "remove_last - удалить последний элемент\n" +
                                    "remove - удалить элемент\n" +
                                    "add - добавить элемент\n" +
                                    "show - показать коллекцию\n" +
                                    "exit - выход"
                    );
                    break;
                }
                default: {
                    System.out.println("ПРОИЗОШЛО ЧТО-ТО СТРАННОЕ: " + comanda);
                }
            }
        }
    }
}
