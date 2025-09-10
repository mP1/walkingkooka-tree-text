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
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a badge and a child {@link TextNode}.
 */
public final class Badge extends TextParentNode {

    public final static TextNodeName NAME = TextNodeName.with(Badge.class.getSimpleName());

    static Badge with(final String badgeText) {
        return with(
            badgeText,
            NO_CHILDREN
        );
    }

    // @VisibleForTesting
    static Badge with(final String badgeText,
                      final List<TextNode> children) {
        Objects.requireNonNull(badgeText, "badgeText");

        return new Badge(
            NO_INDEX,
            children,
            badgeText
        );
    }

    /**
     * Private ctor
     */
    private Badge(final int index,
                  final List<TextNode> children,
                  final String badgeText) {
        super(index, children);
        this.badgeText = badgeText;
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    /**
     * Always returns this because badges cannot have a parent.
     */
    @Override
    public Badge removeParent() {
        return this;
    }

    @Override
    public Badge setText(final String text) {
        return this.replaceChildrenWithText(text);
    }

    // badgeText.......................................................................................................

    /**
     * Retrieves the mandatory badge text property.
     */
    public String badgeText() {
        return this.badgeText;
    }

    /**
     * Would be setter that returns a new instance if the given {@link String} is different.
     */
    private Badge setBadgeText(final String badgeText) {
        Objects.requireNonNull(badgeText, "badgeText");

        return this.badgeText.equals(badgeText) ?
            this :
            this.replaceBadgeText(badgeText);
    }

    /**
     * Create a new {@link Badge}.
     */
    private Badge replaceBadgeText(final String badgeText) {
        return new Badge(
            this.index,
            this.children,
            badgeText
        );
    }

    private final String badgeText;

    // children.........................................................................................................

    /**
     * Would be setter that returns an array instance with the provided children, creating a new instance if necessary.
     */
    @Override
    public Badge setChildren(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        return this.setChildren0(children)
            .cast();
    }

    @Override
    public Badge appendChild(final TextNode child) {
        return super.appendChild(child)
            .cast();
    }

    @Override
    public Badge replaceChild(final TextNode oldChild,
                              final TextNode newChild) {
        return super.replaceChild(
            oldChild,
            newChild
        ).cast();
    }

    @Override
    public Badge removeChild(final int child) {
        return super.removeChild(child)
            .cast();
    }

    // replace..........................................................................................................

    @Override
    Badge replace0(final int index,
                   final List<TextNode> children) {
        return new Badge(
            index,
            children,
            this.badgeText
        );
    }

    // toHtml...........................................................................................................

    @Override
    boolean buildHtml(final boolean shouldIndent,
                      final IndentingPrinter html) {
            this.buildChildNodesHtml(
                true,
                html
            );

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
        return Badge.class.getSimpleName();
    }

    @Override
    void printTreeAttributes(final IndentingPrinter printer) {
        printer.indent();
        {
            printer.println("badgeText");
            printer.indent();
            {
                printer.println(
                    CharSequences.quoteAndEscape(
                        this.badgeText
                    )
                );
            }
            printer.outdent();
        }
        printer.outdent();
    }

    // Object ..........................................................................................................

    @Override //
    boolean equals0(final TextNode other) {
        return this.equals1((Badge) other);
    }

    private boolean equals1(final Badge other) {
        return this.badgeText.equals(other.badgeText) &&
            this.children.equals(other.children);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToStringBefore(final ToStringBuilder b) {
        b.value(this.badgeText);
        b.separator(" ");
    }

    // json.............................................................................................................

    static Badge unmarshallBadge(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        String badgeText = null;
        List<TextNode> children = NO_CHILDREN;

        for (JsonNode child : node.children()) {
            switch (child.name().value()) {
                case BADGE_TEXT:
                    badgeText = context.unmarshall(
                        child,
                        String.class
                    );
                    break;
                case CHILDREN:
                    children = context.unmarshallListWithType(child);
                    break;
                default:
                    NeverError.unhandledCase(
                        child,
                        BADGE_TEXT_PROPERTY,
                        CHILDREN_PROPERTY
                    );
            }
        }

        return with(
            badgeText
        ).setChildren(children);
    }

    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        JsonObject json = JsonNode.object()
            .set(
                BADGE_TEXT_PROPERTY,
                context.marshall(this.badgeText)
            );
        return this.addChildrenValuesJson(
            json,
            context
        );
    }

    private final static String BADGE_TEXT = "badgeText";
    private final static JsonPropertyName BADGE_TEXT_PROPERTY = JsonPropertyName.with(BADGE_TEXT);
}
