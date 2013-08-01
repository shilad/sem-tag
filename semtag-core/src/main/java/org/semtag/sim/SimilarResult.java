package org.semtag.sim;

/**
 * @author Ari Weiland
 */
public class SimilarResult implements Comparable<SimilarResult> {

    private final String id;
    private final double value;

    public SimilarResult(long id, double value) {
        this.id = String.valueOf(id);
        this.value = value;
    }

    public SimilarResult(String id, double value) {
        this.id = id;
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
}
