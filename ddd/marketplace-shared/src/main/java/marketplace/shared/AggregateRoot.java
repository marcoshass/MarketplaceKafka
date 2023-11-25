package marketplace.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AggregateRoot<TId> {
    private TId id;
    public TId getId() {
        return id;
    }
    protected void setId(TId id) {
        this.id = id;
    }

    protected void when(Object event) {
        throw new UnsupportedOperationException();
    }

    private List<Object> changes;

    protected AggregateRoot() {
        changes = new ArrayList<>();
    }

    protected void apply(Consumer<Object> delegate) {
        delegate.accept(null);
        EnsureValidState();
    }

    public List<Object> getChanges() {
        return this.changes;
    }

    public void clearChanges() {
        this.changes.clear();
    }

    protected abstract void EnsureValidState();
}
