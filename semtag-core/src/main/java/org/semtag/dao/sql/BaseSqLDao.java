package org.semtag.dao.sql;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.semtag.dao.Dao;
import org.semtag.dao.DaoException;
import org.wikapidia.core.dao.sql.JooqUtils;
import org.wikapidia.core.dao.sql.WpDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * BaseSqlDao provides an abstract base implementation for all SQL daos.
 * It implements the clear(), beginLoad(), and endLoad() methods and
 * provides a plethora of other utility methods for SQL daos.
 *
 * @author Ari Weiland
 */
public abstract class BaseSqLDao<T> implements Dao<T> {

    public static final Logger LOG = Logger.getLogger(BaseSqLDao.class.getName());

    public static final int DEFAULT_FETCH_SIZE = 1000;

    protected final SQLDialect dialect;
    protected final String sqlScriptPrefix;
    protected final Table<Record> table;
    protected final WpDataSource dataSource;

    private int fetchSize = DEFAULT_FETCH_SIZE;

    /**
     * Constructs a SqlDao with a datasource, prespecified SQL script prefix, and jOOQ table.
     * The latter two should be hardcoded into the subclass constructor.
     * @param dataSource
     * @param sqlScriptPrefix
     * @param table
     * @throws DaoException
     */
    public BaseSqLDao(WpDataSource dataSource, String sqlScriptPrefix, Table<Record> table) throws DaoException {
        this.dataSource = dataSource;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            this.dialect = JooqUtils.dialect(conn);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            WpDataSource.closeQuietly(conn);
        }

        this.sqlScriptPrefix = sqlScriptPrefix;
        this.table = table;
    }

    @Override
    public void clear() throws DaoException {
        dropIndexes();
        dropTables();
    }

    @Override
    public void beginLoad() throws DaoException {
        dropIndexes();
        createTables();
    }

    /**
     * Saves the object over the given connection.
     * Used by the SqlSaveHandler primarily.
     * @param context
     * @param obj
     * @throws DaoException
     */
    public abstract void save(DSLContext context, T obj) throws DaoException;

    @Override
    public void endLoad() throws DaoException {
        createIndexes();
    }

    /**
     * Returns the fetch size used by lazy fetches.
     * @return
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * Sets the fetch size used by lazy fetches.
     * @param fetchSize
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    protected void insert(Object... values) throws DaoException {
        DSLContext context = getJooq();
        try {
            insert(context, values);
            JooqUtils.commit(context);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        } finally {
            freeJooq(context);
        }
    }

    protected void insert(DSLContext context, Object... values) {
        context.insertInto(table)
                .values(values)
                .execute();
    }

    protected Record fetchOne(Condition... conditions) throws DaoException {
        return fetchOne(Arrays.asList(conditions));
    }

    protected Record fetchOne(Collection<Condition> conditions) throws DaoException {
        DSLContext context = getJooq();
        try {
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchOne();
        } finally {
            freeJooq(context);
        }
    }

    protected Result<Record> fetch(Condition... conditions) throws DaoException {
        return fetch(Arrays.asList(conditions));
    }

    protected Result<Record> fetch(Collection<Condition> conditions) throws DaoException {
        DSLContext context = getJooq();
        try {
            return context.selectFrom(table)
                    .where(conditions)
                    .fetch();
        } finally {
            freeJooq(context);
        }
    }

    protected Cursor<Record> fetchLazy(Condition... conditions) throws DaoException {
        return fetchLazy(Arrays.asList(conditions));
    }

    protected Cursor<Record> fetchLazy(Collection<Condition> conditions) throws DaoException {
        DSLContext context = getJooq();
        try {
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchLazy(fetchSize);
        } finally {
            freeJooq(context);
        }
    }

    protected int fetchCount(Condition... conditions) throws DaoException {
        return fetchCount(Arrays.asList(conditions));
    }

    protected int fetchCount(Collection<Condition> conditions) throws DaoException {
        DSLContext context = getJooq();
        try {
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchCount();
        } finally {
            freeJooq(context);
        }

    }

    /**
     * Runs the create-tables skip.
     * @throws DaoException
     */
    public void createTables() throws DaoException {
        executeSqlScriptWithSuffix("-create-tables.sql");
    }

    /**
     * Runs the drop-tables skip.
     * @throws DaoException
     */
    public void dropTables() throws DaoException {
        executeSqlScriptWithSuffix("-drop-tables.sql");
    }

    /**
     * Runs the create-indexes skip.
     * @throws DaoException
     */
    public void createIndexes() throws DaoException {
        executeSqlScriptWithSuffix("-create-indexes.sql");
    }

    /**
     * Runs the drop-indexes skip.
     * @throws DaoException
     */
    public void dropIndexes() throws DaoException {
        executeSqlScriptWithSuffix("-drop-indexes.sql");
    }

    /**
     * Executes the appropriate sql script with a particular suffix (.e.g. "-drop-tables.sql").
     * @param suffix
     * @throws DaoException
     */
    protected void executeSqlScriptWithSuffix(String suffix) throws DaoException {
        try {
            dataSource.executeSqlResource(sqlScriptPrefix + suffix);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    protected DSLContext getJooq() throws DaoException {
        try {
            return dataSource.getJooq();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    protected void freeJooq(DSLContext context) {
        dataSource.freeJooq(context);
    }
}
