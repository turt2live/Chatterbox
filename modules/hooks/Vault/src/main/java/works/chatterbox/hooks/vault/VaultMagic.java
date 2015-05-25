package works.chatterbox.hooks.vault;

import com.google.common.base.Preconditions;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;

public class VaultMagic {

    private static Permission permission;
    private static Chat chat;
    private final ServicesManager servicesManager;

    public VaultMagic(@NotNull final ServicesManager manager) {
        Preconditions.checkNotNull(manager, "manager was null");
        this.servicesManager = manager;
        this.setUpPermission();
        this.setUpChat();
    }

    private void setUpChat() {
        final RegisteredServiceProvider<Chat> chatProvider = servicesManager.getRegistration(Chat.class);
        if (chatProvider == null) return;
        VaultMagic.chat = chatProvider.getProvider();
    }

    private void setUpPermission() {
        final RegisteredServiceProvider<Permission> permissionProvider = servicesManager.getRegistration(Permission.class);
        if (permissionProvider == null) return;
        VaultMagic.permission = permissionProvider.getProvider();
    }

    public Chat getChat() {
        return VaultMagic.chat;
    }

    public Permission getPermission() {
        return VaultMagic.permission;
    }

    public boolean hasChat() {
        return VaultMagic.chat != null;
    }

    public boolean hasPermission() {
        return VaultMagic.permission != null;
    }
}
