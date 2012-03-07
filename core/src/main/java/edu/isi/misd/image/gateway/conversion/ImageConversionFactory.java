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

import edu.isi.misd.image.gateway.conversion.loci.LociConversionImplementation;
import edu.isi.misd.image.gateway.conversion.loci.LociImageDestination;
import edu.isi.misd.image.gateway.conversion.loci.LociImageSource;

/**
 * Factory class for generating the ImageConversion instance, based on the
 * source and destination formats.
 * 
 * @author David Smith
 * 
 */
public class ImageConversionFactory {

    /**
     * 
     * @param source
     *            the source filename
     * @param destination
     *            the destination filename
     * @return the ImageConversion instance for this operation
     * @throws UnsupportedConversionException
     *             if the source or destination format are not supported.
     */
    public static ImageConversion getImageConversion(final String source,
            final String destination) throws UnsupportedConversionException {

        if (source == null || source.length() == 0 || destination == null
                || destination.length() == 0) {
            throw new IllegalArgumentException(
            "Source and destination must be specified.");
        }

        final ImageSource imageSource = ImageSourceFactory
        .getImageSource(source);

        ImageConversion conversion = null;
        if (imageSource instanceof LociImageSource) {

            final String extension = ConversionUtils.getExtension(destination);
            if (extension.equals("")) {
                // create tiles
                conversion = new ConvertImageToZoomifyTiles(imageSource,
                        destination);
            }
            else {
                final Class<? extends ImageDestination> imageDestinationClass = ImageDestinationFactory
                .getImageDestinationClass(destination);
                if (LociImageDestination.class.equals(imageDestinationClass)) {
                    conversion = new LociConversionImplementation(source,
                            destination);
                }
            }
        }
        if (conversion == null) {
            throw new UnsupportedConversionException("Cannot convert "
                    + ConversionUtils.getExtension(source) + " to "
                    + ConversionUtils.getExtension(destination));
        }
        return conversion;
    }
}
