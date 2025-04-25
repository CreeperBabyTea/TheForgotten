package dev.pages.creeperbabytea.the_forgotten.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.enrichment.ItemEnrichment;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class EnrichmentArgument implements ArgumentType<ItemEnrichment> {
    public static ItemEnrichment getEnrichment(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ItemEnrichment.class);
    }

    private final HolderLookup.RegistryLookup<ItemEnrichment> enrichments;

    private EnrichmentArgument(CommandBuildContext context) {
        enrichments = context.lookupOrThrow(Registrations.ENRICHMENTS.getRegistryKey());
    }

    @Nullable
    @Override
    public ItemEnrichment parse(StringReader reader) throws CommandSyntaxException {
        var loc = ResourceLocation.read(reader);
        var ref = enrichments.get(ResourceKey.create(Registrations.ENRICHMENTS.getRegistryKey(), loc));
        return ref.map(Holder.Reference::value).orElse(null);
    }


    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(enrichments.listElementIds().map(ResourceKey::location), builder);
    }

    public static EnrichmentArgument enrichment(CommandBuildContext context) {
        return new EnrichmentArgument(context);
    }

    public static ArgumentTypeInfo<EnrichmentArgument, SingletonArgumentInfo<EnrichmentArgument>.Template> getInfo() {
        return SingletonArgumentInfo.contextAware(EnrichmentArgument::enrichment);
    }
}
