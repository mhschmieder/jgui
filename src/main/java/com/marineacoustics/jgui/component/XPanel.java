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

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * {@code XPanel} is an enhanced panel base class for Swing that adds
 * boilerplate code to reduce copy/paste mistakes and inconsistencies in new
 * projects. It isn't declared as abstract, as often it is enough on its own.
 * <p>
 * This class is especially designed for downstream clients that do a lot of
 * heavy graphics and/or charting, as it sets up some methodologies for dealing
 * with z-buffer issues for background images and different paint order for
 * various vector graphics export formats, the most notoriously picky being EPS
 * due to its lack of support for alpha compositing and other transparencies.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class XPanel extends JPanel
        implements RenderingHintSource, ForegroundManager, ZBufferManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = 8162753813950838670L;

    /**
     * Keep a cached copy of the Rendering Hints for this panel.
     */
    private RenderingHints    renderingHints;

    /**
     * This flag indicates to the {@code paintComponent} method that the
     * off-screen z-buffer needs to be regenerated. It is a manual dirty flag
     * for layout rendering, and graphics output is composited manually as well.
     * <p>
     * Off-screen z-buffering is the responsibility of the client, as it would
     * not otherwise be possible to guarantee correct compositing of downstream
     * graphics operations, whether for on-screen rendering or graphics output.
     */
    protected boolean         regenerateOffScreenImage;

    /**
     * The off-screen image is used for accelerating graphics using traditional
     * off-screen z-buffering.
     * <p>
     * Off-screen z-buffering is the responsibility of the client, as it would
     * not otherwise be possible to guarantee correct compositing of downstream
     * graphics operations, whether for on-screen rendering or EPS Export.
     */
    protected BufferedImage   offScreenImage;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Default constructor. This is the preferred constructor for this class.
     * <p>
     * Creates a new {@code XPanel} instance with a {@code FlowLayout} and a
     * double buffer. Most derived classes will need to set custom layouts.
     * <p>
     * Please note that the double buffering in this case refers strictly to the
     * GUI layout components; the special z-buffering implemented by this
     * extended version of {@link JPanel} is for supporting complex graphics
     * such as charts and large collections of graphical shapes.
     *
     * @since 1.0
     */
    public XPanel() {
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
     * Creates a new {@code XPanel} instance with a {@code FlowLayout} and the
     * specified buffering strategy. If {@code isDoubleBuffered} is
     * {@code true}, the {@code XPanel} will use a double buffer.
     * <p>
     * Please note that the double buffering in this case refers strictly to the
     * GUI layout components; the special z-buffering implemented by this
     * extended version of {@link JPanel} is for supporting complex graphics
     * such as charts and large collections of graphical shapes.
     *
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public XPanel( final boolean isDoubleBuffered ) {
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
     * Creates a new {@code XPanel} instance with the specified
     * {@link LayoutManager} and a double buffer.
     * <p>
     * Please note that the double buffering in this case refers strictly to the
     * GUI layout components; the special z-buffering implemented by this
     * extended version of {@link JPanel} is for supporting complex graphics
     * such as charts and large collections of graphical shapes.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     *
     * @since 1.0
     */
    public XPanel( final LayoutManager layout ) {
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
     * Creates a new {@code XPanel} instance with the specified
     * {@link LayoutManager} and buffering strategy.
     * <p>
     * Please note that the double buffering in this case refers strictly to the
     * GUI layout components; the special z-buffering implemented by this
     * extended version of {@link JPanel} is for supporting complex graphics
     * such as charts and large collections of graphical shapes.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public XPanel( final LayoutManager layout, final boolean isDoubleBuffered ) {
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
        // Load all of the JAR-resident resources/images for this panel.
        loadResources();

        // Measure the fonts. This is something we want to do only once.
        measureFonts();

        // Initialize all class variables, to avoid null pointers at startup.
        initVariables();
    }

    /**
     * Loads all of the JAR-resident resources and images for this panel.
     * <p>
     * Not all panels need to load images and/or other resources, but when they
     * do, it should be done immediately, so the method is declared and called
     * at the highest level and overridden as necessary. Any overrides should
     * call the super-class.
     *
     * @since 1.0
     */
    protected void loadResources() {}

    /**
     * Measures all of the class-level cached fonts used by this panel.
     * <p>
     * Not all panels need to measure fonts, but when they do, it should be done
     * immediately, so the method is declared and called at the highest level
     * and overridden as necessary. Any overrides should call the super-class.
     *
     * @since 1.0
     */
    protected void measureFonts() {}

    /**
     * Initializes all class variables, to avoid null pointers at startup.
     * <p>
     * Not all panels have class variables to initialize, but when they do, it
     * should be done right after measuring fonts (which is an expensive
     * operation), so this method is declared and called at the highest level
     * and overridden as necessary. Any overrides should call the super-class.
     *
     * @since 1.0
     */
    protected void initVariables() {
        // Make sure we initially generate the off-screen z-buffer.
        regenerateOffScreenImage = true;
    }

    ////////////////////// Model/View syncing methods ////////////////////////

    /**
     * Returns the status of the view-to-model syncing ({@code true} if the data
     * changed) after performing it hierarchically on the full panel layout.
     * <p>
     * As not all derived classes have data to sync, declaring this as abstract
     * is the wrong approach, so we provide a default implementation that says
     * no data changed.
     *
     * @return {@code true} if the model changed after syncing it from the view
     *
     * @since 1.0
     */
    @SuppressWarnings("static-method")
    public boolean updateModel() {
        return false;
    }

    /**
     * Syncs the view to the data model hierarchically on the full panel layout.
     * <p>
     * As not all derived classes have data to sync, declaring this as abstract
     * is the wrong approach, so we provide a default implementation that is a
     * no-op.
     *
     * @since 1.0
     */
    public void updateView() {}

    ////////////// RenderingHintSource implementation methods ////////////////

    /**
     * Returns the {@link RenderingHints} that are applied to this panel.
     *
     * @return
     *         The {@link RenderingHints} that are applied to this panel
     *
     * @since 1.0
     */
    @Override
    public final RenderingHints getRenderingHints() {
        return renderingHints;
    }

    /**
     * Sets the {@link RenderingHints} to apply when painting this panel.
     * <p>
     * This method applies {@link RenderingHints} to the layout container,
     * usually passed down from a parent component for consistency within a GUI
     * layout hierarchy. Any overrides should call their super-class, and then
     * go about setting the {@link RenderingHints} for all layout components.
     *
     * @param parentRenderingHints
     *            The {@link RenderingHints} to apply to this panel
     *
     * @since 1.0
     */
    @Override
    public void setRenderingHints( final RenderingHints parentRenderingHints ) {
        renderingHints = parentRenderingHints;
    }

    /////////////// ForegroundManager implementation methods /////////////////

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

        // Any change to background requires regenerating the off-screen buffer.
        regenerateOffScreenImage = true;
    }

    ///////////////// ZBufferManager implementation methods //////////////////

    /**
     * Returns an off-screen z-buffer to double-buffer the graphics image.
     * <p>
     * The {@link Graphics2D} canvas to use for off-screen z-buffering, may in
     * some cases be the one that was passed in, based on the status of several
     * internal flags that keep track of z-buffering.
     *
     * @param graphicsContext
     *            A Graphics Context reference.
     * @return A Graphics Context to use for the off-screen z-buffer.
     *
     * @since 1.0
     */
    @Override
    public final Graphics2D createGraphics( final Graphics graphicsContext ) {
        // Determine whether zoom conditions require image regeneration.
        resetOffScreenImages();

        // Create a graphics context and update graphics only if changes have
        // occurred, as signaled by the "regenerate off-screen image" flag.
        // Otherwise, just re-display the current off-screen z-buffer.
        Graphics2D graphics2D = null;

        if ( regenerateOffScreenImage ) {
            // Create a Graphics Context for this off-screen image
            if ( offScreenImage != null ) {
                graphics2D = offScreenImage.createGraphics();
            }
            if ( graphics2D != null ) {
                // Initialize the off-screen canvas.
                initializeOffScreenCanvas( graphics2D );
            }
        }

        // Now that we have (conditionally) generated a new off-screen buffer,
        // there is no need to regenerate it until external conditions require.
        regenerateOffScreenImage = false;

        return graphics2D;
    }

    /**
     * This method either shows the new background image, or re-displays the
     * old background image, depending on whether the cached image is null or
     * not. If null, show the new background image, otherwise show then old.
     *
     * @param graphicsContext
     *            A Graphics Context to use for showing the background image
     *
     * @since 1.0
     */
    @Override
    public final void showBackgroundImage( final Graphics graphicsContext ) {
        if ( offScreenImage != null ) {
            final Graphics2D g2 = ( Graphics2D ) graphicsContext;

            final Composite composite = g2.getComposite();
            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) );
            g2.drawImage( offScreenImage, 0, 0, this );
            g2.setComposite( composite );
        }
    }

    /**
     * Reset all off-screen images based on zooming, and other factors.
     *
     * @since 1.0
     */
    @Override
    public final void resetOffScreenImages() {
        // Get current component size on screen.
        final Dimension userAreaSize = getSize();

        // Create new off-screen z-buffer if necessary.
        if ( ( offScreenImage == null ) || ( offScreenImage.getWidth() != userAreaSize.width )
                || ( offScreenImage.getHeight() != userAreaSize.height ) ) {
            offScreenImage =
                           ( BufferedImage ) createImage( userAreaSize.width, userAreaSize.height );
            regenerateOffScreenImage = true;
        }
    }

    /**
     * This method initializes the off-screen canvas associated with a
     * supplied Graphics Context, for future rendering.
     *
     * @param graphics2D
     *            The Graphics Context to initialize for off-screen rendering
     *
     * @since 1.0
     */
    @Override
    public final void initializeOffScreenCanvas( final Graphics2D graphics2D ) {
        // Get current component size on screen.
        final Dimension userAreaSize = getSize();

        // Clear the off-screen canvas; otherwise zooming overlays rather than
        // replaces.
        graphics2D.setBackground( getBackground() );
        graphics2D.setPaintMode();
        graphics2D.clearRect( 0, 0, userAreaSize.width, userAreaSize.height );
    }

    ///////////////////// JComponent method overrides ////////////////////////

    /**
     * Sets the enablement of this panel, regarding user input response.
     * <p>
     * This method augments the normal panel enablement behavior to account for
     * an oversight by the Swing development team in not accounting for the
     * presence of a {@link TitledBorder}. It proved to be very confusing for
     * users to see the controls disabled inside the bordered layout manager,
     * but for the border itself and its title to be enabled.
     *
     * @param enabled
     *            Flag for whether this panel should be enabled or not
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
     * Paints this panel using the current Graphics Context canvas.
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
     *            panel
     *
     * @since 1.0
     */
    @Override
    public void paintComponent( final Graphics graphicsContext ) {
        // Call the "super" class first, to make sure we preserve the
        // look-and-feel of the container that owns this panel.
        super.paintComponent( graphicsContext );

        if ( renderingHints != null ) {
            // Set the default Rendering Hints for this panel.
            final Graphics2D g2 = ( Graphics2D ) graphicsContext;
            g2.addRenderingHints( renderingHints );
        }
    }

}
