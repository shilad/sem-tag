package org.semtag.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.semtag.core.jooq.Tables;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.mapper.ConceptMapper;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.sql.WpDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A SQL implementation of ConceptDao.
 *
 * @author Ari Weiland
 */
public class ConceptSqlDao extends BaseSqLDao<Concept> implements ConceptDao {

    private final ConceptMapper mapper;

    public ConceptSqlDao(WpDataSource dataSource, ConceptMapper mapper) throws DaoException {
        super(dataSource, "/db/concepts", Tables.CONCEPTS);
        this.mapper = mapper;
    }

    @Override
    public void save(Concept concept) throws DaoException {
        if (getCount(new DaoFilter().setConcept(concept)) == 0) {
            insert(concept.getConceptId());
        }
    }

    @Override
    public void save(DSLContext conn, Concept concept) throws DaoException {
        if (getCount(new DaoFilter().setConcept(concept)) == 0) {
            insert(conn, concept.getConceptId());
        }
    }

    @Override
    public Iterable<Concept> get(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getConceptIds() != null) {
            conditions.add(Tables.CONCEPTS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        Cursor<Record> cursor = fetchLazy(conditions);
        return buildConceptIterable(cursor);
    }

    @Override
    public int getCount(DaoFilter filter) throws DaoException {
        Collection<Condition> conditions = new ArrayList<Condition>();
        if (filter.getConceptIds() != null) {
            conditions.add(Tables.CONCEPTS.CONCEPT_ID.in(filter.getConceptIds()));
        }
        return fetchCount(conditions);
    }

    private Iterable<Concept> buildConceptIterable(Cursor<Record> cursor) {
        return new SqlDaoIterable<Concept>(cursor) {
            @Override
            public Concept transform(Record record) throws DaoException {
                return buildConcept(record);
            }
        };
    } 
    
    private Concept buildConcept(Record record) throws DaoException {
        return new Concept(record.getValue(Tables.CONCEPTS.CONCEPT_ID));
    }

    public static class Provider extends org.wikapidia.conf.Provider<ConceptDao> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return ConceptDao.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.dao.concept";
        }

        @Override
        public ConceptSqlDao get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new ConceptSqlDao(
                        getConfigurator().get(WpDataSource.class, config.getString("datasource")),
                        getConfigurator().get(ConceptMapper.class, config.getString("mapper"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
