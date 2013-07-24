package org.semtag.core.dao.sql;

import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.model.concept.Concept;

import javax.sql.DataSource;

/**
 * @author Ari Weiland
 */
public class ConceptSqlDao extends BaseSqLDao<Concept> implements ConceptDao {

    public ConceptSqlDao(DataSource dataSource, String sqlScriptPrefix) throws DaoException {
        super(dataSource, sqlScriptPrefix);
    }

    @Override
    public void save(Concept item) throws DaoException {
        insert();
    }
}