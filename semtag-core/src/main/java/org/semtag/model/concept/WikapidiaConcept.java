package org.semtag.model.concept;

import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;

/**
 * A SemTag concept that represents a Wikipedia local page,
 * as determined by the WikAPIdia API.
 *
 * @author Ari Weiland
 */
public class WikapidiaConcept extends Concept<LocalId> {

    public WikapidiaConcept(int conceptId, String type, byte[] objBytes) {
        super(conceptId, type, objBytes);
    }

    public WikapidiaConcept(LocalId conceptObj, String type) {
        super(conceptObj.getId(), type, conceptObj);
    }

    @Override
    protected String conceptObjToString() {
        return conceptObj.getId() + " " + conceptObj.getLanguage().getId();
    }

    @Override
    protected LocalId stringToConceptObj(String s) {
        String[] split = s.split(" ");
        return new LocalId(
                Language.getById(new Integer(split[1])),
                new Integer(split[0]));
    }
}
