package se.tdfpro.elements.client.ui;

public interface Cooldown {
    boolean ready();

    void start();

    int getRemaining();

    String getRemainingText();

    float getRemainingQuotient();

    int getMax();
}
