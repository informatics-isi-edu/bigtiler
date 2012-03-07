package edu.isi.misd.image.gateway.conversion.camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * Processes the proper headers for each file that has been split from a list.
 * 
 * @author David Smith
 * 
 */
public class FileListSplitProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(FileListSplitProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        final File file = exchange.getIn().getBody(File.class);
        final String parent = new File(exchange.getIn()
                .getHeader("CamelFilePath")
                .toString()).getParentFile().getAbsolutePath()
                + File.separator;
        final String newFilename = file.getAbsolutePath().replace(parent, "");
        exchange.getIn().setHeader("CamelFileName", newFilename);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Set CamelFileName to " + newFilename);
        }
    }
}
