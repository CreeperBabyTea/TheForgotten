package dev.pages.creeperbabytea.the_forgotten.client;

import dev.pages.creeperbabytea.client.gui.LayerWrapper;
import dev.pages.creeperbabytea.the_forgotten.client.gui.ManaLayer;
import dev.pages.creeperbabytea.the_forgotten.client.gui.OverflowBarLayer;

public class Layers {
    public static void init() {
        LayerWrapper.register(new ManaLayer());
        LayerWrapper.register(new OverflowBarLayer());
    }
}
