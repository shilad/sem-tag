package org.semtag.core.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.Result;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.*;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
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
    public void save(TagApp tagApp) throws DaoException {
        insert(
                null,
                tagApp.getUser().getUserId(),
                tagApp.getTag().getRawTag(),
                tagApp.getTag().getNormalizedTag(),
                tagApp.getItem().getItemId(),
                tagApp.getTimestamp(),
                tagApp.getConceptId()
        );
    }

    @Override
    public void save(Connection conn, TagApp tagApp) throws DaoException {
        insert(
                conn,
                null,
                tagApp.getUser().getUserId(),
                tagApp.getTag().getRawTag(),
                tagApp.getTag().getNormalizedTag(),
                tagApp.getItem().getItemId(),
                tagApp.getTimestamp(),
                tagApp.getConceptId()
        );
    }

    @Override
    public Iterable<TagApp> get(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserIds() != null) {
            conditions.add(Tables.TAGAPPS.USER_ID.in(filter.getUserIds()));
        }
        if (filter.getTags() != null) {
            conditions.add(Tables.TAGAPPS.NORM_TAG.in(filter.getTags()));
        }
        if (filter.getItemIds() != null) {
            conditions.add(Tables.TAGAPPS.ITEM_ID.in(filter.getItemIds()));
        }
        if (filter.getConceptIds() != null) {
            conditions.add(Tables.TAGAPPS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        Cursor<Record> cursor = fetchLazy(conditions);
        return buildTagAppIterable(cursor);
    }

    @Override
    public int getCount(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserIds() != null) {
            conditions.add(Tables.TAGAPPS.USER_ID.in(filter.getUserIds()));
        }
        if (filter.getTags() != null) {
            conditions.add(Tables.TAGAPPS.NORM_TAG.in(filter.getTags()));
        }
        if (filter.getItemIds() != null) {
            conditions.add(Tables.TAGAPPS.ITEM_ID.in(filter.getItemIds()));
        }
        if (filter.getConceptIds() != null) {
            conditions.add(Tables.TAGAPPS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        return fetchCount(conditions);
    }

    @Override
    public TagApp getByTagAppId(long tagAppId) throws DaoException {
        Record record = fetchOne(Tables.TAGAPPS.TAG_APP_ID.eq(tagAppId));
        return buildTagApp(record);
    }

    @Override
    public TagAppGroup getGroup(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getUserId() != null) {
            conditions.add(Tables.TAGAPPS.USER_ID.eq(filter.getUserId()));
        }
        if (filter.getTag() != null) {
            conditions.add(Tables.TAGAPPS.NORM_TAG.eq(filter.getTag()));
        }
        if (filter.getItemId() != null) {
            conditions.add(Tables.TAGAPPS.ITEM_ID.eq(filter.getItemId()));
        }
        if (filter.getConceptId() != null) {
            conditions.add(Tables.TAGAPPS.CONCEPT_ID.eq(filter.getConceptId()));
        }
        Result<Record> result = fetch(conditions);
        return buildTagAppGroup(filter, result);
    }

    private Iterable<TagApp> buildTagAppIterable(Cursor<Record> cursor) {
        return new SqlDaoIterable<TagApp>(cursor) {
            @Override
            public TagApp transform(Record record) throws DaoException {
                return buildTagApp(record);
            }
        };
    }

    private TagAppGroup buildTagAppGroup(DaoFilter filter, Result<Record> result) {
        Set<TagApp> tagApps = new HashSet<TagApp>();
        for (Record record : result) {
            tagApps.add(buildTagApp(record));
        }
        return new TagAppGroup(filter, tagApps);
    }

    private TagApp buildTagApp(Record record) {
        if (record == null) {
            return null;
        }
        return new TagApp(
                record.getValue(Tables.TAGAPPS.TAG_APP_ID),
                new User(record.getValue(Tables.TAGAPPS.USER_ID)),
                new Tag(record.getValue(Tables.TAGAPPS.RAW_TAG)),
                new Item(record.getValue(Tables.TAGAPPS.ITEM_ID)),
                record.getValue(Tables.TAGAPPS.TIMESTAMP),
                record.getValue(Tables.TAGAPPS.CONCEPT_ID));
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagAppDao> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagAppDao.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.dao.tagApp";
        }

        @Override
        public TagAppSqlDao get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new TagAppSqlDao(
                        getConfigurator().get(DataSource.class, config.getString("datasource"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
