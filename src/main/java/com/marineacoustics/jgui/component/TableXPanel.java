/**
 * MIT License
 *
 * Copyright (c) 2020, 2025 Mark Schmieder
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

import com.marineacoustics.jgui.border.BorderUtilities;
import com.mhschmieder.jcontrols.control.TableUtilities;
import com.mhschmieder.jcontrols.control.XTable;
import com.mhschmieder.jcontrols.table.TableHeaderRenderer;
import com.mhschmieder.jcontrols.table.TableInitializationUtilities;
import com.mhschmieder.jcontrols.table.TableVectorizationUtilities;
import com.mhschmieder.jgraphics.color.ColorUtilities;
import org.apache.commons.math3.util.FastMath;

import javax.swing.BorderFactory;
import javax.swing.CellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * {@code TableXPanel} is an abstract base class that serves as a
 * specialization of {@link XPanel} that wraps an {@link XTable} in appropriate
 * layout and with expected behavior for primary application content components.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public abstract class TableXPanel extends XPanel {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = 8715148654648213855L;

    /**
     * The font size to use in table cells; may be OS or LAF-dependent.
     * <p>
     * Dynamically growable tables don't need to worry about whether vertical
     * scroll bars eventually show up when rows are added, and so a font size
     * legible from a distance is desirable. This same font size is preferable
     * for most other contexts as well, so is declared as a static constant vs.
     * a variable.
     */
    protected final float     tableCellFontSize;

    /**
     * The main panel that combines the layout elements; possibly redundant?
     */
    private JPanel            mainPanel;

    /**
     * The Panel that hosts the Scroll Pane that in turn hosts the Table.
     */
    private JPanel            scrollableTablePanel;

    /**
     * The Scroll Pane that hosts the Table and allows a viewport that is
     * smaller than the table size, adding scrollbars if clipping would
     * otherwise result.
     */
    private JScrollPane       scrollPane;

    /**
     * The Table that hosts the data.
     */
    protected XTable          table;

    /**
     * Flag for whether a Table Header is in use; {@code true} if so.
     */
    private boolean           tableHeaderInUse;

    /**
     * The index for the first column in the Table.
     */
    private final int         firstColumnIndex;

    /**
     * The index for the last column in the Table.
     */
    private final int         lastColumnIndex;

    /**
     * Flag for whether auto-selection is enabled, when nothing is selected.
     */
    private final boolean     autoSelectionEnabled;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Constructs a {@code TableXPanel} with minimal initial specifications.
     * <p>
     * This is an abstract base class, so its purpose is to avoid copy/paste
     * code in the derived classes; it is unable to function on its own.
     *
     * @param firstColumn
     *            The index for the first column in the Table
     * @param lastColumn
     *            The index for the last column in the Table
     * @param autoSelectionIsEnabled
     *            {@code true} if auto-selection is enabled when nothing is
     *            manually or programmatically selected
     *
     * @since 1.0
     */
    protected TableXPanel( final int firstColumn,
                           final int lastColumn,
                           final boolean autoSelectionIsEnabled ) {
        // Always call the superclass constructor first!
        super();

        firstColumnIndex = firstColumn;
        lastColumnIndex = lastColumn;

        autoSelectionEnabled = autoSelectionIsEnabled;

        tableHeaderInUse = false;

        tableCellFontSize = 12f;
    }

    /////////////////////// Initialization methods ///////////////////////////

    /**
     * Initializes this panel in an encapsulated way that protects all
     * constructors from run-time exceptions that might prevent instantiation.
     * <p>
     * This method is designed to be invoked by derived classes during their own
     * initialization, as it requires parameters that aren't available in this
     * abstract base class.
     * <p>
     * This is fairly old legacy code, so it never got modernized to take
     * advantage of custom string conversion classes for different data types
     * via the {@code TableStringConverter} class added in Java 1.6.
     * <p>
     * The code might also benefit from summing the provided column widths to
     * get the resulting table width, and using the row count for table height
     * derivation vs. redundantly pre-fetching these as call parameters.
     *
     * @param borderTitle
     *            The title to use for the styled border; can be {@code null} if
     *            no styled border is wanted
     * @param tableHeaderIsInUse
     *            {@code true} if a Table Header is in use
     * @param tableModel
     *            The Table Model to use for mapping the data type of each
     *            column in this table
     * @param columnWidths
     *            A list of the widths for each table column
     * @param columnAutoEdit
     *            Auto-edit settings for each individual column
     * @param columnAutoSelect
     *            Auto-select settings for each individual column
     * @param selectionMode
     *            Not an enum, so must be a valid int matching single selection,
     *            single interval selection, or multiple interval selection, as
     *            defined in {@code ListSelectionModel}
     * @param columnBasedSelectionAllowed
     *            {@code true} if entire columns can be selected as a cell group
     * @param rowBasedSelectionAllowed
     *            {@code true} if entire rows can be selected as a cell group
     * @param autoCreateRowSorter
     *            {@code true} if an automatic row sorter should be set up that
     *            is strictly UTF-8 alphabetical
     * @param tableWidthPixels
     *            The width of the table's viewport, in pixels
     * @param tableHeightPixels
     *            The height of the table's viewport, in pixels
     *
     * @since 1.0
     */
    protected final void initPanel( final String borderTitle,
                                    final boolean tableHeaderIsInUse,
                                    final TableModel tableModel,
                                    final int[] columnWidths,
                                    final Boolean[] columnAutoEdit,
                                    final Boolean[] columnAutoSelect,
                                    final int selectionMode,
                                    final boolean columnBasedSelectionAllowed,
                                    final boolean rowBasedSelectionAllowed,
                                    final boolean autoCreateRowSorter,
                                    final int tableWidthPixels,
                                    final int tableHeightPixels ) {
        // Load all of the subsidiary components for this panel.
        //
        // Some components may be used in subsidiary panels, so we may need to
        // initialize those components after we have initialized all of the
        // panels.
        loadComponents( tableModel,
                        columnWidths,
                        columnAutoEdit,
                        columnAutoSelect,
                        selectionMode,
                        columnBasedSelectionAllowed,
                        rowBasedSelectionAllowed,
                        autoCreateRowSorter );

        // Load all of the sub-panels for this panel.
        loadPanels();

        // Load the scroll panes, which may depend both on panels and components
        // and thus must be instantiated last.
        loadScrollPanes( tableWidthPixels, tableHeightPixels );

        // Load the main panel last, as it may have circular dependencies
        // between the subsidiary panels, tabbed panes, split panes, etc.
        loadMainPanel( borderTitle, tableHeaderIsInUse );

        // Load the cell editors and renderers.
        loadCellEditorsAndRenderers();
    }

    /**
     * @param tableModel
     *            The Table Model to use for mapping the data type of each
     *            column in this table
     * @param columnWidths
     *            A list of the widths for each table column
     * @param columnAutoEdit
     *            Auto-edit settings for each individual column
     * @param columnAutoSelect
     *            Auto-select settings for each individual column
     * @param selectionMode
     *            Not an enum, so must be a valid int matching single selection,
     *            single interval selection, or multiple interval selection, as
     *            defined in {@code ListSelectionModel}
     * @param columnBasedSelectionAllowed
     *            {@code true} if entire columns can be selected as a cell group
     * @param rowBasedSelectionAllowed
     *            {@code true} if entire rows can be selected as a cell group
     * @param autoCreateRowSorter
     *            {@code true} if an automatic row sorter should be set up that
     *            is strictly UTF-8 alphabetical
     *
     * @since 1.0
     */
    protected void loadComponents( final TableModel tableModel,
                                   final int[] columnWidths,
                                   final Boolean[] columnAutoEdit,
                                   final Boolean[] columnAutoSelect,
                                   final int selectionMode,
                                   final boolean columnBasedSelectionAllowed,
                                   final boolean rowBasedSelectionAllowed,
                                   final boolean autoCreateRowSorter ) {
        // Given a Table Model, construct the Table and associated components.
        //
        // Adding a component to a scroll pane constructor makes that component
        // the viewport, which follows the rules of JScrollPaneLayout.
        //
        // As we add a panel wrappers for tables vs. adding tables to components
        // directly, we lose the header and must re-add it later in the final
        // layout for the panel that wraps the scroll pane.
        table = new XTable( tableModel,
                            columnAutoEdit,
                            columnAutoSelect,
                            selectionMode,
                            columnBasedSelectionAllowed,
                            rowBasedSelectionAllowed,
                            autoCreateRowSorter );

        // Initialize table metrics, such as row height, column width.
        TableInitializationUtilities.initTableMetrics( table, columnWidths );
    }

    /**
     * Creates and lays out all of the sub-panels in this panel.
     *
     * @since 1.0
     */
    protected void loadPanels() {
        // Layout the scrollable table panel with its components.
        scrollableTablePanel = TableUtilities.makeScrollableTablePanel( table );
    }

    /**
     * Creates and lays out the scroll panes that are needed for avoiding
     * clipping when the viewport is smaller than the table itself.
     *
     * @param tableWidthPixels
     *            The width of the table's viewport, in pixels
     * @param tableHeightPixels
     *            The height of the table's viewport, in pixels
     *
     * @since 1.0
     */
    private final void loadScrollPanes( final int tableWidthPixels,
                                        final int tableHeightPixels ) {
        scrollPane = TableUtilities.makeTableScrollPane( table,
                                                         scrollableTablePanel,
                                                         tableWidthPixels,
                                                         tableHeightPixels );
    }

    /**
     * Creates and lays out the main panel that serves as the container for the
     * entire hierarchy of components in {@code TableXPanel}.
     *
     * @param borderTitle
     *            The title to use for the styled border; can be {@code null} if
     *            no styled border is wanted
     * @param tableHeaderIsInUse
     *            {@code true} if a Table Header is in use
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    protected void loadMainPanel( final String borderTitle, final boolean tableHeaderIsInUse ) {
        // Cache the Table Header in use status, for later export decisions.
        tableHeaderInUse = tableHeaderIsInUse;

        // Layout the main panel with its components.
        //
        // NOTE: No table control panel in this context as this version is for 
        //  fixed size vs. dynamic tables.
        mainPanel = TableUtilities.makeTablePanel( null, 
                                                   null, 
                                                   tableHeaderIsInUse, 
                                                   scrollPane, 
                                                   table );

        // Layout the main content.
        setLayout( new BorderLayout() );
        if ( borderTitle != null ) {
            setBorder( BorderUtilities.makeTitledBorder( borderTitle ) );
        }
        else {
            setBorder( BorderFactory.createEmptyBorder( 6, 6, 6, 6 ) );
        }
        add( mainPanel, "Center" );
    }

    /**
     * Creates and assigns all of the cell editors and renderers.
     * <p>
     * This method should be overridden by all derived classes, as each table
     * implementation will have a different table model and size, and thus
     * different data type mappings to each column.
     *
     * @since 1.0
     */
    protected void loadCellEditorsAndRenderers() {
        // Set a custom cell renderer so we can have colored headers (the
        // default renderer for table columns is grey), if header is in use.
        if ( tableHeaderInUse ) {
            final JTableHeader tableHeader = table.getTableHeader();
            if ( tableHeader != null ) {
                tableHeader
                        .setDefaultRenderer( new TableHeaderRenderer( true, tableCellFontSize ) );
            }
        }
    }

    /**
     * Initializes the column-based custom cell editors and renderers.
     * <p>
     * This method is mandated to be implemented by all derived classes, and
     * should be invoked inside their overrides of loadCellEditorsAndRenderers()
     * as the final step.
     * <p>
     * We may switch to a column-based array storage of editors/renderers.
     *
     * @since 1.0
     */
    protected abstract void initCellEditorsAndRenderers();

    ////////////////// Accessor methods for private data /////////////////////

    /**
     * Returns {@code true} if auto-selection is enabled when nothing is
     * manually or programmatically selected.
     *
     * @return {@code true} if auto-selection is enabled when nothing is
     *         manually or programmatically selected
     *
     * @since 1.0
     */
    public final boolean isAutoSelectionEnabled() {
        return autoSelectionEnabled;
    }

    ////////////////////// Model/View syncing methods ////////////////////////

    /**
     * Updates the model to match the table view at the specified row.
     * <p>
     * Check for columns to exclude, such as action buttons, to avoid side
     * effects of bringing another window forward.
     *
     * @param row
     *            The row index for the table row to be synced to the model
     * @return {@code true} if the model changed after syncing it from the view
     *
     * @since 1.0
     */
    public final boolean updateModelAt( final int row ) {
        boolean modelChanged = false;
        final TableModel tableModel = table.getModel();
        final int numberOfColumns = tableModel.getColumnCount();

        // Iterate through the individual columns in the current row.
        for ( int column = 0; column < numberOfColumns; column++ ) {
            modelChanged |= updateModelAt( row, column );
        }

        return modelChanged;
    }

    /**
     * Updates the model to match the table view at the specified cell.
     *
     * @param row
     *            The row index for the table cell to be synced to the model
     * @param column
     *            The column index for the table cell to be synced to the model
     * @return {@code true} if the model changed after syncing it from the view
     *
     * @since 1.0
     */
    protected boolean updateModelAt( final int row, final int column ) {
        // If the index is out of bounds, report an error and return.
        final int lastRowIndex = getLastRowIndex();
        if ( ( row < 0 ) || ( row > lastRowIndex ) ) {
            System.err.println( "WARNING: invalid table row index in " + toString() ); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    /**
     * Post-processes anything that needs to happen after the model is synced.
     * <p>
     * As this is an abstract base class, and as not all derived classes will
     * need to do anything after syncing the model from the view, it is given a
     * no-op default implementation.
     * <p>
     * This method will generally be invoked inside event callbacks; the same
     * ones that invoke the model update methods.
     *
     * @param row
     *            The row index for the table cell that was synced to the model
     * @param column
     *            The column index for the table cell that was synced to the
     *            model
     * @param modelChanged
     *            {@code true} if the model changed after syncing it from the
     *            view
     *
     * @since 1.0
     */
    protected void updateModelPostProcessing( final int row,
                                              final int column,
                                              final boolean modelChanged ) {}

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

        // Vectorize the full table, optionally including the table header.
        TableVectorizationUtilities.vectorizeTable( graphicsContext,
                                                    offsetX,
                                                    offsetY,
                                                    table,
                                                    tableHeaderInUse,
                                                    rowsToExclude,
                                                    backgroundColor );
    }

    ////////////////////// Table manipulation methods ////////////////////////

    /**
     * Cancels cell editing on all of the table columns.
     *
     * @since 1.0
     */
    public final void cancelCellEditing() {
        for ( int column = firstColumnIndex; column <= lastColumnIndex; column++ ) {
            final CellEditor cellEditor =
                                        table.getColumnModel().getColumn( column ).getCellEditor();
            if ( cellEditor != null ) {
                cellEditor.cancelCellEditing();
            }
        }
    }

    /**
     * Clears any active selection in the table.
     *
     * @since 1.0
     */
    public final void clearSelection() {
        table.clearSelection();
    }

    /**
     * Returns the index of the last column in the table.
     *
     * @return The index of the last column in the table
     *
     * @since 1.0
     */
    public final int getLastColumnIndex() {
        final TableModel tableModel = table.getModel();
        return tableModel.getColumnCount() - 1;
    }

    /**
     * Returns the index of the last row in the table.
     *
     * @return The index of the last row in the table
     *
     * @since 1.0
     */
    public final int getLastRowIndex() {
        final TableModel tableModel = table.getModel();
        return tableModel.getRowCount() - 1;
    }

    /**
     * Returns the number of selected rows, or zero if none selected.
     *
     * @return The number of selected rows, or zero if none selected
     */
    public final int getNumberOfSelectedRows() {
        final Integer[] selectedRowIndices = getSelectedRows();
        final int numberOfSelectedRows = ( selectedRowIndices != null )
            ? selectedRowIndices.length
            : 0;

        return numberOfSelectedRows;
    }

    /**
     * Returns the list of currently selected table row indices, in reverse
     * order so that deletions and other actions can be performed sequentially
     * without any of the indices "going bad" mid-stream.
     *
     * @return A list of the selected table row indices, or {@code null} if none
     *         selected
     *
     * @since 1.0
     */
    public final Integer[] getSelectedRows() {
        final int[] selectedTableRows = table.getSelectedRows();
        final int selectionLength = selectedTableRows.length;
        if ( selectionLength <= 0 ) {
            return null;
        }

        // In order to guarantee that all indices are valid at the time they are
        // applied without having to be altered to accommodate a dynamically
        // changing table, we apply Java's sort algorithms to rearrange the list
        // of indices in reverse numerical order.
        final Integer[] selectedRowIndices = new Integer[ selectionLength ];
        for ( int rowIndex = 0; rowIndex < selectionLength; rowIndex++ ) {
            selectedRowIndices[ rowIndex ] = Integer.valueOf( selectedTableRows[ rowIndex ] );
        }
        Arrays.sort( selectedRowIndices, Collections.reverseOrder() );

        return selectedRowIndices;
    }

    /**
     * Returns the hierarchically-lower-most selected row, or the last row in
     * the table if none were selected.
     *
     * @param minimumRowIndex
     *            The lowest index that is considered valid for the selected row
     * @return The hierarchically-lower-most selected row, or the last row in
     *         the table if none were selected
     *
     * @since 1.0
     */
    public final int getSelectedRow( final int minimumRowIndex ) {
        // If the user didn't select any rows, default initially to the row
        // before the first valid row index, to make sure any selected row
        // overrides the initial default.
        int selectionIndex = minimumRowIndex - 1;

        final Integer[] selectedRowIndices = getSelectedRows();
        if ( ( selectedRowIndices != null ) && ( selectedRowIndices.length > 0 ) ) {
            final int maximumRowIndex = selectedRowIndices.length - 1;
            selectionIndex = selectedRowIndices[ maximumRowIndex ].intValue();
        }
        else {
            // If no rows were selected, and auto-selection is enabled, correct
            // the default selection to be the last valid row index.
            if ( autoSelectionEnabled ) {
                final int maximumRowIndex = getLastRowIndex();
                selectionIndex = FastMath.max( selectionIndex, maximumRowIndex );
            }
        }

        return selectionIndex;
    }

    /**
     * Auto-selects the default table row selection, which is the last row in
     * the table in this implementation.
     *
     * @since 1.0
     */
    public final void setDefaultSelection() {
        // Trigger the default auto-selection by mimicking an empty or invalid
        // selection.
        selectRow( -1 );
    }

    /**
     * Selects the specified row in the table.
     * <p>
     * This method clears any active selections before making a new row
     * selection, so that only one row is selected at a time, avoiding confusion
     * over focus and selection status.
     * <p>
     * This is safer than performing two separate actions, and also more
     * performant, as it avoids the interim state where the selected row index
     * is deliberately set to the invalid selection indicator of "-1".
     *
     * @param rowIndex
     *            The index of the table row to select
     *
     * @since 1.0
     */
    public void selectRow( final int rowIndex ) {
        // Avoid any unnecessary table events, to minimize recursion.
        final int currentSelectedRowIndex = table.getSelectedRow();
        if ( rowIndex == currentSelectedRowIndex ) {
            return;
        }

        // Clear any active row selections, in case the table is now empty.
        clearSelection();

        // Select the requested row, or auto-select the last row in the table if
        // the requested row is invalid. If the table is now empty, select
        // nothing as otherwise an index out of range exception is thrown.
        final int lastRowIndex = getLastRowIndex();
        final int adjustedRowIndex = ( ( rowIndex < 0 ) || ( rowIndex > lastRowIndex ) )
            ? lastRowIndex
            : FastMath.min( rowIndex, lastRowIndex );
        if ( adjustedRowIndex >= 0 ) {
            table.setRowSelectionInterval( adjustedRowIndex, adjustedRowIndex );
        }
    }

    /**
     * Selects the specified cell in the table.
     *
     * @param rowIndex
     *            The row index of the table cell to select
     * @param columnIndex
     *            The column index of the table cell to select
     *
     * @since 1.0
     */
    public final void selectCell( final int rowIndex, final int columnIndex ) {
        // First select the row. This is necessary even if redundant.
        table.setRowSelectionInterval( rowIndex, rowIndex );

        // Now select the column. This should retain the selected row.
        table.setColumnSelectionInterval( columnIndex, columnIndex );
    }

    //////////////////////// XPanel method overrides /////////////////////////

    /**
     * Returns the status of the view-to-model syncing ({@code true} if the data
     * changed) after performing it hierarchically on the full panel layout.
     * <p>
     * This method iterates through all of the table's rows to sync the data
     * model to their current values and to determine if any data changed
     * anywhere in the table.
     *
     * @return {@code true} if the model changed after syncing it from the view
     *
     * @since 1.0
     */
    @Override
    public boolean updateModel() {
        boolean modelChanged = false;
        final TableModel tableModel = table.getModel();
        final int numberOfRows = tableModel.getRowCount();

        // Iterate through the individual rows.
        for ( int row = 0; row < numberOfRows; row++ ) {
            modelChanged |= updateModelAt( row );
        }

        return modelChanged;
    }

    /**
     * Syncs the view to the data model hierarchically on the full panel layout.
     * <p>
     * As not all derived classes have data to sync, declaring this as abstract
     * is the wrong approach, so we provide a default no-op implementation.
     *
     * @since 1.0
     */
    @Override
    public void updateView() {}

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

        // Forward this method to the subcomponents.
        mainPanel.setEnabled( enabled );
        scrollableTablePanel.setEnabled( enabled );
        scrollPane.setEnabled( enabled );
        table.setEnabled( enabled );
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
        mainPanel.setBackground( backColor );
        mainPanel.setForeground( foreColor );

        scrollableTablePanel.setBackground( backColor );
        scrollableTablePanel.setForeground( foreColor );

        // Set the background color that will be used if the main viewport view
        // is smaller than the viewport, or is not opaque.
        //
        // We set the color of the viewport along with the scroll pane as the
        // scroll pane's viewport is opaque which, among other things, means it
        // will completely fill in its background using its background color.
        // Therefore when the scroll pane draws its background the viewport will
        // usually draw over it, but there is no harm in setting both.
        scrollPane.setBackground( backColor );
        scrollPane.setForeground( foreColor );
        scrollPane.getViewport().setBackground( backColor );
        scrollPane.getViewport().setForeground( foreColor );

        // Table backgrounds are generally white regardless of other factors.
        table.setForegroundFromBackground( Color.WHITE );
    }

}
