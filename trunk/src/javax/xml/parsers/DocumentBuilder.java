/**
 * $Id$
 */
package javax.xml.parsers;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import org.xml.sax.Parser;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;

/**
 * Defines the API to obtain DOM Document instances from an XML
 * document. Using this class, an application programmer can obtain a
 * <code>org.w3c.dom.Document</code> from XML.<p>
 *
 * An instance of this class can be obtained from the
 * <code>DocumentBuilderFactory.newDocumentBuilder</code> method. Once
 * an instance of this class is obtained, XML can be parsed from a
 * variety of input sources. These input sources are InputStreams,
 * Files, URLs, and SAX InputSources.<p>
 *
 * Note that this class reuses several classes from the SAX API. This
 * does not require that the implementor of the underlying DOM
 * implmenetation use a SAX parser to parse XML document into a
 * <code>Document</code>. It merely requires that the implementation
 * communicate with the application using these existing APIs.
 *
 * @since JAXP 1.0
 * @version 1.0
 */

public abstract class DocumentBuilder {

    protected DocumentBuilder () {
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object.
     *
     * @param is InputStream containing the content to be parsed.
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @exception IllegalArgumentException If the InputStream is null
     * @see org.xml.sax.DocumentHandler
     */
    
    public Document parse(InputStream is)
        throws SAXException, IOException
    {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        
        InputSource in = new InputSource(is);
        return parse(in);
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object.
     *
     * @param is InputStream containing the content to be parsed.
     * @param systemId Provide a base for resolving relative URIs. 
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @exception IllegalArgumentException If the InputStream is null
     * @see org.xml.sax.DocumentHandler
     */
    
    public Document parse(InputStream is, String systemId)
        throws SAXException, IOException
    {
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        
        InputSource in = new InputSource(is);
	in.setSystemId(systemId);
        return parse(in);
    }

    /**
     * Parse the content of the given URI as an XML document
     * and return a new DOM Document object.
     *
     * @param uri The location of the content to be parsed.
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @exception IllegalArgumentException If the URI is null
     * @see org.xml.sax.DocumentHandler
     */
    
    public Document parse(String uri)
        throws SAXException, IOException
    {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        
        InputSource in = new InputSource(uri);
        return parse(in);
    }

    /**
     * Parse the content of the given file as an XML document
     * and return a new DOM Document object.
     *
     * @param f The file containing the XML to parse
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @exception IllegalArgumentException If the file is null
     * @see org.xml.sax.DocumentHandler
     */
    
    public Document parse(File f)
       throws SAXException, IOException
    {
        if (f == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        
        String uri = "file:" + f.getAbsolutePath();
	if (File.separatorChar == '\\') {
	    uri = uri.replace('\\', '/');
	}
        InputSource in = new InputSource(uri);
        return parse(in);
    }

    /**
     * Parse the content of the given input source as an XML document
     * and return a new DOM Document object.
     *
     * @param is InputSource containing the content to be parsed.
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @exception IllegalArgumentException If the InputSource is null
     * @see org.xml.sax.DocumentHandler
     */
    
    public abstract Document parse(InputSource is)
        throws  SAXException, IOException;

    
    /**
     * Indicates whether or not this parser is configured to
     * understand namespaces.
     */

    public abstract boolean isNamespaceAware();

    /**
     * Indicates whether or not this parser is configured to
     * validate XML documents.
     */
    
    public abstract boolean isValidating();

    /**
     * Specify the <code>EntityResolver</code> to be used to resolve
     * entities present in the XML document to be parsed. Setting
     * this to <code>null</code> will result in the underlying
     * implementation using it's own default implementation and
     * behavior.
     */

    // XXX
    // Add that the underlying impl doesn't have to use SAX, but
    // must understand how to resolve entities from this object.
    
    public abstract void setEntityResolver(org.xml.sax.EntityResolver er);

    /**
     * Specify the <code>ErrorHandler</code> to be used to resolve
     * entities present in the XML document to be parsed. Setting
     * this to <code>null</code> will result in the underlying
     * implementation using it's own default implementation and
     * behavior.
     */

    // XXX
    // Add that the underlying impl doesn't have to use SAX, but
    // must understand how to handle errors using this object.
    
    public abstract void setErrorHandler(org.xml.sax.ErrorHandler eh);

    /**
     * Obtain a new instance of a DOM Document object to build a DOM
     * tree with.
     */
    
    public abstract Document newDocument();

    /**
     * Get the DOMImplementation object.
     */

    public abstract DOMImplementation getDOMImplementation();
}
