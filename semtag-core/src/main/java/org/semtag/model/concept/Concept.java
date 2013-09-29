package org.semtag.model.concept;

import org.wikapidia.core.lang.LocalId;

/**
 * An abstract concept in SemTag that TagApps are mapped to.
 * It has an int ID, a String type identifier which describes
 * what type of concept it is, and a specifiable object that
 * represents the concept. The ID does not necessarily need to
 * relate to the concept object, but it should when possible.
 *
 * SemTag comes with a predefined WikapidiaConcept of type
 * "wikapidia", but a SemTag user may create their own. To do
 * so, you must create a Concept subclass, a class that implements
 * ConceptMapper and maps to your concept type, and a class that
 * implements ConceptSimilar to compare your concept type.
 *
 * @author Ari Weiland
 * @author Yulun Li
 */
public class Concept {
    /**
     * Wikipedia id packed into a long
     */
    protected final long conceptId;

    /**
     * Constructs a concept with given ID, type, and concept object.
     * @param localId
     */
    public Concept(LocalId localId) {
        this.conceptId = localId.toLong();
    }

    public Concept(long conceptId) {
        this.conceptId = conceptId;
    }


    public long getConceptId() {
        return conceptId;
    }

    public LocalId getLocalId() {
        return LocalId.fromLong(conceptId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;
        return conceptId == ((Concept)o).conceptId;
    }

    @Override
    public int hashCode() {
        return new Long(conceptId).hashCode();
    }

    @Override
    public String toString() {
        LocalId lid = getLocalId();
        return "Concept{" +
                "conceptId=" + conceptId +
                ", lang=\'" + lid.getLanguage() + '\'' +
                ", wpid=" + lid.getId() +
                '}';
    }
}
