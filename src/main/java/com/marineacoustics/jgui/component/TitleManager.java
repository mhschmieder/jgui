/**
 * MIT License
 *
 * Copyright (c) 2020, 2022 Mark Schmieder
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

import java.awt.Graphics2D;

/**
 * {@code TitleManager} is an interface that establishes the contract for
 * components that are top-most in the GUI layout hierarchy for purposes of
 * vectorization, so that only one component in each GUI layout hierarchy needs
 * to store the title and associated fonts as well as knowing where to position
 * the title relative to the on-screen components when going to vector output.
 * <p>
 * The purpose of providing this interface, is to decouple the functionality
 * related to title handling, from the rest of vector output, so that it only
 * needs to be implemented in a few places.
 * <p>
 * This functionality might be augmented at some point to add support for
 * styled character data using the {@code TextLayout} class in AWT.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public interface TitleManager {

    /**
     * Returns the title that is managed by the implementing component.
     *
     * @return The title that is managed by the implementing component
     *
     * @since 1.0
     */
    String getTitle();

    /**
     * Sets the title that is managed by the implementing component, trimming
     * for leading and trailing blanks (as they won't show on most vector
     * graphics output pages anyway) and avoiding null pointers by replacing
     * with empty titles.
     *
     * @param newTitle
     *            The title that is managed by the implementing component
     *
     * @since 1.0
     */
    void setTitle( final String newTitle );

    /**
     * Returns the x coordinate of the absolute position of the title for
     * downstream consumers.
     *
     * @return The x coordinate of the absolute position of the title for
     *         downstream consumers
     *
     * @since 1.0
     */
    int getTitleX();

    /**
     * Returns the y coordinate of the absolute position of the title for
     * downstream consumers.
     *
     * @return The y coordinate of the absolute position of the title for
     *         downstream consumers
     *
     * @since 1.0
     */
    int getTitleY();

    /**
     * Returns the x coordinate of the relative position of the title for
     * downstream consumers.
     * <p>
     * Implementing classes will consider whether to center or justify the
     * title, or to draw it in place sans adjustments.
     *
     * @return The x coordinate of the relative position of the title for
     *         downstream consumers
     *
     * @since 1.0
     */
    int getTitleOffsetX();

    /**
     * Returns the y coordinate of the relative position of the title for
     * downstream consumers.
     * <p>
     * As text positioning is from the baseline, this contracts implementing
     * classes to calculate Font Metrics involved.
     *
     * @return The y coordinate of the relative position of the title for
     *         downstream consumers
     *
     * @since 1.0
     */
    int getTitleOffsetY();

    /**
     * Draws and positions the cached title via a supplied {@link Graphics2D}
     * Graphics Context.
     * <p>
     * Although the title is usually just added to file header metadata when
     * targeting vectorization of a GUI component for a vector graphics file
     * format, this method is purposed for physically manifesting the title at
     * the top of the visible layout of the existing page layout, adding it
     * above the existing on-screen content.
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context to use for drawing the
     *            title
     *
     * @since 1.0
     */
    void drawTitle( final Graphics2D graphicsContext );

}
