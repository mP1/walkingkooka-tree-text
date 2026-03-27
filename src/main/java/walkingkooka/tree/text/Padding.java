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

import java.util.Objects;
import java.util.Optional;

/**
 * Provides a bean like view of a padding.
 */
public final class Padding extends BorderMarginPadding {

    public static Padding parse(final String text) {
        return with(
            BoxEdge.ALL,
            TextStyle.parse0(
                text,
                PREFIX,
                TextStylePropertyName::isPadding,
                InvalidTextStylePropertyNameException::padding
            )
        );
    }

    static Padding with(final BoxEdge edge,
                        final TextStyle textStyle) {
        return new Padding(
            Objects.requireNonNull(edge, "edge"),
            textStyle
        );
    }

    private Padding(final BoxEdge edge,
                    final TextStyle textStyle) {
        super(
            edge,
            textStyle
        );
    }

    @Override
    public <V> Padding setProperty(final TextStylePropertyName<V> propertyName,
                                   final Optional<V> value) {
        return this.setProperty0(
            propertyName,
            value
        ).cast();
    }

    @Override
    boolean isProperty(final TextStylePropertyName<?> propertyName) {
        return propertyName.isPadding();
    }

    @Override
    InvalidTextStylePropertyNameException invalidTextStylePropertyNameException(final TextStylePropertyName<?> propertyName) {
        return InvalidTextStylePropertyNameException.padding(propertyName);
    }

    @Override
    public Padding setEdge(final BoxEdge edge) {
        return this.setEdge0(edge)
            .cast();
    }

    @Override
    public Padding setWidth(final Optional<Length<?>> width) {
        return this.setWidth0(width)
            .cast();
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName() {
        return this.edge.paddingPropertyName();
    }

    @Override
    BorderMarginPadding replace(final BoxEdge edge,
                                final TextStyle textStyle) {
        return new Padding(
            edge,
            textStyle
        );
    }

    // HasText..........................................................................................................

    @Override
    int textPrefixLength() {
        return PREFIX_LENGTH;
    }

    private final static String PREFIX = "padding-";

    private final static int PREFIX_LENGTH = PREFIX.length();

    // valuesAsText()...................................................................................................

    @Override
    public String valuesAsText() {
        return this.widthAsText();
    }
}
