package me.diffusehyperion.inertiaanticheat.mixins.client;

import me.diffusehyperion.inertiaanticheat.interfaces.ServerInfoInterface;
import me.diffusehyperion.inertiaanticheat.util.AnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.GroupAnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.IndividualAnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.ModlistCheckMethod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants.MODID;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class MultiplayerServerListWidgetServerEntryMixin {
    @Shadow @Final private JoinMultiplayerScreen screen;
    @Shadow @Final private ServerData serverData;

    @Unique
    private static final ResourceLocation ICON_ENABLED = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/enabled.png");
    @Unique
    private static final ResourceLocation ICON_WHITELIST = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/whitelist.png");
    @Unique
    private static final ResourceLocation ICON_BLACKLIST = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/blacklist.png");
    @Unique
    private static final ResourceLocation ICON_MODPACK = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/modpack.png");

    @Inject(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I")
    )
    private void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        ServerInfoInterface upgradedServerInfo = ((ServerInfoInterface) serverData);
        Boolean installed = upgradedServerInfo.inertiaAntiCheat$isInertiaInstalled();
        AnticheatDetails anticheatDetails = upgradedServerInfo.inertiaAntiCheat$getAnticheatDetails();
        if (Objects.nonNull(installed) && installed.equals(true) && anticheatDetails.showInstalled()) {
            int iconX = x + entryWidth - 15;
            int iconY = y + 10;
            context.blit(ICON_ENABLED, iconX, iconY, 0.0f, 0.0f, 10, 10, 10, 10);
            if (mouseX > iconX && mouseX < iconX + 10 && mouseY > iconY && mouseY < iconY + 10) {
                screen.setTooltipForNextRenderPass(Component.nullToEmpty("InertiaAntiCheat installed"));
            }
        }
        if (Objects.nonNull(anticheatDetails)) {
            if (anticheatDetails.getCheckMethod() == ModlistCheckMethod.INDIVIDUAL) {
                IndividualAnticheatDetails details = (IndividualAnticheatDetails) anticheatDetails;

                if ((details.getWhitelistedMods().size() == 1 && !Objects.equals(details.getWhitelistedMods().getFirst(), "")) || details.getWhitelistedMods().size() >= 2) {
                    int whitelistIconX = x + entryWidth - 25;
                    int whitelistIconY = y + 20;
                    context.blit(ICON_WHITELIST, whitelistIconX, whitelistIconY, 0.0f, 0.0f, 10, 10, 10, 10);
                    if (mouseX > whitelistIconX && mouseX < whitelistIconX + 10 && mouseY > whitelistIconY && mouseY < whitelistIconY + 10) {
                        screen.setTooltipForNextRenderPass(details.getWhitelistedMods().stream().map(Component::nullToEmpty).map(Component::getVisualOrderText).toList());
                    }
                }

                if ((details.getBlacklistedMods().size() == 1 && !Objects.equals(details.getBlacklistedMods().getFirst(), "")) || details.getBlacklistedMods().size() >= 2) {
                    int blacklistIconX = x + entryWidth - 15;
                    int blacklistIconY = y + 20;
                    context.blit(ICON_BLACKLIST, blacklistIconX, blacklistIconY, 0.0f, 0.0f, 10, 10, 10, 10);
                    if (mouseX > blacklistIconX && mouseX < blacklistIconX + 10 && mouseY > blacklistIconY && mouseY < blacklistIconY + 10) {
                        screen.setTooltipForNextRenderPass(details.getBlacklistedMods().stream().map(Component::nullToEmpty).map(Component::getVisualOrderText).toList());
                    }
                }
            } else {
                GroupAnticheatDetails details = (GroupAnticheatDetails) anticheatDetails;
                if ((details.getModpackDetails().size() == 1 && !Objects.equals(details.getModpackDetails().getFirst(), "")) || details.getModpackDetails().size() >= 2) {
                    int blacklistIconX = x + entryWidth - 15;
                    int blacklistIconY = y + 20;
                    context.blit(ICON_MODPACK, blacklistIconX, blacklistIconY, 0.0f, 0.0f, 10, 10, 10, 10);
                    if (mouseX > blacklistIconX && mouseX < blacklistIconX + 10 && mouseY > blacklistIconY && mouseY < blacklistIconY + 10) {
                        screen.setTooltipForNextRenderPass(details.getModpackDetails().stream().map(Component::nullToEmpty).map(Component::getVisualOrderText).toList());
                    }
                }
            }
        }
    }
}
