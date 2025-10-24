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
package com.marineacoustics.jgui.frame;

import com.mhschmieder.jcontrols.util.ForegroundManager;
import com.mhschmieder.jgraphics.color.ColorUtilities;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;

/**
 * {@code XFileChooser} is an enhancement of the basic {@link JFileChooser}
 * class, that formalizes some of the most common usage scenarios.
 * <p>
 * This is legacy code and does not include special handling that I added for
 * the QAqua Look and Feel for Java 1.6, and which didn't port very well to Java
 * 8 anyway. It was in fact a primary motivator behind switching to JavaFX for
 * application development
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class XFileChooser extends JFileChooser implements ForegroundManager {
    /**
     * Unique Serial Version ID for this class, to avoid class loader conflicts.
     */
    private static final long serialVersionUID = -2671355581124127118L;

    /**
     * The preferred size for the file chooser on next launch.
     * <p>
     * This is an important feature enhancement, as non-native Swing file
     * choosers don't behave very well on most platforms, especially regarding
     * retention of user preferences from the previous launch.
     */
    private Dimension         preferredSize;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * Constructs an {@code XFileChooser} pointing to the user's default
     * directory. This default depends on the operating system.
     *
     * @version 1.0
     */
    public XFileChooser() {
        // Always call the superclass constructor first!
        super();

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /**
     * Constructs an {@code XFileChooser} using the given path. Passing in a
     * {@code null} string causes the file chooser to point to the user's
     * default directory. This default depends on the operating system.
     *
     * @param currentDirectoryPath
     *            A {@link String} giving the path to a file or directory
     *
     * @version 1.0
     */
    public XFileChooser( final String currentDirectoryPath ) {
        // Always call the superclass constructor first!
        super( currentDirectoryPath );

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /**
     * Constructs an {@code XFileChooser} using the given {@link File}. Passing
     * in a {@code null} file causes the file chooser to point to the user's
     * default directory. This default depends on the operating system.
     *
     * @param currentDirectory
     *            A {@link File} object specifying the path to a file or
     *            directory
     *
     * @version 1.0
     */
    public XFileChooser( final File currentDirectory ) {
        // Always call the superclass constructor first!
        super( currentDirectory );

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /**
     * Constructs an {@code XFileChooser} using the given
     * {@link FileSystemView}, and setting the file chooser to point to the
     * user's default directory. This default depends on the operating system..
     *
     * @param fileSystemView
     *            The OS-specific file system view, usually pointing to the
     *            user's default directory
     *
     * @version 1.0
     */
    public XFileChooser( final FileSystemView fileSystemView ) {
        // Always call the superclass constructor first!
        super( fileSystemView );

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /**
     * Constructs an {@code XFileChooser} using the given path. Passing in a
     * {@code null} string causes the file chooser to point to the user's
     * default directory. This default depends on the operating system.
     *
     * @param currentDirectoryPath
     *            A {@link String} giving the path to a file or directory
     * @param fileSystemView
     *            The OS-specific file system view, usually pointing to the
     *            specified directory
     *
     * @version 1.0
     */
    public XFileChooser( final String currentDirectoryPath, final FileSystemView fileSystemView ) {
        // Always call the superclass constructor first!
        super( currentDirectoryPath, fileSystemView );

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /**
     * Constructs an {@code XFileChooser} using the given {@link File}. Passing
     * in a {@code null} file causes the file chooser to point to the user's
     * default directory. This default depends on the operating system.
     *
     * @param currentDirectory
     *            A {@link File} object specifying the path to a file or
     *            directory
     * @param fileSystemView
     *            The OS-specific file system view, usually pointing to the
     *            specified directory
     *
     * @version 1.0
     */
    public XFileChooser( final File currentDirectory, final FileSystemView fileSystemView ) {
        // Always call the superclass constructor first!
        super( currentDirectory, fileSystemView );

        // Cache the initial size after constructing the file chooser.
        preferredSize = getPreferredSize();
    }

    /////////////// ForegroundManager implementation methods /////////////////

    /**
     * Sets the appropriate foreground color for this file chooser based on the
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
     *            The current background color to apply to this file chooser
     *
     * @since 1.0
     */
    @Override
    public void setForegroundFromBackground( final Color backColor ) {
        // Make sure the foreground color is never masked by the background.
        final Color foreColor = ColorUtilities.getForegroundFromBackground( backColor );

        setBackground( backColor );
        setForeground( foreColor );
    }

    ////////////////// File Chooser manipulation methods /////////////////////

    /**
     * Sets the primary properties of the file chooser. These parameters are not
     * available in any of the available constructors, so it is convenient to
     * combine them into one utility function here.
     *
     * @param defaultDirectory
     *            The default directory for initial selection in the file
     *            chooser
     * @param defaultFile
     *            The default file to pre-select in the file chooser (can be
     *            {@code null} for no selection
     * @param multiSelectionIsEnabled
     *            {@code true} if multi selection is enabled; {@code false} if
     *            only single selection is enabled
     *
     * @version 1.0
     */
    public final void setProperties( final File defaultDirectory,
                                     final File defaultFile,
                                     final boolean multiSelectionIsEnabled ) {
        // Use the most recent directory as the default, if there is one.
        if ( defaultDirectory != null ) {
            setCurrentDirectory( defaultDirectory );
        }

        // Conditionally select the default file (if provided).
        if ( defaultFile != null ) {
            setSelectedFile( defaultFile );
        }

        // Disallow the "AllFile" filter, as it prevents default extensions from
        // being appended to new file selection that have no extension suffix.
        setAcceptAllFileFilterUsed( false );

        // Make sure that hidden files do not appear in the files list (by
        // default they are not, but we disable them explicitly as some LAF's
        // may override this behavior).
        setFileHidingEnabled( true );

        // Prevent non-files from displaying in the selection list.
        setFileSelectionMode( JFileChooser.FILES_ONLY );

        // Allow multi-selection per context (supplied by the caller).
        setMultiSelectionEnabled( multiSelectionIsEnabled );
    }

    //////////////////// JFileChooser method overrides ///////////////////////

    /**
     * Pops up an "Open File" file chooser dialog. Note that the text that
     * appears in the approve button is determined by the L&amp;F.
     * <p>
     * The reason that this method is overridden, is that the file chooser by
     * default does not cache the current size to use on re-launch.
     *
     * @param parent
     *            The parent component of the dialog, can be {@code null}; see
     *            {@code showDialog} for details
     * @return The return state of the file chooser on pop-down:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION if an error occurs or the dialog is
     *         dismissed
     *         </ul>
     * @exception HeadlessException
     *                If {@code GraphicsEnvironment.isHeadless()} returns
     *                {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #showDialog
     *
     * @version 1.0
     */
    @Override
    public int showOpenDialog( final Component parent ) {
        // Restore the cached file chooser size from previous launch.
        setPreferredSize( preferredSize );

        int fileStatus = JFileChooser.ERROR_OPTION;

        try {
            fileStatus = super.showOpenDialog( parent );
        }
        catch ( final HeadlessException he ) {
            he.printStackTrace();
            return fileStatus;
        }

        // Cache the file chooser size for next launch.
        preferredSize = getSize();

        return fileStatus;
    }

    /**
     * Pops up a "Save File" file chooser dialog. Note that the text that
     * appears in the approve button is determined by the L&amp;F.
     * <p>
     * The reason that this method is overridden, is that the file chooser by
     * default does not cache the current size to use on re-launch.
     *
     * @param parent
     *            The parent component of the dialog, can be {@code null}; see
     *            {@code showDialog} for details
     * @return The return state of the file chooser on pop-down:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION if an error occurs or the dialog is
     *         dismissed
     *         </ul>
     * @exception HeadlessException
     *                If {@code GraphicsEnvironment.isHeadless()} returns
     *                {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #showDialog
     *
     * @version 1.0
     */
    @Override
    public int showSaveDialog( final Component parent ) {
        // Restore the cached file chooser size from previous launch.
        setPreferredSize( preferredSize );

        int fileStatus = JFileChooser.ERROR_OPTION;

        try {
            fileStatus = super.showSaveDialog( parent );
        }
        catch ( final HeadlessException he ) {
            he.printStackTrace();
            return fileStatus;
        }

        // Cache the file chooser size for next launch.
        preferredSize = getSize();

        return fileStatus;
    }

    /**
     * Pops up a custom file chooser dialog with a custom approve button.
     * <p>
     * See {@link JFileChooser#showDialog} for a complete description.
     * <p>
     * The reason that this method is overridden, is that the file chooser by
     * default does not cache the current size to use on re-launch.
     *
     * @param parent
     *            The parent component of the dialog, can be {@code null}; see
     *            {@code showDialog} for details
     * @return The return state of the file chooser on pop-down:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION if an error occurs or the dialog is
     *         dismissed
     *         </ul>
     * @exception HeadlessException
     *                If {@code GraphicsEnvironment.isHeadless()} returns
     *                {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #showDialog
     *
     * @version 1.0
     */
    @Override
    public int showDialog( final Component parent, final String approveButtonText ) {
        // Restore the cached file chooser size from previous launch.
        setPreferredSize( preferredSize );

        int fileStatus = JFileChooser.ERROR_OPTION;

        try {
            fileStatus = super.showDialog( parent, approveButtonText );
        }
        catch ( final HeadlessException he ) {
            he.printStackTrace();
            return fileStatus;
        }

        // Cache the file chooser size for next launch.
        preferredSize = getSize();

        return fileStatus;
    }

    /**
     * Sets the accessory component. An accessory is often used to show a
     * preview image of the selected file; however, it can be used for anything
     * that the programmer wishes, such as extra custom file chooser controls.
     * <p>
     * If there was a previous accessory, you should unregister any listeners
     * that the accessory might have registered with the file chooser.
     * <p>
     * The reason that this method is overridden, is that the file chooser by
     * default does not resize to fit the accessory component.
     *
     * @param newAccessory
     *            The new accessory component to set on the file chooser.
     *
     * @version 1.0
     */
    @Override
    public void setAccessory( final JComponent newAccessory ) {
        super.setAccessory( newAccessory );

        // Re-validate the new accessory to compute sizing, etc.
        newAccessory.revalidate();

        // Cache the initial size after re-validating the new accessory.
        preferredSize = getPreferredSize();
    }

}
