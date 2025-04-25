package dev.pages.creeperbabytea.the_forgotten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import dev.pages.creeperbabytea.the_forgotten.command.argument.AbilityArgument;
import dev.pages.creeperbabytea.the_forgotten.command.argument.AbilitySlotArgument;
import dev.pages.creeperbabytea.the_forgotten.command.argument.EnrichmentArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCommands {
    public static void init(IEventBus game) {
        game.addListener(ModCommands::registerCommands);
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext context = event.getBuildContext();

        SetAbilityCommand.register(dispatcher, context);
        SetEnrichmentCommand.register(dispatcher, context);

        registerArgumentTypes();
    }

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<AbilitySlotArgument, SingletonArgumentInfo<AbilitySlotArgument>.Template>>
            ABILITY_SLOT_ARG_INFO = Registrations.ARGUMENT_TYPES.register("ability_slot", AbilitySlotArgument::getInfo);
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<AbilityArgument, SingletonArgumentInfo<AbilityArgument>.Template>>
            ABILITY_ARG_INFO = Registrations.ARGUMENT_TYPES.register("ability", AbilityArgument::getInfo);
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<EnrichmentArgument, SingletonArgumentInfo<EnrichmentArgument>.Template>>
            ENRICHMENT_ARG_INFO = Registrations.ARGUMENT_TYPES.register("enrichment", EnrichmentArgument::getInfo);

    public static void registerArgumentTypes() {
        ArgumentTypeInfos.registerByClass(AbilitySlotArgument.class, ABILITY_SLOT_ARG_INFO.get());
        ArgumentTypeInfos.registerByClass(AbilityArgument.class, ABILITY_ARG_INFO.get());
        ArgumentTypeInfos.registerByClass(EnrichmentArgument.class, ENRICHMENT_ARG_INFO.get());
    }
}
