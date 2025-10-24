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
package com.marineacoustics.jgui.component;

import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.Color;

// NOTE: This class is used to format a single radio button in a panel layout.
// NOTE: This may be an obsolete component. Last used in 2014.
public class RadioButtonPanel extends XPanel {
    /**
     * 
     */
    private static final long   serialVersionUID    = 7743960131821661786L;

    // //////////////////////////////////////////////////////////////////////////
    // Panel View Variables
    public JRadioButton         _radioButton        = null;

    // //////////////////////////////////////////////////////////////////////////
    // Constructors and Initialization
    public RadioButtonPanel(    final String text,
                                final int mnemonic,
                                final boolean selected ) {
        // Always call the superclass constructor first!
        super();

        try {
            initPanel( text, mnemonic, selected );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    private void initPanel( final String text,
                            final int mnemonic,
                            final boolean selected ) {
        // Make the radio button and set its main properties.
        _radioButton = new JRadioButton();
        _radioButton.setText( text );

        // Add mnemonics/shortcuts for all components.
        _radioButton.setMnemonic( mnemonic );

        // Left-align all components that would be centered by default.
        _radioButton.setHorizontalAlignment( SwingConstants.LEFT );

        // Layout the radio button panel with with its components, adding
        // horizontal glue to ensure that the solitary radio button is
        // left-aligned and follows the formatting of higher components.
        setLayout( new BoxLayout( this, BoxLayout.LINE_AXIS ) );
        setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 6 ) );
        _radioButton.setSelected( selected );
        add( _radioButton );
        add( Box.createHorizontalGlue() );
    }

    @Override
    public void setEnabled( final boolean enabled ) {
        super.setEnabled( enabled );

        // Forward this function to the subcomponents.
        _radioButton.setEnabled( enabled );
    }

    // This method sets the background color, and where appropriate, the
    // foreground color is set to complement it for text-based components.
    @Override
    public final void setForegroundFromBackground( final Color backColor ) {
        super.setForegroundFromBackground( backColor );

        // Forward this function to the subcomponents.
        final Color foreColor = ColorUtilities
                .getForegroundFromBackground( backColor );

        _radioButton.setBackground( backColor );
        _radioButton.setForeground( foreColor );
    }

    public final void setLabel( final String label ) {
        _radioButton.setText( label );
    }
}
