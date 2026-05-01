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

import java.util.Optional;
import java.util.function.Function;

/**
 * Base class for {@link Margin} and {@link Padding}.
 */
abstract public class MarginOrPadding extends BorderMarginPadding {

    /**
     * Package private to limit sub classing.
     */
    MarginOrPadding(final BoxEdge edge,
                    final TextStyle textStyle) {
        super(
            edge,
            textStyle
        );
    }

    public final Optional<Length<?>> top() {
        return this.getProperty(
            this.kind()
                .top()
        );
    }

    public final Optional<Length<?>> right() {
        return this.getProperty(
            this.kind()
                .right()
        );
    }

    public final Optional<Length<?>> bottom() {
        return this.getProperty(
            this.kind()
                .bottom()
        );
    }

    public final Optional<Length<?>> left() {
        return this.getProperty(
            this.kind()
                .left()
        );
    }

    abstract MarginOrPaddingKind kind();

    final String lengthsToText(final Function<BoxEdge, TextStylePropertyName<Length<?>>> lengthPropertyNameGetter) {
        final String text;

        final TextStyle textStyle = this.textStyle;

        if (textStyle.isEmpty()) {
            text = "";
        } else {
            final BoxEdge boxEdge = this.edge;

            if (BoxEdge.ALL == boxEdge) {

                final Length<?> topLength = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.TOP)
                ).orElse(null);

                final Length<?> rightLength = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.RIGHT)
                ).orElse(null);

                final Length<?> bottomLength = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.BOTTOM)
                ).orElse(null);

                final Length<?> leftLength = textStyle.get(
                    lengthPropertyNameGetter.apply(BoxEdge.LEFT)
                ).orElse(null);

                if (areAllEqual(topLength, rightLength, bottomLength, leftLength)) {
                    text = topLength.text();
                } else {
                    final StringBuilder b = new StringBuilder();

                    if (null != topLength && null != rightLength && null != bottomLength && null != leftLength) {
                        appendIfNotNull(
                            topLength,
                            b
                        );
                        appendIfNotNull(
                            rightLength,
                            b
                        );
                        appendIfNotNull(
                            bottomLength,
                            b
                        );
                        appendIfNotNull(
                            leftLength,
                            b
                        );
                    } else {
                        appendIfNotNull(
                            BoxEdge.TOP,
                            topLength,
                            b
                        );
                        appendIfNotNull(
                            BoxEdge.RIGHT,
                            rightLength,
                            b
                        );
                        appendIfNotNull(
                            BoxEdge.BOTTOM,
                            bottomLength,
                            b
                        );
                        appendIfNotNull(
                            BoxEdge.LEFT,
                            leftLength,
                            b
                        );
                    }

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

    // top: 1px; right: 2px;
    static void appendIfNotNull(final BoxEdge edge,
                                final Length<?> valueOrNull,
                                final StringBuilder b) {
        if (null != valueOrNull) {
            if(b.length() > 0) {
                b.append(TextStyle.SEPARATOR)
                    .append(' ');
            }
            // PROPERTY
            // left: 1px
            b.append(edge.textName)
                .append(TextStyle.ASSIGNMENT.character())
                .append(' ')
                .append(valueOrNull);
        }
    }
}
