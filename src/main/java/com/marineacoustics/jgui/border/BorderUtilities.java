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
package com.marineacoustics.jgui.border;

import com.mhschmieder.jgraphics.color.ColorConstants;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;

/**
 * {@code BorderUtilities} is a utility class for working more effectively and
 * consistently with custom borders.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class BorderUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private BorderUtilities() {}

    /**
     * Returns a {@link TitledBorder} that is stylized to stand out well in
     * almost any layout style, and background and foreground color combination,
     * while also being muted enough to not serve as a visual distraction.
     * <p>
     * This is strictly a convenience method to enforce a style guideline across
     * an application without having to be concerned about copy/paste
     * inconsistencies over time. It is trivial to write similar functions that
     * serve the same purpose but which present a different style.
     * <p>
     * This particular set of parameters should be Nimbus-compatible.
     *
     * @param title
     *            The title to apply to the border decoration
     * @return A {@link TitledBorder} that is stylized to stand out well
     *
     * @since 1.0
     */
    public static TitledBorder makeTitledBorder( final String title ) {
        final EtchedBorder etchedBorder = new EtchedBorder( EtchedBorder.RAISED,
                                                            Color.WHITE,
                                                            ColorConstants.GRAY55 );
        final TitledBorder titledBorder = BorderFactory.createTitledBorder( etchedBorder, title );
        return titledBorder;
    }

}
