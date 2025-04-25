package dev.pages.creeperbabytea.the_forgotten.api.common.networking;

import dev.pages.creeperbabytea.common.init.Packets;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener.CAbilityActivateListener;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener.CPlayerReloadingListener;
import dev.pages.creeperbabytea.the_forgotten.api.common.networking.listener.SModifiableItemRawMouseInputListener;

public class ListenerInit {
    public static void init() {
        Packets.RAW_MOUSE_INPUT_PACKET_PACKET_PROVIDER.addServerListener(new SModifiableItemRawMouseInputListener());
        ForgottenPackets.S_ABILITY_ACTIVATE.addClientListener(new CAbilityActivateListener());
        ForgottenPackets.S_PLAYER_RELOADING.addClientListener(new CPlayerReloadingListener());
    }
}
