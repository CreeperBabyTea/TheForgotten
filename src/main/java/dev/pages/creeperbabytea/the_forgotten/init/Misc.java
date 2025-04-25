package dev.pages.creeperbabytea.the_forgotten.init;

import dev.pages.creeperbabytea.the_forgotten.Registrations;
import dev.pages.creeperbabytea.the_forgotten.api.common.core.SorceryAttachments;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Misc {
    public static final DeferredHolder<AttachmentType<?> , AttachmentType<SorceryAttachments>> ENTITY_ATTACHMENTS =
            Registrations.ATTACHMENTS.register("sorcery", () -> AttachmentType.builder(SorceryAttachments::empty).serialize(SorceryAttachments.CODEC).build());

    public static void init() {
    }
}
