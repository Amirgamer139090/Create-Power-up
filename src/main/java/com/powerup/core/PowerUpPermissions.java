package com.powerup.core;

import com.powerup.PowerUp;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

/**
 * Permission checks for PowerUp devices, built on NeoForge's permission API.
 * <p>
 * <strong>Server side only:</strong> every check takes a {@link ServerPlayer}. Clients must not decide
 * access on their own; they may only reflect a decision the server made.
 * <p>
 * Both nodes default to <em>allowed</em>, so nothing is restricted unless a server operator installs a
 * permission handler (for example from a permissions mod) that denies them. Nodes are registered through
 * {@link #registerNodes(PermissionGatherEvent.Nodes)}, which must be subscribed on the game event bus
 * ({@code NeoForge.EVENT_BUS}); {@code PowerUp} does this.
 */
public final class PowerUpPermissions {

    /** Permission to use a device (open it, read its data). Node name: {@code create_powerup.device.access}. */
    public static final PermissionNode<Boolean> ACCESS_DEVICE = new PermissionNode<>(
            PowerUp.MOD_ID, "device.access", PermissionTypes.BOOLEAN, (player, playerUuid, context) -> true);

    /** Permission to change a device's configuration. Node name: {@code create_powerup.device.configure}. */
    public static final PermissionNode<Boolean> CONFIGURE_DEVICE = new PermissionNode<>(
            PowerUp.MOD_ID, "device.configure", PermissionTypes.BOOLEAN, (player, playerUuid, context) -> true);

    private PowerUpPermissions() {
    }

    /** Registers this mod's permission nodes. Every node that is queried must be registered here. */
    public static void registerNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(ACCESS_DEVICE, CONFIGURE_DEVICE);
    }

    /** Returns whether the player may use devices. */
    public static boolean canAccess(ServerPlayer player) {
        return PermissionAPI.getPermission(player, ACCESS_DEVICE);
    }

    /** Returns whether the player may change device configuration. */
    public static boolean canConfigure(ServerPlayer player) {
        return PermissionAPI.getPermission(player, CONFIGURE_DEVICE);
    }
}
