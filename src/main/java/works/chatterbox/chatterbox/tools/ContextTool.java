package works.chatterbox.chatterbox.tools;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class ContextTool {

    public static void withContext(@NotNull final ClassLoader loader, @NotNull final NoParameterConsumer run) {
        Preconditions.checkNotNull(loader, "loader was null");
        Preconditions.checkNotNull(run, "run was null");
        final ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(loader);
            run.apply();
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

    // I swear that there's a built-in functional interface for this
    public interface NoParameterConsumer {

        void apply();
    }

}
