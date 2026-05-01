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
 * Provides a bean like view of a margin.
 */
public final class Margin extends MarginOrPadding {

    /**
     * Parses syntax similar CSS with supporting full {@link TextStylePropertyName} aswell as
     * dropping the margin prefix or just {@link Length}.
     * <pre>
     * margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-left: 4px;
     * top: 1px; right: 2px; bottom: 3px; left: 4px;
     * 1px 2px 3px 4px
     *
     * margin-top: 5px; margin-right: 5px; margin-bottom: 5px; margin-left: 5px;
     * top: 5px; right: 5px; bottom: 5px; left: 5px;
     * 5px
     * </pre>
     */
    public static Margin parse(final String text) {
        return BoxEdge.ALL.parseMargin(text);
    }

    static Margin with(final BoxEdge edge,
                       final TextStyle textStyle) {
        return new Margin(
            Objects.requireNonNull(edge, "edge"),
            textStyle
        );
    }

    private Margin(final BoxEdge edge,
                   final TextStyle textStyle) {
        super(
            edge,
            textStyle
        );
    }

    @Override
    MarginOrPaddingKind kind() {
        return MarginOrPaddingKind.MARGIN;
    }

    @Override
    public <V> Margin setProperty(final TextStylePropertyName<V> propertyName,
                                  final V value) {
        return this.setProperty0(
            propertyName,
            value
        ).cast();
    }

    @Override
    public Margin removeProperty(final TextStylePropertyName<?> propertyName) {
        return this.removeProperty0(propertyName)
            .cast();
    }

    @Override
    public <V> Margin setOrRemoveProperty(final TextStylePropertyName<V> propertyName,
                                          final Optional<V> value) {
        return this.setOrRemoveProperty0(
            propertyName,
            value
        ).cast();
    }

    @Override
    boolean isProperty(final TextStylePropertyName<?> propertyName) {
        return propertyName.isMargin();
    }

    @Override
    InvalidTextStylePropertyNameException invalidTextStylePropertyNameException(final TextStylePropertyName<?> propertyName) {
        return InvalidTextStylePropertyNameException.margin(propertyName);
    }

    @Override
    public Margin setEdge(final BoxEdge edge) {
        return this.setEdge0(edge)
            .cast();
    }

    @Override
    public Margin setWidth(final Optional<Length<?>> width) {
        return this.setWidth0(width)
            .cast();
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName() {
        return this.edge.marginPropertyName();
    }

    @Override
    BorderMarginPadding replace(final BoxEdge edge,
                                final TextStyle textStyle) {
        return new Margin(
            edge,
            textStyle
        );
    }

    // HasText..........................................................................................................

    @Override
    String prepareText() {
        return this.lengthsToText(BoxEdge::marginPropertyName);
    }

    @Override
    int textPrefixLength() {
        return PREFIX_LENGTH;
    }

    final static String PREFIX = "margin-";

    private final static int PREFIX_LENGTH = PREFIX.length();
}
