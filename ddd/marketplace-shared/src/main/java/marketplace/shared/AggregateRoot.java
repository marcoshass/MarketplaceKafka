package marketplace.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AggregateRoot<TId> {
    public TId id;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof AggregateRoot<?> that))
            return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName() + id);
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
