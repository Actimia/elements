package se.tdfpro.elements.server.command;

import se.tdfpro.elements.server.command.client.MoveCommand;
import se.tdfpro.elements.server.engine.Vec2;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

public class Encoder {
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    private byte[] getBytes() {
        var res = new byte[buf.position()];
        buf.rewind();
        buf.get(res,0, res.length);
        return res;
    }

    private void encode(Command obj) {
        encode(obj.getClass().getName());
        Arrays.stream(obj.getClass().getFields())
                .filter(f -> f.getAnnotation(Send.class) != null)
                .sorted(Comparator.comparing(Field::getName))
                .forEach(f -> encode(obj, f));
    }

    private void encode(Command obj, Field f) {
        try {
            if (f.getType().equals(Integer.TYPE)) {
                encode(f.getInt(obj));
            } else if(f.getType().equals(Float.TYPE)) {
                encode(f.getFloat(obj));
            } else if(f.getType().equals(String.class)) {
                encode((String)f.get(obj));
            } else if(f.getType().equals(Vec2.class)) {
                encode((Vec2)f.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void encode(int i) {
        buf.putInt(i);
    }

    private void encode(float f) {
        encode(Float.floatToIntBits(f));
    }

    private void encode(Vec2 vec) {
        encode(vec.x);
        encode(vec.y);
    }

    private void encode(String s) {
        byte[] bytes = s.getBytes(Charset.forName("utf-8"));

        encode(bytes.length);
        buf.put(bytes);
    }

    public static void main(String[] args) {
        Encoder e = new Encoder();
        e.encode(4);
        e.encode("hello \uD83D\uDE09");
        e.encode(3.1415f);
        byte[] buf = e.getBytes();
        System.out.println(Arrays.toString(buf));
        // [0, 0, 0, 4, 0, 0, 0, 5, 104, 101, 108, 108, 111, 64, 73, 14, 86]


        MoveCommand move = new MoveCommand();
        move.pid = 1;
        move.something = "Hello, World!";
        move.velo = new Vec2(4, (float) -Math.PI);

        var iters = 100;
        var start = System.currentTimeMillis();
        for (int i = 0; i < iters; i++) {
            var bytes = move.encode();
            System.out.println(Arrays.toString(bytes));
            var comm = Decoder.decode(bytes);
        }
        var dur = System.currentTimeMillis() - start;
        System.out.println("enc: " + dur / (float) iters + " ms");


    }

    public static byte[] encodeCommand(Command command) {
        var enc = new Encoder();
        enc.encode(command);
        return enc.getBytes();
    }
}
