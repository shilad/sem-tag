package org.semtag.core.dao.sql;

import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.TagApp;

import javax.sql.DataSource;

/**
* @author Ari Weiland
*/
public class TagAppSqlDao extends BaseSqLDao<TagApp> implements TagAppDao {

    public TagAppSqlDao(DataSource dataSource) throws DaoException {
        super(dataSource, "tagapps");
    }

    @Override
    public void save(TagApp item) throws DaoException {
        insert(
                null,
                item.getUser().getUserId(),
                item.getTag().getNormalizedTag(),
                item.getItem().getItemId(),
                item.getTimestamp(),
                item.getConcept().getConceptId()
        );
    }
}
