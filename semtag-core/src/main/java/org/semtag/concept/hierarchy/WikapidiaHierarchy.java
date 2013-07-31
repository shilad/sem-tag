package org.semtag.concept.hierarchy;

import org.semtag.core.dao.ConceptDao;
import org.semtag.core.model.concept.Concept;
import org.wikapidia.core.dao.LocalCategoryMemberDao;

import java.util.Collection;

/**
 * @author Ari Weiland
 */
public class WikapidiaHierarchy implements ConceptHierarchy {

    private final ConceptDao conceptDao;
    private final LocalCategoryMemberDao hierarchyDao;

    public WikapidiaHierarchy(ConceptDao conceptDao, LocalCategoryMemberDao hierarchyDao) {
        this.conceptDao = conceptDao;
        this.hierarchyDao = hierarchyDao;
    }

    @Override
    public Collection<Concept> getSuperConcepts(Concept concept) {

        return null;
    }

    @Override
    public Collection<Concept> getSubConcepts(Concept concept) {
        return null;
    }
}
