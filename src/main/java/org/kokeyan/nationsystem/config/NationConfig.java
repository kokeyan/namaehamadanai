package org.kokeyan.nationsystem.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

/** config/nationsystem-common.toml */
public final class NationConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<String> TOKEN_ITEM;
    public static final ModConfigSpec.BooleanValue GRANT_ON_FOUNDING;
    public static final ModConfigSpec.IntValue TOKENS_PER_NATION;
    public static final ModConfigSpec.BooleanValue ONCE_PER_PLAYER;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> VEINS;
    public static final ModConfigSpec.ConfigValue<String> COMMAND_TEMPLATE;
    public static final ModConfigSpec.DoubleValue MULTIPLIER;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.push("token");
        TOKEN_ITEM = b.comment("鉱脈トークンのアイテムID(KubeJSで登録したもの)")
                .define("tokenItem", "namaehamadanai:vein_token");
        GRANT_ON_FOUNDING = b.comment("建国した瞬間に、建国者へトークンを渡すか")
                .define("grantOnFounding", true);
        TOKENS_PER_NATION = b.comment("建国時に渡すトークンの個数")
                .defineInRange("tokensPerNation", 1, 1, 64);
        ONCE_PER_PLAYER = b.comment("true: 1人のプレイヤーにつき生涯1回だけ渡す(建国→解散を繰り返してトークンを稼ぐのを防ぐ)")
                .define("oncePerPlayer", true);
        b.pop();

        b.push("vein");
        VEINS = b.comment("選択できる鉱脈。書式は「鉱脈レシピID|画面に表示する名前」")
                .defineList("veins", List.of(
                        "coedeposits:iron_vein|鉄の鉱脈",
                        "coedeposits:copper_vein|銅の鉱脈",
                        "coedeposits:coal_vein|石炭の鉱脈"
                ), o -> o instanceof String);
        COMMAND_TEMPLATE = b.comment("鉱脈生成コマンド(先頭のスラッシュ無し)。{vein} と {multiplier} が置き換わる。",
                        "~ ~ ~ はトークンを使ったプレイヤーの位置。サーバー権限(OP4)で実行される。")
                .define("commandTemplate", "coe setvein ~ ~ ~ {vein} {multiplier}");
        MULTIPLIER = b.comment("鉱脈の産出量の倍率(COE側の範囲は 0〜1000、省略時のCOE既定値は 0.8)")
                .defineInRange("multiplier", 1.0, 0.0, 1000.0);
        b.pop();

        SPEC = b.build();
    }

    private NationConfig() {}
}
