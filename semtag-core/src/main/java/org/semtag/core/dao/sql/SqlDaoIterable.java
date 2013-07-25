package org.semtag.core.dao.sql;

/**
 * @author Ari Weiland
 */

import org.jooq.Cursor;
import org.jooq.Record;
import org.semtag.core.dao.DaoException;

import java.util.Iterator;

/**
 * @author Ari Weiland
 *
 * This iterable is used by the SQL Daos to convert a jOOQ Cursor into an
 * Iterable of the appropriate class. This iterable can only be iterated
 * over once, and will throw exceptions if a user tries otherwise.
 */
public abstract class SqlDaoIterable<E> implements Iterable<E> {
    private Cursor<Record> result;
    private Iterator<Record> iterator;

    private boolean usedUp = false;
    private boolean finished = false;

    /**
     * Constructs a SqlDaoIterable that generates E objects from result.
     * @param result a collection of Records to be converted into outputs
     */
    public SqlDaoIterable(Cursor<Record> result){
        this.result = result;
        this.iterator = result.iterator();
    }

    /**
     * Abstract method to be implemented at use. Describes how the SqlDaoIterable
     * converts records from the input iterator to E items to be outputted.
     * @param record an element from the input iterator.
     * @return an object of class E
     * @throws DaoException
     */
    public abstract E transform(Record record) throws DaoException;

    /**
     * Closes this iterable, disabling all functionality.
     */
    public void close() {
        usedUp = true;
        finished = true;
        while (iterator.hasNext()) {
            iterator.next();
        }
        if (!result.isClosed()) {
            result.close();
        }
    }

    @Override
    public Iterator<E> iterator() {
        if (usedUp) {
            throw new IllegalStateException("SqlDaoIterable can only be iterated over once.");
        }
        usedUp = true;
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                try {
                    finished = !iterator.hasNext();
                    if (finished) {
                        close();
                    }
                    return !finished;
                } catch (Exception e) {
                    close();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public E next() {
                try {
                    Record item = iterator.next();
                    if (finished || item == null) {
                        finished = true;
                        close();
                        return null;
                    }
                    return transform(item);
                } catch (Exception e) {
                    close();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
