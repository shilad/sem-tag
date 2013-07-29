package org.semtag.core.model;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.semtag.SemTagException;
import org.semtag.core.dao.SaveHandler;
import org.semtag.loader.TagAppLoader;
import org.semtag.mapper.ConceptMapper;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 */
public class TestMacademia {

    public static final Logger LOG = Logger.getLogger(TestMacademia.class.getName());

    @Test
    public void loadMacademia() throws ConfigurationException, IOException, SemTagException {
        LOG.info("Preprocessing");
        Configurator configurator = new Configurator(new Configuration());
        TagAppLoader loader = new TagAppLoader(
                configurator.get(SaveHandler.class),
                configurator.get(ConceptMapper.class));
        List<String> tagApps = FileUtils.readLines(new File("macademia_tag_apps.txt"));
        LOG.info("Begin Load");
        loader.beginLoad();
        LOG.info("Loading");
        int i=0;
        for (String tagApp : tagApps) {
            i++;
            String[] split = tagApp.split(tagApp);
            loader.add(split[0], split[1], split[2], Timestamp.valueOf(split[3]));
            if (i%1000 == 0)
                LOG.info("Loaded TagApps: " + i);
        }
        LOG.info("End Load");
        loader.endLoad();
        LOG.info("DONE");
    }
}
