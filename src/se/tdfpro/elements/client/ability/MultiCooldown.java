package se.tdfpro.elements.client.ability;

import java.util.Comparator;
import java.util.List;

public class MultiCooldown implements Cooldown {

    private List<Cooldown> cooldowns;

    public MultiCooldown(Cooldown... cds) {
        this(List.of(cds));
    }

    public MultiCooldown(List<Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    @Override
    public boolean ready() {
        return cooldowns.stream().allMatch(Cooldown::ready);
    }

    @Override
    public void start() {
        cooldowns.forEach(Cooldown::start);
    }

    @Override
    public int getRemaining() {
        return cooldowns.stream()
            .max(Comparator.comparing(Cooldown::getRemaining))
            .map(Cooldown::getRemaining).orElse(0);
    }

    @Override
    public String getRemainingText() {
        return cooldowns.stream()
            .max(Comparator.comparing(Cooldown::getRemaining))
            .map(Cooldown::getRemainingText).orElse("");
    }

    @Override
    public float getRemainingQuotient() {
        return cooldowns.stream()
            .max(Comparator.comparing(Cooldown::getRemaining))
            .map(Cooldown::getRemainingQuotient).orElse(0f);
    }

    @Override
    public int getMax() {
        return cooldowns.stream()
            .mapToInt(Cooldown::getMax)
            .max().orElse(0);
    }
}
