package dev.pages.creeperbabytea.the_forgotten.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.AbilitiesContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class AbilitySlotArgument implements ArgumentType<AbilitiesContainer.AbilitySlot> {
    private static final Collection<String> EXAMPLES = Stream.of(AbilitiesContainer.AbilitySlot.left, AbilitiesContainer.AbilitySlot.shift_left).map(s -> s.name().toLowerCase()).toList();
    private static final AbilitiesContainer.AbilitySlot[] VALUES = AbilitiesContainer.AbilitySlot.values();

    public static AbilitiesContainer.AbilitySlot getSlot(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, AbilitiesContainer.AbilitySlot.class);
    }

    public static AbilitySlotArgument abilitySlot() {
        return new AbilitySlotArgument();
    }

    @Override
    public AbilitiesContainer.AbilitySlot parse(StringReader reader) {
        return AbilitiesContainer.AbilitySlot.valueOf(reader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return builder.suggest("left").suggest("right").suggest("left_shift").suggest("right_shift").buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static ArgumentTypeInfo<AbilitySlotArgument, SingletonArgumentInfo<AbilitySlotArgument>.Template> getInfo() {
        return SingletonArgumentInfo.contextFree(AbilitySlotArgument::abilitySlot);
    }
}
