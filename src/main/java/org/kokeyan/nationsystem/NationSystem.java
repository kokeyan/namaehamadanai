package org.kokeyan.nationsystem;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.kokeyan.nationsystem.config.NationConfig;
import org.kokeyan.nationsystem.network.NetworkHandler;
import org.kokeyan.nationsystem.token.FoundingListener;
import org.slf4j.Logger;

@Mod(NationSystem.MOD_ID)
public class NationSystem {

    public static final String MOD_ID = "nationsystem";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NationSystem(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, NationConfig.SPEC);
        modBus.addListener(NetworkHandler::register);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Warring Nations の建国イベントを購読する(WNは必須依存なので参照して問題ない)
        event.enqueueWork(FoundingListener::register);
    }
}
