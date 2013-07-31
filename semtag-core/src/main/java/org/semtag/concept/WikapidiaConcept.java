package org.semtag.concept;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.SimilarResult;
import org.semtag.core.model.SimilarResultList;
import org.semtag.core.model.TagApp;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;
import org.wikapidia.sr.SRResult;
import org.wikapidia.sr.SRResultList;

/**
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept<LocalId> {

    private final LocalSRMetric srMetric;

    public WikapidiaConcept(int conceptId, LocalSRMetric srMetric, byte[] objBytes) {
        super(conceptId, srMetric.getName(), objBytes);
        this.srMetric = srMetric;
    }

    public WikapidiaConcept(LocalId conceptObj, LocalSRMetric srMetric) {
        super(conceptObj.getId(), srMetric.getName(), conceptObj);
        this.srMetric = srMetric;
    }

    public LocalSRMetric getSrMetric() {
        return srMetric;
    }

    @Override
    public double getSimilarityTo(Concept other) throws DaoException {
        if (!(other instanceof WikapidiaConcept)) {
            throw new IllegalArgumentException("Concept types do not match");
        }
        try {
            SRResult result = srMetric.similarity(
                    conceptObj.asLocalPage(),
                    ((WikapidiaConcept) other).conceptObj.asLocalPage(),
                    false);
            return result.getValue();
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public SimilarResultList getMostSimilar(int maxResults, TagAppDao helperDao) throws DaoException {
        Iterable<TagApp> tagApps = helperDao.get(new DaoFilter());
        TIntSet validIds = new TIntHashSet();
        for (TagApp t : tagApps) {
            validIds.add(t.getConceptId());
        }
        SRResultList results;
        try {
            results = srMetric.mostSimilar(conceptObj.asLocalPage(), maxResults, validIds);
        } catch (org.wikapidia.core.dao.DaoException e) {
            throw new DaoException(e);
        }
        SimilarResultList list = new SimilarResultList(maxResults);
        for (SRResult r : results) {
            list.add(new SimilarResult(r.getId(), r.getValue()));
        }
        list.lock();
        return list;
    }

    @Override
    protected String conceptObjToString() {
        return conceptObj.getId() + " " +conceptObj.getLanguage().getId();
    }

    @Override
    protected LocalId stringToConceptObj(String s) {
        String[] split = s.split(" ");
        return new LocalId(
                Language.getById(new Integer(split[1])),
                new Integer(split[0]));
    }
}
