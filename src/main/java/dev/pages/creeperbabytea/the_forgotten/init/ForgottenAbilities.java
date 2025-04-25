package dev.pages.creeperbabytea.the_forgotten.init;

import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import dev.pages.creeperbabytea.the_forgotten.items.abilities.OverflowAbility;
import dev.pages.creeperbabytea.the_forgotten.items.abilities.StarterAbility;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ForgottenAbilities {
    public static final DeferredHolder<ActiveAbility, StarterAbility> STARTER = Registrations.ABILITIES.registerSingleton("starter", new StarterAbility());
    public static final DeferredHolder<ActiveAbility, OverflowAbility> OVER_FLOW = Registrations.ABILITIES.registerSingleton("overflow", new OverflowAbility());

    public static void init() {
    }
}
