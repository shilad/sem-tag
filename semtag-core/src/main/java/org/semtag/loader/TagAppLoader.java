package org.semtag.loader;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.semtag.SemTagException;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.SaveHandler;
import org.semtag.core.model.Item;
import org.semtag.core.model.Tag;
import org.semtag.core.model.TagApp;
import org.semtag.core.model.User;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public class TagAppLoader {

    private static final Logger LOG = Logger.getLogger(TagAppLoader.class.getName());
    private final SaveHandler handler;
    private final ConceptMapper mapper;
    private final AtomicInteger i;

    public TagAppLoader(SaveHandler handler, ConceptMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
        i = new AtomicInteger(0);
    }

    public void clear() throws SemTagException {
        try {
            handler.clear();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    public void beginLoad() throws SemTagException {
        try {
            handler.beginLoad();
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    /**
     * Splits the input string into parts at tab characters and calls
     * the other {@code load} method on the parts. The string must be
     * in order: user ID, item ID, tag text, timestamp.
     * @param tagApp
     * @throws SemTagException
     */
    public void load(String tagApp) throws SemTagException {
        String[] split = tagApp.split("\t");
        if (split.length == 4 && StringUtils.containsOnly(split[3], "0123456789 :-/.")) {
            load(split[0], split[2], split[1], Timestamp.valueOf(StringUtils.removeEnd(split[3], "-05")));
        }
    }

    /**
     * Call this method to load a tagApp into the semtag db.
     * @param userId
     * @param rawTagString
     * @param itemId
     * @param timestamp
     * @throws DaoException
     * @throws SemTagException
     */
    public void load(String userId, String rawTagString, String itemId, Timestamp timestamp) throws SemTagException {
        TagApp tagApp = mapper.map(
                new User(userId),
                new Tag(rawTagString),
                new Item(itemId),
                timestamp);
        try {
            handler.save(tagApp);
        } catch (DaoException e) {
            throw new SemTagException(e);
        }
    }

    public void endLoad() throws SemTagException {
        try {
            handler.endLoad();
        } catch (DaoException e) {
            throw new SemTagException(e);
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
            new HelpFormatter().printHelp("TagAppLoader", options);
            return;
        }

        Env env = new Env(cmd);
        Configurator conf = env.getConfigurator();
        SaveHandler handler = conf.get(SaveHandler.class);
        ConceptMapper mapper = conf.get(ConceptMapper.class);
        final TagAppLoader loader = new TagAppLoader(handler, mapper);
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
                        loader.load(tagApp);
                    }
                },
                1000);
        LOG.info("End Load");
        loader.endLoad();
        LOG.info("DONE");
    }
}
