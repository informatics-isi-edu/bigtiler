package edu.isi.misd.image.gateway.conversion.test;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.CompositeImage;

public class CompositeImageTest {
    private CompositeImage image;

    @Before
    public void setUp() {
        image = new CompositeImage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddImage_nullSource() {
        image.addImage(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddImage_invalidPosition() {
        image.addImage(new BufferedImage(1, 1, 0), 20);
    }

    @Test
    public void testAddGetImage() {
        final BufferedImage image1 = new BufferedImage(12, 20,
                BufferedImage.TYPE_INT_RGB);
        image.addImage(image1, CompositeImage.HORIZONTAL);
        final BufferedImage storedImage = image.getImage();
        if (storedImage == null) {
            fail("Stored image should not be null.");
        }
        if (image1.getWidth() != storedImage.getWidth()) {
            fail("Stored image width is " + storedImage.getWidth()
                    + ", should be " + image1.getWidth());
        }
        if (image1.getHeight() != storedImage.getHeight()) {
            fail("Stored image height is " + storedImage.getHeight()
                    + ", should be " + image1.getHeight());
        }
    }

    @Test
    public void testAddHorizonalImages() {
        final BufferedImage i1 = new BufferedImage(12, 13,
                BufferedImage.TYPE_INT_RGB);
        final BufferedImage i2 = new BufferedImage(15, 16,
                BufferedImage.TYPE_INT_RGB);
        image.addImage(i1, CompositeImage.HORIZONTAL);
        image.addImage(i2, CompositeImage.HORIZONTAL);
        final BufferedImage stored = image.getImage();
        if (stored == null) {
            fail("Stored image should not be null.");
        }
        if (stored.getWidth() != (i1.getWidth() + i2.getWidth())) {
            fail("Stored image width is " + stored.getWidth() + ", should be "
                    + (i1.getWidth() + i2.getWidth()));
        }
        if (stored.getHeight() != Math.max(i1.getHeight(), i2.getHeight())) {
            fail("Stored image height is " + stored.getHeight()
                    + ", should be " + Math.max(i1.getHeight(), i2.getHeight()));
        }
    }

    @Test
    public void testAddVerticalImages() {
        final BufferedImage i1 = new BufferedImage(12, 13,
                BufferedImage.TYPE_INT_RGB);
        final BufferedImage i2 = new BufferedImage(15, 13,
                BufferedImage.TYPE_INT_RGB);
        image.addImage(i1, CompositeImage.VERTICAL);
        image.addImage(i2, CompositeImage.VERTICAL);
        final BufferedImage stored = image.getImage();
        if (stored == null) {
            fail("Stored image should not be null");
        }
        if (stored.getHeight() != (i1.getHeight() + i2.getHeight())) {
            fail("Stored image height is " + stored.getHeight()
                    + ", should be " + (i1.getHeight() + i2.getHeight()));
        }
        if (stored.getWidth() != Math.max(i1.getWidth(), i2.getWidth())) {
            fail("Stored image width is " + stored.getWidth() + ", should be "
                    + Math.max(i1.getWidth(), i2.getWidth()));
        }
    }

    @Test
    public void testHasImage() {
        if (image.hasImage()) {
            fail("Stored image should not have an image.");
        }
        image.addImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), 0);
        if (!image.hasImage()) {
            fail("Stored image should have an image.");
        }
    }
}
