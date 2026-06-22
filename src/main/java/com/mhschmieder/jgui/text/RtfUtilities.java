/*
 * MIT License
 *
 * Copyright (c) 2020, 2026 Mark Schmieder. All rights reserved.
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
 * This file is part of the jgui Library
 *
 * You should have received a copy of the MIT License along with the jgui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/jgui
 */
package com.mhschmieder.jgui.text;

import com.mhschmieder.jcommons.io.IoUtilities;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * {@code RtfUtilities} is a utilities class for handling the RTF format, and
 * especially for converting from RTF to HTML. Although it must make use of the
 * Swing toolkit to accomplish this, the functionality is still closely related
 * to the rest of this library's emphasis on AWT as the Swing invocations are
 * strictly to gain access to the necessary conversion methods and don't
 * require an actual Swing GUI for the application that uses these utilities.
 * <p>
 * This class and its methods should be reviewed to see if they are too
 * specific compared to what they actually do; perhaps the methods should add
 * parameters for the source and destination MIME types, and see what all works?
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class RtfUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private RtfUtilities() {}

    /**
     * This is the W3C approved standard Mime Type for RTF Documents.
     */
    public static final String MIME_TYPE_RTF  = "text/rtf";

    /**
     * This is the W3C approved standard Mime Type for HTML Documents.
     */
    public static final String MIME_TYPE_HTML = "text/html";

    /**
     * Returns a {@link String} containing a single aggregated HTML content
     * stream, as converted from the original RTF content.
     * <p>
     * This method translates RTF content to HTML using some little-known
     * Swing toolkit methods that are hidden away on the {@link JEditorPane}
     * class. There is no need for the invoker of this method to be part of a
     * Swing GUI application. Functions started getting placed in some strange
     * places when the "javax" package name was added for Swing and other
     * functionality beyond the original AWT toolkit.
     * <p>
     * The main problem with this Swing dependency is that this may cause
     * problems with modularization later on, as Swing may otherwise not get
     * pulled into every build, so it's best to avoid when it isn't key to the
     * application's GUI.
     *
     * @param rtfReader
     *            The {@link Reader} that wraps the RTF content to translate to
     *            HTML
     * @return The original RTF content converted to a single HTML string
     *
     * @since 1.0
     */
    public static String rtfToHtml( final Reader rtfReader ) {
        // Unfortunately we must instantiate a non-displayed Swing GUI component
        // in order to gain access to the editor framework in Swing.
        final JEditorPane editorPane = new JEditorPane();

        // First get and set the editor kit and document model for RTF input.
        //
        // We must explicitly set the editor kit on the input format.
        final EditorKit kitRtf = editorPane.getEditorKitForContentType(
                MIME_TYPE_RTF );
        editorPane.setEditorKit( kitRtf );
        final Document rtfDocument = editorPane.getDocument();

        try {
            // Read the RTF input into the editor framework.
            kitRtf.read( rtfReader, rtfDocument, 0 );

            // Now get the editor kit and document model for HTML output.
            //
            // We only set the editor kit on the input format.
            final EditorKit kitHtml = editorPane.getEditorKitForContentType(
                    MIME_TYPE_HTML );
            final Document htmlDocument = editorPane.getDocument();

            // Write the HTML output from the editor framework.
            final Writer writer = new StringWriter();
            kitHtml.write( writer, htmlDocument, 0, htmlDocument.getLength() );

            // Return the entire HTML content as a single string.
            return writer.toString();
        }
        catch ( final BadLocationException | IOException e ) {
            e.printStackTrace();
        }

        // Avoid downstream problems by returning an empty string.
        return "";
    }

    /**
     * Returns a {@link String} containing a single aggregated HTML content
     * stream, as converted from the original RTF content.
     * <p>
     * This method translates RTF content to HTML using some little-known
     * Swing toolkit methods that are hidden away on the {@link JEditorPane}
     * class. There is no need for the invoker of this method to be part of a
     * Swing GUI application. Functions started getting placed in some strange
     * places when the "javax" package name was added for Swing and other
     * functionality beyond the original AWT toolkit.
     * <p>
     * The main problem with this Swing dependency is that this may cause
     * problems with modularization later on, as Swing may otherwise not get
     * pulled into every build, so it's best to avoid when it isn't key to the
     * application's GUI.
     *
     * @param rtfFilename
     *            The name of the file that contains the RTF content to
     *            translate to HTML
     * @return The original RTF content converted to a single HTML string
     *
     * @since 1.0
     */
    public static String rtfToHtml( final String rtfFilename ) {
        // Read the RTF file contents into a String and convert to HTML.
        try ( final InputStream inputStream = RtfUtilities.class
                .getResourceAsStream( rtfFilename ) ) {
            // Convert the text based file to a standard string message.
            final String rtfContent = IoUtilities.streamToString( inputStream,
                                                                  StandardCharsets.UTF_8 );
            final String htmlContent = rtfToHtml( new StringReader( rtfContent ) );
            return htmlContent;
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

}
