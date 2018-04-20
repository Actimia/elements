package se.tdfpro.elements.client.ui;

import se.tdfpro.elements.client.GameClient;

public class SingleCooldown implements Cooldown {
    private long target = 0;
    private final int max;

    public SingleCooldown(float seconds) {this.max = (int) (seconds * 1000);}

    public SingleCooldown(int millis) {this.max = millis;}

    @Override
    public boolean ready() {
        return target <= GameClient.getTime();
    }

    @Override
    public void start() {
        target = GameClient.getTime() + max;
    }

    @Override
    public int getRemaining() {
        return (int) Math.max(target - GameClient.getTime(), 0);
    }

    @Override
    public String getRemainingText() {
        var remaining = getRemaining();
        if (remaining == 0) return "";
        return String.format("%.1f", remaining / 1000f);
    }

    @Override
    public float getRemainingQuotient() {
        return getRemaining() / (float) max;
    }

    @Override
    public int getMax() {
        return max;
    }
}

