package se.tdfpro.elements.server.command;

import se.tdfpro.elements.client.engine.entity.Entity;
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
        buf.get(res, 0, res.length);
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
        var type = f.getType();
        try {
            if (type.equals(Integer.TYPE)) {
                encode(f.getInt(obj));
            } else if(type.equals(Float.TYPE)) {
                encode(f.getFloat(obj));
            } else if(type.equals(String.class)) {
                encode((String)f.get(obj));
            } else if(type.equals(Vec2.class)) {
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

    private void encode(Class<? extends Entity> cls, Object... params) {
        try {
            encode(cls.getName());
            var ptypes = cls.getDeclaredConstructor().getParameterTypes();
            if (ptypes.length != params.length) {
                throw new RuntimeException("Mismatch in constructor args length");
            }
            for(int i = 0; i < ptypes.length; i++) {
                var type = ptypes[i];
                var param = params[i];

                if(!type.isAssignableFrom(param.getClass())){
                    throw new RuntimeException("Mismatch in constructor types");
                }
                if (type.equals(Integer.TYPE)) {
                    encode((int) param);
                } else if(type.equals(Float.TYPE)) {
                    encode((float) param);
                } else if(type.equals(String.class)) {
                    encode((String) param);
                } else if(type.equals(Vec2.class)) {
                    encode((Vec2) param);
                }
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor found");
        }
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
//            comm.execute(null);
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
