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

import walkingkooka.color.Color;

import java.util.Objects;
import java.util.Optional;

/**
 * Provides a bean like view of a border and its properties.
 */
public final class Border extends BorderMarginPadding {

    public static Border parse(final String text) {
        return with(
            BoxEdge.ALL,
            TextStyle.parse0(
                text,
                PREFIX,
                TextStylePropertyName::isBorder,
                InvalidTextStylePropertyNameException::border
            )
        );
    }

    static Border with(final BoxEdge edge,
                       final TextStyle textStyle) {
        return new Border(
            Objects.requireNonNull(edge, "edge"),
            textStyle
        );
    }

    private Border(final BoxEdge edge,
                   final TextStyle textStyle) {
        super(edge, textStyle);
    }

    @Override
    public <V> Border setProperty(final TextStylePropertyName<V> propertyName,
                                  final V value) {
        return this.setProperty0(
            propertyName,
            value
        ).cast();
    }

    @Override
    public <V> Border removeProperty(final TextStylePropertyName<V> propertyName) {
        return this.removeProperty0(propertyName)
            .cast();
    }

    @Override
    public <V> Border setOrRemoveProperty(final TextStylePropertyName<V> propertyName,
                                          final Optional<V> value) {
        return this.setOrRemoveProperty0(
            propertyName,
            value
        ).cast();
    }

    @Override
    boolean isProperty(final TextStylePropertyName<?> propertyName) {
        return propertyName.isBorder();
    }

    @Override
    InvalidTextStylePropertyNameException invalidTextStylePropertyNameException(final TextStylePropertyName<?> propertyName) {
        return InvalidTextStylePropertyNameException.border(propertyName);
    }

    public Optional<Color> color() {
        return this.textStyle.get(
            this.edge.borderColorPropertyName()
        );
    }

    public Border setColor(final Optional<Color> color) {
        return this.setOrRemoveProperty0(
            this.edge.borderColorPropertyName(),
            color
        ).cast();
    }

    @Override
    public Border setEdge(final BoxEdge edge) {
        return this.setEdge0(edge)
            .cast();
    }

    public Optional<BorderStyle> style() {
        return this.textStyle.get(
            this.edge.borderStylePropertyName()
        );
    }

    public Border setStyle(final Optional<BorderStyle> style) {
        return this.setOrRemoveProperty0(
            this.edge.borderStylePropertyName(),
            style
        ).cast();
    }

    /**
     * Would be setter that returns a {@link Border} with the given width creating a new instance if necessary.
     */
    @Override
    public Border setWidth(final Optional<Length<?>> width) {
        return this.setWidth0(width).cast();
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName() {
        return this.edge.borderWidthPropertyName();
    }

    @Override
    BorderMarginPadding replace(final BoxEdge edge,
                                final TextStyle textStyle) {
        return new Border(
            edge,
            textStyle
        );
    }

    // HasText..........................................................................................................

    @Override
    int textPrefixLength() {
        return PREFIX_LENGTH;
    }

    private final static String PREFIX = "border-";

    private final static int PREFIX_LENGTH = PREFIX.length();

    // valuesAsText()...................................................................................................

    @Override
    public String valuesAsText() {
        final StringBuilder b = new StringBuilder();

        valueAsTextAppend(
            this.color(),
            b
        );

        valueAsTextAppend(
            this.style(),
            b
        );

        valueAsTextAppend(
            this.width(),
            b
        );

        return b.toString();
    }

    private static void valueAsTextAppend(final Optional<?> value,
                                          final StringBuilder b) {
        if (value.isPresent()) {
            if (b.length() > 0) {
                b.append(' ');
            }
            b.append(
                value.get()
            );
        }
    }
}
