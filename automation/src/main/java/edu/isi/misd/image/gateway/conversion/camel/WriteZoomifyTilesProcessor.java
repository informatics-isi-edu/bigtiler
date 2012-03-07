package edu.isi.misd.image.gateway.conversion.camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ConvertImageToZoomifyTiles;
import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Tiles an image to a given base directory.
 * 
 * @author David Smith
 * 
 */
public class WriteZoomifyTilesProcessor implements Processor {

    private static final Logger LOG = Logger
            .getLogger(WriteZoomifyTilesProcessor.class);

    private final String TILE_DIRECTORY_HEADER = "tileDirectory";

    @Override
    public void process(Exchange exchange) throws Exception {
        final File f = exchange.getIn().getBody(File.class);
        final ImageSource source = ImageSourceFactory.getImageSource(f
                .getAbsolutePath());
        final ConvertImageToZoomifyTiles convert = new ConvertImageToZoomifyTiles(
                source, exchange.getIn().getHeader(TILE_DIRECTORY_HEADER)
                .toString());
        convert.run();
        source.close();
        exchange.getOut()
        .setBody(
                new File(exchange.getIn().getHeader(TILE_DIRECTORY_HEADER)
                        .toString()));
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created tiles for "
                    + f.getAbsolutePath()
                    + " in "
                    + exchange.getIn().getHeader(TILE_DIRECTORY_HEADER)
                            .toString());
        }
    }
}
