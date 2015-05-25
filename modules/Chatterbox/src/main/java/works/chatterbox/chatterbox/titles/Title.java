package works.chatterbox.chatterbox.titles;

public abstract class Title {

    abstract public String getTitle();

    @Override
    public String toString() {
        return this.getTitle();
    }
}
