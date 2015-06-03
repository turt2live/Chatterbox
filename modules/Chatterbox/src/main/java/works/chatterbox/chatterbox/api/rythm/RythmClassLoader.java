/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.rythm;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

class RythmClassLoader extends ClassLoader {

    private final Chatterbox chatterbox;

    RythmClassLoader(@NotNull final ClassLoader parent, @NotNull final Chatterbox chatterbox) {
        super(parent);
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @Nullable
    private URL findResourceInEveryGoddamnedPluginAndHook(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        return this.getThatFuckingStream()
            .map(cl -> {
                try {
                    final Method m = cl.getClass().getMethod("findResource", String.class);
                    m.setAccessible(true);
                    return (URL) m.invoke(cl, name);
                } catch (final ReflectiveOperationException ex) {
                    return null;
                }
            })
            .filter(url -> url != null)
            .findFirst()
            .orElse(null);
    }

    @NotNull
    private Stream<ClassLoader> getThatFuckingStream() {
        return Stream.concat(
            Arrays.stream(this.chatterbox.getServer().getPluginManager().getPlugins()),
            this.chatterbox.getHookManager().getHooks().stream()
        )
            .map(p -> p.getClass().getClassLoader());
    }

    @NotNull
    private Class<?> loadClassInEveryGoddamnedPluginAndHook(@NotNull final String name) throws ClassNotFoundException {
        Preconditions.checkNotNull(name, "name was null");
        return this.getThatFuckingStream()
            .map(cl -> {
                try {
                    return cl.loadClass(name);
                } catch (final ClassNotFoundException ex) {
                    return null;
                }
            })
            .filter(clazz -> clazz != null)
            .findFirst()
            .orElseThrow(ClassNotFoundException::new);
    }

    @Override
    public Class<?> loadClass(@NotNull final String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        Preconditions.checkNotNull(name, "name was null");
        try {
            return super.loadClass(name, resolve);
        } catch (final ClassNotFoundException ex) {
            return this.loadClassInEveryGoddamnedPluginAndHook(name);
        }
    }

    @Override
    protected URL findResource(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        final URL url = super.findResource(name);
        return url != null ? url : this.findResourceInEveryGoddamnedPluginAndHook(name);
    }
}
