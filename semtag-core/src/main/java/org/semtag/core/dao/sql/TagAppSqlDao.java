package org.semtag.core.dao.sql;

import org.jooq.Record;
import org.jooq.Result;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.*;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

/**
* @author Ari Weiland
*/
public class TagAppSqlDao extends BaseSqLDao<TagApp> implements TagAppDao {

    public TagAppSqlDao(DataSource dataSource) throws DaoException {
        super(dataSource, "tagapps", Tables.TAGAPPS);
    }

    @Override
    public void save(TagApp item) throws DaoException {
        insert(
                null,
                item.getUser().getUserId(),
                item.getTag().getNormalizedTag(),
                item.getItem().getItemId(),
                item.getTimestamp(),
                item.getConceptId()
        );
    }

    /**
     * Fetches a TagApp from the database by specified TagApp ID.
     * @param tagAppId
     * @return
     * @throws DaoException
     */
    public TagApp getByTagAppId(long tagAppId) throws DaoException {
        Record record = fetchOne(Tables.TAGAPPS.TAG_APP_ID.eq(tagAppId));
        return buildTagApp(record);
    }



    private TagAppGroup buildTagAppGroup(DaoFilter filter, Result<Record> result) {
        Set<TagApp> tagApps = new HashSet<TagApp>();
        for (Record record : result) {
            tagApps.add(buildTagApp(record));
        }
        return new TagAppGroup(
                userId,
                tag,
                itemId,
                conceptId,
                tagApps);
    }

    private TagApp buildTagApp(Record record) {
        if (record == null) {
            return null;
        }
        return new TagApp(
                record.getValue(Tables.TAGAPPS.TAG_APP_ID),
                new User(record.getValue(Tables.TAGAPPS.USER_ID)),
                new Tag(record.getValue(Tables.TAGAPPS.TAG)),
                new Item(record.getValue(Tables.TAGAPPS.ITEM_ID)),
                record.getValue(Tables.TAGAPPS.TIMESTAMP),
                record.getValue(Tables.TAGAPPS.CONCEPT_ID));
    }

}
