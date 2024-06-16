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
import walkingkooka.net.Url;
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
 * Represents a hyperlink with children.
 */
public final class Hyperlink extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.fromClass(Hyperlink.class);

    static Hyperlink with(final Url url) {
        return with(
                url,
                NO_CHILDREN
        );
    }

    // @VisibleForTesting
    static Hyperlink with(final Url url,
                          final List<TextNode> children) {
        Objects.requireNonNull(url, "url");

        return new Hyperlink(
                NO_INDEX,
                children,
                url
        );
    }

    /**
     * Private ctor
     */
    private Hyperlink(final int index,
                      final List<TextNode> children,
                      final Url url) {
        super(index, children);
        this.url = url;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Returns a {@link Hyperlink} with no parent but equivalent children.
     */
    @Override
    public Hyperlink removeParent() {
        return this.removeParent0().cast();
    }

    // url.......................................................................................................

    /**
     * Retrieves the mandatory url property.
     *
     * @return
     */
    public Url url() {
        return this.url;
    }

    /**
     * Would be setter that returns a new instance if the given {@link Url} is different.
     */
    private Hyperlink setUrl(final Url url) {
        Objects.requireNonNull(url, "url");

        return this.url.equals(url) ?
                this :
                this.replaceUrl(url);
    }

    /**
     * Create a new {@link Hyperlink}.
     */
    private Hyperlink replaceUrl(final Url url) {
        return new Hyperlink(
                this.index,
                this.children,
                url
        );
    }

    private final Url url;

    // children.........................................................................................................

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public Hyperlink setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children)
                .cast();
    }

    @Override
    public Hyperlink appendChild(final TextNode child) {
        return super.appendChild(child)
                .cast();
    }

    @Override
    public Hyperlink replaceChild(final TextNode oldChild,
                                  final TextNode newChild) {
        return super.replaceChild(
                oldChild,
                newChild
        ).cast();
    }

    @Override
    public Hyperlink removeChild(final int child) {
        return super.removeChild(child)
                .cast();
    }

    // replace..........................................................................................................

    @Override
    Hyperlink replace0(final int index,
                       final List<TextNode> children) {
        return new Hyperlink(
                index,
                children,
                this.url
        );
    }

    // toHtml...........................................................................................................

    @Override
    boolean buildHtml(final boolean shouldIndent,
                      final IndentingPrinter html) {
        if (shouldIndent) {
            html.lineStart();
            html.indent();
        }

        html.print("<A href=\"");
        html.print(this.url.toString());
        html.print("\"");

        if (this.children.isEmpty()) {
            html.print("/>");

        } else {
            html.print(">");
            this.buildChildNodesHtml(
                    true,
                    html
            );
            html.print("</A>");
        }

        if (shouldIndent) {
            html.println();
            html.outdent();
        }

        return true;
    }

    // Visitor .........................................................................................................

    @Override
    void accept(final TextNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // attributes.......................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> attributes() {
        return Maps.empty();
    }

    @Override
    TextNode setAttributesEmptyTextStyleMap() {
        return this;
    }

    @Override
    TextNode setAttributesNonEmptyTextStyleMap(final TextNodeMap textStyleMap) {
        return TextStyleNode.with(
                Lists.of(
                        this
                ),
                textStyleMap
        );
    }

    // textStyle........................................................................................................

    @Override
    public TextStyle textStyle() {
        return TextStyle.EMPTY;
    }

    // TreePrintable....................................................................................................

    @Override
    String printTreeTypeName() {
        return Hyperlink.class.getSimpleName();
    }

    @Override
    void printTreeAttributes(final IndentingPrinter printer) {
        printer.indent();
        {
            printer.println(this.url.toString());
        }
        printer.outdent();
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Hyperlink;
    }

    @Override //
    boolean equals0(final TextNode other) {
        return this.equals1((Hyperlink) other);
    }

    private boolean equals1(final Hyperlink other) {
        return this.url.equals(other.url) &&
                this.children.equals(other.children);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        b.value(this.url);
        b.separator(" ");
    }

    // json.............................................................................................................

    static Hyperlink unmarshallHyperLink(final JsonNode node,
                                         final JsonNodeUnmarshallContext context) {
        Url url = null;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case URL:
                    url = context.unmarshall(
                            child,
                            Url.class
                    );
                    break;
                case CHILDREN:
                    children = context.unmarshallWithTypeList(child);
                    break;
                default:
                    NeverError.unhandledCase(
                            child,
                            URL_PROPERTY,
                            CHILDREN_PROPERTY
                    );
            }
        }

        return with(
                url
        ).setChildren(children);
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        JsonObject json = JsonNode.object()
                .set(
                        URL_PROPERTY,
                        context.marshall(this.url)
                );
        return this.addChildrenValuesJson(
                json,
                context
        );
    }

    private final static String URL = "url";
    private final static JsonPropertyName URL_PROPERTY = JsonPropertyName.with(URL);
}
