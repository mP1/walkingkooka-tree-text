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
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for {@link Border}, {@link Margin} and {@link Padding}.
 */
abstract class BorderMarginPadding implements HasTextStyle,
    HasText {

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
        return this.setProperty0(
            this.widthPropertyName(),
            width
        );
    }

    /**
     * Returns the {@link TextStylePropertyName} for this width property.
     */
    abstract TextStylePropertyName<Length<?>> widthPropertyName();

    public abstract <V> BorderMarginPadding setProperty(final TextStylePropertyName<V> propertyName,
                                                        final Optional<V> value);

    /**
     * Replaces a property if the value is different returning a new {@link BorderMarginPadding}.
     */
    final <V> BorderMarginPadding setProperty0(final TextStylePropertyName<V> propertyName,
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
            this.text = this.textStyle.toText(
                (n) -> n.value()
                    .substring(
                        this.textPrefixLength()
                    )
            );
        }
        return this.text;
    }

    private String text;

    /**
     * The length of the prefix of each {@link TextStylePropertyName}. This will be used to remove the prefix when
     * creating the {@link #text()} and {@link #toString()}.
     */
    abstract int textPrefixLength();

    // valuesAsText()...................................................................................................

    /**
     * Returns the values for this {@link BorderMarginPadding} without any property names. This text should be
     * usable by the matching {@link BoxEdge#parseBorder(String)}, {@link BoxEdge#parseMargin(String)} and {@link BoxEdge#parsePadding(String)}.
     * <pre>
     * 12px
     * black solid 1px
     * </pre>
     */
    abstract String valuesAsText();

    final String widthAsText() {
        return this.textStyle.get(
                this.widthPropertyName()
            ).map(Length::toString)
            .orElse("");
    }
}
