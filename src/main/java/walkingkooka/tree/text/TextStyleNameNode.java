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

import walkingkooka.NeverError;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * A {@link TextNode} with a {@link TextStyleName}.
 */
public final class TextStyleNameNode extends TextParentNode {

    /**
     * Factory that creates a {@link TextStyleNameNode}.
     */
    static TextStyleNameNode with(final TextStyleName styleName) {
        checkStyleName(styleName);

        return new TextStyleNameNode(NO_INDEX, NO_CHILDREN, styleName);
    }

    private TextStyleNameNode(final int index, final List<TextNode> children, final TextStyleName styleName) {
        super(index, children);

        this.styleName = styleName;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    public final static TextNodeName NAME = TextNodeName.fromClass(TextStyleNameNode.class);

    public TextStyleName styleName() {
        return this.styleName;
    }

    public TextStyleNameNode setStyleName(final TextStyleName styleName) {
        checkStyleName(styleName);

        return this.styleName.equals(styleName) ?
            this :
            this.replace1(this.index, this.children, styleName);
    }

    private final TextStyleName styleName;

    private static void checkStyleName(final TextStyleName styleName) {
        Objects.requireNonNull(styleName, "styleName");
    }

    @Override
    public TextStyleNameNode removeParent() {
        return this.removeParent0().cast();
    }

    // children........................................................................................................

    @Override
    public TextStyleNameNode setChildren(final List<TextNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    public TextStyleNameNode appendChild(final TextNode child) {
        return super.appendChild(child).cast();
    }

    @Override
    public TextStyleNameNode replaceChild(final TextNode oldChild, final TextNode newChild) {
        return super.replaceChild(oldChild, newChild).cast();
    }

    @Override
    public TextStyleNameNode removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    // attribute........................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> attributes() {
        return Maps.empty();
    }

    @Override
    TextNode setAttributesEmptyTextStyleMap() {
        return this;
    }

    /**
     * Factory that creates a new {@link TextStyleNode} with the given attributes and this as the only child.
     */
    @Override
    TextNode setAttributesNonEmptyTextStyleMap(final TextNodeMap textStyleMap) {
        return TextStyleNode.with(
            Lists.of(
                this
            ),
            textStyleMap
        );
    }

    @Override
    public TextStyle textStyle() {
        return TextStyle.EMPTY;
    }

    // replace..........................................................................................................

    @Override
    TextStyleNameNode replace0(final int index,
                               final List<TextNode> children) {
        return new TextStyleNameNode(index, children, this.styleName);
    }

    private TextStyleNameNode replace1(final int index,
                                       final List<TextNode> children,
                                       final TextStyleName styleName) {
        return new TextStyleNameNode(index, children, styleName);
    }

    // toHtml...........................................................................................................

    @Override
    boolean buildHtml(final boolean shouldIndent,
                      final IndentingPrinter html) {
        if (shouldIndent) {
            html.lineStart();
            html.indent();
        }

        html.print("<SPAN class=\"");
        html.print(this.styleName().value());
        html.print("\">");

        final boolean i = this.buildChildNodesHtml(
            true, // shouldIndent
            html
        );

        html.print("</SPAN>");

        if (shouldIndent) {
            html.outdent();
        }

        return i;
    }

    // TreePrintable....................................................................................................

    @Override
    String printTreeTypeName() {
        return "StyleName " + this.styleName();
    }

    @Override
    void printTreeAttributes(final IndentingPrinter printer) {
        printer.indent();

        for (final Entry<TextStylePropertyName<?>, Object> nameAndValue : this.attributes().entrySet()) {
            printer.print(nameAndValue.getKey().value());
            printer.print("=");
            printer.print(CharSequences.quoteIfChars(nameAndValue.getValue()));
            printer.println();
        }

        printer.outdent();
    }

    // HasJsonNode.....................................................................................................

    /**
     * Accepts a json string which holds {@link TextStyleNameNode}.
     */
    static TextStyleNameNode unmarshallTextStyleNameNode(final JsonNode node,
                                                         final JsonNodeUnmarshallContext context) {
        TextStyleName styleName = null;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case STYLE_NAME:
                    styleName = context.unmarshall(child, TextStyleName.class);
                    break;
                case CHILDREN:
                    children = context.unmarshallWithTypeList(child);
                    break;
                default:
                    NeverError.unhandledCase(child, STYLE_NAME_PROPERTY, CHILDREN_PROPERTY);
            }
        }

        if (null == styleName) {
            JsonNodeUnmarshallContext.missingProperty(STYLE_NAME_PROPERTY, node);
        }

        return TextStyleNameNode.with(styleName)
            .setChildren(children);
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        return this.addChildrenValuesJson(JsonNode.object()
                .set(STYLE_NAME_PROPERTY, context.marshall(this.styleName)),
            context);
    }

    private final static String STYLE_NAME = "style-name";
    private final static JsonPropertyName STYLE_NAME_PROPERTY = JsonPropertyName.with(STYLE_NAME);

    // Visitor .................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object ..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextStyleNameNode;
    }

    @Override //
    boolean equals0(final TextNode other) {
        return this.equals1((TextStyleNameNode) other);
    }

    private boolean equals1(final TextStyleNameNode other) {
        return this.styleName.equals(other.styleName) &&
            this.children.equals(other.children);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        b.value(this.styleName);
    }
}
