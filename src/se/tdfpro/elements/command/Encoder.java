package se.tdfpro.elements.command;

import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.ClientEntity;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;

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
            } else if (type.equals(PhysicsEntity.class)){
                encode((PhysicsEntity) f.get(obj));
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

    public void encode(PhysicsEntity ent) {
        encode(ent.getClass().getName());
        encode(ent.getEid());
        ent.encodeConstructorParams(this);
    }

    public void encode(String s) {
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
