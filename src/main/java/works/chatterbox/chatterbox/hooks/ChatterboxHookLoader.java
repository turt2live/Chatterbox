package works.chatterbox.chatterbox.hooks;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class ChatterboxHookLoader extends URLClassLoader {

    private final static String SHADED = "works.chatterbox.chatterbox.shaded.";

    public ChatterboxHookLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public ChatterboxHookLoader(final URL[] urls) {
        super(urls);
    }

    public ChatterboxHookLoader(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    private Class<?> findShadedClass(final String name) throws ClassNotFoundException {
        return this.findClass(ChatterboxHookLoader.SHADED + name);
    }

    private boolean isShadedClass(final String name) {
        return name.startsWith(ChatterboxHookLoader.SHADED);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (final ClassNotFoundException ex) {
            if (!this.isShadedClass(name)) {
                return this.findShadedClass(name);
            }
            throw ex;
        }
    }
}
