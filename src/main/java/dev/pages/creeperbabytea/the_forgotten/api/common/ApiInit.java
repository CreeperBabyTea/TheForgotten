package dev.pages.creeperbabytea.the_forgotten.api.common;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import dev.pages.creeperbabytea.the_forgotten.client.Layers;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.ForgottenPackets;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.ListenerInit;
import net.neoforged.bus.api.IEventBus;

public class ApiInit {
    //public static final DeferredHolder<MobEffect, ActiveAbility.AbilityCooldownEffect> ABILITY_COOLDOWN_EFFECT =
    //        Registrations.EFFECTS.register("ability_cooldown_effect", ActiveAbility.AbilityCooldownEffect::new);
    //public static final DeferredHolder<MobEffect, DurableAbility.DurableAbilityMarkEffect> DURABLE_ABILITY_EFFECT =
    //        Registrations.EFFECTS.register("durable_ability_effect", DurableAbility.DurableAbilityMarkEffect::new);

    public static void init(IEventBus mod, IEventBus game) {
        ListenerInit.init();
        ForgottenPackets.init(mod, game);
        Layers.init();
        SorceryAttachments.init(mod, game);
    }
}
