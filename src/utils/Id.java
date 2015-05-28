package utils;

/**
 * Created by neikila on 10.05.15.
 */
public class Id <T>{
    final private long id;

    public Id(long id) { this.id = id; }

    public long getId() { return id; }

    @Override
    public String toString() {
        return "" + (int)id;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object o) {
        return (id == ((Id<T>) o).id);
    }
}
