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
package com.mhschmieder.jgui.component;

import com.mhschmieder.jcontrols.control.XComboBox;
import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

// NOTE: This class is used to format a single combo box in a panel layout.
// NOTE: This may be an obsolete component. Last used in 2014.
public class ComboBoxPanel extends XPanel {
    /**
     * 
     */
    private static final long   serialVersionUID    = -1877746540154617225L;

    // //////////////////////////////////////////////////////////////////////////
    // Panel View Variables
    public JLabel               _label              = null;
    public XComboBox _comboBox           = null;

    // //////////////////////////////////////////////////////////////////////////
    // Constructors and Initialization
    public ComboBoxPanel(   final String label,
                            final int mnemonic ) {
        // Always call the superclass constructor first!
        super();

        try {
            initPanel( label, mnemonic );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void addItem( final Object object, final boolean disabled ) {
        // Forward this function to the combo box.
        _comboBox.addItem( object, disabled );
    }

    private void initPanel( final String label, final int mnemonic ) {
        // Make the combo box and set its main properties.
        _label = new JLabel( label );
        _comboBox = new XComboBox();

        // Add mnemonics/shortcuts for all components, with all
        // labels linked to their associated components as well.
        _label.setLabelFor( _comboBox );
        _label.setDisplayedMnemonic( mnemonic );

        // Left-align all components that would be centered by default.
        _label.setHorizontalAlignment( SwingConstants.LEFT );

        // Layout the combo box panel with with its components, adding
        // horizontal glue to ensure that the solitary combo box is
        // left-aligned and follows the formatting of higher components.
        setLayout( new BoxLayout( this, BoxLayout.LINE_AXIS ) );
        setBorder( BorderFactory.createEmptyBorder( 6, 6, 6, 6 ) );
        add( _label );
        add( Box.createRigidArea( new Dimension( 11, 11 ) ) );
        add( _comboBox );
        add( Box.createHorizontalGlue() );
    }

    @Override
    public void setEnabled( final boolean enabled ) {
        super.setEnabled( enabled );

        // Forward this function to the subcomponents.
        _comboBox.setEnabled( enabled );
    }

    // This method sets the background color, and where appropriate, the
    // foreground color is set to complement it for text-based components.
    @Override
    public final void setForegroundFromBackground( final Color backColor ) {
        super.setForegroundFromBackground( backColor );

        // Forward this function to the subcomponents.
        final Color foreColor = ColorUtilities
                .getForegroundFromBackground( backColor );

        _label.setBackground( backColor );
        _label.setForeground( foreColor );

        // _comboBox.setForegroundFromBackground( backColor );
    }

    public final void setLabel( final String label ) {
        _label.setText( label );
    }
}