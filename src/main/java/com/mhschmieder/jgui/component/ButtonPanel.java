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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.FlowLayout;

// NOTE: This class is used to format a single button in a panel layout.
// NOTE: This may be an obsolete component. Last used in 2014.
public class ButtonPanel extends XPanel {
    /**
     *
     */
    private static final long   serialVersionUID    = -5728612449257406141L;
    
    // //////////////////////////////////////////////////////////////////////////
    // Panel View Variables
    public JButton              _button             = null;
    
    // //////////////////////////////////////////////////////////////////////////
    // Constructors and Initialization
    public ButtonPanel( final JButton button,
                        final int mnemonic,
                        final int align ) {
        // Always call the superclass constructor first!
        super();
    
        try {
            initPanel( button, mnemonic, align );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }
    
    protected void initPanel(   final JButton button,
                                final int mnemonic,
                                final int align ) {
        // Set the button and its main properties.
        _button = button;
    
        // Add mnemonics/shortcuts for all components.
        _button.setMnemonic( mnemonic );
    
        // Layout the button panel with with its components.
        setLayout( new FlowLayout( align ) );
        setBorder( BorderFactory.createEmptyBorder( 6, 6, 6, 6 ) );
        add( _button );
        add( Box.createHorizontalGlue() );
    }
    
    @Override
    public void setEnabled( final boolean enabled ) {
        super.setEnabled( enabled );
    
        // Forward this function to the subcomponents.
        _button.setEnabled( enabled );
    }
}
