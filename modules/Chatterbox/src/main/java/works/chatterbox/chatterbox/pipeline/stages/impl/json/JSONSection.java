package works.chatterbox.chatterbox.pipeline.stages.impl.json;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class JSONSection implements Iterable<JSONSectionPart> {

    private final List<JSONSectionPart> parts = Lists.newArrayList();

    public JSONSection(@NotNull final Collection<JSONSectionPart> initialParts) {
        Preconditions.checkNotNull(initialParts, "initialParts was null");
        this.getParts().addAll(initialParts);
    }

    public JSONSection() {}

    public void addPart(@NotNull final JSONSectionPart part) {
        Preconditions.checkNotNull(part, "part was null");
        this.parts.add(part);
    }

    @NotNull
    public List<JSONSectionPart> getParts() {
        return this.parts;
    }

    @Override
    public Iterator<JSONSectionPart> iterator() {
        return this.parts.iterator();
    }

    @Override
    public void forEach(final Consumer<? super JSONSectionPart> action) {
        this.parts.forEach(action);
    }

    @Override
    public Spliterator<JSONSectionPart> spliterator() {
        return this.parts.spliterator();
    }

    public void removePart(@NotNull final JSONSectionPart part) {
        Preconditions.checkNotNull(part, "part was null");
        this.parts.remove(part);
    }
}
