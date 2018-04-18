package se.tdfpro.elements.server.command;

import se.tdfpro.elements.server.physics.entity.ClientEntity;
import se.tdfpro.elements.server.physics.Vec2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

public class Decoder <T extends Command> {
    private ByteBuffer buf;

    private Decoder(ByteBuffer buf) {
        this.buf = buf;
    }

    private T decode() {
        try {
            String clsName = decodeString();
            Class<?> cls = Class.forName(clsName);
            //noinspection unchecked
            T comm = (T) cls.getDeclaredConstructor().newInstance();
            Arrays.stream(cls.getFields())
                    .filter(f -> f.getAnnotation(Send.class) != null)
                    .sorted(Comparator.comparing(Field::getName))
                    .forEach(f -> decode(comm, f));
            return comm;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void decode(T obj, Field f) {
        var type = f.getType();
        try {
            if (type.equals(Integer.TYPE)) {
                f.setInt(obj, decodeInt());
            } else if(type.equals(Float.TYPE)) {
                f.setFloat(obj, decodeFloat());
            } else if(type.equals(String.class)) {
                f.set(obj, decodeString());
            } else if(type.equals(Vec2.class)) {
                f.set(obj, decodeVec2());
            } else if(type.equals(ClientEntity.class)){
                f.set(obj, decodeEntity());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private int decodeInt() {
        return buf.getInt();
    }

    private float decodeFloat() {
        return buf.getFloat();
    }

    private Vec2 decodeVec2() {
        return new Vec2(decodeFloat(), decodeFloat());
    }

    private ClientEntity decodeEntity() {

        try {
            var name = decodeString();
            Class<? extends ClientEntity> cls = (Class<? extends ClientEntity>) Class.forName(name);
            var constructor = cls.getDeclaredConstructor();
            var ptypes = constructor.getParameterTypes();
            var params = new Object[constructor.getParameterCount()];

            for (int i = 0; i < params.length; i++) {
                var type = ptypes[i];
                if (type.equals(Integer.TYPE)) {
                    params[i] = decodeInt();
                } else if(type.equals(Float.TYPE)) {
                    params[i] = decodeFloat();
                } else if(type.equals(String.class)) {
                    params[i] = decodeString();
                } else if(type.equals(Vec2.class)) {
                    params[i] = decodeVec2();
                } else if(type.equals(ClientEntity.class)){
                    params[i] = decodeEntity();
                }
            }

            var res = constructor.newInstance(params);
            return res;
        } catch (ClassNotFoundException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String decodeString() {
        var len = decodeInt();
        byte[] strbuf = new byte[len];
        buf.get(strbuf, 0, len);
        return Charset.forName("UTF-8").decode(ByteBuffer.wrap(strbuf)).toString();
    }

    public static <T extends Command> T decode(byte[] buffer){
        return decode(ByteBuffer.wrap(buffer));
    }

    public static <T extends Command> T decode(ByteBuffer buffer) {
        return new Decoder<T>(buffer).decode();
    }
}
