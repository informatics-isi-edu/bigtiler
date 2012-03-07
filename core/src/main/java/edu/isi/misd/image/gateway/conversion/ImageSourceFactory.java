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
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Factory class used to generate an image source for the given image type.
 * 
 * @author David Smith
 * 
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImageSourceFactory {

    private static final Logger LOG = Logger
            .getLogger(ImageSourceFactory.class);

    private static final TreeMap<String, Class<? extends ImageSource>> imageSources = new TreeMap<String, Class<? extends ImageSource>>();

    private static final String REGISTER_TYPES_METHOD = "registerTypes";

    private static final String[] sourceClasses = new String[]{
        "edu.isi.misd.image.gateway.conversion.ndpi.NdpiImageSource",
        "edu.isi.misd.image.gateway.conversion.loci.LociNdpiImageSource",
        "edu.isi.misd.image.gateway.conversion.loci.LociImageSource",
    };

    // cause these classes to register their types
    static {
        try {
            for(int i=0; i < sourceClasses.length; i++) {
                String sourceClassName = sourceClasses[i];
                try {
                    Class sourceClass = Class.forName(sourceClassName);
                    Method registerTypes = sourceClass.getMethod(REGISTER_TYPES_METHOD);
                    registerTypes.invoke(null);
                }
                catch(ClassNotFoundException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Image Source class "
                                + sourceClassName
                                + " was not found and won't be used to read images.");
                    }
                }
            }
        }
        catch(Exception e) {
            LOG.error("Error retrieving image source classes.", e);
        }
    }

    /**
     * Main method lists the image source types that are available
     * 
     * @param args
     */
    public static void main(final String[] args) {
        final Set<String> imageTypes = imageSources.keySet();
        for (final String imageType : imageTypes) {
            System.out.println(imageType);
        }
    }

    /**
     * Registers a class for a specified file type
     * 
     * @param type
     *            the file type
     * @param sourceClass
     *            the class
     */
    synchronized public static void register(final String type,
            final Class<? extends ImageSource> sourceClass) {
        if (type == null || sourceClass == null) {
            throw new IllegalArgumentException(
                    "Type and class must be specified.");
        }
        if (!imageSources.containsKey(type)) {
            imageSources.put(type, sourceClass);
        }
    }

    /**
     * 
     * @param filename
     *            the filename to read
     * @return the ImageSource to use for this file type
     * @throws UnsupportedConversionException
     *             if no ImageSource is available for this file type.
     */
    public static ImageSource getImageSource(final String filename)
            throws UnsupportedConversionException {
        if (filename == null || filename.length() == 0) {
            throw new IllegalArgumentException("Filename must be specified.");
        }
        ImageSource source = null;
        Class sourceClass = imageSources.get(ConversionUtils
                .getExtension(filename));

        if (sourceClass == null) {
            throw new UnsupportedConversionException("Reading of type "
                    + ConversionUtils.getExtension(filename)
                    + " is not supported.");
        }
        try {
            source = (ImageSource) sourceClass.getConstructor(String.class)
                    .newInstance(
                            filename);
        } catch (Exception e) {
            LOG.error("Error constructing Image Source for " + filename, e);
        }

        return source;
    }

    /**
     * 
     * @param filename
     *            the image filename to test
     * @return true if there is a supported image source implementation for this
     *         extension
     */
    public static boolean isSupportedType(final String fileType) {
        return imageSources.containsKey(fileType);
    }
}
