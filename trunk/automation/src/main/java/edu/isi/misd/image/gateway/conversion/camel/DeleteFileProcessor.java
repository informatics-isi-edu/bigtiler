package edu.isi.misd.image.gateway.conversion.camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * Deletes a specified file (used for multi-file image deletion)
 * 
 * @author David Smith
 * 
 */
public class DeleteFileProcessor implements Processor {

    private final Logger LOG = Logger.getLogger(DeleteFileProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        File f = exchange.getIn().getBody(File.class);
        if (!f.delete()) {
            LOG.warn("Could not delete " + f.getAbsolutePath());
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Deleted " + f.getAbsolutePath());
            }
        }
    }

}
