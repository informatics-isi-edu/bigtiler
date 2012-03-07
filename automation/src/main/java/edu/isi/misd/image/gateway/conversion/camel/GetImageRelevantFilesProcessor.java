package edu.isi.misd.image.gateway.conversion.camel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Retreives the list of relevant files necessary to process an image. Writes
 * the list of absolute file paths to the outbound message.
 * 
 * @author David Smith
 * 
 */
public class GetImageRelevantFilesProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(GetImageRelevantFilesProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        final File f = exchange.getIn().getBody(File.class);
        final ImageSource source = ImageSourceFactory.getImageSource(f
                .getAbsolutePath());
        final List<String> files = source.getRelevantFiles();
        final List<File> fileList = new ArrayList<File>(files.size());
        for (String fn : files) {
            fileList.add(new File(fn));
            if (LOG.isDebugEnabled()) {
                LOG.debug("Added file " + fn + " to the relevant files list.");
            }
        }
        source.close();
        exchange.getOut().setBody(fileList, List.class);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }

}
