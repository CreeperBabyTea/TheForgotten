package dev.pages.creeperbabytea.the_forgotten.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemInfo;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiersContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilityInstance;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import dev.pages.creeperbabytea.the_forgotten.command.argument.AbilityArgument;
import dev.pages.creeperbabytea.the_forgotten.command.argument.AbilitySlotArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SetAbilityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("ability")
                        .requires(stack -> stack.hasPermission(2))
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .then(
                                                Commands.argument("slot", AbilitySlotArgument.abilitySlot())
                                                        .then(
                                                                Commands.argument("ability", AbilityArgument.ability(context))
                                                                        .then(
                                                                                Commands.argument("lvl", IntegerArgumentType.integer(0, 255))
                                                                                        .then(
                                                                                                Commands.argument("forced", BoolArgumentType.bool())
                                                                                                        .executes(ctx -> {
                                                                                                            var targets = EntityArgument.getPlayers(ctx, "targets");
                                                                                                            var slot = AbilitySlotArgument.getSlot(ctx, "slot");
                                                                                                            var ability = AbilityArgument.getAbility(ctx, "ability");
                                                                                                            var lvl = IntegerArgumentType.getInteger(ctx, "lvl");
                                                                                                            var forced = BoolArgumentType.getBool(ctx, "forced");
                                                                                                            targets.forEach(player -> setAbilityFor(player, slot, ability, lvl, forced));
                                                                                                            return 1;
                                                                                                        })
                                                                                        )
                                                                                        .executes(ctx -> {
                                                                                            var targets = EntityArgument.getPlayers(ctx, "targets");
                                                                                            var slot = AbilitySlotArgument.getSlot(ctx, "slot");
                                                                                            var ability = AbilityArgument.getAbility(ctx, "ability");
                                                                                            var lvl = IntegerArgumentType.getInteger(ctx, "lvl");
                                                                                            targets.forEach(player -> setAbilityFor(player, slot, ability, lvl, false));
                                                                                            return 1;

                                                                                        })
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    public static int setAbilityFor(ServerPlayer player, AbilitiesContainer.AbilitySlot slot, ActiveAbility ability, int lvl, boolean forced) {
        if (!applyAbility(player.getMainHandItem(), slot, ability, lvl, forced))
            if (!applyAbility(player.getOffhandItem(), slot, ability, lvl, forced))
                return -1;
        return 1;
    }

    private static boolean applyAbility(ItemStack stack, AbilitiesContainer.AbilitySlot slot, ActiveAbility ability, int lvl, boolean forced) {
        if (ModifiableItemInfo.isModifiable(stack.getItem()) && (
                forced
                        || !ModifiableItemInfo.getInfo(stack.getItem()).getDisabledSlots().contains(slot)
                        && ability.getMaxLvl() <= lvl)) {
            var container = ModifiersContainer.of(stack);
            container.getAbilities().put(slot, new AbilityInstance(ability, lvl));
            container.update();
            return true;
        }
        return false;
    }
}
