/**
 * MIT License
 *
 * Copyright (c) 2024, 2025 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the GuiToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * GuiToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/guitoolkit
 */
package com.marineacoustics.jgui.component;

import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A simple panel that draws a checker board with the color over it.
 */
public class ColorPanel extends JPanel {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = 1060448530831374209L;

    // the off-screen image we are rendering to.
    private BufferedImage offScreenImage;

    // the off-screen graphics we are rendering to
    private Graphics2D offScreenGraphics;

    // the size of the last image we displayed
    private int offscreenImageHeight;
    private int offscreenImageWidth;

    private Color color;
    
    public ColorPanel() {
        // Always call the superclass constructor first!
        super();
    }

    public void setColor( final Color pColor ) {
        color = pColor;
    }

    /*
     * paintComponent
     */
    @Override
    public void paintComponent( final Graphics g ) {
        // Only bother to do this complex drawing if there is an alpha value.
        final int alpha = color.getAlpha();
        if ( alpha < 255 ) {
            // See if we need to create our off-screen buffer
            if ( ( offScreenImage == null )
                    || ( offscreenImageHeight != getHeight() )
                    || ( offscreenImageWidth != getWidth() ) ) {
                offScreenImage = new BufferedImage(
                        getWidth(),
                        getHeight(),
                        BufferedImage.TYPE_INT_RGB );

                offScreenGraphics = offScreenImage.createGraphics();

                offscreenImageHeight = getHeight();
                offscreenImageWidth = getWidth();
            }

            // Fill the background with white.
            offScreenGraphics.setColor( Color.WHITE );
            offScreenGraphics.fillRect( 0, 0, getWidth(), getHeight() );

            // Draw a gray checker board pattern.
            offScreenGraphics.setColor( Color.GRAY );
            for ( int sY = 0; sY < getHeight(); sY += 5 ) {
                for ( int sX = 0; sX < getWidth(); sX += 10 ) {
                    offScreenGraphics.fillRect( sX, sY, 5, 5 );
                }
                sY += 5;
                for ( int sX = 5; sX < getWidth(); sX += 10 ) {
                    offScreenGraphics.fillRect( sX, sY, 5, 5 );
                }
            }

            final Composite currentComposite
                    = offScreenGraphics.getComposite();

            // Set opacity to the alpha of the color
            offScreenGraphics.setComposite( AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER ) );

            // Set the color and draw over the existing box
            offScreenGraphics.setColor( color );
            offScreenGraphics.fillRect( 0, 0, getWidth(), getHeight() );

            // Set the composite back the way it was
            offScreenGraphics.setComposite( currentComposite );

            // Draw the image to the screen
            g.drawImage( offScreenImage, 0, 0, null );
        } else {
            // Set the color and draw over the existing box
            g.setColor( color );
            g.fillRect( 0, 0, getWidth(), getHeight() );
        }
    }
}
