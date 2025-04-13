package dev.pages.creeperbabytea.the_forgotten.api.common.item;


import dev.pages.creeperbabytea.common.register.EntryState;
import net.minecraft.world.item.Item;

public class ModifiableItemState extends EntryState<Item> {
    private boolean allowedOffhand = false;

    public ModifiableItemState(Item item) {
        super(item);
        if (item instanceof ModifiableItem)
            ((ModifiableItem) item).setState(this);
    }

    public void setAllowedOffhand(boolean allowedOffhand) {
        this.allowedOffhand = allowedOffhand;
    }

    public boolean isAllowedOffhand() {
        return allowedOffhand;
    }

    public static class ModifiableItem extends Item {
        private ModifiableItemState state;

        public ModifiableItem(Properties properties) {
            super(properties);
        }

        public void setState(ModifiableItemState state) {
            if (this.state != null)
                throw new IllegalStateException("Trying to set state for a modifiable item whose state is already set: " + this);
            this.state = state;
        }
    }
}
