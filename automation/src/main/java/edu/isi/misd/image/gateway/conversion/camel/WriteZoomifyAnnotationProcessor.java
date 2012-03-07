package edu.isi.misd.image.gateway.conversion.camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ConvertImageAnnotationToZoomify;
import edu.isi.misd.image.gateway.conversion.ImageInformation;
import edu.isi.misd.image.gateway.conversion.ImageInformationFactory;
import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Retrieves proprietary annotation data and converts it to Zoomify format in
 * the outbound body.
 * 
 * @author David Smith
 * 
 */
public class WriteZoomifyAnnotationProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(WriteZoomifyAnnotationProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        final File f = exchange.getIn().getBody(File.class);
        final ImageSource source = ImageSourceFactory.getImageSource(f
                .getAbsolutePath());
        final ImageInformation info = ImageInformationFactory
                .getImageInformation(f.getAbsolutePath());
        final ConvertImageAnnotationToZoomify convert = new ConvertImageAnnotationToZoomify(
                source, info);
        final String annotation = convert.convertToString();
        source.close();
        exchange.getOut().setBody(annotation);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created zoomify annotation for " + f.getAbsolutePath());
        }
    }

}
