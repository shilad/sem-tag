package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.semtag.dao.*;
import org.semtag.model.TagApp;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * A SQL implementation of the SaveHandler.
 * Controls the group saving by creating its own connection
 * and passing it to the individual save methods.
 *
 * @author Ari Weiland
 */
public class SqlSaveHandler extends SaveHandler {
    
    private final DataSource dataSource;
    
    public SqlSaveHandler(TagAppDao tagAppDao, UserDao userDao, ItemDao itemDao, ConceptDao conceptDao, DataSource dataSource) {
        super(tagAppDao, userDao, itemDao, conceptDao);
        this.dataSource = dataSource;
    }

    @Override
    public void save(TagApp tagApp) throws DaoException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ((TagAppSqlDao) tagAppDao).save(conn, tagApp);
            ((UserSqLDao) userDao).save(conn, tagApp.getUser());
            ((ItemSqlDao) itemDao).save(conn, tagApp.getItem());
            ((ConceptSqlDao) conceptDao).save(conn, tagApp.getConcept());
            conn.commit();
        } catch (SQLException e) {
            rollback(conn, e);
        } catch (DaoException e) {
            rollback(conn, e);
        } finally {
            BaseSqLDao.quietlyCloseConn(conn);
        }
    }

    private void rollback(Connection conn, Throwable t) throws DaoException {
        LOG.log(Level.WARNING, "Failed to save TagApp; attempting to roll back changes. Cause: " + t);
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LOG.severe("Unable to roll back changes! Database may encounter problems.");
                throw new DaoException(e);
            }
        }
    }

    public static class Provider extends org.wikapidia.conf.Provider<SaveHandler> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return SaveHandler.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.saveHandler";
        }

        @Override
        public SqlSaveHandler get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            return new SqlSaveHandler(
                    getConfigurator().get(TagAppDao.class, "sql"),
                    getConfigurator().get(UserDao.class, "sql"),
                    getConfigurator().get(ItemDao.class, "sql"),
                    getConfigurator().get(ConceptDao.class, "sql"),
                    getConfigurator().get(DataSource.class, config.getString("datasource"))
            );
        }
    }
}
