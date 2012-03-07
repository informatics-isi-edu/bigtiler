/*
 * Copyright 2011 University of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.isi.misd.image.gateway.conversion;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Convenience class for converting images from the command line.
 * 
 * @author David Smith
 * 
 */
public class ConvertImage {

    private static final Logger LOG = Logger.getLogger(ConvertImage.class);

    private static final String USAGE = "Usage: "
            + ConvertImage.class.getName()
            + " <source file> <destination file>";

    public static void main(final String[] args) {
        if (args.length < 2) {
            System.err.println(USAGE);
            System.exit(1);
        }

        try {
            ConvertImage.convert(args[0], args[1]);
        } catch (final Exception e) {
            LOG.error("Error converting image " + args[0] + " to " + args[1]
                    + ".", e);
        }

    }

    /**
     * Converts an image from one format to another.
     * 
     * @param source
     *            the source filename
     * @param destination
     *            the destination file/directory
     * @throws Exception
     *             if the conversion did not occur
     */
    public static void convert(final String source, final String destination)
            throws Exception {
        if (source == null || destination == null) {
            throw new IllegalArgumentException(
                    "Source and destination must be specified.");
        }
        final File f = new File(source);
        if (!f.exists() || !f.canRead()) {
            throw new IllegalArgumentException("Source " + source
                    + " doesn't exist or is not readable.");
        }

        final ImageConversion conversion = ImageConversionFactory
                .getImageConversion(source, destination);

        if (LOG.isInfoEnabled()) {
            LOG.info("Converting file " + source + " to " + destination + "...");
        }
        conversion.run();
        if (LOG.isInfoEnabled()) {
            LOG.info("Conversion of file " + source
                    + " has completed successfully.");
        }
    }
}
