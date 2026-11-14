package org.kokeyan.nationsystem.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.kokeyan.nationsystem.NationSystem;
import org.kokeyan.nationsystem.client.ClientHooks;

import java.util.List;

/** サーバー → クライアント: 鉱脈選択画面を開く */
public record OpenVeinSelectPayload(List<VeinEntry> veins) implements CustomPacketPayload {

    public static final Type<OpenVeinSelectPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(NationSystem.MOD_ID, "open_vein_select"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenVeinSelectPayload> STREAM_CODEC = StreamCodec.composite(
            VeinEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), OpenVeinSelectPayload::veins,
            OpenVeinSelectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenVeinSelectPayload payload, IPayloadContext context) {
        // クライアント専用クラスは、この分岐の中でだけ参照する(専用サーバーでのクラスロード事故を避ける)
        if (FMLEnvironment.dist.isClient()) {
            context.enqueueWork(() -> ClientHooks.openVeinSelect(payload.veins()));
        }
    }
}
