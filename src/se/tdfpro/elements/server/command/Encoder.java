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

    public byte[] getBytes() {
        var res = new byte[buf.position()];
        buf.rewind();
        buf.get(res, 0, res.length);
        return res;
    }

    public void encode(Command obj) {
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

    public void encode(int i) {
        buf.putInt(i);
    }

    public void encode(float f) {
        encode(Float.floatToIntBits(f));
    }

    public void encode(Vec2 vec) {
        encode(vec.x);
        encode(vec.y);
    }

    public void encode(String s) {
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

        Decoder d = new Decoder(ByteBuffer.wrap(buf));
        System.out.println(d.decodeInt());
        System.out.println(d.decodeString());
        System.out.println(d.decodeFloat());


        MoveCommand move = new MoveCommand();
        move.player = 1;
        move.something = "Hello, World!";
        move.velo = new Vec2(4, (float) -Math.PI);

        var iters = 100;
        var start = System.currentTimeMillis();
        for (int i = 0; i < iters; i++) {
            var bytes = move.encode();
//            System.out.println(Arrays.toString(bytes));
            var comm = new Decoder(ByteBuffer.wrap(bytes)).decode();
            comm.execute(null);
        }
        var dur = System.currentTimeMillis() - start;
        System.out.println("enc: " + dur / (float) iters + " ms");


        // [0, 0, 0, 4, 0, 0, 0, 5, 104, 101, 108, 108, 111, 64, 73, 14, 86]
    }
}
