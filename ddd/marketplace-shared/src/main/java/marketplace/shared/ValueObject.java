package marketplace.shared;

public abstract class ValueObject<T> {
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ValueObject))
            return false;

        return equalsCore(o);
    }

    protected abstract boolean equalsCore(Object other);

    @Override
    public int hashCode() {
        return getHashCodeCore();
    }

    protected abstract int getHashCodeCore();
}
