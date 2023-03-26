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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.HasText;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A parent holding zero or more child expressions.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
abstract class TextParentNode extends TextNode {

    /**
     * Package private to limit sub classing.
     */
    TextParentNode(final int index, final List<TextNode> children) {
        super(index);

        final Optional<TextNode> p = Optional.of(this);

        final List<TextNode> copy = Lists.array();
        this.children = Lists.readOnly(copy);

        int i = 0;
        for (TextNode child : children) {
            copy.add(child.setParent(p, i));
            i++;
        }
    }

    // children.........................................................................................................

    @Override
    public final List<TextNode> children() {
        return this.children;
    }

    final List<TextNode> children;

    /**
     * Sub classes should call this and cast the returned value to their type.
     */
    final TextNode setChildren0(final List<TextNode> children) {
        Objects.requireNonNull(children, "children");

        final List<TextNode> copy = Lists.immutable(children);
        return Lists.equals(this.children(), copy, (first, other) -> first.equalsIgnoringParentAndChildren(other) && first.equalsDescendants0(other)) ?
                this :
                this.replaceChildren(copy);
    }

    @Override
    final TextNode setChild(final TextNode newChild, final int index) {
        //int index = newChild.index();
        final TextNode previous = this.children().get(index);
        return previous.equalsIgnoringParentAndChildren(newChild) && previous.equalsDescendants(newChild) ?
                this :
                this.replaceChild0(newChild, index);
    }

    private TextNode replaceChild0(final TextNode newChild, final int index) {
        final List<TextNode> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(index, newChild);

        return this.replaceChildren(newChildren);
    }

    private TextParentNode replaceChildren(final List<TextNode> children) {
        final int index = this.index;
        return this.replace0(index, children)
                .replaceChild(this.parent(), index)
                .cast();
    }

    @Override
    final TextNode replace(final int index) {
        return this.replace0(index, this.children());
    }

    abstract TextParentNode replace0(final int index, final List<TextNode> children);

    // HasTextOffset...................................................................................................

    /**
     * Combine the text of all children(descendants). Note property names and indices will not be included.
     */
    @Override
    public String text() {
        return this.children().stream()
                .map(HasText::text)
                .collect(Collectors.joining());
    }

    @Override
    public int textLength() {
        return this.children().stream()
                .mapToInt(HasText::textLength)
                .sum();
    }

    // toHtml...........................................................................................................

    /**
     * Placeholders must be resolved before converting to html.
     */
    @Override
    public final String toHtml() {
        final StringBuilder html = new StringBuilder();

        try (final IndentingPrinter printer = Printers.stringBuilder(html, LineEnding.SYSTEM).indenting(Indentation.SPACES2)) {
            this.buildHtml(
                    false, // shouldIndent
                    printer
            );
        }

        return html.toString();
    }

    final boolean buildChildNodesHtml(final boolean shouldIndent,
                                      final IndentingPrinter html) {
        boolean i = shouldIndent;

        for (final TextNode child : this.children()) {
            i = child.buildHtml(
                    i,
                    html
            );

            i = child.isStyle() || child.isStyleName();
        }

        return i;
    }

    // JsonNodeContext..................................................................................................

    /**
     * Because there are multiple types of children each json representation must be wrapped with the actual type.
     */
    final JsonObject addChildrenValuesJson(final JsonObject node,
                                               final JsonNodeMarshallContext context) {
        final List<TextNode> children = this.children();
        return children.isEmpty() ?
                node :
                node.set(CHILDREN_PROPERTY, context.marshallWithTypeCollection(children));
    }

    final static String CHILDREN = "children";
    final static JsonPropertyName CHILDREN_PROPERTY = JsonPropertyName.with(CHILDREN);

    // Visitor ........................................................................................................

    final void acceptValues(final TextNodeVisitor visitor) {
        for (TextNode node : this.children()) {
            visitor.accept(node);
        }
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.print(this.printTreeTypeName());
        printer.println();

        this.printTreeAttributes(printer);
        this.printChildren(printer);
    }

    abstract String printTreeTypeName();

    abstract void printTreeAttributes(final IndentingPrinter printer);

    final void printChildren(final IndentingPrinter printer) {
        printer.indent();
        for (final TextNode child : this.children()) {
            child.printTree(printer);
        }
        printer.outdent();
    }

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return this.children().hashCode();
    }

    final boolean equalsDescendants0(final TextNode other) {
        return this.equalsDescendants1(other.children());
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    private boolean equalsDescendants1(final List<TextNode> otherChildren) {
        final List<TextNode> children = this.children();
        final int count = children.size();
        boolean equals = count == otherChildren.size();

        if (equals) {
            for (int i = 0; equals && i < count; i++) {
                equals = children.get(i).equalsDescendants(otherChildren.get(i));
            }
        }

        return equals;
    }

    // UsesToStringBuilder..............................................................................................

    @Override final void buildToString0(final ToStringBuilder b) {
        this.buildToStringBefore(b);

        b.surroundValues("[", "]");
        b.value(new Object[]{this.children()});
    }

    abstract void buildToStringBefore(final ToStringBuilder b);
}
