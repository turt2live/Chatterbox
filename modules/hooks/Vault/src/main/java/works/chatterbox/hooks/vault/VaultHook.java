/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.hooks.vault;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.hooks.ChatterboxHook;
import works.chatterbox.hooks.vault.listeners.PlayerListener;
import works.chatterbox.hooks.vault.listeners.ServerListener;

public class VaultHook extends ChatterboxHook {

    private VaultMagic vaultMagic;

    @Override
    public void deinit() {
        this.vaultMagic = null;
        this.getLogger().info("Unloaded.");
    }

    @Override
    public void init() {
        final Plugin vault = this.getChatterbox().getServer().getPluginManager().getPlugin("Vault");
        if (vault == null || !vault.isEnabled()) return;
        this.vaultMagic = new VaultMagic(this.getChatterbox().getServer().getServicesManager());
        final PluginManager pm = this.getChatterbox().getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this.getChatterbox());
        pm.registerEvents(new ServerListener(this), this.getChatterbox());
        this.getLogger().info("Loaded.");
    }

    @Nullable
    public VaultMagic getVaultMagic() {
        return this.vaultMagic;
    }
}
