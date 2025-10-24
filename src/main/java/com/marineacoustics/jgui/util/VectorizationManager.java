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

/**
 * {@code VectorizationManager} is an interface that establishes the contract
 * for components signifying their awareness of vectorization being active
 * within the Swing or AWT GUI layout hierarchy to which the interface
 * implementing component belongs.
 * <p>
 * The purpose of providing this interface, is to account for GUI components
 * that don't directly export to graphics formats but which may need to exclude
 * or re-order some of their repaint actions due to some peculiarities of how
 * overlay of graphics and images behaves in vector output {@code Graphics2D}
 * overrides vs. what is seen in AWT and Swing screen output.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public interface VectorizationManager {

    /**
     * Return a flag for whether vectorization is active ({@code true}) or not
     * ({@code false}).
     *
     * @return {@code true} if vectorization is active, {@code false} if
     *         inactive
     *
     * @since 1.0
     */
    boolean isVectorizationActive();

    /**
     * Sets (if active) or clears (if inactive) the vectorization active state.
     *
     * @param active
     *            {@code true} to indicate vectorization is active,
     *            {@code false} if inactive
     *
     * @since 1.0
     */
    void setVectorizationActive( final boolean active );

}
