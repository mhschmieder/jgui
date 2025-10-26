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
package com.mhschmieder.jgui.component;

import com.mhschmieder.jcontrols.icon.IconFactory;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;

/**
 * General utilities for working with Swing Tabs providing boilerplate code.
 */
public class TabUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private TabUtilities() {}
    
    /**
     * Adds a closable tab to the provided tabbed pane.
     * 
     * @param tabbedPane The Tabbed Pane to add the new Closable Tab to
     * @param tabComponent The Component that fills the Tab's Content Pane
     * @param tabLabel The Label to use for the Tab's Title and Icon
     */
    public static void addClosableTab( final JTabbedPane tabbedPane,
                                       final Component tabComponent,
                                       final JLabel tabLabel ) {
        final String tabTitle = tabLabel.getText();
        final Icon tabIcon = tabLabel.getIcon();
        addClosableTab( tabbedPane, tabComponent, tabTitle, tabIcon );
    }
    
    /**
     * Adds a closable tab to the provided tabbed pane.
     * 
     * @param tabbedPane The Tabbed Pane to add the new Closable Tab to
     * @param tabComponent The Component that fills the Tab's Content Pane
     * @param tabTitle The title to display in the Tab Label
     * @param iconResourceName The resource name of the Icon to display in
     *                         the Tab Label
     */
    public static void addClosableTab( final JTabbedPane tabbedPane,
                                       final Component tabComponent,
                                       final String tabTitle,
                                       final String iconResourceName ) {
        final Icon tabIcon = makeTabIcon( iconResourceName );
        addClosableTab( tabbedPane, tabComponent, tabTitle, tabIcon );
    }
    
    /**
     * Adds a closable tab to the provided tabbed pane.
     * 
     * @param tabbedPane The Tabbed Pane to add the new Closable Tab to
     * @param tabComponent The Component that fills the Tab's Content Pane
     * @param tabTitle The title to display in the Tab Label
     * @param tabIcon The Icon to display in the Tab Label
     */
    public static void addClosableTab( final JTabbedPane tabbedPane,
                                       final Component tabComponent,
                                       final String tabTitle,
                                       final Icon tabIcon ) {
        // First, add the tab component in the traditional way, leaving out
        // the tooltip as our custom TabButton handles that in its UI layout.
        tabbedPane.addTab( tabTitle, tabIcon, tabComponent );
        
        // Now, make a custom wrapper that supports closable tabs.
        final ButtonTabComponent buttonTabComponent
            = new ButtonTabComponent( tabbedPane, tabIcon );
        
        // Find the index where the tab was added, to be on the safe side,
        // even though it is probably sufficient to call getTabCount().
        final int tabIndex = tabbedPane.indexOfComponent( tabComponent );
        
        // Set the tab component that shows in the tab bar, so that it uses 
        // the ButtonTabComponent custom wrapper class that allows for closing
        // the tab and that preserves its title, noting that we have to
        // "manually" reset the original tab icon here as well or it goes away.
        tabbedPane.setTabComponentAt( tabIndex, buttonTabComponent );
    }
    
    /**
     * Returns a JLabel that includes a Tab Title and an optional Tab Icon.
     * 
     * @param tabTitle The title to display in the Tab Label
     * @param iconResourceName The resource name of the Icon to display in
     *                         the Tab Label
     * @return a JLabel that includes a Tab Title and an optional Tab Icon
     */
    public static JLabel makeTabLabel( final String tabTitle,
                                       final String iconResourceName ) {
        // It is mostly OK to pass in a null or empty title, regardless of
        // whether an icon file path is provided, so it is sufficient to just
        // check for the latter condition rather than add more complexity. In
        // any case, it is better to set the icon during initial construction.
        final Icon tabIcon = makeTabIcon( iconResourceName );
        return makeTabLabel( tabTitle, tabIcon );
    }
    
    /**
     * Returns a JLabel that includes a Tab Title and an optional Tab Icon.
     * 
     * @param tabTitle The title to display in the Tab Label
     * @param tabIcon The Icon to display in the Tab Label
     * @return a JLabel that includes a Tab Title and an optional Tab Icon
     */
    public static JLabel makeTabLabel( final String tabTitle,
                                       final Icon tabIcon ) {
        // It is mostly OK to pass in a null or empty title, regardless of
        // whether a tab icon is provided, so it is sufficient to just check
        // for the latter condition rather than add more complexity. In any
        // case, it is better to set the icon during initial construction.
        JLabel tabLabel;
        if ( tabIcon == null ) {
            tabLabel = new JLabel( tabTitle, SwingConstants.LEADING );
        }
        else {
            tabLabel = new JLabel( tabTitle, tabIcon, SwingConstants.LEADING );
        }
        
        return tabLabel;
    }
    
    /**
     * Returns an ImageIcon that is scaled to fit in a tab.
     * <p>
     * NOTE: Swing TabbedPane does not directly support identifier icons, but
     *  this boilerplate code is needed everywhere and is unlikely to differ by
     *  much, so is provided as a utility method here. In many cases, this icon
     *  will then be loaded into a Label (which also likely will contain the tab
     *  title), so we also provide a variant of this method that wraps this call.
     * 
     * @param iconResourceName The resource name of the icon image to use
     * @return The ImageIcon that is scaled to fit in a tab
     */
    public static ImageIcon makeTabIcon( final String iconResourceName ) {
        if ( ( iconResourceName == null ) || iconResourceName.isEmpty() ) {
            return null;
        }
        
        // Get an icon to show in the upper left corner of a tab.
        final URL iconUrl = IconFactory.class.getResource( iconResourceName );
        return makeTabIcon( iconUrl );
    }
    
    /**
     * Returns an ImageIcon that is scaled to fit in a tab.
     * <p>
     * NOTE: Swing TabbedPane does not directly support identifier icons, but
     *  this boilerplate code is needed everywhere and is unlikely to differ by
     *  much, so is provided as a utility method here. In many cases, this icon
     *  will then be loaded into a Label (which also likely will contain the tab
     *  title), so we also provide a variant of this method that wraps this call.
     * 
     * @param iconUrl The URL of the icon image to use
     * @return The ImageIcon that is scaled to fit in a tab
     */
    public static ImageIcon makeTabIcon( final URL iconUrl ) {
        if ( iconUrl == null ) {
            return null;
        }
        
        // Get an icon to show in the upper left corner of a tab.
        final ImageIcon tabIcon = new ImageIcon( iconUrl );
        final Image normalImage = tabIcon.getImage();
        
        // Make a smaller icon that can fit in a Swing Tab with adequate margins.
        final Image tabImage = normalImage.getScaledInstance( 
            20, 20, Image.SCALE_SMOOTH );
        tabIcon.setImage( tabImage );
        
        return tabIcon;
    }
    
    /**
     * Adds the supplied Tab Component to the correct Tabbed Pane if not already
     * present; but first removes it from the incorrect Tabbed Pane if present
     * there. The Tab Component is combined with a supplied Tab Label, which may
     * include both Text and an Icon, as otherwise we get blank or default text.
     * 
     * @param correctTabbedPane The Tabbed Pane that should receive the Tab
     * @param wrongTabbedPane The Tabbed Pane that should remove the Tab
     * @param tabComponent The Tab Component to use for the Tab to move or add
     * @param tabLabel The Tab Label to use for the Tab to move or add
     */
    public static void moveTabToCorrectTabbedPane(
            final JTabbedPane correctTabbedPane,
            final JTabbedPane wrongTabbedPane,
            final Component tabComponent,
            final JLabel tabLabel ) {
        // To avoid copy/paste code logic, make an array of one to pass forward.
        final JTabbedPane[] wrongTabbedPanes = { wrongTabbedPane };
        
        moveTabToCorrectTabbedPane(
            correctTabbedPane,
            wrongTabbedPanes,
            tabComponent,
            tabLabel );
    }
    
    /**
     * Adds the supplied Tab Component to the correct Tabbed Pane if not already
     * present; but first removes it from the incorrect Tabbed Pane if present
     * there. The Tab Component is combined with a supplied Tab Label, which may
     * include both Text and an Icon, as otherwise we get blank or default text.
     * 
     * @param correctTabbedPane The Tabbed Pane that should receive the Tab
     * @param wrongTabbedPanes The Tabbed Panes that should remove the Tab
     * @param tabComponent The Tab Component to use for the Tab to move or add
     * @param tabLabel The Tab Label to use for the Tab to move or add
     */
    public static void moveTabToCorrectTabbedPane(
            final JTabbedPane correctTabbedPane,
            final JTabbedPane[] wrongTabbedPanes,
            final Component tabComponent,
            final JLabel tabLabel ) {
        // See if the tab component is already present in the correct tabbed pane.
        if ( correctTabbedPane.indexOfComponent( tabComponent ) >= 0 ) {
            return;
        }
        
        // Otherwise, if the tab component is in the wrong tabbed pane, remove it.
        for ( final JTabbedPane wrongTabbedPane : wrongTabbedPanes ) {
            final int tabIndex = wrongTabbedPane.indexOfComponent( tabComponent );
            if ( tabIndex >= 0 ) {
                wrongTabbedPane.remove( tabIndex );
                break;
            }
        }
        
        // Add a closable tab to the correct tabbed pane using our tab component.
        // NOTE: If we don't do it this way, we get blank or "class name" titles.
        addClosableTab( correctTabbedPane, tabComponent, tabLabel );
    }
}
