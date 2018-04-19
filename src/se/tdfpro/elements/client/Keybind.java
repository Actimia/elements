package se.tdfpro.elements.client;

import org.newdawn.slick.Input;

public interface Keybind {
    String getMnemonic();

    boolean test(Input input);

    static Keybind mouse(int btn) {
        return new Keybind() {
            private final int button = btn;

            @Override
            public String getMnemonic() {
                return "M" + button;
            }

            @Override
            public boolean test(Input input) {
                return input.isMouseButtonDown(button);
            }
        };
    }

    static Keybind key(int code) {
        return new Keybind() {
            private final int key = code;

            @Override
            public String getMnemonic() {
                return Input.getKeyName(key);
            }

            @Override
            public boolean test(Input input) {
                return input.isKeyDown(key);
            }
        };
    }
}
