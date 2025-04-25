package dev.pages.creeperbabytea.the_forgotten;

import dev.pages.creeperbabytea.common.register.Register;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import dev.pages.creeperbabytea.the_forgotten.init.ForgottenAbilities;
import dev.pages.creeperbabytea.the_forgotten.init.ForgottenEnrichments;
import dev.pages.creeperbabytea.the_forgotten.init.ForgottenItems;
import dev.pages.creeperbabytea.the_forgotten.init.Misc;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class Registrations {
    public static final dev.pages.creeperbabytea.common.register.Registrations REGISTRATIONS = new dev.pages.creeperbabytea.common.register.Registrations(TheForgotten.MODID);

    public static final ResourceLocation ABILITY_KEY = TheForgotten.modLoc("ability");
    public static final ResourceLocation ENRICHMENT_KEY = TheForgotten.modLoc("enrichment");

    public static final Register<AttachmentType<?>> ATTACHMENTS = REGISTRATIONS.getOrCreate(NeoForgeRegistries.ATTACHMENT_TYPES);
    public static final Register<Item> ITEMS = REGISTRATIONS.getOrCreate(Registries.ITEM).registerAdditionalInfo(Item.CODEC);
    public static final Register<ActiveAbility> ABILITIES = REGISTRATIONS.getOrCreate(ABILITY_KEY);

    public static final Register<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = REGISTRATIONS.getOrCreate(Registries.COMMAND_ARGUMENT_TYPE);
    public static final Register<ItemEnrichment> ENRICHMENTS = REGISTRATIONS.getOrCreate(ENRICHMENT_KEY);

    public static void init(IEventBus mod, IEventBus forge) {
        ABILITIES.makeRegistry(builder -> {});
        ENRICHMENTS.makeRegistry(builder -> {});

        Misc.init();
        ForgottenAbilities.init();
        ForgottenEnrichments.init();
        ForgottenItems.init();

        REGISTRATIONS.registerAll(mod, forge);
    }
}
