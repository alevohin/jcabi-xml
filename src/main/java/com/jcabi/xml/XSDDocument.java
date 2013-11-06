/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.xml;

import com.jcabi.aspects.Loggable;
import java.io.IOException;
import java.io.StringReader;
import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import lombok.EqualsAndHashCode;
import org.xml.sax.SAXException;

/**
 * Implementation of {@link XSD}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.5
 */
@EqualsAndHashCode(of = "xsd")
@Loggable(Loggable.DEBUG)
public final class XSDDocument implements XSD {

    /**
     * Schema factory.
     */
    private static final SchemaFactory FACTORY =
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    /**
     * XSD document.
     */
    @NotNull
    private final transient Source xsd;

    /**
     * Public ctor, from XML as a source.
     * @param src XSL document body
     */
    public XSDDocument(@NotNull final XML src) {
        this(new DOMSource(src.node()));
    }

    /**
     * Public ctor, from XSL as a string.
     * @param src XML document body
     */
    public XSDDocument(@NotNull final String src) {
        this(new StreamSource(new StringReader(src)));
    }

    /**
     * Public ctor, from XML as a source.
     * @param src XML document body
     */
    public XSDDocument(@NotNull final Source src) {
        this.xsd = src;
    }

    @Override
    public String toString() {
        return this.xsd.toString();
    }

    @Override
    @NotNull
    public boolean validate(final XML xml) {
        final Schema schema;
        try {
            schema = XSDDocument.FACTORY.newSchema(this.xsd);
        } catch (SAXException ex) {
            throw new IllegalStateException(ex);
        }
        final Validator validator = schema.newValidator();
        try {
            validator.validate(new DOMSource(xml.node()));
        } catch (SAXException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        return true;
    }

}
