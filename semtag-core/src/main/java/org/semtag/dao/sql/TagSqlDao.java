package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.semtag.core.jooq.Tables;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagDao;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.sql.WpDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * A SQL implementation of TagDao.
 *
 * @author Shilad Sen
 */
public class TagSqlDao extends BaseSqLDao<Tag> implements TagDao {

    public TagSqlDao(WpDataSource dataSource) throws DaoException {
        super(dataSource, "/db/tags", Tables.TAGS);
    }

    @Override
    public void save(Tag tag) throws DaoException {
        if (getCount(new DaoFilter().setTags(tag)) == 0) {
            insert(
                    null,
                    tag.getRawTag(),
                    tag.getNormalizedTag(),
                    new Timestamp(new Date().getTime()),
                    -1,
                    0
            );
        }
    }
    @Override
    public void save(DSLContext ctx, Tag tag) throws DaoException {
        if (getCount(new DaoFilter().setTags(tag)) == 0) {
            insert(
                    ctx,
                    tag.getRawTag(),
                    tag.getNormalizedTag(),
                    new Timestamp(new Date().getTime()),
                    -1,
                    0
            );
        }
    }

    public void save(DSLContext ctx, TagApp tagApp) throws DaoException {
        if (getCount(new DaoFilter().setTags(tagApp.getTag())) == 0) {
            insert(
                    ctx,
                    null,
                    tagApp.getTag().getRawTag(),
                    tagApp.getTag().getNormalizedTag(),
                    new Timestamp(new Date().getTime()),
                    tagApp.getConcept().getConceptId(),
                    1
            );
        } else {
            ctx.update(Tables.TAGS)
                .set(Tables.TAGS.COUNT, Tables.TAGS.COUNT.add(1))
                .where(Tables.TAGS.NORM_TAG.eq(tagApp.getTag().getNormalizedTag()))
                .execute();
        }
    }

    @Override
    public Iterable<Tag> get(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getTags() != null) {
            conditions.add(Tables.TAGS.RAW_TAG.in(filter.getTags()));
        }
        if (filter.getConcepts() != null) {
            conditions.add(Tables.TAGS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        Cursor<Record> cursor = fetchLazy(conditions);
        return buildTagIterable(cursor);
    }

    @Override
    public int getCount(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getTags() != null) {
            conditions.add(Tables.TAGS.NORM_TAG.in(filter.getTags()));
        }
        if (filter.getConcepts() != null) {
            conditions.add(Tables.TAGS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        return fetchCount(conditions);
    }

    @Override
    public Tag getByTag(String tag) throws DaoException {
        Record record = fetchOne(Tables.TAGS.NORM_TAG.eq(tag));
        return buildTag(record);
    }

    private Iterable<Tag> buildTagIterable(Cursor<Record> cursor) {
        return new SqlDaoIterable<Tag>(cursor) {
            @Override
            public Tag transform(Record record) throws DaoException {
                return buildTag(record);
            }
        };
    } 
    
    private Tag buildTag(Record record) throws DaoException {
        return new Tag(
                record.getValue(Tables.TAGS.RAW_TAG),
                record.getValue(Tables.TAGS.TAG_ID),
                record.getValue(Tables.TAGS.COUNT),
                new Concept(record.getValue(Tables.TAGS.CONCEPT_ID))
            );
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagDao> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class<TagDao> getType() {
            return TagDao.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.dao.tag";
        }

        @Override
        public TagSqlDao get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new TagSqlDao(
                        getConfigurator().get(WpDataSource.class, config.getString("datasource"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
