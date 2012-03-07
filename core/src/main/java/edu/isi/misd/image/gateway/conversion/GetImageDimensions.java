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

import org.apache.log4j.Logger;

/**
 * Convenience command class for reporting the dimensions of an image (in
 * pixels).
 * 
 * @author David Smith
 * 
 */
public class GetImageDimensions {

    private static final String USAGE = "Usage: "
        + GetImageDimensions.class.getName() + " <image file>";
    private static final Logger LOG = Logger
    .getLogger(GetImageDimensions.class);

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE);
            System.exit(1);
        }

        try {
            final ImageSource source = ImageSourceFactory
            .getImageSource(args[0]);
            System.out.println(source.getWidth() + "x" + source.getHeight());
        } catch (final UnsupportedConversionException e) {
            LOG.error("Error retrieving dimensions for image " + args[0] + ".",
                    e);
            System.exit(1);
        }
    }
}
