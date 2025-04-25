package dev.pages.creeperbabytea.the_forgotten.api.common.event;

import dev.pages.creeperbabytea.the_forgotten.api.common.core.item.ability.AbilityMarkers;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.sorcery.EntitySorcery;
import dev.pages.creeperbabytea.the_forgotten.api.common.item.ability.ActiveAbility;
import dev.pages.creeperbabytea.the_forgotten.i18n.TranslationKeys;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public abstract class ActivateAbilityEvent extends LivingEvent {
    protected final ActiveAbility ability;
    protected final EntitySorcery capability;

    protected int rawManaCost;
    protected float percentageManaCost;
    protected int lvl;
    protected int finalManaCost;
    protected int expectedManaCost;

    protected float reScaling = 1.0f;

    public ActivateAbilityEvent(ActiveAbility ability, int lvl, LivingEntity player) {
        super(player);
        this.ability = ability;
        this.capability = EntitySorcery.from(player);

        this.lvl = lvl;
        this.rawManaCost = ability.getManaCostRaw();
        this.percentageManaCost = ability.getManaCostPercentage();
    }

    public ActiveAbility getAbility() {
        return ability;
    }

    public EntitySorcery getCapability() {
        return capability;
    }

    public float getReScaling() {
        return reScaling;
    }

    public int getLvl() {
        return lvl;
    }

    public int getFinalManaCost() {
        return finalManaCost = capability.mana().isManaConsumeBlocked() ? 0 : getExpectedManaCost();
    }

    public int getExpectedManaCost() {
        return expectedManaCost = (int) (rawManaCost + capability.mana().getMana() * percentageManaCost);
    }

    public int getRawManaCost() {
        return rawManaCost;
    }

    public float getPercentageManaCost() {
        return percentageManaCost;
    }

    /**
     * 如果被取消了，那就是蓝不够或者冷却。仅在服务端发布。
     */
    public static class Pre extends ActivateAbilityEvent implements ICancellableEvent {
        protected final ItemStack itemStack;

        public Pre(ActiveAbility ability, int lvl, LivingEntity player, ItemStack itemStack) {
            super(ability, lvl, player);
            this.itemStack = itemStack;
            if (capability == null) {
                this.setCanceled(true);
                return;
            }

            if (!getAbility().noCooldown()) {
                var markers = AbilityMarkers.from(getEntity());
                if (markers.isOnCooldown(ability))
                    setCanceled(true);
            }

            getAbility().canActivate(this);
        }

        public void finallyCheck() {
            getExpectedManaCost();
            getFinalManaCost();

            var mana = capability.mana();

            if (getFinalManaCost() > mana.getMana()) {
                if (getEntity() instanceof Player player)
                    player.displayClientMessage(Component.translatable(TranslationKeys.ABILITY_NO_MANA_ALERT), false);
                this.setCanceled(true);
            }
        }

        public void mapRawManaCost(Int2IntFunction function) {
            this.setRawManaCost(function.get(rawManaCost));
        }

        public void setRawManaCost(int rawManaCost) {
            this.rawManaCost = rawManaCost;
        }

        public void mapPercentageManaCost(Float2FloatFunction function) {
            this.setPercentageManaCost(function.get(percentageManaCost));
        }

        public void setPercentageManaCost(float percentageManaCost) {
            this.percentageManaCost = percentageManaCost;
        }

        public void mapRescaling(Float2FloatFunction function) {
            setReScaling(function.get(reScaling));
        }

        public void setReScaling(float reScaling) {
            this.reScaling = reScaling;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    /**
     * 同时在服务端和客户端发布。
     */
    public static class Post extends ActivateAbilityEvent {
        public Post(ActiveAbility ability, int lvl, LivingEntity entity, float reScaling, int finalManaCost, int expectedManaCost) {
            super(ability, lvl, entity);
            this.finalManaCost = finalManaCost;
            this.reScaling = reScaling;
            this.expectedManaCost = expectedManaCost;
            ability.activate(lvl, entity, reScaling);
            ability.postActivate(lvl, entity, reScaling, finalManaCost, expectedManaCost);
        }
    }
}
