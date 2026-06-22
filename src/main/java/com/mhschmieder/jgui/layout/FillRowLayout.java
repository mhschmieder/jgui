/*
 * MIT License
 *
 * Copyright (c) 2026 Mark Schmieder. All rights reserved.
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
 * This file is part of the jgui Library
 *
 * You should have received a copy of the MIT License along with the jgui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/jgui
 */
package com.mhschmieder.jgui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A Layout Manager that is meant to fill a row with the components Those
 * components with a small max size stay that size. Those that have a large max
 * size, Split the rest of the row's length amongst themselves.
 */
public class FillRowLayout implements LayoutManager {

    // The number of pixels Above and below each component
    int m_VertSpacing;

    // The number of pixels in between each component
    int m_HorzSpacing;

    /**
     * Creates a new FillRowLayout
     */
    public FillRowLayout( final int VertSpacing, final int HorzSpacing ) {
        m_VertSpacing = VertSpacing;
        m_HorzSpacing = HorzSpacing;
    }

    /*
     * We don't hold anything specific about each component, so we don't need
     * to do anything here
     */
    @Override
    public void addLayoutComponent( final String p1, final Component p2 ) {
    }

    /*
     * We don't hold anything specific about each component, so we don't need
     * to do anything here
     */
    @Override
    public void removeLayoutComponent( final Component p1 ) {
    }

    /*---------------------------------
     * preferredLayoutSize
     *
     * The sum of all the components preferred widths is the preferred width
     * of the container.
     *
     * The preferred height of the container is the largest single preferred
     * height of all the components.
     *-------------------*/
    @Override
    public Dimension preferredLayoutSize( final Container p1 ) {
        final int NumberOfComponents = p1.getComponentCount();

        int LargestHeight = 0;
        int Width = 0;
        for ( int i = 0; i < NumberOfComponents; i++ ) {
            // Get the preferred size of this component
            final Dimension cPreferredSize = p1.getComponent( i )
                    .getPreferredSize();

            // Look for the single largest height
            if ( cPreferredSize.height > LargestHeight ) {
                LargestHeight = cPreferredSize.height;
            }

            // Add all the widths
            Width += cPreferredSize.width;
        }

        // The final dimensions need to include our spacing settings
        return new Dimension(
                Width + ( m_HorzSpacing * ( NumberOfComponents + 1 ) ),
                LargestHeight + ( 2 * m_VertSpacing ) );
    }

    /*---------------------------------
     * minimumLayoutSize
     *
     * The minimum Width is simply the addition of all the minimum widths.
     * The height is the largest minimum height.
     *-------------------*/
    @Override
    public Dimension minimumLayoutSize( final Container p1 ) {
        // Get the number of components in our container
        final int NumberOfComponents = p1.getComponentCount();

        int LargestHeight = 0;
        int Width = 0;
        for ( int i = 0; i < NumberOfComponents; i++ ) {
            final Dimension CMinSize = p1.getComponent( i ).getMinimumSize();

            // Find the largest minimum height
            if ( CMinSize.height > LargestHeight ) {
                LargestHeight = CMinSize.height;
            }

            // Add together all the widths
            Width += CMinSize.width;
        }

        // The final dimensions need to include our spacing settings
        return new Dimension(
                Width + ( m_HorzSpacing * ( NumberOfComponents + 1 ) ),
                LargestHeight + ( 2 * m_VertSpacing ) );
    }

    /*---------------------------------
     * layoutContainer
     *
     * Actually layout the components in the container.
     *-------------------*/
    @Override
    public void layoutContainer( final Container p1 ) {
        // Get the current size of our window
        final Dimension Size = p1.getSize();

        // get the number of components in our Container
        final int NumberOfComponents = p1.getComponentCount();

        // Count the number of components with a large size, these are
        // considered resizeable.
        // And find the width of the rest.
        int NumberOfStretchableComp = 0;
        int preferredWidth = 0;
        for ( int i = 0; i < NumberOfComponents; i++ ) {
            final Dimension cPreferredSize = p1.getComponent( i )
                    .getMaximumSize();

            if ( cPreferredSize.width > Size.width ) {
                // count our resizeable components
                NumberOfStretchableComp++;
            }
            else {
                // sum the width our or small components
                preferredWidth += cPreferredSize.width;
            }
        }

        int WidthOfStretchableComponents = 0;

        // Divide the remaining size up by our resizable components
        if ( NumberOfStretchableComp != 0 ) {
            WidthOfStretchableComponents = (
                    Size.width - preferredWidth
                            - (
                            m_HorzSpacing * (
                                    NumberOfComponents
                                            + 1 ) ) )
                    / NumberOfStretchableComp;
        }

        int LeftSide = m_HorzSpacing;

        // roll through and resize the components
        for ( int i = 0; i < NumberOfComponents; i++ ) {
            final Component c = p1.getComponent( i );

            final Dimension cPreferredSize = c.getMaximumSize();

            // position the component
            c.setLocation( LeftSide, m_VertSpacing );

            if ( cPreferredSize.width > Size.width ) {
                c.setSize( WidthOfStretchableComponents,
                        Size.height - ( 2 * m_VertSpacing ) );
                LeftSide += WidthOfStretchableComponents + m_HorzSpacing;
            }
            else {
                c.setSize( cPreferredSize.width,
                        Size.height - ( 2 * m_VertSpacing ) );
                LeftSide += cPreferredSize.width + m_HorzSpacing;
            }
        }
    }
}
