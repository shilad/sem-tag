package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.jooq.DSLContext;
import org.semtag.dao.*;
import org.semtag.dao.DaoException;
import org.semtag.model.TagApp;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.*;
import org.wikapidia.core.dao.sql.JooqUtils;
import org.wikapidia.core.dao.sql.WpDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

/**
 * A SQL implementation of the SaveHandler.
 * Controls the group saving by creating its own connection
 * and passing it to the individual save methods.
 *
 * @author Ari Weiland
 */
public class SqlSaveHandler extends SaveHandler {
    
    private final WpDataSource dataSource;
    
    public SqlSaveHandler(TagDao tagDao, TagAppDao tagAppDao, UserDao userDao, ItemDao itemDao, ConceptDao conceptDao, WpDataSource dataSource) {
        super(tagDao, tagAppDao, userDao, itemDao, conceptDao);
        this.dataSource = dataSource;
    }

    @Override
    public void save(TagApp tagApp) throws DaoException {
        DSLContext context = null;
        try {
            context = dataSource.getJooq();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
        try {
            ((TagSqlDao) tagDao).save(context, tagApp);
            ((TagAppSqlDao) tagAppDao).save(context, tagApp);
            ((UserSqLDao) userDao).save(context, tagApp.getUser());
            ((ItemSqlDao) itemDao).save(context, tagApp.getItem());
            ((ConceptSqlDao) conceptDao).save(context, tagApp.getConcept());
            JooqUtils.commit(context);
        } catch (DaoException e) {
            rollback(context, e);
            throw new DaoException(e);
        } catch (org.wikapidia.core.dao.DaoException e) {
            rollback(context, e);
            throw new DaoException(e);
        } finally {
            dataSource.freeJooq(context);
        }
    }

    private void rollback(DSLContext context, Throwable t) throws DaoException {
        LOG.log(Level.WARNING, "Failed to save TagApp; attempting to roll back changes. Cause: " + t);
        JooqUtils.rollbackQuietly(context);
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
        public SqlSaveHandler get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            return new SqlSaveHandler(
                    getConfigurator().get(TagDao.class, "sql"),
                    getConfigurator().get(TagAppDao.class, "sql"),
                    getConfigurator().get(UserDao.class, "sql"),
                    getConfigurator().get(ItemDao.class, "sql"),
                    getConfigurator().get(ConceptDao.class, "sql"),
                    getConfigurator().get(WpDataSource.class, config.getString("datasource"))
            );
        }
    }
}
