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

import com.marineacoustics.jgui.util.RenderingHintSource;
import com.mhschmieder.jcontrols.util.ForegroundManager;
import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * {@code XComponent} is an enhanced panel base class for Swing that adds
 * boilerplate code to reduce copy/paste mistakes and inconsistencies in new
 * projects. It isn't declared as abstract, as often it is enough on its own.
 * <p>
 * Unlike {@code XPanel} this custom class does not deal with z-buffering of
 * background images and the like, as it is assumed that people will use a
 * standard panel for such needs rather than develop their own custom heavy-duty
 * component. The primary purpose of this class is to derive for Table
 * containers that include all of the necessary scrollbars and other component
 * hosting aspects. With enough pushback, I can revisit this decision.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class XComponent extends JComponent implements RenderingHintSource, ForegroundManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = -2759507515205047131L;

    /**
     * Keep a cached copy of the Rendering Hints for this component.
     */
    private RenderingHints    renderingHints;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Default constructor. This is the preferred constructor for this class.
     * <p>
     * Creates a new {@code XComponent} instance with {@link RenderingHints}.
     *
     * @since 1.0
     */
    public XComponent() {
        // Always call the superclass constructor first!
        super();

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initComponent();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    /////////////////////// Initialization methods ///////////////////////////

    /**
     * Initializes this component in an encapsulated way that protects all
     * constructors from run-time exceptions that might prevent instantiation.
     * <p>
     * The method is declared final, as any derived classes should avoid
     * unwanted side effects and simply write their own GUI initialization
     * method that adds any extended behaviour or components to the layout.
     *
     * @since 1.0
     */
    private final void initComponent() {
        // Load all of the JAR-resident resources/images for this component.
        loadResources();

        // Measure the fonts. This is something we want to do only once.
        measureFonts();

        // Initialize all class variables, to avoid null pointers at startup.
        initVariables();
    }

    /**
     * Loads all of the JAR-resident resources and images for this component.
     * <p>
     * Not all components need to load images and/or other resources, but when
     * they do, it should be done immediately, so the method is declared and
     * called at the highest level and overridden as necessary. Any overrides
     * should call their super-class.
     *
     * @since 1.0
     */
    protected void loadResources() {}

    /**
     * Measures all of the class-level cached fonts used by this component.
     * <p>
     * Not all components need to measure fonts, but when they do, it should be
     * done immediately, so the method is declared and called at the highest
     * level and overridden as necessary. Any overrides should call their
     * super-class.
     *
     * @since 1.0
     */
    protected void measureFonts() {}

    /**
     * Initializes all class variables, to avoid null pointers at startup.
     * <p>
     * Not all components have class variables to initialize, but when they do,
     * it should be done right after measuring fonts (which is an expensive
     * operation), so this method is declared and called at the highest level
     * and overridden as necessary. Any overrides should call their super-class.
     *
     * @since 1.0
     */
    protected void initVariables() {}

    ////////////// RenderingHintSource implementation methods ////////////////

    /**
     * Returns the {@link RenderingHints} that are applied to this component.
     *
     * @return
     *         The {@link RenderingHints} that are applied to this component
     *
     * @since 1.0
     */
    @Override
    public final RenderingHints getRenderingHints() {
        return renderingHints;
    }

    /**
     * Sets the {@link RenderingHints} to apply when painting this component.
     * <p>
     * This method applies {@link RenderingHints} to the layout container,
     * usually passed down from a parent component for consistency within a GUI
     * layout hierarchy. Any overrides should call their super-class, and then
     * go about setting the {@link RenderingHints} for all layout components.
     *
     * @param parentRenderingHints
     *            The {@link RenderingHints} to apply to this component
     *
     * @since 1.0
     */
    @Override
    public void setRenderingHints( final RenderingHints parentRenderingHints ) {
        renderingHints = parentRenderingHints;
    }

    /////////////// ForegroundManager implementation methods /////////////////

    /**
     * Sets the appropriate foreground color for this component based on the
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
     *            The current background color to apply to this component
     *
     * @since 1.0
     */
    @Override
    public void setForegroundFromBackground( final Color backColor ) {
        // Make sure the foreground color is never masked by the background.
        final Color foreColor = ColorUtilities.getForegroundFromBackground( backColor );

        // This takes care of the unused parts of the layout, to avoid gaps of
        // the wrong background color between layout regions for subcomponents.
        setBackground( backColor );
        setForeground( foreColor );

        // This clears up problems with background color not being honored by
        // the Mac OS X Look and Feel for Swing when a Spring Layout is used.
        final JRootPane rootPane = getRootPane();
        if ( rootPane != null ) {
            rootPane.setBackground( backColor );
            rootPane.setForeground( foreColor );
        }

        // Always check for the presence of a Titled Border, as it has specific
        // API for matching its title text color to the foreground in use.
        final Border border = getBorder();
        if ( border instanceof TitledBorder ) {
            final TitledBorder titledBorder = ( TitledBorder ) border;
            titledBorder.setTitleColor( foreColor );
        }
    }

    ///////////////////// JComponent method overrides ////////////////////////

    /**
     * Sets the enablement of this component, regarding user input response.
     * <p>
     * This method augments the normal component enablement behavior to
     * account for an oversight by the Swing development team in not accounting
     * for the presence of a {@link TitledBorder}. It proved to be very
     * confusing for users to see the controls disabled inside the bordered
     * layout manager, but for the border itself and its title to be enabled.
     *
     * @param enabled
     *            Flag for whether this component should be enabled or not
     *
     * @since 1.0
     */
    @Override
    public void setEnabled( final boolean enabled ) {
        super.setEnabled( enabled );

        // Workaround for Swing oversight in not accounting for the presence of
        // a {@link TitledBorder} when applying the enabled flag to the current
        // GUI layout hierarchy. We use mid-gray as the disablement cue.
        final Color foreColor = enabled ? getForeground() : Color.GRAY;
        final Border border = getBorder();
        if ( border instanceof TitledBorder ) {
            final TitledBorder titledBorder = ( TitledBorder ) border;
            titledBorder.setTitleColor( foreColor );
        }
    }

    /**
     * Paints this component using the current Graphics Context canvas.
     * <p>
     * This is the preferred repaint method to override and invoke in Swing.
     * <p>
     * The only reason for overriding it here, is so that we can apply the
     * {@link RenderingHints} to the repaint actions.
     * <p>
     * If this is done at initialization time, we have a null Graphics Context.
     *
     * @param graphicsContext
     *            The Graphics Context to use as the canvas for rendering this
     *            component
     *
     * @since 1.0
     */
    @Override
    public void paintComponent( final Graphics graphicsContext ) {
        // Call the "super" class first, to make sure we preserve the
        // look-and-feel of the container that owns this component.
        super.paintComponent( graphicsContext );

        if ( renderingHints != null ) {
            // Set the default Rendering Hints for this component.
            final Graphics2D g2 = ( Graphics2D ) graphicsContext;
            g2.addRenderingHints( renderingHints );
        }
    }

}
