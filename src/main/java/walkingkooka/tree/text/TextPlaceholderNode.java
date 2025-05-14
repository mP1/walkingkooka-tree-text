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
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;

/**
 * A placeholder which should be replaced by actual text during rendering, but does not contain any text by itself.
 */
public final class TextPlaceholderNode extends TextLeafNode<TextPlaceholderName> {

    public final static TextNodeName NAME = TextNodeName.with("Placeholder");

    static TextPlaceholderNode with(final TextPlaceholderName value) {
        Objects.requireNonNull(value, "value");
        return new TextPlaceholderNode(NO_INDEX, value);
    }

    private TextPlaceholderNode(final int index, final TextPlaceholderName text) {
        super(index, text);
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    @Override
    public TextPlaceholderNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    TextPlaceholderNode replace1(final int index, final TextPlaceholderName value) {
        return new TextPlaceholderNode(index, value);
    }

    // HasText..........................................................................................................

    @Override
    public String text() {
        return "";
    }

    // toHtml...........................................................................................................

    /**
     * Placeholders must be resolved before converting to html.
     */
    @Override
    public String toHtml() {
        throw new UnsupportedOperationException();
    }

    // JsonNode.........................................................................................................

    /**
     * Accepts a json string which holds a {@link TextPlaceholderNode}.
     */
    static TextPlaceholderNode unmarshallTextPlaceholderNode(final JsonNode node,
                                                             final JsonNodeUnmarshallContext context) {
        return TextPlaceholderNode.with(context.unmarshall(node, TextPlaceholderName.class));
    }

    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshall(this.value);
    }

    // Visitor .......................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        visitor.visit(this);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("Placeholder " + CharSequences.quote(this.value().value()));
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToString0(final ToStringBuilder b) {
        b.value(this.value);
    }
}
