/**
 * MIT License
 *
 * Copyright (c) 2023 Mark Schmieder
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

import com.mhschmieder.jcontrols.control.TabButton;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;

/**
 * Component to be used as tabComponent; contains a JLabel to show the text
 * and  a JButton to close the tab it belongs to .
 */ 
public class ButtonTabComponent extends JPanel {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     * <p>
     * TODO: Regenerate this once the Eclipse bug is fixed that treats unique and
     *  default Serial Version ID's exactly the same (not the case previously!).
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ButtonTabComponent to host custom TabButtons.
     * 
     * @param tabbedPane The TabbedPane that owns this custom JPanel
     * @param tabIcon An optional icon to display in the Label for the Tab
     */
    public ButtonTabComponent( final JTabbedPane tabbedPane,
                               final Icon tabIcon ) {
        // Un-set the default FlowLayout's gaps.
        super( new FlowLayout( FlowLayout.LEADING, 0, 0 ) );
        
        if ( tabbedPane == null ) {
            throw new NullPointerException( "TabbedPane is Null" );
        }
        
        setOpaque( false );
        
        // Make JLabel read titles from JTabbedPane.
        final JLabel label = new JLabel() {
            /**
             * Unique Serial Version ID for this class, to avoid class loader
             * conflicts.
             * <p>
             * TODO: Regenerate this once the Eclipse bug is fixed that treats
             *  unique and default Serial Version ID's exactly the same (not
             *  the case previously!).
             */
            private static final long serialVersionUID = 1L;

            @Override 
            public String getText() {
                final int tabIndex = tabbedPane.indexOfTabComponent(
                    ButtonTabComponent.this );
                if ( tabIndex != -1 ) {
                    return tabbedPane.getTitleAt( tabIndex );
                }
                
                return null;
            }
        };        
        add( label );
        
        // Set the optional tab icon, if present.
        if ( tabIcon != null ) {
            label.setIcon( tabIcon );
        }
        
        // Add more space between the label and the button.
        label.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
        
        // Make the Tab Button.
        final JButton button = new TabButton( tabbedPane, this );
        add( button );
        
        // Add more space to the top of the component.
        setBorder( BorderFactory.createEmptyBorder( 2, 0, 0, 0 ) );
    }
}
