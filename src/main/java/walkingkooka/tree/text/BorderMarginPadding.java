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

import walkingkooka.CanBeEmpty;
import walkingkooka.Cast;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Base class for {@link Border}, {@link Margin} and {@link Padding}.
 */
abstract class BorderMarginPadding implements HasTextStyle,
    HasText,
    TreePrintable,
    CanBeEmpty {

    /**
     * Package private to limit sub classing.
     */
    BorderMarginPadding(final BoxEdge edge,
                        final TextStyle textStyle) {
        super();
        this.edge = edge;
        this.textStyle = textStyle;
    }

    // width............................................................................................................

    /**
     * Getter that returns the width component.
     */
    public final Optional<Length<?>> width() {
        return this.textStyle.get(
            this.widthPropertyName()
        );
    }

    /**
     * Would be setter that returns a {@link BorderMarginPadding} with the given {@link Length width}.
     */
    abstract BorderMarginPadding setWidth(final Optional<Length<?>> width);

    final BorderMarginPadding setWidth0(final Optional<Length<?>> width) {
        return this.setOrRemoveProperty0(
            this.widthPropertyName(),
            width
        );
    }

    /**
     * Returns the {@link TextStylePropertyName} for this width property.
     */
    abstract TextStylePropertyName<Length<?>> widthPropertyName();

    public abstract <V> BorderMarginPadding setProperty(final TextStylePropertyName<V> propertyName,
                                                        final V value);

    final <V> BorderMarginPadding setProperty0(final TextStylePropertyName<V> propertyName,
                                               final V value) {
        Objects.requireNonNull(propertyName, "propertyName");
        Objects.requireNonNull(value, "value");

        return this.setOrRemoveProperty(
            propertyName,
            Optional.of(value)
        );
    }

    abstract BorderMarginPadding removeProperty(final TextStylePropertyName<?> propertyName);

    final BorderMarginPadding removeProperty0(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        return this.setOrRemoveProperty(
            propertyName,
            Optional.empty()
        );
    }

    public abstract <V> BorderMarginPadding setOrRemoveProperty(final TextStylePropertyName<V> propertyName,
                                                                final Optional<V> value);

    /**
     * Replaces a property if the value is different returning a new {@link BorderMarginPadding}.
     */
    final <V> BorderMarginPadding setOrRemoveProperty0(final TextStylePropertyName<V> propertyName,
                                                       final Optional<V> value) {
        // edge test ensures cannot set border-bottom-XXX will be rejected when edge=TOP, but edge=ALL would allow
        if (false == (this.isProperty(propertyName) && this.edge.isTextStyleProperty(propertyName))) {
            throw this.invalidTextStylePropertyNameException(propertyName);
        }

        final TextStyle before = this.textStyle;
        final TextStyle after = value.isPresent() ?
            before.set(propertyName, value.get()) :
            before.remove(propertyName);

        return before == after ?
            this :
            this.replace(
                this.edge,
                after
            );
    }

    abstract boolean isProperty(final TextStylePropertyName<?> propertyName);

    abstract InvalidTextStylePropertyNameException invalidTextStylePropertyNameException(final TextStylePropertyName<?> propertyName);

    // edge........................................................................................................

    /**
     * Getter that returns the {@link BoxEdge}.
     */
    public final BoxEdge edge() {
        return this.edge;
    }

    /**
     * Would be setter that returns a {@link BorderMarginPadding} with the given {@link BoxEdge}.
     */
    abstract BorderMarginPadding setEdge(final BoxEdge edge);

    final BorderMarginPadding setEdge0(final BoxEdge edge) {
        return this.edge.equals(edge) ?
            this :
            this.replace(
                Objects.requireNonNull(edge, "edge"),
                this.textStyle.filter(edge::isTextStyleProperty)
            );

    }

    final BoxEdge edge;

    abstract BorderMarginPadding replace(final BoxEdge edge,
                                         final TextStyle style);

    // textStyle........................................................................................................

    /**
     * Returns the {@link TextStyle}.
     */
    @Override
    public final TextStyle textStyle() {
        return this.textStyle;
    }

    final TextStyle textStyle;

    // CanBeEmpty.......................................................................................................

    @Override
    public final boolean isEmpty() {
        return this.textStyle.isEmpty();
    }

    // helper...........................................................................................................

    final <T extends BorderMarginPadding> T cast() {
        return Cast.to(this);
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.textStyle.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            null != other && this.getClass() == other.getClass() &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final BorderMarginPadding other) {
        return this.edge.equals(other.edge) &&
            this.textStyle.equals(other.textStyle);
    }

    /**
     * <pre>
     * border TOP top-color: #123; top-style: solid; top-width: 456px;
     * </pre>
     */
    @Override
    public final String toString() {
        return this.getClass()
            .getSimpleName()
            .toLowerCase() +
            " " +
            this.edge +
            " " +
            this.text();
    }

    // HasText..........................................................................................................

    /**
     * Returns a text representation without the redundant prefix of border, margin or padding.
     * <pre>
     * border-top-color: #111; border-top-style: SOLID; border-top-width: 22px;
     * top-color: #111; top-style: SOLID; top-width: 22px;
     *
     * margin-top: 1px;
     * top: 1px;
     * </pre>
     */
    @Override
    public final String text() {
        if (null == this.text) {
            this.text = this.prepareText();
        }
        return this.text;
    }

    private String text;

    abstract String prepareText();

    final String marginPaddingLengthsOrTextStyleToText(final Function<BoxEdge, TextStylePropertyName<Length<?>>> lengthPropertyNameGetter) {
        final String text;

        final TextStyle textStyle = this.textStyle;

        if (textStyle.isEmpty()) {
            text = "";
        } else {
            final BoxEdge boxEdge = this.edge;

            if (BoxEdge.ALL == boxEdge) {
                final Length<?> top = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.TOP)
                ).orElse(null);

                final Length<?> right = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.RIGHT)
                ).orElse(null);

                final Length<?> bottom = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.BOTTOM)
                ).orElse(null);

                final Length<?> left = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.LEFT)
                ).orElse(null);

                if (areAllEqual(top, right, bottom, left)) {
                    text = top.text();
                } else {
                    final StringBuilder b = new StringBuilder();
                    appendIfNotNull(
                        top,
                        b
                    );
                    appendIfNotNull(
                        right,
                        b
                    );
                    appendIfNotNull(
                        bottom,
                        b
                    );
                    appendIfNotNull(
                        left,
                        b
                    );

                    text = b.toString();
                }

            } else {
                text = textStyle.get(
                        lengthPropertyNameGetter.apply(boxEdge)
                    ).map(Length::toString)
                    .orElse("");
            }
        }

        return text;
    }

    static <T> boolean areAllEqual(final T top, final T right, final T bottom, final T left) {
        return Objects.equals(top, right) && Objects.equals(bottom, left) && Objects.equals(top, bottom);
    }

    static void appendIfNotNull(final Object valueOrNull,
                                final StringBuilder b) {
        if (null != valueOrNull) {
            if(b.length() > 0) {
                b.append(" ");
            }
            b.append(valueOrNull);
        }
    }

    /**
     * The length of the prefix of each {@link TextStylePropertyName}. This will be used to remove the prefix when
     * creating the {@link #text()} and {@link #toString()}.
     */
    abstract int textPrefixLength();

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            printer.println(this.edge.name());

            printer.indent();
            {
                this.textStyle.printTree(printer);
            }
            printer.outdent();
        }
        printer.outdent();
    }

    // json.............................................................................................................

    static Border unmarshallBorder(final JsonNode json,
                                   final JsonNodeUnmarshallContext context) {
        return unmarshall(
            json,
            BoxEdge::parseBorder
        );
    }

    static Margin unmarshallMargin(final JsonNode json,
                                   final JsonNodeUnmarshallContext context) {
        return unmarshall(
            json,
            BoxEdge::parseMargin
        );
    }

    static Padding unmarshallPadding(final JsonNode json,
                                     final JsonNodeUnmarshallContext context) {
        return unmarshall(
            json,
            BoxEdge::parsePadding
        );
    }

    private static <BMP extends BorderMarginPadding> BMP unmarshall(final JsonNode json,
                                                                    final BiFunction<BoxEdge, String, BMP> parse) {
        final String string = json.stringOrFail();
        final int space = string.indexOf(' ');
        if(-1 == space || 0 == space) {
            throw new IllegalArgumentException("Missing BoxEdge");
        }
        final BoxEdge boxEdge = BoxEdge.valueOf(
            string.substring(
                0,
                space
            )
        );

        return parse.apply(
            boxEdge,
            string.substring(space + 1)
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(
            this.edge + " " + this.text()
        );
    }

    static {
        JsonNodeContext.register(
            "border",
            BorderMarginPadding::unmarshallBorder,
            BorderMarginPadding::marshall,
            Border.class
        );

        JsonNodeContext.register(
            "margin",
            BorderMarginPadding::unmarshallMargin,
            BorderMarginPadding::marshall,
            Margin.class
        );

        JsonNodeContext.register(
            "padding",
            BorderMarginPadding::unmarshallPadding,
            BorderMarginPadding::marshall,
            Padding.class
        );
    }
}
