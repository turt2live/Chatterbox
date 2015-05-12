package works.chatterbox.chatterbox.hooks;

import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.ChatterboxAPI;

import java.io.File;

public abstract class ChatterboxHook {

    private Chatterbox chatterbox;
    private File dataFolder;
    private ConfigurationNode descriptor;
    private boolean enabled = false;

    /**
     * This is called if the hook is ever disabled.
     */
    public void deinit() {
    }

    /**
     * Gets the instance of Chatterbox that is currently loaded.
     *
     * @return Chatterbox
     */
    @NotNull
    public Chatterbox getChatterbox() {
        return this.chatterbox;
    }

    /**
     * Convenience method that is equivalent to {@code getChatterbox().getAPI()}.
     *
     * @return ChatterboxAPI
     */
    @NotNull
    public ChatterboxAPI getChatterboxAPI() {
        return this.getChatterbox().getAPI();
    }

    /**
     * Gets the directory reserved for this hook. The directory may or may not exist.
     *
     * @return File representing directory
     */
    @NotNull
    public File getDataFolder() {
        return this.dataFolder;
    }

    /**
     * Gets the descriptor for the hook (the hook.yml file).
     *
     * @return Descriptor
     */
    @NotNull
    public ConfigurationNode getDescriptor() {
        return this.descriptor;
    }

    /**
     * This is called when a hook is first loaded.
     */
    public void init() {
    }

    final void internalInit(final Chatterbox chatterbox, final File dataFolder, final ConfigurationNode descriptor) {
        this.chatterbox = chatterbox;
        this.dataFolder = dataFolder;
        this.descriptor = descriptor;
    }

    /**
     * If this hook is enabled, this will return true;
     *
     * @return Enabled status
     */
    public final boolean isEnabled() {
        return this.enabled;
    }

    final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
