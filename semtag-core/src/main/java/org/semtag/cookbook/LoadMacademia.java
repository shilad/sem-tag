package org.semtag.cookbook;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.semtag.SemTagException;
import org.semtag.loader.TagAppLoader;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.DefaultOptionBuilder;

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

    public final TagAppLoader loader;

    public LoadMacademia(TagAppLoader loader) {
        this.loader = loader;
    }

    public void load(String filepath) throws ConfigurationException, IOException, SemTagException {
        LOG.info("Begin Load");
        loader.beginLoad();
        LOG.info("Loading");
        List<String> tagApps = FileUtils.readLines(new File(filepath));
        int i=0;
        for (String tagApp : tagApps) {
            if (i > 0) {
                String[] split = tagApp.split("\t");
                if (split.length == 4) {
                    loader.add(split[0], split[2], split[1], Timestamp.valueOf(StringUtils.removeEnd(split[3], "-05")));
                }
            }
            i++;
            if (i%1000 == 0) {
                LOG.info("Loaded TagApps: " + i);
            }
        }
        LOG.info("End Load");
        loader.endLoad();
        LOG.info("DONE");
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(
                new DefaultOptionBuilder()
                        .hasArg()
                        .withLongOpt("configuration")
                        .withDescription("specify a custom configuration file")
                        .create("c"));
        options.addOption(
                new DefaultOptionBuilder()
                        .withLongOpt("drop-tables")
                        .withDescription("drop and recreate all tables")
                        .create("d"));
        options.addOption(
                new DefaultOptionBuilder()
                        .hasArg()
                        .withLongOpt("file")
                        .withDescription("file path to a tab-delimited file with columns: user ID, item ID, tag text, timestamp")
                        .create("f"));

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Invalid option usage: " + e.getMessage());
            new HelpFormatter().printHelp("DumpLoader", options);
            return;
        }

        // TODO: FINISH ME
    }
}
