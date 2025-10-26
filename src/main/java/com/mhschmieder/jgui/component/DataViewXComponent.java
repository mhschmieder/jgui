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

import com.mhschmieder.jcontrols.control.XTable;
import com.mhschmieder.jcontrols.table.DataViewCellRenderer;
import com.mhschmieder.jcontrols.table.DataViewTableModel;
import com.mhschmieder.jcontrols.table.TableVectorizationUtilities;
import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.HashSet;

/**
 * {@code DataViewXComponent} is an abstract base class that serves as a
 * specialization of {@link XComponent} that wraps a data table in appropriate
 * layout and with expected behavior for primary application content components.
 * <p>
 * This component has a specific style applied, for blending in with the
 * background (as opposed to usual black text on white background) and skipping
 * the horizontal and vertical grid lines. Additionally, there is no header.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public abstract class DataViewXComponent extends XComponent {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long          serialVersionUID = 1808435503678141938L;

    /**
     * The Table that hosts the read-only parameter set representing the data.
     */
    protected JTable                   table;

    /**
     * The custom cell renderer for read-only table data.
     */
    private final DataViewCellRenderer dataViewCellRenderer;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Constructs a {@code DataViewXComponent} that hosts an initially empty
     * data table.
     *
     * @version 1.0
     */
    protected DataViewXComponent() {
        // Always call the superclass constructor first!
        super();

        dataViewCellRenderer = new DataViewCellRenderer();

        // Avoid constructor failure by wrapping the layout initialization in an
        // exception handler that logs the exception and then returns an object.
        try {
            initComponent();
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
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
     * @version 1.0
     */
    private final void initComponent() {
        // Set the default grid layout scheme, to contain the table and its
        // optional scroll bar.
        setLayout( new GridLayout() );
    }

    /**
     * Initializes the table that is hosted by this component.
     * <p>
     * The table itself has to be initialized outside of this abstract base
     * class, as the derived classes cannot pass their domain-specific data to
     * this parent class until after they have invoked their super-constructor.
     *
     * @param data
     *            The initial data for the table
     * @param columnNames
     *            The names of the table columns
     * @param horizontalAlignment
     *            The horizontal alignment to use for row header cells
     * @param isFirstRowHeader
     *            {@code true} if the first row is to be treated as column
     *            headers; {@code false} if it is just a regular data row
     *
     * @version 1.0
     */
    protected final void initTable( final Object[][] data,
                                    final Object[] columnNames,
                                    final int horizontalAlignment,
                                    final boolean isFirstRowHeader ) {
        // Set the custom table model for more control over the display of data.
        final TableModel tableModel = new DataViewTableModel( data, columnNames, false );

        // Create the table from the custom table model.
        //
        // Do not allow column reordering or row sorting as this destroys the
        // relationship of the data.
        //
        // Grid lines between cells are disabled, as we want to blend in with
        // the layout's background for this special custom implementation.
        table = new XTable( tableModel, false, false );

        // Use the custom Table Cell Renderer for the single column, set to
        // specified alignment and column header status.
        dataViewCellRenderer.setHorizontalAlignment( horizontalAlignment );
        dataViewCellRenderer.setFirstRowHeader( isFirstRowHeader );
        final TableColumn column = table.getColumnModel().getColumn( 0 );
        column.setCellRenderer( dataViewCellRenderer );

        // Add the table to this component.
        add( table );
    }

    //////////////////////// Vectorization methods ///////////////////////////

    /**
     * This method vectorizes the table, via direct custom graphics calls.
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context for vectorizing the
     *            content of this component
     * @param offsetX
     *            The initial offset to apply along the x-axis for positioning
     *            the table rows in the vectorized output
     * @param offsetY
     *            The initial offset to apply along the y-axis for positioning
     *            the table columns in the vectorized output
     * @param rowsToExclude
     *            A {@link HashSet} of the rows to exclude from vectorization;
     *            not required to be contiguous
     *
     * @version 1.0
     */
    public final void vectorize( final Graphics2D graphicsContext,
                                 final int offsetX,
                                 final int offsetY,
                                 final HashSet< Integer > rowsToExclude ) {
        final Color backgroundColor = getBackground();

        // Vectorize the full table, but skip the table header in this context.
        TableVectorizationUtilities.vectorizeTable( graphicsContext,
                                                    offsetX,
                                                    offsetY,
                                                    table,
                                                    false,
                                                    rowsToExclude,
                                                    backgroundColor );
    }

    ////////////////////// XComponent method overrides ///////////////////////

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
        // Always call the superclass first!
        super.setForegroundFromBackground( backColor );

        // Make sure the foreground color is never masked by the background.
        final Color foreColor = ColorUtilities.getForegroundFromBackground( backColor );

        // We bypass the usual table rendering as data view tables are meant to
        // blend in with their background (they are read-only grids of labels
        // and values).
        table.setBackground( backColor );
        table.setForeground( foreColor );
    }

}
