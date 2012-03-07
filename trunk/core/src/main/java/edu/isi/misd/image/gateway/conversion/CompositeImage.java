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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Represents an image object that is built from combining multiple images on
 * either the X or Y axis.
 * 
 * @author David Smith
 * 
 */
public class CompositeImage {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private BufferedImage combined = null;

    /**
     * Adds an image to the existing Composite image
     * 
     * @param source
     *            the image to add
     * @param position
     *            the position to add the new image to, must be either
     *            CompositeImage.HORIZONTAL or CompositeImage.VERTICAL.
     */
    public void addImage(final BufferedImage source, final int position) {
        if (source == null) {
            throw new IllegalArgumentException(
            "Source image must be specified.");
        }
        if (position != HORIZONTAL && position != VERTICAL) {
            throw new IllegalArgumentException("Invalid image position '"
                    + position + "'");
        }
        if (combined == null) {
            combined = source;
        } else {
            BufferedImage newImage = null;
            Graphics2D g = null;
            if (position == HORIZONTAL) {
                newImage = new BufferedImage(combined.getWidth()
                        + source.getWidth(), Math.max(combined.getHeight(),
                                source.getHeight()), source.getType());
                g = newImage.createGraphics();
                g.drawImage(combined, null, 0, 0);
                g.drawImage(source, null, combined.getWidth(), 0);
            } else {
                newImage = new BufferedImage(Math.max(combined.getWidth(),
                        source.getWidth()), combined.getHeight()
                        + source.getHeight(), source.getType());
                g = newImage.createGraphics();
                g.drawImage(combined, null, 0, 0);
                g.drawImage(source, null, 0, combined.getHeight());
            }
            combined = newImage;
        }
    }

    /**
     * 
     * @return the image in its current state
     */
    public BufferedImage getImage() {
        return combined;
    }

    /**
     * 
     * @return true if at least one image has been added to this composite image
     */
    public boolean hasImage() {
        return combined != null;
    }
}
