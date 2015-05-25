package works.chatterbox.chatterbox.hooks;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.ChatterboxAPI;

import java.io.File;

public abstract class ChatterboxHook {

    private Chatterbox chatterbox;
    private File dataFolder;
    private HookDescriptor descriptor;
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
    public HookDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * This is called when a hook is first loaded.
     */
    public void init() {
    }

    /**
     * Sets necessary values for the hook. Called before {@link #init()}. This is done to prevent extending classes from
     * having to make a constructor that matches this class.
     *
     * @param chatterbox Chatterbox instance
     * @param dataFolder Data folder for this hook
     * @param descriptor This hook's descriptor node
     */
    final void internalInit(@NotNull final Chatterbox chatterbox, @NotNull final File dataFolder, @NotNull final HookDescriptor descriptor) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        Preconditions.checkNotNull(dataFolder, "dataFolder was null");
        Preconditions.checkNotNull(descriptor, "descriptor was null");
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

    /**
     * Sets if this plugin is enabled. Used for {@link #isEnabled()} so hooks can see their own status.
     *
     * @param enabled Enabled status
     */
    final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
