package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.TagApp;

import javax.sql.DataSource;

/**
* @author Ari Weiland
*/
public class TagAppSqlDao extends BaseSqLDao<TagApp> implements TagAppDao {

    public TagAppSqlDao(DataSource dataSource, String sqlScriptPrefix) throws DaoException {
        super(dataSource, sqlScriptPrefix);
    }

    @Override
    public void save(TagApp item) throws DaoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
