package org.semtag.relate;

/**
 * @author Ari Weiland
 */
public class RelatedResult<T> implements Comparable<RelatedResult> {

    private final String id;
    private final T obj;
    private final double value;

    public RelatedResult(long id, double value) {
        this(id, null, value);
    }

    public RelatedResult(String id, double value) {
        this(id, null, value);
    }

    public RelatedResult(long id, T obj, double value) {
        this.id = String.valueOf(id);
        this.obj = obj;
        this.value = value;
    }

    public RelatedResult(String id, T obj, double value) {
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
    public int compareTo(RelatedResult relatedResult) {
        if (this.value > relatedResult.value) {
            return 1;
        } else if (this.value == relatedResult.value) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelatedResult)) return false;

        RelatedResult that = (RelatedResult) o;

        return Double.compare(that.value, value) == 0 && id.equals(that.id);

    }
}
