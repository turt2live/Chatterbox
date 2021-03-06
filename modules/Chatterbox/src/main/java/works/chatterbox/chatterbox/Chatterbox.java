/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox;

import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.api.DataFolderHolder;
import works.chatterbox.chatterbox.api.LanguageAPI;
import works.chatterbox.chatterbox.api.impl.DefaultChatterboxAPI;
import works.chatterbox.chatterbox.channels.tasks.MembershipTask;
import works.chatterbox.chatterbox.commands.ReflectiveCommandRegistrar;
import works.chatterbox.chatterbox.hooks.HookManager;
import works.chatterbox.chatterbox.listeners.ChatterboxListener;
import works.chatterbox.chatterbox.localization.Language;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelRecipientsStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.TagStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.color.ColorStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.color.ColorStripStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.json.JSONStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.literal.LiteralStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.radius.RadiusStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.recipient.RecipientStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.SpecialStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.sanitize.TrimStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.validation.ValidationStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.world.WorldStage;
import works.chatterbox.chatterbox.shaded.mkremins.fanciful.FancyMessage;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Chatterbox extends JavaPlugin implements DataFolderHolder, ChatterboxAPIHolder {

    private final HookManager hm = new HookManager(this);
    private ReflectiveCommandRegistrar<Chatterbox> rcr;
    private Language language;
    private ChatterboxAPI api;
    private ConfigurationNode configurationNode;
    private MembershipTask membershipTask;

    private void addInternalPipelineStages() {
        Stream.of(
            // Routing
            new TagStage(this), // Processes any @-tags for channel destination
            new ChannelRecipientsStage(), // Set the recipients of the message
            new RadiusStage(), // Removes all recipients not in the channel radius, if enabled
            new WorldStage(), // Removes all recipients not in the correct world
            // Formatting
            new ChannelStage(), // Sets the base format
            new ColorStripStage(this), // Remove colors if necessary
            new TrimStage(), // Trim leading and ending colors and spaces if necessary
            new RythmStage(this), // Processes the Rythm syntax
            new ColorStage(this), // Applies colors
            new SpecialStage(), // Handles methods from ChatterboxSpecialUtilities
            // Sanitization
            new LiteralStage(), // Handles literals
            // Validation
            new ValidationStage(),
            // Recipients
            new RecipientStage(this), // Handles recipient sections
            // JSON
            new JSONStage(this) // Processes any JSON, if necessary
        ).forEach(this.api.getMessageAPI().getMessagePipeline()::addStage);
    }

    private boolean loadConfiguration() {
        try {
            this.configurationNode = YAMLConfigurationLoader.builder()
                .setFile(new File(this.getDataFolder(), "config.yml"))
                .build()
                .load();
            return true;
        } catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void loadHooks() {
        this.getServer().getScheduler().runTask(this, () -> {
            // Load internal Vault hook
            this.loadVaultHook();
            // Then load hooks on disk
            this.hm.loadHooks();
        });
    }

    private void loadVaultHook() {
        final ConfigurationNode vaultNode = this.getConfiguration().getNode("dependencies").getNode("vault");
        // If we weren't instructed to install the hook, don't
        if (!vaultNode.getNode("install").getBoolean(true)) return;
        // Don't load if another Vault hook is already loaded
        if (this.hm.getHooks().stream().anyMatch(h -> h.getDescriptor().getName().equals("Vault"))) return;
        final File vaultJar = new File(this.hm.getHooksDirectory(), "Vault.jar");
        // If a Vault.jar already exists and we were told not to overwrite, don't
        if (!vaultNode.getNode("overwrite").getBoolean(true) && vaultJar.exists()) return;
        try {
            // Delete the old jar if it exists
            Files.deleteIfExists(vaultJar.toPath());
            // Copy the internal jar
            Files.copy(this.getResource("Vault.jar"), vaultJar.toPath());
        } catch (final IOException ex) {
            if (ex.getMessage().contains("being used by another process")) {
                // I hate Windows so, so much.
                this.getLogger().warning("If the Vault hook needs to be updated, please restart your server.");
            } else {
                ex.printStackTrace();
            }
        }
    }

    private void registerCommands() {
        this.rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
    }

    private void registerListeners() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatterboxListener(this), this);
    }

    @Override
    @NotNull
    public ChatterboxAPI getAPI() {
        return this.api;
    }

    @NotNull
    public ConfigurationNode getConfiguration() {
        if (this.configurationNode == null) {
            this.loadConfiguration();
        }
        return this.configurationNode;
    }

    public HookManager getHookManager() {
        return this.hm;
    }

    /**
     * Gets the default language for the plugin.
     *
     * @return Language
     * @deprecated Use {@link LanguageAPI#getLanguage()} instead
     */
    @Deprecated
    @NotNull
    public Language getLanguage() {
        return this.language;
    }

    @Nullable
    public ReflectiveCommandRegistrar<Chatterbox> getReflectiveCommandRegistrar() {
        return this.rcr;
    }

    public void load(final boolean isReload) {
        this.saveDefaultConfig(); // save the default config before loading it
        if (!this.loadConfiguration()) {
            this.getLogger().severe("Could not load configuration. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.api = new DefaultChatterboxAPI(this);
        try {
            this.language = this.getAPI().getLanguageAPI().getLanguage();
        } catch (final NullPointerException ex) {
            this.getLogger().severe("Could not load language file. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Rejoin all previous channels
        Sets.newHashSet(this.getServer().getOnlinePlayers()).stream()
            .map(this.api.getPlayerAPI()::getCPlayer)
            .forEach(CPlayer::joinPreviousChannels);
        if (!isReload) {
            this.registerCommands();
        }
        this.addInternalPipelineStages();
        this.registerListeners();
        this.loadHooks();
        this.getServer().getScheduler().runTaskTimer(this, this.membershipTask = new MembershipTask(this), 36000L, 36000L);
    }

    @Override
    public boolean onCommand(final CommandSender cs, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            new FancyMessage(this.getAPI().getLanguageAPI().getLanguage(cs).getAString("SEE_DOCUMENTATION"))
                .color(ChatColor.RED)
                .tooltip(this.getAPI().getLanguageAPI().getLanguage(cs).getAString("CLICK_FOR_DOCUMENTATION"))
                .link("https://github.com/Chatterbox/Chatterbox/wiki/Commands")
                .send(cs);
            return true;
        }
        final ReflectiveCommandRegistrar<Chatterbox> rcr = this.getReflectiveCommandRegistrar();
        if (rcr == null) {
            return true;
        }
        rcr.getCommandHandler().runCommand(cs, label, args);
        return true;
    }

    @Override
    public void onDisable() {
        // Unload all loaded hooks
        this.hm.unloadHooks();
        // Shut down the RythmEngine
        this.getAPI().getRythmAPI().getRythmEngine().shutdown();
        // Cancel all of our tasks
        this.getServer().getScheduler().cancelTasks(this);
        // Save all memberships
        if (this.membershipTask != null) {
            this.membershipTask.run();
        }
    }

    @Override
    public void onEnable() {
        // I should probably find a better way to do this
        this.load(false);
    }
}
