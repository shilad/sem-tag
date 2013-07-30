package org.semtag.cookbook;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.semtag.SemTagException;
import org.semtag.core.dao.SaveHandler;
import org.semtag.loader.TagAppLoader;
import org.semtag.mapper.ConceptMapper;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.conf.DefaultOptionBuilder;
import org.wikapidia.core.cmd.Env;
import org.wikapidia.utils.ParallelForEach;
import org.wikapidia.utils.Procedure;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 */
public class LoadMacademia {

    public static final Logger LOG = Logger.getLogger(LoadMacademia.class.getName());

    private final TagAppLoader loader;

    public LoadMacademia(TagAppLoader loader) {
        this.loader = loader;
    }

    public void load(String tagApp) throws SemTagException {
        String[] split = tagApp.split("\t");
        if (split.length == 4) {
            loader.add(split[0], split[2], split[1], Timestamp.valueOf(StringUtils.removeEnd(split[3], "-05")));
        }
    }


    public static void main(String[] args) throws ConfigurationException, SemTagException, IOException {
        Options options = new Options();
        options.addOption(
                new DefaultOptionBuilder()
                        .withLongOpt("drop-tables")
                        .withDescription("drop and recreate all tables")
                        .create("d"));
        options.addOption(
                new DefaultOptionBuilder()
                        .hasArg()
                        .isRequired()
                        .withLongOpt("file")
                        .withDescription("file path to a tab-delimited file with columns: user ID, item ID, tag text, timestamp")
                        .create("f"));
        Env.addStandardOptions(options);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Invalid option usage: " + e.getMessage());
            new HelpFormatter().printHelp("DumpLoader", options);
            return;
        }

        Env env = new Env(cmd);
        Configurator conf = env.getConfigurator();
        SaveHandler handler = conf.get(SaveHandler.class);
        ConceptMapper mapper = conf.get(ConceptMapper.class);
        TagAppLoader loader = new TagAppLoader(handler, mapper);
        final LoadMacademia macademia = new LoadMacademia(loader);
        List<String> tagApps = FileUtils.readLines(new File(cmd.getOptionValue("f")));

        if (cmd.hasOption("d")) {
            LOG.info("Dropping tables");
            loader.clear();
        }
        LOG.info("Begin Load");
        loader.beginLoad();
        LOG.info("Loading");
        ParallelForEach.loop(tagApps,
                env.getMaxThreads(),
                new Procedure<String>() {
                    @Override
                    public void call(String tagApp) throws Exception {
                        macademia.load(tagApp);
                    }
                },
                1000);
        LOG.info("End Load");
        loader.endLoad();
        LOG.info("DONE");
    }
}
