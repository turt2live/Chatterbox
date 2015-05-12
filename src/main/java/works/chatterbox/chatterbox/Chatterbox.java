package works.chatterbox.chatterbox;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.commands.ReflectiveCommandRegistrar;
import works.chatterbox.chatterbox.hooks.HookManager;
import works.chatterbox.chatterbox.listeners.PipelineListener;
import works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.color.ColorStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Chatterbox extends JavaPlugin {

    private ChatterboxAPI api;
    private ConfigurationNode configurationNode;

    private void addInternalPipelineStages() {
        Arrays.asList(
            new ChannelStage(), // Sets the base format
            new RythmStage(this), // Processes the Rythm syntax
            new ColorStage() // Applies colors
        ).forEach(this.api.getMessageAPI().getMessagePipeline()::addStage);
    }

    private boolean loadConfiguration() {
        try {
            this.configurationNode = YAMLConfigurationLoader.builder().setFile(new File(this.getDataFolder(), "config.yml")).build().load();
            return true;
        } catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void loadHooks() {
        final Runnable r = () -> {
            final HookManager hm = new HookManager(this);
            hm.loadHooks();
        };
        this.getServer().getScheduler().runTask(this, r);
    }

    private void registerCommands() {
        final ReflectiveCommandRegistrar<Chatterbox> rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
    }

    private void registerListeners() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PipelineListener(this), this);
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

    @Override
    public void onEnable() {
        this.api = new ChatterboxAPI(this); // api must be initialized before anything else
        this.saveDefaultConfig(); // save the default config before loading it
        if (!this.loadConfiguration()) {
            this.getLogger().severe("Could not load configuration. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.registerCommands();
        this.addInternalPipelineStages();
        this.registerListeners();
        this.loadHooks();
    }
}
