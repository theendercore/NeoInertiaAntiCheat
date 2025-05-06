package me.diffusehyperion.inertiaanticheat.util;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InertiaAntiCheatConstants {
    public static final ResourceLocation ANTICHEAT_DETAILS_ID = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "anticheat_details");

    public static final ResourceLocation MOD_TRANSFER_START_ID = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "mod_transfer_start");
    public static final ResourceLocation MOD_TRANSFER_CONTINUE_ID = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "mod_transfer_continue");

    public static final Logger MODLOGGER = LoggerFactory.getLogger("InertiaAntiCheat");
    public static final String MODID = "inertiaanticheat";

    public static final long CURRENT_SERVER_CONFIG_VERSION = 7;
    public static final long CURRENT_CLIENT_CONFIG_VERSION = 2;
}
