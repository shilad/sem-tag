package org.semtag.sim;

/**
 * Wraps the result of a most similar call. Contains an ID
 * of type int, long, or string, a double similarity score,
 * and an optional object that the ID refers to.
 *
 * @author Ari Weiland
 */
public class SimilarResult<T> implements Comparable<SimilarResult> {

    private final String id;
    private final T obj;
    private final double value;

    public SimilarResult(long id, double value) {
        this(id, null, value);
    }

    public SimilarResult(String id, double value) {
        this(id, null, value);
    }

    public SimilarResult(long id, T obj, double value) {
        this.id = String.valueOf(id);
        this.obj = obj;
        this.value = value;
    }

    public SimilarResult(String id, T obj, double value) {
        this.id = id;
        this.obj = obj;
        this.value = value;
    }

    public int getIntId() {
        return Integer.valueOf(id);
    }

    public long getLongId() {
        return Long.valueOf(id);
    }

    public String getStringId() {
        return id;
    }

    public T getObj() {
        return obj;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(SimilarResult similarResult) {
        if (this.value > similarResult.value) {
            return 1;
        } else if (this.value == similarResult.value) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimilarResult)) return false;

        SimilarResult that = (SimilarResult) o;

        return Double.compare(that.value, value) == 0 && id.equals(that.id);

    }
}
