package org.kokeyan.nationsystem.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.kokeyan.nationsystem.NationSystem;
import org.kokeyan.nationsystem.token.TokenService;

/** クライアント → サーバー: どの鉱脈を選んだか(番号)。検証はすべてサーバー側で行う */
public record SelectVeinPayload(int index) implements CustomPacketPayload {

    public static final Type<SelectVeinPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(NationSystem.MOD_ID, "select_vein"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectVeinPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SelectVeinPayload::index,
            SelectVeinPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SelectVeinPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                TokenService.useToken(player, payload.index());
            }
        });
    }
}
