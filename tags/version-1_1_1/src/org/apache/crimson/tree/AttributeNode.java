/*
 * $Id$
 *
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

import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.*;
import org.apache.crimson.util.XmlNames;

/**
 * Node representing an XML attribute.  Many views of attributes can be
 * useful, but only the first of these is explicitly supported: <DL>
 *
 *      <DT> <em>Logical View</em> </DT><DD>  Attributes always hold a
 *      string created by expanding character and entity references from
 *      source text conforming to the XML specification.  If this
 *      attribute was declared in a DTD, normalization will often be
 *      done to eliminate insignificant whitespace.</DD>
 *
 *      <DT> <em>DTD Validated View</em> </DT><DD>  If the attribute was
 *      declared in a DTD, it will have minimal semantics provided by its
 *      declaration, and checked by validating parsers.  For example, the
 *      logical view may name one (or many) unparsed entities or DOM nodes.
 *      This view could provide direct access to them for any DTD, since
 *      these attribute semantics are defined by XML itself.</DD>
 *
 *      <DT> <em>Semantic View</em> </DT><DD> The person who wrote the
 *      DTD (or other namespace) defined what each attribute's logical
 *      view "means".  For example, that it's a URL, or that the unparsed
 *      entity referred to identifies a particular database to be used.
 *      This view would provide direct access to such values, but would
 *      need to have code specialized to that DTD or namespace.</DD>
 *
 *      <DT> <em>Physical View</em> </DT><DD>  Attributes may have children
 *      to represent text and entity reference nodes found in unexpanded
 *      and unnormalized XML source text.  Such views are mostly of interest
 *      when editing XML text that is not dynamically generated by programs.
 *      This implementation does not currently support physical views of
 *      attributes.  </DD>
 *
 *      </DL>
 *
 *
 * @author David Brownell
 * @author Rajiv Mordani
 * @version $Revision$
 */
public class AttributeNode extends NamespacedNode implements Attr
{
    private String value;
    private boolean specified;
    private String defaultValue;

    /** At construction time ownerElement is null, it gets set whenever an
        attribute is set on an Element */
    private Element ownerElement;

    /** Constructs an attribute node. Used for SAX2 and DOM2 */
    public AttributeNode(String namespaceURI, String qName,
                         String value, boolean specified, String defaultValue)
        throws DOMException
    {
        super(namespaceURI, qName);
        this.value = value;
        this.specified = specified;
        this.defaultValue = defaultValue;
    }

    /**
     * Make a clone of this node and return it.  Used for cloneNode().
     */
    AttributeNode makeClone() {
        AttributeNode retval = new AttributeNode(namespaceURI, qName, value,
                                                 specified, defaultValue);
        retval.ownerDocument = ownerDocument;
        return retval;
    }

    /**
     * Package private method to check arguments for constructor
     */
    static void checkArguments(String namespaceURI, String qualifiedName)
        throws DomEx
    {
        // [6] QName ::= (Prefix ':')? LocalPart
        // [7] Prefix ::= NCName
        // [8] LocalPart ::= NCName

        if (qualifiedName == null) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }

        int first = qualifiedName.indexOf(':');

        if (first <= 0) {
            // no Prefix, only check LocalPart
            if (!XmlNames.isUnqualifiedName(qualifiedName)) {
                throw new DomEx(DomEx.INVALID_CHARACTER_ERR);
            }
            if ("xmlns".equals(qualifiedName) &&
                !XmlNames.SPEC_XMLNS_URI.equals(namespaceURI)) {
                throw new DomEx(DomEx.NAMESPACE_ERR);
            }
            return;
        }

        // Prefix exists, check everything

