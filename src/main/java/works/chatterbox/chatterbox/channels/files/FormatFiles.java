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

    @Nullable
    public String getFileContents(final String name) {
        try {
            return this.cachedFiles.get(name);
        } catch (final ExecutionException ex) {
            return null;
        }
    }

    @Nullable
    public String getFileContents(final File file) {
        return this.getFileContents(file.getAbsolutePath());
    }
}
