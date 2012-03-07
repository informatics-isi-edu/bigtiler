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

package edu.isi.misd.image.gateway.conversion.annotation;

import java.io.IOException;

import edu.isi.misd.image.gateway.conversion.Constants;
import edu.isi.misd.image.gateway.conversion.ConversionUtils;
import edu.isi.misd.image.gateway.conversion.ImageInformation;
import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ndpi.NdpiImageInformation;

public class AnnotationConversionFactory {

    /**
     * Retrieves the instance of the annotation conversion for the specified
     * image type
     * 
     * @param source
     *            the image source
     * @param information
     *            additional information about the image
     */
    public static final AnnotationConversion getAnnotationConversion(
            ImageSource source,
            ImageInformation information) throws IOException {
        String imageFilename = source.getFilename();
        AnnotationConversion conversion = null;
        String extension = ConversionUtils.getExtension(imageFilename)
        .toLowerCase();
        if (Constants.SVS_FILE_EXTENSION.equals(extension)) {
            conversion = new SvsToZoomify(information.getAnnotationFilename(),
                    source.getWidth(), source.getHeight());
        } else if (Constants.NDPI_FILE_EXTENSION.equals(extension)) {
            conversion = new NdpiToZoomify(
                    information.getAnnotationFilename(),
                    source.getWidth(),
                    source.getHeight(),
                    Long.valueOf((Integer)information
                    .getIFDValue(NdpiImageInformation.IFD_PHYSICAL_X_CENTER)),
                    Long.valueOf((Integer)information
                    .getIFDValue(NdpiImageInformation.IFD_PHYSICAL_Y_CENTER)));
        }

        return conversion;
    }
}
