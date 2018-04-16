package se.tdfpro.elements.server.command;

import se.tdfpro.elements.server.engine.Vec2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

public class Decoder {
    private ByteBuffer buf;

    public Decoder(ByteBuffer buf) {
        this.buf = buf;
    }

    public Decoder(byte[] buf) {
        this(ByteBuffer.wrap(buf));
    }

    public Command decode() {
        try {
            String clsName = decodeString();
            Class<?> cls = Class.forName(clsName);
            Command comm = (Command) cls.getDeclaredConstructor().newInstance();
            Arrays.stream(cls.getFields())
                    .filter(f -> f.getAnnotation(Send.class) != null)
                    .sorted(Comparator.comparing(Field::getName))
                    .forEach(f -> decode(comm, f));
            return comm;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void decode(Command obj, Field f) {
        try {
            if (f.getType().equals(Integer.TYPE)) {
                f.setInt(obj, decodeInt());
            } else if(f.getType().equals(Float.TYPE)) {
                f.setFloat(obj, decodeFloat());
            } else if(f.getType().equals(String.class)) {
                f.set(obj, decodeString());
            } else if(f.getType().equals(Vec2.class)) {
                f.set(obj, decodeVec2());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int decodeInt() {
        return buf.getInt();
    }

    public float decodeFloat() {
        return buf.getFloat();
    }

    public Vec2 decodeVec2() {
        return new Vec2(decodeFloat(), decodeFloat());
    }

    public String decodeString() {
        var len = decodeInt();
        byte[] strbuf = new byte[len];
        buf.get(strbuf, 0, len);
        return Charset.forName("UTF-8").decode(ByteBuffer.wrap(strbuf)).toString();
    }
}
