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

    private String inputStreamToString(@NotNull final InputStream is) {
        Preconditions.checkNotNull(is, "is was null");
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

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
    public Set<ChatterboxHook> getHooks() {
        return ImmutableSet.copyOf(this.hooks);
    }

    public File getHooksDirectory() {
        return new File(this.chatterbox.getDataFolder(), "hooks");
    }

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
            throw new RuntimeException(ex);
        }
        hook.internalInit(this.chatterbox, new File(this.getHooksDirectory(), descriptor.getNode("name").getString()), descriptor);
        try {
            hook.init();
        } catch (final Throwable t) {
            throw new RuntimeException("An exception occurred while running the hook's init method.", t);
        }
        hook.setEnabled(true);
        this.hooks.add(hook);
    }

    public void loadHooks() {
        final File[] files = this.getHooksDirectory().listFiles();
        Preconditions.checkState(files != null, "Could not list the files in the hooks directory.");
        Arrays.stream(files).forEach(this::loadHook);
    }
}
