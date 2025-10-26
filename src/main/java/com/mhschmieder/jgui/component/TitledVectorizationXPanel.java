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
package com.mhschmieder.jgui.component;

import org.apache.commons.math3.util.FastMath;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

/**
 * {@code TitledVectorizationXPanel} is an example of a top-level Swing
 * {@link JPanel} that can vectorize via a {@link Graphics2D} instance, and that
 * places a header title above the existing panel content when vectorizing to an
 * external graphics file.
 * <p>
 * This also avoids the bloat of an entire GUI layout hierarchy storing the
 * title redundantly when it is only used at top of page.
 * <p>
 * This class will usually be used as the primary panel in a window layout.
 * <p>
 * There is no need to provide standard {code JPanel} and {code CardLayout}
 * based versions of this class, as the former doesn't contract to measure fonts
 * as does {@link XPanel}, and normally a panel that uses {@link CardLayout} is
 * wrapped in at least one more top-level panel container due to anomalies in
 * Swing rendering when a {code CardLayout} is the top-most component in the
 * GUI hierarchy for a particular window or its main content pane.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class TitledVectorizationXPanel extends VectorizationXPanel implements TitleManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID     = -5197086100207909216L;

    /**
     * Guarantee additional padding to prevent the header title from clipping.
     */
    public static final int   TITLE_PADDING_TOP    = 6;

    /**
     * Guarantee additional padding to account for title font descenders.
     */
    public static final int   TITLE_PADDING_BOTTOM = 12;

    /**
     * The header title, to be used during vectorization but not for display.
     */
    protected String          title;

    /**
     * Declare the {@link Font} for the header title.
     */
    protected Font            titleFont;

    /**
     * Declare the {@link FontMetrics} for the header title.
     */
    protected FontMetrics     titleFontMetrics;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Default constructor. This is the preferred constructor for this class.
     * <p>
     * Creates a new {@code TitledVectorizationXPanel} instance with a
     * {@code FlowLayout} and a double buffer. Most derived classes will need to
     * set custom layouts.
     *
     * @since 1.0
     */
    public TitledVectorizationXPanel() {
        // Always call the superclass constructor first!
        super();
    }

    /**
     * Creates a new {@code TitledVectorizationXPanel} instance with a
     * {@code FlowLayout} and the specified buffering strategy. If
     * {@code isDoubleBuffered} is {@code true}, the
     * {@code TitledVectorizationXPanel} will use a double buffer.
     *
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public TitledVectorizationXPanel( final boolean isDoubleBuffered ) {
        // Always call the superclass constructor first!
        super( isDoubleBuffered );
    }

    /**
     * Creates a new {@code TitledVectorizationXPanel} instance with the
     * specified {@link LayoutManager} and a double buffer.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     *
     * @since 1.0
     */
    public TitledVectorizationXPanel( final LayoutManager layout ) {
        // Always call the superclass constructor first!
        super( layout );
    }

    /**
     * Creates a new {@code TitledVectorizationXPanel} instance with the
     * specified {@link LayoutManager} and buffering strategy.
     *
     * @param layout
     *            The {@link LayoutManager} to use for panel layout
     * @param isDoubleBuffered
     *            {@code true} for double-buffering, which uses additional
     *            memory space to achieve fast, flicker-free updates
     *
     * @since 1.0
     */
    public TitledVectorizationXPanel( final LayoutManager layout, final boolean isDoubleBuffered ) {
        // Always call the superclass constructor first!
        super( layout, isDoubleBuffered );
    }

    ////////////////// TitleManager implementation methods ///////////////////

    /**
     * Returns the title that is managed by this panel.
     *
     * @return The title that is managed by this panel
     *
     * @since 1.0
     */
    @Override
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the header title that is managed by the implementing component,
     * trimming for leading and trailing blanks (as those won't show on most
     * vector graphics output pages anyway) and avoiding null pointers by
     * replacing with empty titles.
     *
     * @param newTitle
     *            The header title that is managed by this panel
     *
     * @since 1.0
     */
    @Override
    @SuppressWarnings("nls")
    public final void setTitle( final String newTitle ) {
        // Replace null titles with empty titles, to avoid run-time errors.
        title = ( newTitle != null ) ? newTitle.trim() : "";
    }

    /**
     * Returns the x coordinate of the absolute position of the header title for
     * downstream consumers.
     *
     * @return The x coordinate of the absolute position of the header title for
     *         downstream consumers
     *
     * @since 1.0
     */
    @Override
    public int getTitleX() {
        final int titleOffsetX = getTitleOffsetX();
        final int titleX = getX() + titleOffsetX;
        return titleX;
    }

    /**
     * Returns the y coordinate of the absolute position of the header title for
     * downstream consumers.
     *
     * @return The y coordinate of the absolute position of the header title for
     *         downstream consumers
     *
     * @since 1.0
     */
    @Override
    public int getTitleY() {
        final int titleOffsetY = getTitleOffsetY();
        final int titleY = getY() + titleOffsetY;
        return titleY;
    }

    /**
     * Returns the x coordinate of the relative position of the header title for
     * downstream consumers.
     * <p>
     * This default implementation centers the header title within the overall
     * width of this panel.
     *
     * @return The x coordinate of the relative position of the header title for
     *         downstream consumers
     *
     * @since 1.0
     */
    @Override
    public int getTitleOffsetX() {
        // Center the header title over the entire panel.
        final int titleWidth = titleFontMetrics.stringWidth( title );
        final int titleOffsetX = ( int ) FastMath.round( 0.5d * ( getWidth() - titleWidth ) );
        return titleOffsetX;
    }

    /**
     * Returns the y coordinate of the relative position of the header title for
     * downstream consumers.
     * <p>
     * This default implementation places the header title at the top of the
     * page layout, above the existing content of this panel, accounting for
     * title height as measured by the Font Metrics.
     *
     * @return The y coordinate of the relative position of the header title for
     *         downstream consumers
     *
     * @since 1.0
     */
    @Override
    public int getTitleOffsetY() {
        // Account for text starting at its baseline vs. at its top.
        //
        // Note that we assume a one-line header title with no word wrap.
        final int titleHeight = titleFontMetrics.getHeight();
        final int titleOffsetY = FastMath.round( TITLE_PADDING_TOP + titleHeight );
        return titleOffsetY;
    }

    /**
     * Draws and positions the cached header title via a supplied
     * {@link Graphics2D} Graphics Context.
     * <p>
     * Although the header title is usually just added to file header metadata
     * when targeting vectorization of a GUI component for a vector graphics
     * file format, this method is purposed for physically manifesting the
     * header title at the top of the visible layout of the existing page
     * layout, adding it above the existing on-screen content.
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context to use for drawing the
     *            header title
     *
     * @since 1.0
     */
    @Override
    public void drawTitle( final Graphics2D graphicsContext ) {
        // Cache the current color and font to restore later.
        final Color color = graphicsContext.getColor();
        final Font font = graphicsContext.getFont();

        // Use the standard foreground color with the title font.
        graphicsContext.setColor( getForeground() );
        graphicsContext.setFont( titleFont );

        // Write the header title at the top of the output page layout.
        final int titleX = getTitleX();
        final int titleY = getTitleY();
        graphicsContext.drawString( title, titleX, titleY );

        // Note that due to having to stay within a zeroed bounding box, on
        // account of clipping issues in Acrobat Pro, we instead have to adjust
        // the y-offset by the font height after writing the title, before
        // writing the rest of the page, knowing that this is also accounted for
        // in the overall bounding box computed for the total page output.
        final int titleOffsetY = getTitleOffsetY();
        final int titleAdjustmentY = titleOffsetY + TITLE_PADDING_BOTTOM;
        graphicsContext
                .translate( graphicsContext.getTransform().getTranslateX(),
                            graphicsContext.getTransform().getTranslateY() + titleAdjustmentY );

        // Restore the previous color and font.
        graphicsContext.setColor( color );
        graphicsContext.setFont( font );
    }

    ///////////////// VectorizationXPanel method overrides ///////////////////

    /**
     * Vectorize this panel to a specific Vector Graphics output format.
     * <p>
     * This is an override implementation, which avoids multiple titles due to
     * there usually being multiple sub-panels to export.
     * <p>
     * This method should always be called by the derived classes right away in
     * their own overrides; it simply enforces that the header title is always
     * drawn during Vector Graphics Export, and how it is drawn.
     *
     * @param graphicsContext
     *            The wrapped Graphics Context to vectorize the content to
     * @return The status of whether this export succeeded or not
     */
    @Override
    public boolean vectorize( final Graphics2D graphicsContext ) {
        // Draw the title first. There is no other header information at this
        // time. If any is added later on, it should also be drawn here.
        drawTitle( graphicsContext );

        return true;
    }

    /**
     * Returns the maximum x-coordinate for the source of the vectorization.
     *
     * @return The x-coordinate of the bottom right corner of this panel's
     *         bounding box, adjusted for the prepended title's font height,
     *         along with extra intra-element padding.
     *
     * @since 1.0
     */
    @Override
    public double getVectorSourceMaxY() {
        final int titleOffsetY = getTitleOffsetY();
        final float vectorSourceMaxY = getY() + titleOffsetY + TITLE_PADDING_BOTTOM + getHeight();
        return vectorSourceMaxY;
    }

    //////////////////////// XPanel method overrides /////////////////////////

    /**
     * Measures all of the class-level cached fonts used by this panel.
     *
     * @since 1.0
     */
    @Override
    protected void measureFonts() {
        // We only measure the fonts once, and we do it from initPanel().
        // Use the default font as the basis, for consistent look-and-feel
        // across the application and also to take advantage of the user's
        // preference for GUI Look And Feel themes (e.g. Nimbus vs. Metal).
        if ( titleFont == null ) {
            titleFont = getFont().deriveFont( Font.BOLD, 24f );
        }

        titleFontMetrics = getFontMetrics( titleFont );
    }

}
