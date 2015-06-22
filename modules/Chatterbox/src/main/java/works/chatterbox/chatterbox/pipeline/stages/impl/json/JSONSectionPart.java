package works.chatterbox.chatterbox.pipeline.stages.impl.json;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class JSONSectionPart {

    private final String section;
    private final JSONSectionType type;

    public JSONSectionPart(@NotNull final String section, @NotNull final JSONSectionType type) {
        Preconditions.checkNotNull(section, "section was null");
        Preconditions.checkNotNull(type, "type was null");
        this.section = section;
        this.type = type;
    }

    @NotNull
    public String getSection() {
        return this.section;
    }

    @NotNull
    public JSONSectionType getType() {
        return this.type;
    }

    public enum JSONSectionType {
        TOOLTIP,
        LINK,
        EXECUTE_COMMAND,
        SUGGEST_COMMAND,
        FILE,
        ACHIEVEMENT,
        ITEM,
        STATISTIC
    }

}
