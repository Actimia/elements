package se.tdfpro.elements.client;

import org.newdawn.slick.Input;

public interface Keybind {
    String getMnemonic();

    boolean test(Input input);

    static Keybind mouse(int btn) {
        return new Keybind() {
            private int button = btn;
            @Override
            public String getMnemonic() {
                return "M" + btn;
            }

            @Override
            public boolean test(Input input) {
                return input.isMouseButtonDown(btn);
            }
        };
    }

    static Keybind key(int code) {
        return new Keybind() {
            private int key = code;
            @Override
            public String getMnemonic() {
                return Input.getKeyName(key);
            }

            @Override
            public boolean test(Input input) {
                return input.isKeyDown(code);
            }
        };
    }
}
