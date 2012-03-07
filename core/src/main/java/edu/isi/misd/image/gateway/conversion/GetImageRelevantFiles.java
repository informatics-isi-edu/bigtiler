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

import java.util.List;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Retrieves the list of relevant files for an image. 
 */
public class GetImageRelevantFiles {

    private static final Logger LOG = Logger.getLogger(GetImageRelevantFiles.class);

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Usage: " + GetImageRelevantFiles.class.getName() + " <image filename>");
            System.exit(1);
        }
        try {
            ImageSource source = ImageSourceFactory.getImageSource(args[0]);
            List<String> files = source.getRelevantFiles();
            for(String file:files) {
                System.out.println(file);
            }
        }
        catch(Exception e) { 
            LOG.error("Error retrieving relevant files for image.", e);
            System.exit(1);
        }
    }
}

