/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Crimson" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Sun Microsystems, Inc., 
 * http://www.sun.com.  For more information on the Apache Software 
 * Foundation, please see <http://www.apache.org/>.
 */

package org.apache.crimson.tree;

import org.w3c.dom.*;
import org.apache.crimson.util.XmlNames;

/**
 * This class implements the DOM <em>DOMImplementation</em> interface.
 *
 * @author Edwin Goei
 * @version $Revision$
 */
public class DOMImplementationImpl implements DOMImplementation
{
    /** DOM implementation singleton. */
    private static DOMImplementationImpl singleton =
        new DOMImplementationImpl();

    /** NON-DOM: Obtain and return the single shared object */
    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }  

    public DOMImplementationImpl() {
        // No-op
    }

    /**
     * Test if the DOM implementation implements a specific feature.
     */
    public boolean hasFeature(String feature, String version) {
        return hasFeature0(feature, version);
    }

    /**
     * Reports on features that this implementation supports.  Allows code to
     * be shared with NodeBase.supports().
     */
    static boolean hasFeature0(String feature, String version) {
	if (!"XML".equalsIgnoreCase(feature)) {
	    return false;
        }
        if (version == null || "2.0".equals(version)
            || "1.0".equals(version)) {
            return true;
        }
        return false;
    }

    /**
     * Creates an empty <code>DocumentType</code> node.
     */
    public DocumentType createDocumentType(String qualifiedName,
                                           String publicId,
                                           String systemId)
    {
        if (!XmlNames.isName(qualifiedName)) {
            throw new DomEx(DOMException.INVALID_CHARACTER_ERR);
        }
        if (!XmlNames.isQualifiedName(qualifiedName)) {
            throw new DomEx(DOMException.NAMESPACE_ERR);
        }

        // Note that DOM2 specifies that ownerDocument = null
        return new Doctype(qualifiedName, publicId, systemId,
                           /* internalSubset */ null);
    }

    /**
     * Creates an XML <code>Document</code> object of the specified type with 
     * its document element.
     */
    public Document createDocument(String namespaceURI, 
                                   String qualifiedName, 
                                   DocumentType doctype)
        throws DOMException
    {
        // Create document and if doctype is specified appends it to the
        // document.  Note: WRONG_DOCUMENT_ERR is checked by appendChild().
        Document doc = new XmlDocument();
        if (doctype != null) {
            doc.appendChild(doctype);
        }

        // Create document element and append it
        // Note: name exceptions are checked by createElementNS()
        Element docElement = doc.createElementNS(namespaceURI, qualifiedName);
        doc.appendChild(docElement);

        return doc;
    }
}
