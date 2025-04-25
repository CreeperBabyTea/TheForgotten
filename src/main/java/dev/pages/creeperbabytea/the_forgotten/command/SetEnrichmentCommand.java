package dev.pages.creeperbabytea.the_forgotten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemInfo;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.EnrichmentInstance;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import dev.pages.creeperbabytea.the_forgotten.command.argument.EnrichmentArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SetEnrichmentCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("enrichment")
                        .requires(s -> s.hasPermission(2))
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .then(
                                                Commands.argument("enrichment", EnrichmentArgument.enrichment(context))
                                                        .then(
                                                                Commands.argument("lvl", IntegerArgumentType.integer())
                                                                        .then(
                                                                                Commands.argument("forced", BoolArgumentType.bool())
                                                                                        .executes(ctx -> {
                                                                                            var targets = EntityArgument.getPlayers(ctx, "targets");
                                                                                            var enrichment = EnrichmentArgument.getEnrichment(ctx, "enrichment");
                                                                                            var lvl = IntegerArgumentType.getInteger(ctx, "lvl");
                                                                                            var forced = BoolArgumentType.getBool(ctx, "forced");
                                                                                            targets.forEach(p -> setEnrichmentFor(p, enrichment, lvl, forced));
                                                                                            return 1;
                                                                                        })
                                                                        )
                                                                        .executes(ctx -> {
                                                                            var targets = EntityArgument.getPlayers(ctx, "targets");
                                                                            var enrichment = EnrichmentArgument.getEnrichment(ctx, "enrichment");
                                                                            var lvl = IntegerArgumentType.getInteger(ctx, "lvl");
                                                                            targets.forEach(p -> setEnrichmentFor(p, enrichment, lvl, false));
                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        )
        );
    }

    private static int setEnrichmentFor(Player player, ItemEnrichment enrichment, int lvl, boolean forced) {
        if (!applyEnrichment(player, player.getMainHandItem(), enrichment, lvl, forced))
            if (!applyEnrichment(player, player.getOffhandItem(), enrichment, lvl, forced))
                return -1;
        return 1;
    }

    private static boolean applyEnrichment(Player player, ItemStack stack, ItemEnrichment enrichment, int lvl, boolean forced) {
        if (ModifiableItemInfo.isModifiable(stack.getItem()) &&
                (forced || enrichment.getMaxLvl() <= lvl)) {
            var container = ModifiersContainer.of(stack);
            container.getEnrichments().put(new EnrichmentInstance(enrichment, lvl, player));
            container.update();
            return true;
        }
        return false;
    }
}
