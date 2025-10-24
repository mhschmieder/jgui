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
package com.marineacoustics.jgui.layout;

/**
 * {@code CardManager} is an interface that establishes the contract for what
 * methods an implementing class must provide in order to meet all of the
 * functionality of {@code CardLayout} in a more flexible and extensive way.
 * <p>
 * Note that classes that implement this interface will also need to declare a
 * class variable for {@code CardLayout} in order to provide all of the expected
 * functionality that is contracted by the listed methods.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public interface CardManager {

    /**
     * Sets the number of Cards in the {@code CardLayout}.
     * <p>
     * In order to support dynamic switching of available Cards, it is best to
     * call this method during panel initialization than to set it in a
     * constructor as a final variable.
     *
     * @param numberOfCardsToAllow
     *            The number of Cards to allow; set to zero if negative.
     *
     * @since 1.0
     */
    void setNumberOfCards( final int numberOfCardsToAllow );

    /**
     * Cycles through all the Cards in the {@code CardLayout}, starting from the
     * current Card, to ensure that they have all been pixelated at least once
     * in the session (usually for JPEG export), without having a side effect on
     * the current active Card.
     *
     * @since 1.0
     */
    void cycleCards();

    /**
     * Shows the named Card by switching the current active Card based on the
     * supplied Card Name.
     *
     * @param cardNameToShow
     *            The name of the Card to show. Must be valid.
     *
     * @since 1.0
     */
    void showCard( final String cardNameToShow );

    /**
     * Returns the currently selected Card Name.
     *
     * @return The name of the current selected Card.
     *
     * @since 1.0
     */
    String getCardName();

}
