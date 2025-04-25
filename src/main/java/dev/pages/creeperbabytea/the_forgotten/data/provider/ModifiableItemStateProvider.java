package dev.pages.creeperbabytea.the_forgotten.data.provider;

import dev.pages.creeperbabytea.common.register.EntryInfo;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.data.ModifiableItemDefinitionReader;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ModifiableItemInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModifiableItemStateProvider extends JsonCodecProvider<EntryInfo.BuiltInEntryInfo<Item>> {
    public ModifiableItemStateProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, PackOutput.Target.DATA_PACK, ModifiableItemDefinitionReader.FOLDER_PATH, ModifiableItemInfo.CODEC, lookupProvider, TheForgotten.MODID);
    }

    /**
     * 提供需要额外注册的物品信息。<br>
     * <strong>注意：不要添加通过{@link EntryInfo}注册的项目</strong>
     */
    @Override
    protected void gather() {
        registerItem(Items.WOODEN_SWORD, info -> info.setRescaling(0.9f));
        registerItem(Items.STONE_SWORD, info -> info.setRescaling(0.85f));
        registerItem(Items.IRON_SWORD, info -> info.setRescaling(0.8f));
        registerItem(Items.GOLDEN_SWORD, info -> info.setRescaling(1.5f));
        registerItem(Items.DIAMOND_SWORD, info -> info.setRescaling(1.1f));
        registerItem(Items.NETHERITE_SWORD, info -> info.setRescaling(1.1f));

        registerItem(Items.NETHERITE_HOE, info -> info.ULTRA_HIGH_RESCALING().disableAllSlots());
    }

    /**
     * 好吧...<br>
     * 这是为了让所有配置文件都生成在the_forgotten下
     */
    /*
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

        gather();

        return lookupProvider.thenCompose(provider -> {
            final DynamicOps<JsonElement> dynamicOps = new ConditionalOps<>(RegistryOps.create(JsonOps.INSTANCE, provider), ICondition.IContext.EMPTY);

            this.conditions.forEach((id, withConditions) -> {
                final Path path = this.pathProvider.json(ResourceLocation.fromNamespaceAndPath(TheForgotten.MODID, id.getPath()));

                futuresBuilder.add(CompletableFuture.supplyAsync(() -> {
                    final Codec<Optional<WithConditions<EntryInfo.BuiltInEntryInfo<Item>>>> withConditionsCodec = ConditionalOps.createConditionalCodecWithConditions(this.codec);
                    return withConditionsCodec.encodeStart(dynamicOps, Optional.of(withConditions)).getOrThrow(msg -> new RuntimeException("Failed to encode %s: %s".formatted(path, msg)));
                }).thenComposeAsync(encoded -> DataProvider.saveStable(cache, encoded, path)));
            });

            return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
        });
    }
*/
    private <I extends Item> void registerItem(I item, Function<ModifiableItemInfo<I>, ModifiableItemInfo<I>> consumer) {
          unconditional(BuiltInRegistries.ITEM.getKey(item), consumer.apply(new ModifiableItemInfo<>(item)).mapToRawInfo());
    }

    @Override
    public String getName() {
        return super.getName() + " of " + TheForgotten.TITLE;
    }
}
