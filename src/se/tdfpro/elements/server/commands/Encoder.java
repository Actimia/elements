package se.tdfpro.elements.server.commands;

import se.tdfpro.elements.server.engine.Vec2;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Encoder {
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    public ByteBuffer getBytes() {
        var len = buf.position();
        return buf.rewind().slice().limit(len);
    }

    public void encode(ClientCommand obj, Field f) {
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
        e.encode("hello");
        e.encode(3.1415f);
        ByteBuffer buf = e.getBytes();
        System.out.println(Arrays.toString(buf.array()));
        System.out.println(e.getBytes());
        // [0, 0, 0, 4, 0, 0, 0, 5, 104, 101, 108, 108, 111, 64, 73, 14, 86]
    }
}
