package se.tdfpro.elements.server.commands;

import se.tdfpro.elements.server.engine.Vec2;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Decoder {
    private ByteBuffer buf;
    private int index = 0;
    public Decoder(ByteBuffer buf) {
        this.buf = buf;
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
        var strbuf = buf.slice().limit(len);
        return Charset.forName("UTF-8").decode(strbuf).toString();
    }
}
