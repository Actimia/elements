package se.tdfpro.elements.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CommandQueue<T extends Command> {

    private ConcurrentLinkedDeque<T> queue = new ConcurrentLinkedDeque<>();

    public void onReceive(T command) {
        System.out.println("recieved command");
        queue.add(command);
    }

    public List<T> getAll() {
        var res = new ArrayList<T>();
        while(!queue.isEmpty()){
            res.add(queue.pop());
        }
        return res;
    }
}
