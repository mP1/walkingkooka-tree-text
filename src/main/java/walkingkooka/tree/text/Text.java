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
import walkingkooka.ToStringBuilderOption;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;

/**
 * Holds plain text which may be empty.
 */
public final class Text extends TextLeafNode<String> implements HasText {

    public final static TextNodeName NAME = TextNodeName.with("Text");

    static Text with(final String value) {
        Objects.requireNonNull(value, "value");
        return new Text(NO_INDEX, value);
    }

    private Text(final int index, final String text) {
        super(index, text);
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Would be setter that returns a {@link Text} with the given text creating a new instance if necessary
     */
    public Text setText(final String text) {
        return this.setValue0(text).cast();
    }

    @Override
    public Text removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    Text replace1(final int index, final String value) {
        return new Text(index, value);
    }

    // HasText.........................................................................................................

    @Override
    public String text() {
        return this.value();
    }

    // toHtml...........................................................................................................

    /**
     * Returns the HTML equivalent of this {@link TextNode}.
     */
    @Override
    public String toHtml() {
        return this.text()
            .replace("\"", "&quot;")
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace(" ", "&nbsp;");
    }

    // JsonNodeContext..................................................................................................

    /**
     * Accepts a json string which holds text.
     */
    static Text unmarshallText(final JsonNode node,
                               final JsonNodeUnmarshallContext context) {
        return Text.with(node.stringOrFail());
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.value);
    }

    // Visitor .......................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        visitor.visit(this);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("Text " + CharSequences.quoteAndEscape(this.text()));
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Text;
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToString0(final ToStringBuilder b) {
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.value(this.value);
    }
}
