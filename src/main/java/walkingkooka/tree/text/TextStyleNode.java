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

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a collection of style properties that apply upon a list of {@link TextNode children}.
 */
public final class TextStyleNode extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.fromClass(TextStyleNode.class);

    final static TextNodeMap NO_ATTRIBUTES_MAP = TextNodeMap.with(Maps.empty());

    /**
     * Factory that creates a {@link TextStyleNode} with the given children and style.
     * If the styles is empty and there is only one child it will be unwrapped.
     */
    // TextStyle.setTextNodes
    static TextNode with(final List<TextNode> children,
                         final TextNodeMap properties) {
        final List<TextNode> copy = Lists.immutable(children);
        return properties.isEmpty() && copy.size() == 1 ?
                copy.get(0) :
                new TextStyleNode(NO_INDEX, copy, properties);
    }

    /**
     * Private ctor
     */
    private TextStyleNode(final int index,
                          final List<TextNode> children,
                          final TextNodeMap attributes) {
        super(index, children);
        this.attributes = attributes;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Returns a {@link TextStyleNode} with no parent but equivalent children.
     */
    @Override
    public TextStyleNode removeParent() {
        return this.removeParent0().cast();
    }

    // children.........................................................................................................

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public TextStyleNode setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children).cast();
    }

    @Override
    public TextStyleNode appendChild(final TextNode child) {
        return super.appendChild(child).cast();
    }

    @Override
    public TextStyleNode replaceChild(final TextNode oldChild, final TextNode newChild) {
        return super.replaceChild(oldChild, newChild).cast();
    }

    @Override
    public TextStyleNode removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    // attributes.......................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> attributes() {
        return this.attributes;
    }

    @Override
    TextNode setAttributesEmptyTextStyleMap() {
        return this.setAttributesTextStyleMap(TextNodeMap.EMPTY);
    }

    @Override
    TextStyleNode setAttributesNonEmptyTextStyleMap(final TextNodeMap textStyleMap) {
        return this.setAttributesTextStyleMap(textStyleMap);
    }

    private TextStyleNode setAttributesTextStyleMap(final TextNodeMap textStyleMap) {
        return this.attributes.equals(textStyleMap) ?
                this :
                this.replaceAttributes(textStyleMap);
    }

    /**
     * Create a new {@link TextStylePropertyName}.
     */
    private TextStyleNode replaceAttributes(final TextNodeMap attributes) {
        return new TextStyleNode(this.index, this.children, attributes);
    }

    @Override
    public TextStyle textStyle() {
        return TextStyle.EMPTY.setValues(this.attributes);
    }

    private final TextNodeMap attributes;

    // replace.........................................................................................................

    @Override
    TextStyleNode replace0(final int index,
                           final List<TextNode> children) {
        return new TextStyleNode(index, children, this.attributes);
    }

    // toHtml...........................................................................................................

    @Override
    boolean buildHtml(final boolean shouldIndent,
                      final IndentingPrinter html) {
        if (shouldIndent) {
            html.lineStart();
            html.indent();
        }

        html.print("<SPAN style=\"");
        html.print(this.textStyle().css());
        html.print("\">");

        this.buildChildNodesHtml(
                true,
                html
        );

        html.print("</SPAN>");

        if (shouldIndent) {
            html.println();
            html.outdent();
        }

        return true;
    }

    // JsonNodeContext...................................................................................................

    /**
     * Accepts a json string which holds {@link TextStyleNode}.
     */
    static TextStyleNode unmarshallTextStyleNode(final JsonNode node,
                                                 final JsonNodeUnmarshallContext context) {
        TextStyle textStyle = TextStyle.EMPTY;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case STYLES:
                    textStyle = textStyle.setValues(
                            TextNodeMap.unmarshall(
                                    child,
                                    context
                            )
                    );
                    break;
                case CHILDREN:
                    children = context.unmarshallWithTypeList(child);
                    break;
                default:
                    NeverError.unhandledCase(child, STYLES_PROPERTY, CHILDREN_PROPERTY);
            }
        }

        return textStyle.setChildren(children).cast();
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        JsonObject json = JsonNode.object();
        if (!this.attributes.isEmpty()) {
            json = json.set(STYLES_PROPERTY, this.attributes.toJson(context));
        }

        return this.addChildrenValuesJson(json, context);
    }

    private final static String STYLES = "styles";
    private final static JsonPropertyName STYLES_PROPERTY = JsonPropertyName.with(STYLES);

    // Visitor .................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // TreePrintable....................................................................................................

    @Override
    String printTreeTypeName() {
        return "Style";
    }

    @Override
    void printTreeAttributes(final IndentingPrinter printer) {
        final TextStyle style = this.textStyle();
        final boolean attributesPresent = !style.isEmpty();

        if (attributesPresent) {
            printer.indent();
            style.printTree(printer);
            printer.outdent();
        }
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextStyleNode;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final TextNode other) {
        return other instanceof TextStyleNode &&
                this.equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final TextStyleNode other) {
        return this.attributes.equals(other.attributes);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        final Map<TextStylePropertyName<?>, Object> attributes = this.attributes;
        if(!attributes.isEmpty()) {
            //b.valueSeparator(", ");
            b.surroundValues("{", "}");
            b.value(attributes);
        }
    }
}
