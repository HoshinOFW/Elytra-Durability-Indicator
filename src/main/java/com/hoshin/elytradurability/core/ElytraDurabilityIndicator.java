package com.hoshin.elytradurability.core;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ElytraDurabilityIndicator.MOD_ID)
public final class ElytraDurabilityIndicator {
    public static final String MOD_ID = "elytradurabilityindicator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    public ElytraDurabilityIndicator() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EDIConfig.CLIENT_SPEC);

        LOGGER.info("Finished Initialization");
    }
}
