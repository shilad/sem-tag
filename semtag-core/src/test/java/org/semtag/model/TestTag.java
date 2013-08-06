package org.semtag.model;

import org.junit.Test;

/**
 * @author Ari Weiland
 */
public class TestTag {

    @Test
    public void testNormalize() {
        String tag = "  ase5 h34[qi   34%#PIa_wkr\tp34j5-poa ijf!\n";
        System.out.println(tag);
        String normalized = Tag.normalize(tag);
        System.out.println(normalized+"*");
        assert (normalized.equals("ase5 h34 qi 34 pia wkr p34j5 poa ijf"));
    }
}
