package works.chatterbox.chatterbox;

import com.google.common.base.Preconditions;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.commands.ReflectiveCommandRegistrar;
import works.chatterbox.chatterbox.hooks.HookManager;
import works.chatterbox.chatterbox.listeners.ChatterboxListener;
import works.chatterbox.chatterbox.localization.Language;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelRecipientsStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.TagStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.color.ColorStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.json.JSONStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.radius.RadiusStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.recipient.RecipientStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.SpecialStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.world.WorldStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// TODO: https://gist.github.com/jkcclemens/9b6c7a54f71cf0628f7b

public class Chatterbox extends JavaPlugin {

    private final HookManager hm = new HookManager(this);
    private Language language;
    private ChatterboxAPI api;
    private ConfigurationNode configurationNode;

    private void addInternalPipelineStages() {
        Arrays.asList(
            // Routing
            new TagStage(this), // Processes any @-tags for channel destination
            new ChannelRecipientsStage(), // Set the recipients of the message
            new RadiusStage(), // Removes all recipients not in the channel radius, if enabled
            new WorldStage(), // Removes all recipients not in the correct world
            // Formatting
            new ChannelStage(), // Sets the base format
            new RythmStage(this), // Processes the Rythm syntax
            new SpecialStage(), // Handles methods from ChatterboxSpecialUtilities
            new ColorStage(), // Applies colors
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
        this.getServer().getScheduler().runTask(this, this.hm::loadHooks);
    }

    private void registerCommands() {
        final ReflectiveCommandRegistrar<Chatterbox> rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
    }

    private void registerListeners() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatterboxListener(this), this);
    }

    private void saveLanguageFiles() {
        final File langFile = new File(this.getDataFolder(), "lang");
        if (!langFile.exists()) {
            Preconditions.checkState(langFile.mkdir(), "Could not create lang directory");
        }
        final JarFile jar;
        try {
            jar = new JarFile(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
        } catch (final IOException | URISyntaxException ex) {
            ex.printStackTrace();
            return;
        }
        final Enumeration<JarEntry> e = jar.entries();
        while (e.hasMoreElements()) {
            final JarEntry entry = e.nextElement();
            if (!entry.getName().startsWith("lang/")) continue;
            final File location = new File(this.getDataFolder(), entry.getName());
            if (!location.exists()) {
                try (final InputStream is = jar.getInputStream(entry)) {
                    Files.copy(is, location.toPath());
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

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

    public Language getLanguage() {
        return this.language;
    }

    public void load(final boolean isReload) {
        this.saveDefaultConfig(); // save the default config before loading it
        if (!this.loadConfiguration()) {
            this.getLogger().severe("Could not load configuration. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.saveLanguageFiles();
        try {
            final String langLocation = this.getConfiguration().getNode("language").getNode("file").getString();
            this.language = new Language(new FileInputStream(new File(this.getDataFolder(), langLocation)));
        } catch (final IOException ex) {
            this.getLogger().severe("Could not load language file. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.api = new ChatterboxAPI(this);
        if (!isReload) {
            this.registerCommands();
        }
        this.addInternalPipelineStages();
        this.registerListeners();
        this.loadHooks();
    }

    @Override
    public void onDisable() {
        this.hm.unloadHooks();
    }

    @Override
    public void onEnable() {
        // I should probably find a better way to do this
        this.load(false);
    }
}
