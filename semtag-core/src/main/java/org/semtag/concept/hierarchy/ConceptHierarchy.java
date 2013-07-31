package org.semtag.concept.hierarchy;

import org.semtag.core.model.concept.Concept;

import java.util.Collection;

/**
 * @author Ari Weiland
 */
public interface ConceptHierarchy {

    /**
     * Returns a collection of concepts that are categorical
     * parents of the specified concept.
     * @param concept
     * @return
     */
    public Collection<Concept> getSuperConcepts(Concept concept);

    /**
     * Returns a collection of concepts that are categorical
     * children of the specified concept.
     * @param concept
     * @return
     */
    public Collection<Concept> getSubConcepts(Concept concept);
}
