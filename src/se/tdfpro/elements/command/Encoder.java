package se.tdfpro.elements.command;

import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.ClientEntity;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

public class Encoder {
    private final ByteBuffer buf = ByteBuffer.allocate(1024);

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
            } else if (type.equals(Float.TYPE)) {
                encode(f.getFloat(obj));
            } else if (type.equals(String.class)) {
                encode((String) f.get(obj));
            } else if (type.equals(Vec2.class)) {
                encode((Vec2) f.get(obj));
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

    private void encode(Class<? extends ClientEntity> cls, Object... params) {
        try {
            encode(cls.getName());
            var pTypes = cls.getDeclaredConstructor().getParameterTypes();
            if (pTypes.length != params.length) {
                throw new RuntimeException("Mismatch in constructor args length");
            }
            for (int i = 0; i < pTypes.length; i++) {
                var type = pTypes[i];
                var param = params[i];

                if (!type.isAssignableFrom(param.getClass())) {
                    throw new RuntimeException("Mismatch in constructor types");
                }
                if (type.equals(Integer.TYPE)) {
                    encode((int) param);
                } else if (type.equals(Float.TYPE)) {
                    encode((float) param);
                } else if (type.equals(String.class)) {
                    encode((String) param);
                } else if (type.equals(Vec2.class)) {
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

    public static byte[] encodeCommand(Command command) {
        var enc = new Encoder();
        enc.encode(command);
        return enc.getBytes();
    }
}
