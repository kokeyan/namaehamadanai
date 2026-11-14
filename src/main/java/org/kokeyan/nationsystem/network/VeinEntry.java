package org.kokeyan.nationsystem.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/** 選択画面に表示する鉱脈1件(サーバーのconfigをクライアントへ運ぶための入れ物) */
public record VeinEntry(String id, String label) {

    public static final StreamCodec<ByteBuf, VeinEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, VeinEntry::id,
            ByteBufCodecs.STRING_UTF8, VeinEntry::label,
            VeinEntry::new
    );
}
