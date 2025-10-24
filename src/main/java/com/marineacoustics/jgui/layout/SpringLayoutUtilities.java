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
package com.marineacoustics.jgui.layout;

import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import java.awt.Component;
import java.awt.Container;

/**
 * {@code SpringLayoutUtilities} is a utility class for working more effectively
 * with the {@link SpringLayout} layout manager that was added in Java 1.4.
 * <p>
 * This code is based on an original tutorial that was meant to be folded into
 * the JDK/JRE but for some reason never was.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class SpringLayoutUtilities {

    /**
     * This method serves merely as a sanity check that the Maven integration
     * and builds work properly and also behave correctly inside Eclipse IDE. It
     * will likely get removed once I gain more confidence that I have solved
     * the well-known issues with Maven inside Eclipse as I move on to more
     * complex projects with dependencies (this project is quite simple and has
     * no dependencies at this time, until more functionality is added).
     *
     * @param args
     *            The command-line arguments for executing this class as the
     *            main entry point for an application
     *
     * @since 1.0
     */
    public static void main( final String[] args ) {
        System.out.println( "Hello Maven from GuiToolkit!" ); //$NON-NLS-1$
    }

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private SpringLayoutUtilities() {}

    /**
     * Aligns the first {@code rows} {@code cols} components of {@code parent}
     * in a grid. Each component in a column is as wide as the maximum preferred
     * width of the components in that column; height is similarly determined
     * for each row. The parent is made just big enough to fit them all.
     *
     * @param parent
     *            The parent container for the layout manager
     * @param rows
     *            The number of rows in the layout
     * @param cols
     *            The number of columns in the layout
     * @param initialX
     *            The x location to start the grid at
     * @param initialY
     *            The y location to start the grid at
     * @param xPad
     *            The x padding between cells
     * @param yPad
     *            The y padding between cells
     */
    public static void makeGrid( final Container parent,
                                 final int rows,
                                 final int cols,
                                 final int initialX,
                                 final int initialY,
                                 final int xPad,
                                 final int yPad ) {
        SpringLayout layout;
        try {
            layout = ( SpringLayout ) parent.getLayout();
        }
        catch ( final ClassCastException cce ) {
            System.err.println( "The first argument to makeGrid must use SpringLayout." ); //$NON-NLS-1$
            return;
        }

        final Spring xPadSpring = Spring.constant( xPad );
        final Spring yPadSpring = Spring.constant( yPad );
        final Spring initialXSpring = Spring.constant( initialX );
        final Spring initialYSpring = Spring.constant( initialY );
        final int max = rows * cols;

        // Calculate Springs that are the max of the width/height so that all
        // cells have the same size.
        Spring maxWidthSpring = layout.getConstraints( parent.getComponent( 0 ) ).getWidth();
        Spring maxHeightSpring = layout.getConstraints( parent.getComponent( 0 ) ).getWidth();
        for ( int i = 1; i < max; i++ ) {
            final Constraints cons = layout.getConstraints( parent.getComponent( i ) );

            maxWidthSpring = Spring.max( maxWidthSpring, cons.getWidth() );
            maxHeightSpring = Spring.max( maxHeightSpring, cons.getHeight() );
        }

        // Apply the new width/height Spring. This forces all the
        // components to have the same size.
        for ( int i = 0; i < max; i++ ) {
            final Constraints constraints = layout.getConstraints( parent.getComponent( i ) );

            constraints.setWidth( maxWidthSpring );
            constraints.setHeight( maxHeightSpring );
        }

        // Adjust the x/y constraints of all the cells so that they are aligned
        // in a grid.
        Constraints lastConstraints = null;
        Constraints lastRowConstraints = null;
        for ( int i = 0; i < max; i++ ) {
            final Constraints constraints = layout.getConstraints( parent.getComponent( i ) );
            if ( constraints != null ) {
                if ( ( i % cols ) == 0 ) {
                    // Start of new row.
                    lastRowConstraints = lastConstraints;
                    constraints.setX( initialXSpring );
                }
                else if ( lastConstraints != null ) {
                    // X position depends on previous component.
                    constraints
                            .setX( Spring.sum( lastConstraints.getConstraint( SpringLayout.EAST ),
                                               xPadSpring ) );
                }

                if ( ( i / cols ) == 0 ) {
                    // First row.
                    constraints.setY( initialYSpring );
                }
                else if ( lastRowConstraints != null ) {
                    // Y position depends on previous row.
                    constraints.setY( Spring
                            .sum( lastRowConstraints.getConstraint( SpringLayout.SOUTH ),
                                  yPadSpring ) );
                }

                lastConstraints = constraints;
            }
        }

        // Set the parent's overall size constraints.
        if ( lastConstraints != null ) {
            final Constraints parentConstraints = layout.getConstraints( parent );
            parentConstraints
                    .setConstraint( SpringLayout.SOUTH,
                                    Spring.sum( Spring.constant( yPad ),
                                                lastConstraints
                                                        .getConstraint( SpringLayout.SOUTH ) ) );
            parentConstraints
                    .setConstraint( SpringLayout.EAST,
                                    Spring.sum( Spring.constant( xPad ),
                                                lastConstraints
                                                        .getConstraint( SpringLayout.EAST ) ) );
        }
    }

    /**
     * Aligns the first {@code rows} {@code cols} components of {@code parent}
     * in a grid. Each component in a column is as wide as the maximum preferred
     * width of the components in that column; height is similarly determined
     * for each row. The parent is made just big enough to fit them all.
     *
     * @param parent
     *            The parent container for the layout manager
     * @param rows
     *            The number of rows in the layout
     * @param cols
     *            The number of columns in the layout
     * @param initialX
     *            The x location to start the grid at
     * @param initialY
     *            The y location to start the grid at
     * @param xPad
     *            The x padding between cells
     * @param yPad
     *            The y padding between cells
     */
    public static void makeCompactGrid( final Container parent,
                                        final int rows,
                                        final int cols,
                                        final int initialX,
                                        final int initialY,
                                        final int xPad,
                                        final int yPad ) {
        SpringLayout layout;
        try {
            layout = ( SpringLayout ) parent.getLayout();
        }
        catch ( final ClassCastException cce ) {
            System.err.println( "The first argument to makeCompactGrid must use SpringLayout." ); //$NON-NLS-1$
            return;
        }

        // Align all cells in each column and make them the same width.
        Spring x = Spring.constant( initialX );
        for ( int c = 0; c < cols; c++ ) {
            Spring width = Spring.constant( 0 );
            for ( int r = 0; r < rows; r++ ) {
                width = Spring.max( width, getConstraintsForCell( parent, cols, r, c ).getWidth() );
            }
            for ( int r = 0; r < rows; r++ ) {
                final Constraints constraints = getConstraintsForCell( parent, cols, r, c );
                constraints.setX( x );
                constraints.setWidth( width );
            }
            x = Spring.sum( x, Spring.sum( width, Spring.constant( xPad ) ) );
        }

        // Align all cells in each row and make them the same height.
        Spring y = Spring.constant( initialY );
        for ( int r = 0; r < rows; r++ ) {
            Spring height = Spring.constant( 0 );
            for ( int c = 0; c < cols; c++ ) {
                height = Spring.max( height,
                                     getConstraintsForCell( parent, cols, r, c ).getHeight() );
            }
            for ( int c = 0; c < cols; c++ ) {
                final Constraints constraints = getConstraintsForCell( parent, cols, r, c );
                constraints.setY( y );
                constraints.setHeight( height );
            }
            y = Spring.sum( y, Spring.sum( height, Spring.constant( yPad ) ) );
        }

        // Set the parent's overall size constraints.
        final Constraints parentConstraints = layout.getConstraints( parent );
        parentConstraints.setConstraint( SpringLayout.SOUTH, y );
        parentConstraints.setConstraint( SpringLayout.EAST, x );
    }

    /**
     * Returns the {@link Constraints} for the {@link SpringLayout} associated
     * with the provided {@link Container}, given a row and a column.
     * <p>
     * This method is private because its implementation might need to change to
     * match revisions within the Swing toolkit. It is used by
     * {@link #makeCompactGrid}.
     *
     * @param parent
     *            The parent container for the layout manager
     * @param cols
     *            The number of columns in the layout
     * @param row
     *            The row to query for layout constraints
     * @param col
     *            The column to query for layout constraints
     * @return The {@link Constraints} for the {@link SpringLayout} associated
     *         with the provided {@link Container}, given a row and a column
     *
     * @version 1.0
     */
    private static Constraints getConstraintsForCell( final Container parent,
                                                      final int cols,
                                                      final int row,
                                                      final int col ) {
        // It is OK to do a class cast here, as this is a private method and its
        // only invocation context checks for class cast exceptions.
        final SpringLayout layout = ( SpringLayout ) parent.getLayout();

        final Component component = parent.getComponent( ( row * cols ) + col );

        final Constraints constraints = layout.getConstraints( component );

        return constraints;
    }

}
