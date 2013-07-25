package org.semtag.core.dao.sql;

import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.Record;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.jooq.Tables;
import org.semtag.core.model.concept.Concept;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ari Weiland
 */
public class ConceptSqlDao extends BaseSqLDao<Concept> implements ConceptDao {

    public ConceptSqlDao(DataSource dataSource) throws DaoException {
        super(dataSource, "concepts", Tables.CONCEPTS);
    }

    @Override
    public void save(Concept item) throws DaoException {
        insert(
                item.getConceptId(),
                item.getMetric(),
                item.conceptObjToBytes()
        );
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
    
    private Concept buildConcept(Record record) {
        // TODO: this method
        return null;
    }
}