        int last = qualifiedName.lastIndexOf(':');
        if (last != first) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }
        
        String prefix = qualifiedName.substring(0, first);
        String localName = qualifiedName.substring(first + 1);
        if (!XmlNames.isUnqualifiedName(prefix)
                || !XmlNames.isUnqualifiedName(localName)) {
            throw new DomEx(DomEx.INVALID_CHARACTER_ERR);
        }

        // If we get here then we must have a valid prefix
        if (namespaceURI == null
            || ("xml".equals(prefix)
                && !XmlNames.SPEC_XML_URI.equals(namespaceURI))) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }
    }

    // package private
    String getDefaultValue ()
    {
        return defaultValue;
    }

    /**
     * <b>DOM2:</b>
     */
    public Element getOwnerElement() {
        return ownerElement;
    }

    // package private
    void setOwnerElement(Element element) {
        if (element != null && ownerElement != null) {
            throw new IllegalStateException(getMessage("A-000", 
                            new Object[] { element.getTagName() }));
        }
        ownerElement = element;
    }

    
    /** DOM:  Returns the ATTRIBUTE_NODE node type constant. */
    public short getNodeType () { return ATTRIBUTE_NODE; }

    /** DOM:  Returns the attribute name */
    public String getName () { return qName; }

    /** DOM:  Returns the attribute value. */
    public String getValue () { return value; }

    /** DOM:  Assigns the value of this attribute. */
    public void setValue (String value) { setNodeValue (value); }

    /** DOM:  Returns the attribute value. */
    public String getNodeValue () { return value; }

    /** DOM:  Returns true if the source text specified the attribute. */
    public boolean getSpecified () { return specified; }

    /** DOM:  Assigns the value of this attribute. */
    public void setNodeValue (String value)
    {
        if (isReadonly ())
            throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
        this.value = value;
        specified = true;
    }

    /** Flags whether the source text specified the attribute. */
    // pubic
    void setSpecified (boolean specified) { this.specified = specified; }

    /** DOM:  Returns null */
    public Node getParentNode () { return null; }
    
    /** DOM:  Returns null */
    public Node getNextSibling () { return null; }
    
    /** DOM:  Returns null */
    public Node getPreviousSibling () { return null; }

    
    /**
     * Writes the attribute out, as if it were assigned within an
     * element's starting tag (<em>name="value"</em>).
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
        Writer  out = context.getWriter ();

        out.write (qName);
        out.write ("=\"");
        writeChildrenXml (context);
        out.write ('"');
    }

    /**
     * Writes the attribute's value.
     */
    public void writeChildrenXml (XmlWriteContext context) throws IOException
    {
        Writer  out = context.getWriter ();
        for (int i = 0; i < value.length (); i++) {
            int c = value.charAt (i);
            switch (c) {
              // XXX only a few of these are necessary; we
              // do what "Canonical XML" expects
              case '<':  out.write ("&lt;"); continue;
              case '>':  out.write ("&gt;"); continue;
              case '&':  out.write ("&amp;"); continue;
              case '\'': out.write ("&apos;"); continue;
              case '"':  out.write ("&quot;"); continue;
              default:   out.write (c); continue;
            }
        }
    }

    /**
     * DOM: returns a copy of this node which is not owned by an Element
     */
    public Node cloneNode(boolean deep) {
        AttributeNode attr = cloneAttributeNode(deep);
        // DOM says specified should be true
        attr.specified = true;
        return attr;
    }

    /**
     * Clone this AttributeNode and possibly its children (which cannot be
     * AttributeNodes themselves).  "ownerElement" will remain null.
     */
    AttributeNode cloneAttributeNode(boolean deep) {
        try {
            AttributeNode attr = makeClone();
            if (deep) {
                Node node;
                for (int i = 0; (node = item (i)) != null; i++) {
                    node = node.cloneNode (true);
                    attr.appendChild (node);
                }
            }
            return attr;
        } catch (DOMException e) {
            throw new RuntimeException (getMessage ("A-002"));
        }
    }

    void checkChildType(int type) throws DOMException {
        switch(type) {
          case TEXT_NODE:
          case ENTITY_REFERENCE_NODE:
            return;
          default:
            throw new DomEx (DomEx.HIERARCHY_REQUEST_ERR);
        }
    }
}
