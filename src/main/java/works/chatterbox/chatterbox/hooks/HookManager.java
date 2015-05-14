package works.chatterbox.chatterbox.hooks;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class HookManager {

    private final Chatterbox chatterbox;
    private final Set<ChatterboxHook> hooks = Sets.newHashSet();

    public HookManager(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
        try {
            Files.createDirectories(this.getHooksDirectory().toPath());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the descriptor for a hook. The descriptor is a {@link ConfigurationNode} representing the {@code hook.yml}.
     *
     * @param file Hook's jar
     * @return ConfigurationNode
     * @throws RuntimeException If an IOException occurs
     */
    @NotNull
    private ConfigurationNode getHookDescriptor(@NotNull final File file) {
        Preconditions.checkNotNull(file, "file was null");
        try (final ZipFile zf = new ZipFile(file)) {
            final ZipEntry hook = zf.getEntry("hook.yml");
            Preconditions.checkState(hook != null, "hook.yml didn't exist");
            return this.loadYAML(this.inputStreamToString(zf.getInputStream(hook)));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reads an entire InputStream into one String.
     *
     * @param is InputStream
     * @return Contents of the InputStream
     */
    @NotNull
    private String inputStreamToString(@NotNull final InputStream is) {
        Preconditions.checkNotNull(is, "is was null");
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Creates a ConfigurationNode from a YAML string.
     *
     * @param string String of YAML
     * @return ConfigurationNode
     * @throws IOException If any IOException occurs
     */
    @NotNull
    private ConfigurationNode loadYAML(@NotNull final String string) throws IOException {
        Preconditions.checkNotNull(string, "string was null");
        return YAMLConfigurationLoader.builder()
            .setSource(CharSource.wrap(string))
            .build()
            .load();
    }

    /**
     * Gets an immutable set of the loaded hooks.
     *
     * @return Immutable set
     */
    @NotNull
    public Set<ChatterboxHook> getHooks() {
        return ImmutableSet.copyOf(this.hooks);
    }

    /**
     * Gets the directory in which hooks should be stored.
     *
     * @return File
     */
    @NotNull
    public File getHooksDirectory() {
        return new File(this.chatterbox.getDataFolder(), "hooks");
    }

    /**
     * Loads a hook, given its jar file. This will ensure that the {@code hook.yml} is present and valid, ensure the
     * hook is a subclass of {@link ChatterboxHook}, prepare the hook, call the {@link ChatterboxHook#init()} method,
     * and then set the hook as enabled. If all of this happens without an error, the hook is added to the list of
     * loaded hooks.
     *
     * @param file Hook's jar
     * @throws RuntimeException If any exception occurs while loading the hook
     */
    public void loadHook(@NotNull final File file) {
        Preconditions.checkNotNull(file, "file was null");
        Preconditions.checkArgument(file.isFile(), "file was not a file");
        final ChatterboxHook hook;
        final ConfigurationNode descriptor;
        try {
            descriptor = this.getHookDescriptor(file);
            Preconditions.checkState(descriptor.getNode("name").getValue() != null, "Hook had no name in the hook.yml");
            Preconditions.checkState(descriptor.getNode("main").getValue() != null, "Hook had no main class in the hook.yml");
            final ClassLoader cl = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            final Class<?> mainClass = cl.loadClass(descriptor.getNode("main").getString());
            Preconditions.checkState(ChatterboxHook.class.isAssignableFrom(mainClass), "The main class did not extend ChatterboxHook");
            hook = (ChatterboxHook) mainClass.newInstance();
        } catch (final Throwable ex) {
            throw new RuntimeException("An exception occurred while loading a hook.", ex);
        }
        hook.internalInit(this.chatterbox, new File(this.getHooksDirectory(), descriptor.getNode("name").getString()), new HookDescriptor(descriptor));
        try {
            hook.init();
        } catch (final Throwable t) {
            throw new RuntimeException("An exception occurred while running a hook's init method.", t);
        }
        hook.setEnabled(true);
        this.hooks.add(hook);
    }

    /**
     * Loads all hooks in the hook directory ({@link #getHooksDirectory()}).
     */
    public void loadHooks() {
        final File hooksDirectory = this.getHooksDirectory();
        final String[] files = hooksDirectory.list();
        Preconditions.checkState(files != null, "Could not list the files in the hooks directory.");
        Arrays.stream(files)
            .filter(file -> file.toLowerCase().endsWith(".jar"))
            .map(file -> new File(hooksDirectory, file))
            .filter(File::isFile)
            .forEach(hook -> {
                try {
                    this.loadHook(hook);
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            });
    }
}
