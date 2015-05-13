package works.chatterbox.chatterbox.channels.files;

import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FormatFiles {

    private final LoadingCache<String, String> cachedFiles = CacheBuilder.newBuilder()
        .expireAfterAccess(1L, TimeUnit.HOURS)
        .build(new CacheLoader<String, String>() {
            @Override
            public String load(@NotNull final String key) throws Exception {
                // Files should replace newlines with spaces for consistency with the YAML version
                return Joiner.on(' ').join(
                    Files.readAllLines(
                        new File(key).toPath()
                    )
                );
            }
        });

    /**
     * Gets the contents of a file. This will return null if any error occurs while loading.
     * <p>These values are cached, and they expire one hour after the last access.
     *
     * @param path Path of the file to get
     * @return Contents of the file or null
     */
    @Nullable
    public String getFileContents(@NotNull final String path) {
        Preconditions.checkNotNull(path, "path was null");
        try {
            return this.cachedFiles.get(path);
        } catch (final ExecutionException ex) {
            return null;
        }
    }

    /**
     * Gets the contents of a file. See {@link #getFileContents(String)}.
     *
     * @param file File to get contents of
     * @return Contents of the file or null
     * @see #getFileContents(String)
     */
    @Nullable
    public String getFileContents(final File file) {
        return this.getFileContents(file.getAbsolutePath());
    }
}
