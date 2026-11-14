package org.kokeyan.nationsystem.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(OpenVeinSelectPayload.TYPE, OpenVeinSelectPayload.STREAM_CODEC, OpenVeinSelectPayload::handle);
        registrar.playToServer(SelectVeinPayload.TYPE, SelectVeinPayload.STREAM_CODEC, SelectVeinPayload::handle);
    }

    private NetworkHandler() {}
}
