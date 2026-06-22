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
 * This is the same as the box layout except we allow padding between the boxes.
 */
public class PaddedBoxLayout implements LayoutManager {

    // The number of pixels Above and below each component
    int m_VertSpacing = 0;

    // The number of pixels in between each component
    int m_HorzSpacing = 0;

    /*---------------------------------
     * Creates new PaddedBoxLayout
     *-------------------*/
    public PaddedBoxLayout(int vertSpacing, int horzSpacing) {
        m_VertSpacing = vertSpacing;
        m_HorzSpacing = horzSpacing;
    }

    /*---------------------------------
     * preferredLayoutSize
     *
     * The maximum preferred width is
     * the preferred width of the container.
     *
     * The preferred height of the container
     * is all the preferred heights of all the components.
     *-------------------*/
    public Dimension preferredLayoutSize(Container p1) {
        int componentCount = p1.getComponentCount();

        int height = 0;
        int largestWidth = 0;
        for(int i = 0; i < componentCount; i++) {
            Component component = p1.getComponent(i);

            //don't bother counting components that are invisible
            if(!component.isVisible()) {
                continue;
            }

            //Get the preferred size of this component
            Dimension preferredSize = component.getPreferredSize();

            // Loook for the single largest height
            if(preferredSize.width > largestWidth) {
                largestWidth = preferredSize.width;
            }

            //Add all the widths
            height += preferredSize.height;

        }

        // The final dimensions need to include our spacing settings
        return new Dimension(
                largestWidth + 2 *m_HorzSpacing,
                height + m_VertSpacing* (componentCount +1) );
    }

    /*--------------------
     *removeLayoutComponent
     *
     * We don't hold anything specific about each component
     * so we don't need to do anything here
     *-------------------*/
    public void removeLayoutComponent(Component p1) {
    }

    /*---------------------------------
     * minimumLayoutSize
     *
     * The minimum Height is simply the addition of all the minimum widths.
     * The Width is the largest minimum height.
     *-------------------*/
    public Dimension minimumLayoutSize(Container p1) {
        //Get the number of components in our container
        int componentCount = p1.getComponentCount();

        int height = 0;
        int largestWidth = 0;
        for(int i = 0; i < componentCount; i++) {
            Component component = p1.getComponent(i);

            //don't bother counting components that are invisible
            if(!component.isVisible()) {
                continue;
            }

            Dimension minimumSize = component.getMinimumSize();

            //Find the largest minimum width
            if(minimumSize.width > largestWidth) {
                largestWidth = minimumSize.width;
            }

            //Add together all the widths
            height += minimumSize.height;
        }

        // The final dimensions need to include our spacing settings
        return new Dimension(
                largestWidth + 2* m_HorzSpacing,
                height + m_VertSpacing * (componentCount +1));
    }

    /*--------------------
     *addLayoutComponent
     *
     * We don't hold anything specific about each component
     * so we don't need to do anything here
     *-------------------*/
    public void addLayoutComponent(String p1,Component p2) {
    }

    /*---------------------------------
     * layoutContainer
     *
     * Actually layout the components in the container.
     *-------------------*/
    public void layoutContainer(Container p1) {
        //Get the current size of our window
        Dimension size = p1.getSize();

        //get the number of components in our Container
        int componentCount = p1.getComponentCount();

        int topOfComponent = m_VertSpacing;

        // roll through a resize the components
        for(int i = 0; i < componentCount; i++) {
            Component c = p1.getComponent(i);

            //don't bother counting components that are invisible
            if(!c.isVisible()) {
                continue;
            }

            Dimension preferredSize = c.getPreferredSize();

            //position the component
            c.setLocation(m_HorzSpacing,topOfComponent);

            c.setSize(size.width - 2*m_HorzSpacing ,preferredSize.height);

            topOfComponent += preferredSize.height + m_VertSpacing;
        }
    }
}
