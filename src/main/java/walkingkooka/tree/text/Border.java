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

import java.util.Optional;

/**
 * Provides a bean like view of a border and its properties.
 */
public final class Border extends BorderMarginPadding {

    static Border with(final BoxEdge edge, final TextStyle textStyle) {
        return new Border(edge, textStyle);
    }

    private Border(final BoxEdge edge, final TextStyle textStyle) {
        super(edge, textStyle);
    }

    public Optional<Color> color() {
        return this.textStyle.get(this.edge.borderColorPropertyName());
    }

    public Border setColor(final Optional<Color> color) {
        return this.setProperty(this.edge.borderColorPropertyName(), color).cast();
    }

    @Override
    public Border setEdge(final BoxEdge edge) {
        return this.setEdge0(edge).cast();
    }

    public Optional<BorderStyle> style() {
        return this.textStyle.get(this.edge.borderStylePropertyName());
    }

    public Border setStyle(final Optional<BorderStyle> style) {
        return this.setProperty(this.edge.borderStylePropertyName(), style).cast();
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
    BorderMarginPadding replace(final BoxEdge edge, final TextStyle textStyle) {
        return new Border(edge, textStyle);
    }

    // Object...........................................................................................................

}
