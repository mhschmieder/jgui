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
package com.marineacoustics.jgui.util;

import java.awt.Graphics2D;

/**
 * {@code VectorSource} is an interface that establishes the contract for
 * interacting with components that support vectorization. In most cases this
 * means a Swing or AWT component, as there is a dependency on a
 * {@link Graphics2D} Graphics Context to do the actual vectorization.
 * <p>
 * The purpose of providing this interface, is to allow for compositing of the
 * vector output from several different input sources, with manual or automatic
 * gaps (via output page translations and/or scaling) between them, making use
 * of the same {@link Graphics2D} instance. This increases the flexibility of
 * the library beyond just doing all-at-once single-stream file output.
 * <p>
 * For flexibility, implement this interface on the base class for all of your
 * graphics oriented panels, and have derived panel classes forward their own
 * method overrides to those of the sub-panels in those component's layouts.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public interface VectorSource {

    /**
     * Returns the status of whether the vectorization succeeded (if
     * {@code true}) or not (if {@code false}).
     * <p>
     * This method vectorizes the vector source, usually via paintComponent().
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context for vectorizing the
     *            content of the object that implements this interface
     * @return {@code true} if the export succeeded; {@code false} if it failed
     *
     * @since 1.0
     */
    boolean vectorize( final Graphics2D graphicsContext );

    /**
     * Returns the minimum x-coordinate for the source of the vectorization.
     * <p>
     * Although this more often than not just relates to the {@code getX()} call
     * on the implementing component, this method allows for context-specific
     * offsets that may account for additional restrictions or additions (such
     * as a page title) during vectorization.
     *
     * @return The x-coordinate of the top left corner of the vector source's
     *         bounding box.
     *
     * @since 1.0
     */
    double getVectorSourceMinX();

    /**
     * Returns the minimum y-coordinate for the source of the vectorization.
     * <p>
     * Although this more often than not just relates to the {@code getY()} call
     * on the implementing component, this method allows for context-specific
     * offsets that may account for additional restrictions or additions (such
     * as a page title) during vectorization.
     *
     * @return The y-coordinate of the top left corner of the vector source's
     *         bounding box.
     *
     * @since 1.0
     */
    double getVectorSourceMinY();

    /**
     * Returns the maximum x-coordinate for the source of the vectorization.
     * <p>
     * Although this more often than not just relates to the {@code getWidth()}
     * call on the implementing component, this method allows for
     * context-specific offsets that may account for additional restrictions or
     * additions (such as a page title) during vectorization.
     *
     * @return The x-coordinate of the bottom right corner of the vector
     *         source's bounding box.
     *
     * @since 1.0
     */
    double getVectorSourceMaxX();

    /**
     * Returns the maximum y-coordinate for the source of the vectorization.
     * <p>
     * Although this more often than not just relates to the {@code getHeight()}
     * call on the implementing component, this method allows for
     * context-specific offsets that may account for additional restrictions or
     * additions (such as a page title) during vectorization.
     *
     * @return The y-coordinate of the bottom right corner of the vector
     *         source's bounding box.
     *
     * @since 1.0
     */
    double getVectorSourceMaxY();

}
