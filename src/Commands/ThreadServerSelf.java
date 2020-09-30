package Commands;

import java.io.*;
import java.sql.Connection;

public class ThreadServerSelf extends Thread{
    Connection connection;
    CommandServerSelf commandServerSelf;

    public ThreadServerSelf(Connection connection) {
        this.connection = connection;
        commandServerSelf = new CommandServerSelf(connection);

        start(); // вызываем run()
    }

    public void run() {
        try {
            CheckCommandServerSelf checkCommandServerSelf = new CheckCommandServerSelf();
            checkCommandServerSelf.check(commandServerSelf);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            commandServerSelf.save(true);
            System.exit(0);
        }
    }
}
