/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.localization;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.api.DataFolderHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LanguageBuilder {

    private DataFolderHolder dataFolderHolder;
    private String languageFileName = "en_US.properties";
    private String languageFolder = "lang";
    private File languageFile = null;
    private File languageFolderFile = null;
    private boolean saveLanguageFiles = false;
    private boolean overwrite = false;

    public LanguageBuilder(@NotNull final DataFolderHolder holder) {
        Preconditions.checkNotNull(holder, "holder was null");
        this.dataFolderHolder = holder;
    }

    public LanguageBuilder() {}

    public static LanguageBuilder create() {
        return new LanguageBuilder();
    }

    @Nullable
    private JarFile getJarFile() {
        try {
            return new JarFile(new File(this.dataFolderHolder.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
        } catch (final IOException | URISyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private File getLanguageFile() {
        if (this.languageFile != null) {
            return this.languageFile;
        }
        if (this.dataFolderHolder != null) {
            return this.dataFolderHolder.getDataFolder().toPath().resolve(this.languageFolder).resolve(this.languageFileName).toFile();
        }
        throw new IllegalStateException("Neither a language file nor a data folder holder was specified");
    }

    private File getLanguageFolder() {
        if (this.languageFolderFile != null) {
            return this.languageFolderFile;
        }
        if (this.dataFolderHolder != null) {
            return new File(this.dataFolderHolder.getDataFolder(), this.languageFolder);
        }
        throw new IllegalStateException("Neither a language folder file nor a data folder holder was specified");
    }

    private void saveLanguageFiles() throws IOException {
        Preconditions.checkState(this.dataFolderHolder != null, "A data folder holder must be specified");
        final File destination = this.getLanguageFolder();
        if (!destination.exists()) {
            Files.createDirectories(destination.toPath());
        }
        if (!destination.isDirectory()) {
            throw new IOException("languageFolder was not a directory");
        }
        try (final JarFile jarFile = this.getJarFile()) {
            Preconditions.checkState(jarFile != null, "Could not get jar");
            final Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                final JarEntry entry = e.nextElement();
                if (!entry.getName().startsWith(this.languageFolder + "/")) continue;
                final File location = new File(this.dataFolderHolder.getDataFolder(), entry.getName());
                if (!this.overwrite && location.exists()) continue;
                // These InputStreams are closed when the JarFile is closed
                final InputStream is = jarFile.getInputStream(entry);
                Files.copy(is, location.toPath());
            }
        }
    }

    /**
     * Builds a Language with the given options.
     *
     * @return Language
     * @throws RuntimeException If any IOException happens
     */
    @NotNull
    public Language build() throws IOException {
        if (this.saveLanguageFiles) {
            this.saveLanguageFiles();
        }
        final File languageFile = this.languageFile != null ? this.languageFile : this.dataFolderHolder.getDataFolder().toPath().resolve(this.languageFolder).resolve(this.languageFileName).toFile();
        try (
            final FileInputStream fis = new FileInputStream(languageFile);
            final InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)
        ) {
            return new Language(isr);
        }
    }

    @NotNull
    public LanguageBuilder dataFolderHolder(@NotNull final DataFolderHolder dataFolderHolder) {
        Preconditions.checkNotNull(dataFolderHolder, "dataFolderHolder was null");
        this.dataFolderHolder = dataFolderHolder;
        return this;
    }

    @NotNull
    public LanguageBuilder languageFile(@NotNull final File languageFile) {
        Preconditions.checkNotNull(languageFile, "languageFile was null");
        this.languageFile = languageFile;
        return this;
    }

    @NotNull
    public LanguageBuilder languageFileName(@NotNull final String languageFileName) {
        Preconditions.checkNotNull(languageFileName, "languageFileName was null");
        this.languageFileName = languageFileName;
        return this;
    }

    @NotNull
    public LanguageBuilder languageFolder(@NotNull final String languageFolder) {
        Preconditions.checkNotNull(languageFolder, "languageFolder was null");
        this.languageFolder = languageFolder;
        return this;
    }

    @NotNull
    public LanguageBuilder languageFolderFile(@NotNull final File languageFolderFile) {
        Preconditions.checkNotNull(languageFolderFile, "languageFolderFile was null");
        this.languageFolderFile = languageFolderFile;
        return this;
    }

    @NotNull
    public LanguageBuilder overwrite(final boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    @NotNull
    public LanguageBuilder saveLanguageFiles(final boolean saveLanguageFiles) {
        this.saveLanguageFiles = saveLanguageFiles;
        return this;
    }
}
