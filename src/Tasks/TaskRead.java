package Tasks;

import Commands.Command;

import java.io.ObjectInputStream;
import java.util.concurrent.Callable;

public class TaskRead implements Callable<Command> {
    ObjectInputStream IN;

    public TaskRead(ObjectInputStream IN) {
        this.IN = IN;
    }

    @Override
    public Command call() throws Exception {
        return (Command)IN.readObject();

    }
}
