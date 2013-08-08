package org.semtag.relate;

import com.google.common.collect.Iterators;

import java.util.*;

/**
 * This class is represents a list of RelatedResult items. It has a specified
 * maximum size, optional minimal threshold score, and is sorted. It contains
 * a locking mechanism that activates either when manually called or by either
 * the get or iterator methods. Upon locking, the list is sorted, values below
 * the threshold are removed, and it is truncated down to max size. Add methods
 * also can no longer be called.
 *
 * @author Ari Weiland
 */
public class RelatedResultList implements Iterable<RelatedResult> {

    private final int maxSize;
    private final double threshold;
    private List<RelatedResult> results;
    private boolean locked = false;

    /**
     * Constructs an empty RelatedResultList with specified max size.
     * @param maxSize
     */
    public RelatedResultList(int maxSize) {
        this(maxSize, 0);
    }

    /**
     * Constructs an empty RelatedResultList with specified max size.
     * @param maxSize
     */
    public RelatedResultList(int maxSize, double threshold) {
        this(maxSize, threshold, new ArrayList<RelatedResult>());
    }

    /**
     * Constructs a RelatedResultList containing results with a specified max size.
     * @param maxSize
     * @param results
     */
    public RelatedResultList(int maxSize, RelatedResult... results) {
        this(maxSize, 0, results);
    }

    /**
     * Constructs a RelatedResultList containing results with a specified max size.
     * @param maxSize
     * @param results
     */
    public RelatedResultList(int maxSize, Collection<RelatedResult> results) {
        this(maxSize, 0, results);
    }

    /**
     * Constructs a RelatedResultList containing results with a specified max size.
     * @param maxSize
     * @param results
     */
    public RelatedResultList(int maxSize, double threshold, RelatedResult... results) {
        this.maxSize = maxSize;
        this.threshold = threshold;
        this.results = new ArrayList<RelatedResult>(maxSize);
        add(results);
    }

    /**
     * Constructs a RelatedResultList containing results with a specified max size.
     * @param maxSize
     * @param results
     */
    public RelatedResultList(int maxSize, double threshold, Collection<RelatedResult> results) {
        this.maxSize = maxSize;
        this.threshold = threshold;
        this.results = new ArrayList<RelatedResult>(maxSize);
        add(results);
    }

    /**
     * Adds results to the RelatedResultList.
     * If called after List has been locked, an IllegalStateException will be thrown.
     * @param results
     */
    public void add(RelatedResult... results) {
        if (locked) {
            throw new IllegalStateException("RelatedResultList has been locked");
        }
        Collections.addAll(this.results, results);
    }

    /**
     * Adds results to the RelatedResultList.
     * If called after list has been locked, an IllegalStateException will be thrown.
     * @param results
     */
    public void add(Collection<RelatedResult> results) {
        if (locked) {
            throw new IllegalStateException("RelatedResultList has been locked");
        }
        this.results.addAll(results);
    }

    /**
     * Returns the max size of this list.
     * @return
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns the minimum threshold value of this list.
     * @return
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Returns the current size of this list.
     * This method does NOT lock the list.
     * This method will never exceed max size after the list has been locked,
     * though it need not equal max size. Before lock, it may be any value >= 0.
     * @return
     */
    public int size() {
        return results.size();
    }

    /**
     * Returns an unmodifiable view of the underlying list.
     * This method does NOT lock the list.
     * @return
     */
    public List<RelatedResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    /**
     * Returns the relatedness score of the specified ID, or 0 if the ID is not found.
     * This method does NOT lock the list.
     * @param id
     * @return
     */
    public double getValue(long id) {
        for (RelatedResult result : results) {
            if (result.getLongId() == id) {
                return result.getValue();
            }
        }
        return 0;
    }

    /**
     * Returns the relatedness score of the specified ID, or 0 if the ID is not found.
     * This method does NOT lock the list.
     * @param id
     * @return
     */
    public double getValue(String id) {
        for (RelatedResult result : results) {
            if (result.getStringId().equals(id)) {
                return result.getValue();
            }
        }
        return 0;
    }

    /**
     * Locks the list if it was not already locked.
     * If not already locked, it sorts and then truncates the list.
     */
    public void lock() {
        if (!locked) {
            locked = true;
            sortAndTruncate();
        }
    }

    public boolean isLocked() {
        return locked;
    }

    /**
     * Returns the RelatedResult at the specified index.
     * This method DOES lock the list.
     * Throws an IndexOutOfBoundsException.
     * @param index
     * @return
     */
    public RelatedResult get(int index) {
        lock();
        return results.get(index);
    }

    /**
     * Returns an unmodifiable iterator over the underlying list.
     * This method DOES lock the list.
     * @return
     */
    @Override
    public Iterator<RelatedResult> iterator() {
        lock();
        return Iterators.limit(getResults().iterator(), maxSize);
    }

    private void sortAndTruncate() {
        Collections.sort(this.results, Collections.reverseOrder());
        int truncateSize = maxSize;
        for (int i=0; i<results.size() && i<truncateSize; i++) {
            if (results.get(i).getValue() < threshold) {
                truncateSize = i;
            }
        }
        while (results.size() > truncateSize) {
            results.remove(truncateSize);
        }
    }
}
