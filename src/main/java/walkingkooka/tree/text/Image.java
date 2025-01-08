/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.text;

import walkingkooka.ToStringBuilder;
import walkingkooka.net.Url;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;

/**
 * Holds a link to an image.
 */
public final class Image extends TextLeafNode<Url> {

    public final static TextNodeName NAME = TextNodeName.with("Image");

    static Image with(final Url value) {
        Objects.requireNonNull(value, "value");
        return new Image(NO_INDEX, value);
    }

    private Image(final int index, final Url text) {
        super(index, text);
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    @Override
    public Image removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    Image replace1(final int index, final Url value) {
        return new Image(
            index,
            value
        );
    }

    // HasText..........................................................................................................

    @Override
    public String text() {
        return "";
    }

    // toHtml...........................................................................................................

    /**
     * Renders an IMG with a SRC
     */
    @Override
    public String toHtml() {
        return "<IMG src=\"" + this.value + "\"/>";
    }

    // Visitor .......................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        visitor.visit(this);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("Image " + CharSequences.quote(this.value().value()));
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Image;
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToString0(final ToStringBuilder b) {
        b.value(this.value);
    }

    // JsonNode.........................................................................................................

    /**
     * Accepts a json string which holds a {@link Image}.
     */
    static Image unmarshallImage(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        return Image.with(
            context.unmarshall(
                node,
                Url.class
            )
        );
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshall(this.value);
    }
}
