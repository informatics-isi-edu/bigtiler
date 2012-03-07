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



import java.lang.reflect.Method;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Factory class that generates an ImageDestination instance based on the image
 * file type to write.
 * 
 * @author David Smith
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ImageDestinationFactory {
    private static final Logger LOG = Logger
    .getLogger(ImageDestinationFactory.class);

    private static final TreeMap<String, Class<? extends ImageDestination>> imageDestinations = new TreeMap<String, Class<? extends ImageDestination>>();

    private static final String REGISTER_TYPES_METHOD = "registerTypes";

    private static String[] destinationClasses = new String[] {
        "edu.isi.misd.image.gateway.conversion.loci.LociImageDestination",
    };

    // cause these classes to register their types
    static {
        try {
            for (int i=0; i < destinationClasses.length; i++) {
                String destinationClassName = destinationClasses[i];
                try {
                    Class sourceClass = Class.forName(destinationClassName);
                    Method registerTypes = sourceClass
                            .getMethod(REGISTER_TYPES_METHOD);
                    registerTypes.invoke(null);
                } catch (ClassNotFoundException e) {
                    LOG.info("Image Destination class "
                            + destinationClassName
                            + " was not found and won't be used to read images.");
                }
            }
        } catch (Exception e) {
            LOG.error("Error retrieving image source classes.", e);
        }
    }

    /**
     * Registers a file type for an ImageDestination
     * 
     * @param type
     *            the file extension
     * @param destClass
     *            the ImageDestination class
     */
    synchronized public static void register(final String type,
            final Class<? extends ImageDestination> destClass) {
        if (type == null || destClass == null) {
            throw new IllegalArgumentException(
            "Type and ImageDestination class must be specified.");
        }
        if(!imageDestinations.containsKey(type)) {
            imageDestinations.put(type, destClass);
        }
    }

    /**
     * 
     * @param filename
     *            filename to generate for the written file
     * @return the ImageDestination Class to use for this file type
     * @throws UnsupportedConversionException
     *             if the file type isn't supported
     */
    public static Class<? extends ImageDestination> getImageDestinationClass(
            final String filename)
            throws UnsupportedConversionException {
        if (filename == null || filename.length() == 0) {
            throw new IllegalArgumentException("Filename must be specified.");
        }

        Class<? extends ImageDestination> destination = null;
        try {
            destination = imageDestinations
            .get(ConversionUtils
                    .getExtension(filename));
        } catch (final Exception e) {
            LOG.error(
                    "Error retrieving Image destination for file " + filename,
                    e);
        }
        if (destination == null) {
            throw new UnsupportedConversionException("Writing to type "
                    + ConversionUtils.getExtension(filename)
                    + " is not supported.");
        }
        return destination;
    }
}
