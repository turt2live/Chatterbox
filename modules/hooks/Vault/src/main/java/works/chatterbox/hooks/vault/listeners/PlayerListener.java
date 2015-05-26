/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.hooks.vault.listeners;

import com.google.common.base.Preconditions;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.shaded.ninja.leaping.configurate.ConfigurationNode;
import works.chatterbox.chatterbox.titles.Prefix;
import works.chatterbox.chatterbox.titles.Suffix;
import works.chatterbox.chatterbox.titles.Titles;
import works.chatterbox.hooks.vault.VaultHook;
import works.chatterbox.hooks.vault.VaultMagic;

public class PlayerListener implements Listener {

    private final VaultHook vaultHook;

    public PlayerListener(@NotNull final VaultHook hook) {
        Preconditions.checkNotNull(hook, "hook was null");
        this.vaultHook = hook;
    }

    private void addChatterboxTitles(@NotNull final Titles titles, @NotNull final String group) {
        Preconditions.checkNotNull(titles, "titles was null");
        Preconditions.checkNotNull(group, "group was null");
        final ConfigurationNode groupNode = this.vaultHook.getChatterbox().getConfiguration().getNode("titles").getNode("groups").getNode(group);
        this.addTitles(70, titles, groupNode);
    }

    private void addOtherTitles(@NotNull final Titles titles, @NotNull final String group, @NotNull final Chat chat, @NotNull final Player player) {
        final String prefix = chat.getGroupPrefix((String) null, group);
        final String suffix = chat.getGroupSuffix((String) null, group);
        if (prefix != null && !prefix.isEmpty()) {
            titles.addPrefix(50, new Prefix(prefix));
        }
        if (suffix != null && !suffix.isEmpty()) {
            titles.addSuffix(50, new Suffix(suffix));
        }
        final String playerPrefix = chat.getPlayerPrefix(player);
        final String playerSuffix = chat.getPlayerSuffix(player);
        if (playerPrefix != null && !playerPrefix.isEmpty()) {
            titles.addPrefix(60, new Prefix(playerPrefix));
        }
        if (playerSuffix != null && !playerSuffix.isEmpty()) {
            titles.addSuffix(60, new Suffix(playerSuffix));
        }
    }

    private void addTitles(final int priority, @NotNull final Titles titles, @NotNull final ConfigurationNode node) {
        if (node.isVirtual()) return;
        final ConfigurationNode prefix = node.getNode("prefix");
        if (!prefix.isVirtual()) {
            titles.addPrefix(priority, new Prefix(prefix.getString()));
        }
        final ConfigurationNode suffix = node.getNode("suffix");
        if (!suffix.isVirtual()) {
            titles.addSuffix(priority, new Suffix(suffix.getString()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        final VaultMagic vm = this.vaultHook.getVaultMagic();
        if (vm == null) return;
        if (!vm.hasPermission()) return;
        final Permission permission = vm.getPermission();
        final Player player = event.getPlayer();
        final Titles titles = this.vaultHook.getChatterbox().getAPI().getTitleAPI().getTitles(player.getUniqueId());
        final String group = permission.getPrimaryGroup(player);
        if (group != null) {
            this.addChatterboxTitles(titles, group);
            if (vm.hasChat()) {
                this.addOtherTitles(titles, group, vm.getChat(), player);
            }
        }

    }

}
