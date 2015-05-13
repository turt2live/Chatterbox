package works.chatterbox.chatterbox.tools;

public class Wrapper<T> {

    public T value;

    public Wrapper() {
        this.value = null;
    }

    public Wrapper(final T value) {
        this.value = value;
    }

}
