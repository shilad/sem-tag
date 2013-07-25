package org.semtag.core.dao.sql;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.semtag.core.dao.Dao;
import org.semtag.core.dao.DaoException;
import org.wikapidia.core.dao.sql.JooqUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 */
public abstract class BaseSqLDao<T> implements Dao<T> {

    public static final Logger LOG = Logger.getLogger(BaseSqLDao.class.getName());

    public static final int DEFAULT_FETCH_SIZE = 1000;

    protected final SQLDialect dialect;
    protected final String tableName;
    protected final Table<Record> table;
    protected final DataSource ds;

    private int fetchSize = DEFAULT_FETCH_SIZE;

    public BaseSqLDao(DataSource dataSource, String tableName, Table<Record> table) throws DaoException {
        ds = dataSource;
        Connection conn = null;
        try {
            conn = ds.getConnection();
            this.dialect = JooqUtils.dialect(conn);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
        this.tableName = tableName;
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

    @Override
    public void endLoad() throws DaoException {
        createIndexes();
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    protected void insert(Object... values) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            context.insertInto(table)
                    .values(values)
                    .execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    protected Record fetchOne(Condition... conditions) throws DaoException {
        return fetchOne(Arrays.asList(conditions));
    }

    protected Record fetchOne(Collection<Condition> conditions) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchOne();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    protected Result<Record> fetch(Condition... conditions) throws DaoException {
        return fetch(Arrays.asList(conditions));
    }

    protected Result<Record> fetch(Collection<Condition> conditions) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            return context.selectFrom(table)
                    .where(conditions)
                    .fetch();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    protected Cursor<Record> fetchLazy(Condition... conditions) throws DaoException {
        return fetchLazy(Arrays.asList(conditions));
    }

    protected Cursor<Record> fetchLazy(Collection<Condition> conditions) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchLazy(fetchSize);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    protected int fetchCount(Condition... conditions) throws DaoException {
        return fetchCount(Arrays.asList(conditions));
    }

    protected int fetchCount(Collection<Condition> conditions) throws DaoException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DSLContext context = DSL.using(conn, dialect);
            return context.selectFrom(table)
                    .where(conditions)
                    .fetchCount();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    protected void createTables() throws DaoException {
        executeSqlScriptWithSuffix("-create-tables.sql");
    }

    protected void dropTables() throws DaoException {
        executeSqlScriptWithSuffix("-drop-tables.sql");
    }

    protected void createIndexes() throws DaoException {
        executeSqlScriptWithSuffix("-create-indexes.sql");
    }

    protected void dropIndexes() throws DaoException {
        executeSqlScriptWithSuffix("-drop-indexes.sql");
    }

    /**
     * Executes the appropriate sql script with a particular suffix (.e.g. "-drop-tables.sql").
     * @param suffix
     * @throws DaoException
     */
    protected void executeSqlScriptWithSuffix(String suffix) throws DaoException {
        executeSqlResource(tableName + suffix);
    }

    /**
     * Executes a sql resource on the classpath
     * @param name Resource path - e.g. "/db/local-page.schema.sql"
     * @throws DaoException
     */
    public void executeSqlResource(String name) throws DaoException {
        Connection conn=null;
        try {
            conn = ds.getConnection();
            conn.createStatement().execute(
                    IOUtils.toString(
                            BaseSqLDao.class.getResource(name)
                    ));
        } catch (IOException e) {
            throw new DaoException(e);
        } catch (SQLException e){
            throw new DaoException(e);
        } finally {
            quietlyCloseConn(conn);
        }
    }

    /**
     * Close a connection without generating an exception if it fails.
     * @param conn
     */
    public static void quietlyCloseConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.warning("Failed to close connection");
            }
        }
    }
}
