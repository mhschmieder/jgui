/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
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
package com.mhschmieder.jgui.frame;

import com.mhschmieder.jcontrols.util.ForegroundManager;
import com.mhschmieder.jgui.util.RenderingHintSource;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.RenderingHints;

/**
 * {@code XFrame} is an enhanced frame base class for Swing that adds
 * boilerplate code to reduce copy/paste mistakes and inconsistencies in new
 * projects. It isn't declared as abstract, as often it is enough on its own.
 * <p>
 * Much of the purpose of this class is to better manage initialization order of
 * resource loading and container creation, as well as to normalize menu and
 * tool bar handling and visual features such as background to foreground
 * contrast to avoid masking of graphical elements.
 * <p>
 * This class is skeletal for now, so that the release of all related libraries
 * is not held up any further.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class XFrame extends JFrame implements RenderingHintSource, ForegroundManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = -6381420940841300763L;

    /**
     * Keep a cached copy of the Rendering Hints for this frame.
     */
    private RenderingHints    renderingHints;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the preferred constructor; it may be dangerous to call the other
     * superclass constructors as it could result in incomplete initialization
     * for this expanded base class.
     * <p>
     * The title in this case is the initial title, as this class manages the
     * dirty flag for the frame and modifies the frame title bar accordingly.
     *
     * @param title
     *            The initial title to use for the frame
     * @param resizable
     *            {@code true} if this frame is resizable by the user;
     *            {@code false} otherwise
     * @throws HeadlessException
     *             If {@code GraphicsEnvironment.isHeadless()} returns
     *             {@code true}
     *
     * @version 1.0
     */
    public XFrame( final String title, final boolean resizable ) throws HeadlessException {
        // Always call the superclass constructor first!
        super( title );

        setResizable( resizable );

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initFrame();
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
    }

    /////////////////////// Initialization methods ///////////////////////////

    /**
     * Initializes this frame in an encapsulated way that protects all
     * constructors from run-time exceptions that might prevent instantiation.
     * <p>
     * The method is declared final, as any derived classes should avoid
     * unwanted side effects and simply write their own GUI initialization
     * method that adds any extended behaviour or components to the layout.
     *
     * @since 1.0
     */
    private final void initFrame() {
        // Initialize all class variables, to avoid null pointers at startup.
        initVariables();

        // Layout the frame's content pane, with the tool bar at the top, the
        // main content in the center, and an optional status bar at the bottom.
        //
        // The vertical gap is zero as the tool bar should generally be flush
        // against the main content.
        final Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout( 12, 0 ) );

        // Load and set the main tool bar, if present.
        //
        // This is an empty tool bar out until curated from another project.
        //
        // In that project, a method is called that is overridden by derived
        // classes so that each frame generates its own unique tool bar.
        final JToolBar mainToolBar = new JToolBar();
        contentPane.add( mainToolBar, BorderLayout.PAGE_START );

        // Load the main content panel in the center.
        //
        // This too is a blank panel for now, as the other project's code base
        // will take a bit of time to port to this project. It declares methods
        // that are overridden by derived classes, to create the main content
        // panel for the frame.
        final JPanel mainPanel = new JPanel();
        contentPane.add( mainPanel, BorderLayout.CENTER );

        // The Status Bar has not yet been curated from the other project.
    }

    /**
     * Initializes all class variables, to avoid null pointers at startup.
     * <p>
     * Not all frames have class variables to initialize, but when they do, it
     * should be done right after measuring fonts (which is an expensive
     * operation), so this method is declared and called at the highest level
     * and overridden as necessary. Any overrides should call the super-class.
     *
     * @since 1.0
     */
    protected void initVariables() {
        // This base class does not yet have any class variables to initialize,
        // but this method should be overridden by derived classes so that their
        // variable initialization happens early in the startup sequence.
    }

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
     * Sets the appropriate foreground color for this frame based on the
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
     *            The current background color to apply to this frame
     *
     * @since 1.0
     */
    @Override
    public void setForegroundFromBackground( final Color backColor ) {
        // At the level of the base class, there is nothing to do, as most
        // applications will not want to set the menu bar (in the root pane) or
        // the tool bar (usually added to the top of the content pane, which is
        // below the menu bar in the root pane), to match the background color
        // of the main content for each window or frame.
        //
        // Classes that derive from this class should override this method and
        // use it to cycle through the panels and other components that form its
        // main content, invoking this method on each of them in turn.
    }

}
