package se.tdfpro.elements.command;

import org.newdawn.slick.Color;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.util.Vec2;
import se.tdfpro.elements.entity.physics.PhysicsEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class Decoder<T extends Command> {
    private final ByteBuffer buf;

    private Decoder(ByteBuffer buf) {
        this.buf = buf;
    }

    @SuppressWarnings("unchecked")
    private T decode() {
        try {
            String clsName = decodeString();
            Class<?> cls = Class.forName(clsName);
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
            } else if (type.equals(Float.TYPE)) {
                f.setFloat(obj, decodeFloat());
            } else if (type.equals(String.class)) {
                f.set(obj, decodeString());
            } else if (type.equals(Vec2.class)) {
                f.set(obj, decodeVec2());
            } else if (type.equals(PhysicsEntity.class)) {
                f.set(obj, decodeEntity());
            } else if (type.equals(Color.class)){
                f.set(obj, decodeColor());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Color decodeColor() {
        return new Color(decodeInt());
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

    private Entity decodeEntity() {

        try {
            var name = decodeString();
            var eid = decodeInt();
            var parent = decodeInt();

            // hic sunt dracones
            @SuppressWarnings("unchecked")
            Class<? extends Entity> cls = (Class<? extends Entity>) Class.forName(name);
            var constructor = Arrays.stream(cls.getDeclaredConstructors())
                .filter(c -> c.getAnnotation(DecodeConstructor.class) != null).findFirst().get();
            var pTypes = constructor.getParameterTypes();
            var params = new Object[constructor.getParameterCount()];

            for (int i = 0; i < params.length; i++) {
                var type = pTypes[i];
                if (type.equals(Integer.TYPE)) {
                    params[i] = decodeInt();
                } else if (type.equals(Float.TYPE)) {
                    params[i] = decodeFloat();
                } else if (type.equals(String.class)) {
                    params[i] = decodeString();
                } else if (type.equals(Vec2.class)) {
                    params[i] = decodeVec2();
                }
            }
            @SuppressWarnings("JavaReflectionInvocation")
            Entity entity = (Entity) constructor.newInstance(params);

            // terra firma
            entity.setId(eid);
            entity.setParent(parent);
            return entity;
        } catch (ClassNotFoundException |
            InstantiationException |
            IllegalAccessException |
            InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String decodeString() {
        var len = decodeInt();
        byte[] strBuffer = new byte[len];
        buf.get(strBuffer, 0, len);
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(strBuffer)).toString();
    }

    public static <T extends Command> T decode(byte[] buffer) {
        return decode(ByteBuffer.wrap(buffer));
    }

    public static <T extends Command> T decode(ByteBuffer buffer) {
        return new Decoder<T>(buffer).decode();
    }
}
