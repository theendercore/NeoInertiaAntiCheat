package me.diffusehyperion.inertiaanticheat.util;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InertiaAntiCheatConstants {
    public static final ResourceLocation ANTICHEAT_DETAILS_ID = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "anticheat_details");

    public static final ResourceLocation CHECK_CONNECTION = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "check_connection");
    public static final ResourceLocation INITIATE_E2EE = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "initiate_e2ee");
    public static final ResourceLocation SET_ADAPTOR = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "set_adapter");
    public static final ResourceLocation SEND_MOD = ResourceLocation.fromNamespaceAndPath("inertiaanticheat", "send_mod");

    public static final Logger MODLOGGER = LoggerFactory.getLogger("InertiaAntiCheat");
    public static final String MODID = "inertiaanticheat";

    public static final long CURRENT_SERVER_CONFIG_VERSION = 8;
    public static final long CURRENT_CLIENT_CONFIG_VERSION = 2;
}
