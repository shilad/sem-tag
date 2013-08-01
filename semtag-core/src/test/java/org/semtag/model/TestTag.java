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
        System.out.println(Tag.normalize(tag)+"*");
    }
}
