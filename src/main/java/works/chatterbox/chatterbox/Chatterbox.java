package works.chatterbox.chatterbox;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.commands.ReflectiveCommandRegistrar;
import works.chatterbox.chatterbox.listeners.PipelineListener;
import works.chatterbox.chatterbox.pipeline.stages.impl.color.ColorStage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage;

import java.util.Arrays;

// TODO: Make task for loading dependencies, which brings me to number two
// TODO: Dependencies

public class Chatterbox extends JavaPlugin {

    private ChatterboxAPI api;

    private void addInternalPipelineStages() {
        Arrays.asList(
            new RythmStage(),
            new ColorStage()
        ).forEach(this.api.getMessageAPI().getMessagePipeline()::addStage);
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

    @Override
    public void onEnable() {
        this.api = new ChatterboxAPI(this);
        this.registerCommands();
        this.addInternalPipelineStages();
        this.registerListeners();
    }
}
