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

import com.marineacoustics.jgui.layout.CardManager;
import com.mhschmieder.jgraphics.color.ColorUtilities;
import org.apache.commons.math3.util.FastMath;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.LayoutManager;

/**
 * {@code CardXPanel} is a custom {@link XPanel} that wraps the usage of
 * {@link CardLayout} in a way that addresses most of its deficiencies, such as
 * not caching the current Card Name, and its lack of flexibility in more
 * extensive GUI layout hierarchies.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class CardXPanel extends XPanel implements CardManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = -8906522526548892969L;

    /**
     * The Card sub-panel allows us to better control overall layout
     * combinations in inherited classes, than if we apply the
     * {@link CardLayout} to the overall panel.
     */
    protected JPanel          cardSubpanel;

    /**
     * We have to locally cache the {@link CardLayout} as it isn't set on the
     * main panel and thus is more efficient to access this way than via the
     * Card sub-panel when we need to cycle the cards.
     */
    protected CardLayout      cardLayout;

    /**
     * Keep track of how many cards there are, so we can cycle when necessary.
     */
    private int               numberOfCards;

    /**
     * Cache the current selected Card Name, as the {@link CardLayout} API
     * provides no way to query this.
     */
    private String            cardName;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Default constructor. This is the preferred constructor for this class.
     * <p>
     * Creates a new {@code CardXPanel} instance with a {@code FlowLayout} and a
     * double buffer. Most derived classes will need to set custom layouts.
     *
     * @since 1.0
     */
    public CardXPanel() {
        // Always call the superclass constructor first!
        super();

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initPanel();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@code CardXPanel} instance with a {@code FlowLayout} and
     * the specified buffering strategy. If {@code isDoubleBuffered} is
     * {@code true}, the {@code CardXPanel} will use a double buffer.
     *
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public CardXPanel( final boolean isDoubleBuffered ) {
        // Always call the superclass constructor first!
        super( isDoubleBuffered );

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initPanel();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@code CardXPanel} instance with the specified
     * {@link LayoutManager} and a double buffer.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     *
     * @since 1.0
     */
    public CardXPanel( final LayoutManager layout ) {
        // Always call the superclass constructor first!
        super( layout );

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initPanel();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@code CardXPanel} instance with the specified
     * {@link LayoutManager} and buffering strategy.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public CardXPanel( final LayoutManager layout, final boolean isDoubleBuffered ) {
        // Always call the superclass constructor first!
        super( layout, isDoubleBuffered );

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initPanel();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
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
     * @since 1.0
     */
    private final void initPanel() {
        // Establish use of the Card Layout for the card sub-panel.
        cardSubpanel = new JPanel();
        cardLayout = new CardLayout();
        cardSubpanel.setLayout( cardLayout );

        // Layout the main panel with its components, using the Border Layout
        // to enable a custom card-switching Toolbar to display at the top.
        setLayout( new BorderLayout( 0, 0 ) );
        setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );
        add( cardSubpanel, BorderLayout.CENTER );
    }

    ////////////////// CardManager implementation methods ////////////////////

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
    @Override
    public void setNumberOfCards( final int numberOfCardsToAllow ) {
        numberOfCards = FastMath.max( numberOfCardsToAllow, 0 );
    }

    /**
     * Cycles through all the Cards in the {@code CardLayout}, starting from the
     * current Card, to ensure that they have all been pixelated at least once
     * in the session (usually for JPEG export), without having a side effect on
     * the current active Card.
     *
     * @since 1.0
     */
    @Override
    public final void cycleCards() {
        // Cycle through as many cards as we have in the Card sub-panel.
        for ( int i = 0; i < numberOfCards; i++ ) {
            cardLayout.next( cardSubpanel );
        }
    }

    /**
     * Shows the named Card by switching the current active Card based on the
     * supplied Card Name.
     *
     * @param cardNameToShow
     *            The name of the Card to show. Must be valid.
     *
     * @since 1.0
     */
    @Override
    public void showCard( final String cardNameToShow ) {
        // Show the specified card.
        cardLayout.show( cardSubpanel, cardNameToShow );
    }

    /**
     * Return the currently selected Card Name.
     *
     * @return The name of the current selected Card.
     *
     * @since 1.0
     */
    @Override
    public final String getCardName() {
        return cardName;
    }

    //////////////////////// XPanel method overrides /////////////////////////

    /**
     * Initializes all class variables, to avoid null pointers at startup.
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    @Override
    protected void initVariables() {
        // Always call the superclass first!
        super.initVariables();

        // This is defensive programming in case inherited classes forget to
        // initialize the Card Name or fail to make an initial Card selection.
        cardName = "";
    }

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
     *            The current background color to apply to this panel
     *
     * @since 1.0
     */
    @Override
    public void setForegroundFromBackground( final Color backColor ) {
        // Always call the superclass first!
        super.setForegroundFromBackground( backColor );

        // Make sure the foreground color is never masked by the background.
        final Color foreColor = ColorUtilities.getForegroundFromBackground( backColor );

        // Forward this method to the subcomponents.
        cardSubpanel.setBackground( backColor );
        cardSubpanel.setForeground( foreColor );

        // Any change to background requires regenerating the off-screen buffer.
        regenerateOffScreenImage = true;
    }

}
