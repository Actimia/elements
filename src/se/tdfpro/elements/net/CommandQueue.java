package se.tdfpro.elements.net;

import se.tdfpro.elements.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandQueue<T extends Command> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public void accept(T command) {
        queue.add(command);
    }

    public List<T> getCommands() {
        var res = new ArrayList<T>();
        queue.drainTo(res);
        return res;
    }

    public T getCommand() throws InterruptedException {
        return queue.take();
    }
}
