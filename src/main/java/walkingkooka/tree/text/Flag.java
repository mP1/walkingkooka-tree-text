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
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Iterator;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Holds an flag for a single country code.
 */
public final class Flag extends TextLeafNode<String> {

    public final static TextNodeName NAME = TextNodeName.with("flag");

    static Flag with(final String countryCode) {
        return new Flag(
            NO_INDEX,
            checkCountryCode(countryCode)
        );
    }

    private Flag(final int index,
                 final String countryCode) {
        super(index, countryCode);
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    @Override
    public Flag removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    Flag replace1(final int index,
                  final String value) {
        return new Flag(
            index,
            value
        );
    }

    // normalize........................................................................................................

    @Override
    public Flag normalize() {
        return this;
    }

    @Override
    void normalizeSiblings(final Iterator<TextNode> following,
                           final Consumer<TextNode> siblings) {
        siblings.accept(this);
    }

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.value; // the country code
    }

    @Override
    public Flag setText(final String countryCode) {
        return this.replaceChildrenWithText(
            checkCountryCode(countryCode)
        );
    }

    private static String checkCountryCode(final String countryCode) {
        CharSequences.failIfNullOrEmpty(countryCode, "countryCode");

        final int length = countryCode.length();
        if (2 != length) {
            throw new IllegalArgumentException("Invalid flag country code: " + CharSequences.quoteAndEscape(countryCode) + " not 2 letters");
        }

        return countryCode.toUpperCase(ENGLISH); // always upper-cased
    }

    // Locale.ENGLISH constant absent from GWT's Locale
    private final static Locale ENGLISH = Locale.forLanguageTag("EN");

    // toHtml...........................................................................................................

    /**
     * Renders the unicode as a HTML entity withe number codes for each character.
     * This assumes that a font with the flag emojis or similar is already set.
     */
    @Override
    public String toHtml() {
        final String value = this.value;

        // The Unicode sequence for the Australian flag emoji (🇦🇺)
        // is a combination of two regional indicator symbols: U+1F1E6 (🇦) and U+1F1FA (🇺).
        return hex(
            value.charAt(0)
        ) +
            hex(
                value.charAt(1)
            );
    }

    // &#x1F1E6; &#x1F1FA;	&#127462; &#127482;
    private static String hex(final char c) {
        return "&#x" +
            Integer.toHexString(
                REGIONAL_INDICATOR_SYMBOL + ((int) Character.toUpperCase(c) - 'A')
            ).toUpperCase(ENGLISH) +
            ";";
    }

    private final static int REGIONAL_INDICATOR_SYMBOL = 0x1f1e6;

    // Visitor .........................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        visitor.visit(this);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(
            "Flag " +
                CharSequences.quote(
                    this.value()
                )
        );
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToString0(final ToStringBuilder b) {
        b.disable(ToStringBuilderOption.QUOTE);
        b.value(this.value);
    }

    // JsonNode.........................................................................................................

    /**
     * Accepts a json string which holds a {@link Flag}.
     */
    static Flag unmarshallFlag(final JsonNode node,
                               final JsonNodeUnmarshallContext context) {
        return Flag.with(
            context.unmarshall(
                node,
                String.class
            )
        );
    }

    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshall(
            this.value
        );
    }
}
