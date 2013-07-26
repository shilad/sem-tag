package org.semtag.core.dao.sql;

import com.typesafe.config.Config;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.Record;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.concept.Concept;
import org.semtag.mapper.ConceptMapper;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ari Weiland
 */
public class ConceptSqlDao extends BaseSqLDao<Concept> implements ConceptDao {

    private final ConceptMapper mapper;

    public ConceptSqlDao(DataSource dataSource, ConceptMapper mapper) throws DaoException {
        super(dataSource, "concepts", Tables.CONCEPTS);
        this.mapper = mapper;
    }

    @Override
    public void save(Concept concept) throws DaoException {
        if (getCount(new DaoFilter().setConceptId(concept.getConceptId())) == 0) {
            insert(
                    concept.getConceptId(),
                    concept.getMetric(),
                    concept.conceptObjToBytes());
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

    @Override
    public Concept getByConceptId(int conceptId) throws DaoException {
        Record record = fetchOne(Tables.CONCEPTS.CONCEPT_ID.eq(conceptId));
        return buildConcept(record);
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
        return mapper.getConcept(
                record.getValue(Tables.CONCEPTS.CONCEPT_ID),
                record.getValue(Tables.CONCEPTS.METRIC),
                record.getValue(Tables.CONCEPTS.CONCEPT_OBJ));
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
            return "sem-tag.dao.tagAppDao";
        }

        @Override
        public ConceptSqlDao get(String name, Config config) throws ConfigurationException {
            if (!config.getString("type").equals("sql")) {
                return null;
            }
            try {
                return new ConceptSqlDao(
                        getConfigurator().get(DataSource.class, config.getString("datasource")),
                        getConfigurator().get(ConceptMapper.class, config.getString("mapper"))
                );
            } catch (DaoException e) {
                throw new ConfigurationException(e);
            }
        }
    }
}
