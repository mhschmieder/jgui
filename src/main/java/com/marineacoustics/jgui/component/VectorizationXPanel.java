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

import com.marineacoustics.jgui.util.VectorSource;
import com.marineacoustics.jgui.util.VectorizationManager;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

/**
 * {@code VectorizationXPanel} is an example of an {@link XPanel} that can
 * vectorize via a {@link Graphics2D} instance, and that can also be composited
 * with other such panels (or derivatives thereof) to complete a full
 * vectorization of the overall panel layout hierarchy contained by this panel.
 * It provides enough basic functionality that not every client application or
 * layout container will need to customize it further through class inheritance.
 * <p>
 * Usually this class will be used for secondary panels within a larger window
 * layout, with the more advanced version used as the main container panel.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class VectorizationXPanel extends XPanel implements VectorizationManager, VectorSource {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = 4020227329798596574L;

    /**
     * Indicator of whether vectorization is active. This flag may be needed in
     * certain {@code paintComponent()} method overrides, as z-buffer
     * activities are often order-dependent and/or the backing image
     * regeneration may need to happen either first or last. Also, some content
     * might need to be excluded from vectorization on a case by case basis.
     */
    private boolean           vectorizationActive;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Default constructor. This is the preferred constructor for this class.
     * <p>
     * Creates a new {@code VectorizationXPanel} instance with a
     * {@code FlowLayout} and a double buffer. Most derived classes will need to
     * set custom layouts.
     * <p>
     * Please note that the double buffering in this case refers strictly to the
     * GUI layout components; the special z-buffering implemented by this
     * extended version of {@link JPanel} is for supporting complex graphics
     * such as charts and large collections of graphical shapes.
     *
     * @since 1.0
     */
    public VectorizationXPanel() {
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
     * Creates a new {@code VectorizationXPanel} instance with a
     * {@code FlowLayout} and the specified buffering strategy. If
     * {@code isDoubleBuffered} is {@code true}, the {@code VectorizationXPanel}
     * will use a double buffer.
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
    public VectorizationXPanel( final boolean isDoubleBuffered ) {
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
     * Creates a new {@code VectorizationXPanel} instance with the specified
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
    public VectorizationXPanel( final LayoutManager layout ) {
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
     * Creates a new {@code VectorizationXPanel} instance with the specified
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
    public VectorizationXPanel( final LayoutManager layout, final boolean isDoubleBuffered ) {
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
        // Vectorization is initially turned off, until requested by the user.
        vectorizationActive = false;
    }

    ////////////// VectorizationManager implementation methods ///////////////

    /**
     * Returns a flag for whether vectorization is active ({@code true} if
     * active, {@code false} if inactive).
     *
     * @return {@code true} if vectorization is active, {@code false} if
     *         inactive
     *
     * @since 1.0
     */
    @Override
    public final boolean isVectorizationActive() {
        return vectorizationActive;
    }

    /**
     * Sets the vectorization active state to active ({@code true}) or inactive
     * ({@code false}.
     *
     * @param active
     *            {@code true} to indicate vectorization is active,
     *            {@code false} if inactive
     *
     * @since 1.0
     */
    @Override
    public final void setVectorizationActive( final boolean active ) {
        vectorizationActive = active;
    }

    ///////////////// VectorSource implementation methods ////////////////////

    /**
     * Returns the status of whether the vectorization succeeded (if
     * {@code true}) or not (if {@code false}).
     * <p>
     * This method vectorizes the vector source, usually via paintComponent().
     * <p>
     * This is the default implementation, which just directs the paint
     * component call to a special {@link Graphics2D} instance that knows about
     * vectorization to a specific graphics file format. Use this implementation
     * when there is no need for special-casing or other customization.
     * <p>
     * This method can be invoked from many contexts, whether from a top-level
     * layout component or a lower-level one (or called multiple times for
     * sub-panels).
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context for vectorizing the
     *            content of this panel
     * @return {@code true} if the export succeeded; {@code false} if it failed
     *
     * @since 1.0
     */
    @Override
    public boolean vectorize( final Graphics2D graphicsContext ) {
        // Default to "not exported" status, in case of unrecoverable errors.
        boolean panelExported = false;

        // Signify that vectorization is now active.
        setVectorizationActive( true );

        try {
            // Paint this Swing component using a potential override of the
            // paintComponent() method that may process the rendering
            // different if it knows that vectorization is active.
            paintComponent( graphicsContext );
            panelExported = true;
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
        finally {
            // Signify that vectorization is no longer active.
            setVectorizationActive( false );
        }

        return panelExported;
    }

    /**
     * Returns the minimum x-coordinate for the source of the vectorization.
     *
     * @return The x-coordinate of the top left corner of the vector source's
     *         bounding box.
     *
     * @since 1.0
     */
    @Override
    public double getVectorSourceMinX() {
        final int vectorSourceMinX = getX();
        return vectorSourceMinX;
    }

    /**
     * Returns the minimum y-coordinate for the source of the vectorization.
     *
     * @return The y-coordinate of the top left corner of the vector source's
     *         bounding box.
     *
     * @since 1.0
     */
    @Override
    public double getVectorSourceMinY() {
        final int vectorSourceMinY = getY();
        return vectorSourceMinY;
    }

    /**
     * Returns the maximum x-coordinate for the source of the vectorization.
     *
     * @return The x-coordinate of the bottom right corner of the vector
     *         source's bounding box.
     *
     * @since 1.0
     */
    @Override
    public double getVectorSourceMaxX() {
        final int vectorSourceMaxX = getX() + getWidth();
        return vectorSourceMaxX;
    }

    /**
     * Returns the maximum y-coordinate for the source of the vectorization.
     *
     * @return The y-coordinate of the bottom right corner of the vector
     *         source's bounding box.
     *
     * @since 1.0
     */
    @Override
    public double getVectorSourceMaxY() {
        final int vectorSourceMaxY = getY() + getHeight();
        return vectorSourceMaxY;
    }

}
