package edu.isi.misd.image.gateway.conversion.camel;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Reads the pixel data of an image and writes a thumbnail buffered image in the
 * outbound message body
 * 
 * @author David Smith
 * 
 */
public class WriteImageThumbnailProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(WriteImageThumbnailProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        final File f = exchange.getIn().getBody(File.class);
        final ImageSource source = ImageSourceFactory.getImageSource(f
                .getAbsolutePath());
        final BufferedImage thumbImage = source.getThumbnailImage();
        source.close();
        exchange.getOut().setBody(thumbImage);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created thumbnail of size " + thumbImage.getWidth()
                    + "x" + thumbImage.getHeight() + " for image "
                    + f.getAbsolutePath());
        }
    }

}
