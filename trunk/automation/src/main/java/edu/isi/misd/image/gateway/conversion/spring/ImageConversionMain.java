package edu.isi.misd.image.gateway.conversion.spring;

import org.apache.camel.spring.Main;
import org.apache.log4j.Logger;

/**
 * Responsible for running the Camel-based image conversion from the
 * command-line.
 * 
 * @author David Smith
 * 
 */
public class ImageConversionMain {
    private static final Logger LOG = Logger
            .getLogger(ImageConversionMain.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: " + ImageConversionMain.class.toString()
                    + " <image conversion context file>");
            System.exit(1);
        }

        final Main main = new Main();
        main.setApplicationContextUri(args[0]);
        main.enableHangupSupport();
        try {
            main.run();
        } catch (Exception e) {
            LOG.error("Error running the image conversion.", e);
        }
    }
}
