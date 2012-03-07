package edu.isi.misd.image.gateway.conversion.camel;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * Responsible for resizing a buffered image from the body of the input to
 * another image. If the image is smaller than the threshold, the image isn't
 * resized.
 * 
 * @author David Smith
 * 
 */
public class ImageResizeProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(ImageResizeProcessor.class);

    private final int resizeWidth;
    private final int resizeHeight;

    public ImageResizeProcessor(int resizeWidth, int resizeHeight) {
        this.resizeWidth = resizeWidth;
        this.resizeHeight = resizeHeight;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        final BufferedImage image = exchange.getIn().getBody(
                BufferedImage.class);
        if (image.getWidth() > resizeWidth || image.getHeight() > resizeHeight) {
            final BufferedImage newImage = new BufferedImage(resizeWidth,
                    resizeHeight, image.getType());
            final Graphics2D g = newImage.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(
                    (double) resizeWidth / image.getWidth(),
                    (double) resizeHeight / image.getHeight());
            g.drawRenderedImage(image, at);
            exchange.getOut().setBody(newImage);
            g.dispose();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resized image "
                        + exchange.getIn().getHeader("CamelFileName")
                        .toString() + " to " + resizeWidth + "x"
                        + resizeHeight);
            }
        } else {
            exchange.getOut().setBody(image);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Image "
                        + exchange.getIn().getHeader("CamelFileName")
                        .toString() + " is smaller than " + resizeWidth
                        + "x" + resizeHeight + " (" + image.getWidth() + "x"
                        + image.getHeight() + ") and will not be resized.");
            }
        }
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }

}
