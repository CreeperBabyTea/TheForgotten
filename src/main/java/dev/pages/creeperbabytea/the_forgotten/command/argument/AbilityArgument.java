package dev.pages.creeperbabytea.the_forgotten.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class AbilityArgument implements ArgumentType<ActiveAbility> {
    public static ActiveAbility getAbility(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ActiveAbility.class);
    }

    private final HolderLookup.RegistryLookup<ActiveAbility> abilities;

    private AbilityArgument(CommandBuildContext context) {
        abilities = context.lookupOrThrow(Registrations.ABILITIES.getRegistryKey());
    }

    @Nullable
    @Override
    public ActiveAbility parse(StringReader reader) throws CommandSyntaxException {
        var loc = ResourceLocation.read(reader);
        var ref = abilities.get(ResourceKey.create(Registrations.ABILITIES.getRegistryKey(), loc));
        return ref.map(Holder.Reference::value).orElse(null);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(abilities.listElementIds().map(ResourceKey::location), builder);
    }

    public static AbilityArgument ability(CommandBuildContext context) {
        return new AbilityArgument(context);
    }

    public static ArgumentTypeInfo<AbilityArgument, SingletonArgumentInfo<AbilityArgument>.Template> getInfo() {
        return SingletonArgumentInfo.contextAware(AbilityArgument::ability);
    }
}