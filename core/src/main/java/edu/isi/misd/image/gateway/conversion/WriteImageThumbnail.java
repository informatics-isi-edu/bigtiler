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

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * Convenience class for writing a thumbnail image from a source image
 * 
 * @author David Smith
 * 
 */
public class WriteImageThumbnail {

    private static final Logger LOG = Logger
            .getLogger(WriteImageThumbnail.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err
            .println("Usage: "
                    + WriteImageThumbnail.class.getName()
                    + " <source filename> <thumbnail file to write>");
            System.exit(1);
        }

        try {
            ImageSource imageSource = ImageSourceFactory
                    .getImageSource(args[0]);
            BufferedImage thumbImage = imageSource.getThumbnailImage();
            ImageIO.write(thumbImage, "jpg", new File(args[1]));
        } catch (Exception e) {
            LOG.error("Error writing the thumbnail.", e);
            System.exit(1);
        }
    }
}
