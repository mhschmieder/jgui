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

import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

public class LabeledTextFieldPanel extends XPanel {
    /**
     * 
     */
    private static final long   serialVersionUID    = -242007208527899012L;

    // //////////////////////////////////////////////////////////////////////////
    // Panel View Variables
    private JLabel              _label              = null;
    public JTextField           _textField          = null;

    // //////////////////////////////////////////////////////////////////////////
    // Constructors and Initialization
    public LabeledTextFieldPanel(   final String labelText,
                                    final char mnemonic,
                                    final int textFieldSize ) {
        // Always call the superclass constructor first!
        super();

        try {
            initPanel( labelText, mnemonic, textFieldSize );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    public final String getText() {
        // Forward this to the text field.
        return _textField.getText();
    }

    private final void initPanel(   final String labelText,
                                    final char mnemonic,
                                    final int textFieldSize ) {
        // Now that we have the label text, we can make the label.
        _label = new JLabel( labelText, SwingConstants.LEADING );

        // Now that we know the size, we can make the text field.
        _textField = new JTextField( textFieldSize );

        // Add mnemonics/shortcuts for all components, with all labels linked to
        // their associated components as well.
        _label.setLabelFor( _textField );
        _label.setDisplayedMnemonic( mnemonic );

        // Layout the main panel using the spring layout.
        setLayout( new SpringLayout() );
        add( _label );
        add( _textField );

        // TODO: Re-find this lost utility class, or the moved method.
        /*
        SpringUtilities.makeCompactGrid( this, 1, 2, // rows, columns
                                            6,
                                            6, // initX, initY
                                            6,
                                            6 ); // padX, padY
        */

        // Prevent the text field and overall panel from stretching vertically
        // when other components disappear or resize.
        setMaximumSize( new Dimension( getPreferredSize().width, 40 ) );
    }

    @Override
    public void setEnabled( final boolean enabled ) {
        super.setEnabled( enabled );

        // Forward this function to the subcomponents.
        _label.setEnabled( enabled );
        _textField.setEnabled( enabled );
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
    }

    public final void setText( final String text ) {
        // Forward this to the text field.
        _textField.setText( text );
    }
}
