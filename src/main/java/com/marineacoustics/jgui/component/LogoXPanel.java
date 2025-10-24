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

import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

/**
 * {@code LogoXPanel} is a special layout panel that handles some of the issues
 * that arise when hosting company logos within a GUI. Primarily it deals with
 * image-switching when the GUI background switches between light and dark
 * background colors. For this reason, it derives from {@link XPanel}. It is up
 * to the client to produce two contrasting images; usually this means white
 * text against a dark background and black text against a light background.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class LogoXPanel extends XPanel {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = 3736056155127323883L;

    /**
     * This {@link ImageIcon} contains the logo to use for dark GUI backgrounds.
     */
    private final ImageIcon   logoForDarkBackground;

    /**
     * This {@link ImageIcon} contains the logo to use for light GUI
     * backgrounds.
     */
    private final ImageIcon   logoForLightBackground;

    /**
     * Use a {@link JLabel} to host the active {@link ImageIcon} containing the
     * Logo image, pre-loaded by the client so that they have complete
     * flexibility in whether to construct it or load it from a JAR resource.
     */
    private JLabel            logoLabel;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Fully qualified constructor.
     * <p>
     * This is the only exposed constructor; the constructors on the parent
     * class and superclass should not be used as they will fail to assign the
     * {@link ImageIcon} for the Logo and thus result in a blank panel.
     *
     * @param darkBackgroundImageIcon
     *            The {@link ImageIcon} to use as the Logo for dark GUI
     *            backgrounds
     * @param lightBackgroundImageIcon
     *            The {@link ImageIcon} to use as the Logo for light GUI
     *            backgrounds
     * @param leftMargin
     *            The margin to leave within the {@link JLabel} host for the
     *            Logo on its left side
     * @param rightMargin
     *            The margin to leave within the {@link JLabel} host for the
     *            Logo on its right side
     *
     * @since 1.0
     */
    public LogoXPanel( final ImageIcon darkBackgroundImageIcon,
                       final ImageIcon lightBackgroundImageIcon,
                       final int leftMargin,
                       final int rightMargin ) {
        // Always call the superclass constructor first!
        super();

        logoForDarkBackground = darkBackgroundImageIcon;
        logoForLightBackground = lightBackgroundImageIcon;

        try {
            initPanel( leftMargin, rightMargin );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    /////////////////////// Initialization methods ///////////////////////////

    /**
     * Initializes this panel in an encapsulated way that protects all
     * constructors from run-time exceptions that might prevent instantiation.
     * <p>
     * The method is declared final, as any derived classes should avoid
     * unwanted side effects and simply write their own GUI initialization
     * method that adds any extended behaviour or components to the layout.
     *
     * @param leftMargin
     *            The margin to leave within the {@link JLabel} host for the
     *            Logo on its left side
     * @param rightMargin
     *            The margin to leave within the {@link JLabel} host for the
     *            Logo on its right side
     *
     * @since 1.0
     */
    private final void initPanel( final int leftMargin, final int rightMargin ) {
        // Make sure the Logo Label doesn't have a beveled border, as this
        // causes white vertical lines on the left and right edges of the icon.
        logoLabel = new JLabel();
        logoLabel.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );

        // For derived classes that add a Copyright Notice or simple Copyright
        // Label, place the Logo Label at the top of the layout so that room can
        // be made if necessary. Many Logo Images already contain a Copyright.
        logoLabel.setHorizontalAlignment( SwingConstants.LEFT );
        logoLabel.setVerticalAlignment( SwingConstants.TOP );

        // Layout the Logo Panel with with its components. A vertical Box Layout
        // is used so that derived classes can easily add a Copyright Notice
        // below the Logo Label if necessary.
        setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) );
        setBorder( BorderFactory.createEmptyBorder( 10, leftMargin, 10, rightMargin ) );
        add( logoLabel );

        // This panel needs to be given an initial size, or else other
        // components tend to add too much space.
        setPreferredSize( new Dimension( 280, 100 ) );
    }

    //////////////////////// XPanel method overrides /////////////////////////

    /**
     * Sets the appropriate foreground color for this panel based on the
     * specified background color.
     * <p>
     * Both the background and the foreground are applied to the entire layout
     * hierarchy, with the foreground color chosen to provide adequate contrast
     * against the background for text rendering as well as for line graphics.
     * <p>
     * This method should be overridden and called as the first line in the
     * method override, before adding support for GUI elements unique to the
     * derived class hierarchy.
     *
     * @param backColor
     *            The current background color to apply to the layout hierarchy
     *
     * @since 1.0
     */
    @Override
    public final void setForegroundFromBackground( final Color backColor ) {
        // Always call the superclass first!
        super.setForegroundFromBackground( backColor );

        // Make sure the foreground color is never masked by the background.
        final Color foreColor = ColorUtilities.getForegroundFromBackground( backColor );

        // Switch the active Logo based on this panel's background color.
        //
        // We do this before setting the Label's background, to make sure the
        // Logo doesn't overwrite the borders of the Label container.
        final ImageIcon logoIcon = ColorUtilities.isColorDark( backColor )
            ? logoForDarkBackground
            : logoForLightBackground;
        logoLabel.setIcon( logoIcon );

        // By setting the Label's background and foreground last, we can
        // effectively cleanly clip the image if it extends a pixel too far.
        logoLabel.setBackground( backColor );
        logoLabel.setForeground( foreColor );

        // Any change to background requires regenerating the off-screen buffer.
        regenerateOffScreenImage = true;
    }

}// class LogoXPanel
