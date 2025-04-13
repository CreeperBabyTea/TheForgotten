package dev.pages.creeperbabytea.the_forgotten;

import dev.pages.creeperbabytea.common.register.Register;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ItemAbility;
import dev.pages.creeperbabytea.the_forgotten.init.ModAbilities;
import dev.pages.creeperbabytea.the_forgotten.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class Registrations {
    public static final dev.pages.creeperbabytea.common.register.Registrations REGISTRATIONS = new dev.pages.creeperbabytea.common.register.Registrations(TheForgotten.MODID);

    public static final Register<Item> ITEMS = REGISTRATIONS.getOrCreate(Registries.ITEM).makeStateMap();
    public static final Register<ItemAbility> ABILITIES = REGISTRATIONS.getOrCreate(TheForgotten.modLoc("item_ability"));

    public static void init(IEventBus mod, IEventBus forge) {
        ABILITIES.makeRegistry(builder -> builder.defaultKey(TheForgotten.modLoc("item_ability")));

        ModAbilities.init();
        ModItems.init();

        REGISTRATIONS.registerAll(mod, forge);
    }
}
