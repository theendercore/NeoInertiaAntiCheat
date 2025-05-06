package me.diffusehyperion.inertiaanticheat.mixins.client;

import me.diffusehyperion.inertiaanticheat.interfaces.ServerInfoInterface;
import me.diffusehyperion.inertiaanticheat.util.AnticheatDetails;
import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerData.class)
public abstract class ServerInfoMixin implements ServerInfoInterface {
    @Unique
    @Nullable
    private Boolean neoInertiaAntiCheat$inertiaInstalled;
    @Unique
    @Nullable
    private AnticheatDetails neoInertiaAntiCheat$anticheatDetails;

    @Override
    public AnticheatDetails inertiaAntiCheat$getAnticheatDetails() {
        return this.neoInertiaAntiCheat$anticheatDetails;
    }

    @Override
    @Nullable
    public Boolean inertiaAntiCheat$isInertiaInstalled() {
        return this.neoInertiaAntiCheat$inertiaInstalled;
    }

    @Override
    public void inertiaAntiCheat$setAnticheatDetails(AnticheatDetails anticheatDetails) {
        this.neoInertiaAntiCheat$anticheatDetails = anticheatDetails;
    }

    @Override
    public void inertiaAntiCheat$setInertiaInstalled(@Nullable Boolean value) {
        this.neoInertiaAntiCheat$inertiaInstalled = value;
    }
}
