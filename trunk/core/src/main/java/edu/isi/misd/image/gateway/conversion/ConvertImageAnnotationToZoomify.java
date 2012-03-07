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

import java.io.IOException;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.annotation.AnnotationConversion;
import edu.isi.misd.image.gateway.conversion.annotation.AnnotationConversionFactory;

/**
 * Command class for converting an image's proprietary annotation file to
 * zoomify annotation format
 * 
 * @author David Smith
 * 
 */
public class ConvertImageAnnotationToZoomify {

    private final ImageInformation imageInformation;
    private final ImageSource imageSource;

    private static final Logger LOG = Logger.getLogger(ConvertImageAnnotationToZoomify.class);

    private static final String USAGE = "Usage: " + ConvertImageAnnotationToZoomify.class.getName() + " <image>";

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println(USAGE);
            System.exit(1);
        }

        String imageFilename = args[0];
        try {
            ConvertImageAnnotationToZoomify convert = new ConvertImageAnnotationToZoomify(
                    imageFilename);
            System.out.println(convert.convertToString());
        }
        catch(Exception e) {
            LOG.error("Error converting the image annotation.", e);
            System.exit(1);
        }
    }

    /**
     * Default constructor
     * 
     * @param image
     *            image filename
     * @throws UnsupportedConversionException
     *             if the image is not a supported source
     */
    public ConvertImageAnnotationToZoomify(String image)
            throws UnsupportedConversionException {
        if (image == null || image.length() == 0) {
            throw new IllegalArgumentException("Image must be specified.");
        }
        imageSource = ImageSourceFactory.getImageSource(image);
        imageInformation = ImageInformationFactory.getImageInformation(image);
    }

    /**
     * Constructs an image annotation conversion
     * 
     * @param source
     *            image source
     * @param info
     *            image info
     */
    public ConvertImageAnnotationToZoomify(ImageSource source,
            ImageInformation info) {
        if (source == null || info == null) {
            throw new IllegalArgumentException(
                    "Image source and information must be specified.");
        }
        imageSource = source;
        imageInformation = info;
    }

    /**
     * Action method.
     * 
     * @return the Zoomify annotation, or an empty string if the annotation file
     *         doesn't exist
     */
    public String convertToString() throws IOException {
        String annotation = "";
        if (imageInformation.hasAnnotationFile()) {
            AnnotationConversion conversion = AnnotationConversionFactory
                    .getAnnotationConversion(imageSource, imageInformation);
            annotation = conversion.convertToString();

        }
        return annotation;
    }
}
