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

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for {@link Border}, {@link Margin} and {@link Padding}.
 */
abstract class BorderMarginPadding {

    /**
     * Package private to limit sub classing.
     */
    BorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        super();
        this.edge = edge;
        this.textStyle = textStyle;
    }

    // width............................................................................................................

    /**
     * Getter that returns the width component.
     */
    public final Optional<Length<?>> width() {
        return this.textStyle.get(this.widthPropertyName());
    }

    /**
     * Would be setter that returns a {@link BorderMarginPadding} with the given {@link Length width}.
     */
    abstract BorderMarginPadding setWidth(final Optional<Length<?>> width);

    final BorderMarginPadding setWidth0(final Optional<Length<?>> width) {
        return this.setProperty(this.widthPropertyName(), width);
    }

    /**
     * Returns the {@link TextStylePropertyName} for this width property.
     */
    abstract TextStylePropertyName<Length<?>> widthPropertyName();

    /**
     * Replaces a property if the value is different returning a new {@link BorderMarginPadding}.
     */
    final <V> BorderMarginPadding setProperty(final TextStylePropertyName<V> propertyName,
                                              final Optional<V> value) {
        final TextStyle before = this.textStyle;
        final TextStyle after = value.isPresent() ?
            before.set(propertyName, value.get()) :
            before.remove(propertyName);
        return before == after ?
            this :
            this.replace(this.edge, after);
    }

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
        Objects.requireNonNull(edge, "edge");
        return this.edge.equals(edge) ?
            this :
            this.replace(edge, this.textStyle);

    }

    final BoxEdge edge;

    abstract <V> BorderMarginPadding replace(final BoxEdge edge, final TextStyle style);

    // textStyle........................................................................................................

    /**
     * Returns the {@link TextStyle}.
     */
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
            this.canBeEqual(other) &&
                this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final BorderMarginPadding other) {
        return this.edge.equals(other.edge) &&
            this.textStyle.equals(other.textStyle);
    }

    @Override
    public final String toString() {
        return this.edge + " " + this.textStyle;
    }
}
