package dev.pages.creeperbabytea.the_forgotten.api.common.item.ability;

import com.mojang.serialization.Codec;
import dev.pages.creeperbabytea.common.register.Registrable;
import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.TheForgotten;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.Mana;
import dev.pages.creeperbabytea.the_forgotten.api.common.event.ActivateAbilityEvent;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.packets.SAbilityActivatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class ActiveAbility implements Registrable<ActiveAbility> {
    public static final Codec<ActiveAbility> CODEC = Registrations.ABILITIES.getRegistry().get().byNameCodec();

    private final int maxLvl;
    private final int coolDownTicks;
    private int manaCost = 0;
    private float manaCostPercentage = 0f;

    private ResourceLocation name;
    private int toolTipColor = 0xFFFFFF;

    /**
     * @param maxLvl        该技能的最高等级。
     * @param coolDownTicks 冷却时间，单位gt。
     */
    public ActiveAbility(int maxLvl, int coolDownTicks) {
        this.maxLvl = maxLvl;
        this.coolDownTicks = coolDownTicks;
    }

    public boolean noCooldown() {
        return this.getCoolDownTicks() <= 0;
    }

    protected void applyCooldown(LivingEntity user) {
        if (!noCooldown()) {
            var markers = AbilityMarkers.from(user);
            markers.applyCooldown(this, coolDownTicks);
        }
    }

    /**
     * 可能触发技能。这里会检查技能是否已经冷却，以及实现一系列事件系统
     */
    public void maybeActivate(int lvl, LivingEntity user, ItemStack stack) {
        var pre = new ActivateAbilityEvent.Pre(this, lvl, user, stack);
        TheForgotten.GAME.post(pre);
        pre.finallyCheck();
        if (!pre.isCanceled()) {
            var post = new ActivateAbilityEvent.Post(this, lvl, user, pre.getReScaling(), pre.getFinalManaCost(), pre.getExpectedManaCost());
            TheForgotten.GAME.post(post);
            if (user instanceof ServerPlayer serverPlayer)
                TheForgotten.NETWORKING.sendToPlayer(serverPlayer, new SAbilityActivatePacket(this, lvl, pre.getReScaling(), pre.getFinalManaCost(), pre.getExpectedManaCost()));
        }
    }

    /**
     * 蓝量消耗等一系列行为，和{@link #activate}一起由{@link ActivateAbilityEvent.Post}执行。
     */
    public void postActivate(int lvl, LivingEntity entity, float reScaling, int finalManaCost, int expectedManaCost) {
        Mana mana = Mana.from(entity);
        mana.addMana(-finalManaCost);
        if (!noCooldown())
            applyCooldown(entity);
    }

    /**
     * 不要注册到事件总线，会由{@link ActivateAbilityEvent.Pre}自动跑
     */
    public void canActivate(ActivateAbilityEvent.Pre event) {
    }

    /**
     * 在这里实现技能的具体行为。
     */
    public abstract void activate(int lvl, LivingEntity user, float reScaling);

    public int getMaxLvl() {
        return maxLvl;
    }

    public int getCoolDownTicks() {
        return coolDownTicks;
    }

    protected void setToolTipColor(int toolTipColor) {
        this.toolTipColor = toolTipColor;
    }

    public AbilityInstance getDefaultInstance() {
        return new AbilityInstance(this, 1);
    }

    public int getToolTipColor() {
        return toolTipColor;
    }

    public String getTranslationKey() {
        return "item.ability." + name.toLanguageKey();
    }

    public String getLoreTranslationKey() {
        return "item.ability.lore." + name.toLanguageKey();
    }

    @Override
    public void setName(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    public void setManaCostRaw(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setManaCostPercentage(float manaCostPercentage) {
        this.manaCostPercentage = manaCostPercentage;
    }

    public int getManaCostRaw() {
        return manaCost;
    }

    public float getManaCostPercentage() {
        return manaCostPercentage;
    }
}
