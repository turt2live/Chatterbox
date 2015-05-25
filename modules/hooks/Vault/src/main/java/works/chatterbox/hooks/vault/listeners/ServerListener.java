package works.chatterbox.hooks.vault.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import works.chatterbox.hooks.vault.VaultHook;

public class ServerListener implements Listener {

    private final VaultHook vaultHook;

    public ServerListener(final VaultHook hook) {
        this.vaultHook = hook;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPluginDisable(final PluginDisableEvent event) {
        final Plugin plugin = event.getPlugin();
        if (!plugin.getName().equals("Vault")) return;
        this.vaultHook.deinit();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPluginEnable(final PluginEnableEvent event) {
        final Plugin plugin = event.getPlugin();
        if (!plugin.getName().equals("Vault")) return;
        this.vaultHook.deinit();
        this.vaultHook.init();
    }

}
