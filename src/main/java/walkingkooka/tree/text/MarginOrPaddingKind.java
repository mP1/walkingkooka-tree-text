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

/**
 * Abstraction that provides access to the TOP, RIGHT, BOTTOM, LEFT properties for either {@link MarginOrPadding}.
 */
public enum MarginOrPaddingKind {
    
    MARGIN {
        @Override
        public TextStylePropertyName<Length<?>> top() {
            return TextStylePropertyName.MARGIN_TOP;
        }

        @Override
        public TextStylePropertyName<Length<?>> right() {
            return TextStylePropertyName.MARGIN_RIGHT;
        }

        @Override
        public TextStylePropertyName<Length<?>> bottom() {
            return TextStylePropertyName.MARGIN_BOTTOM;
        }

        @Override
        public TextStylePropertyName<Length<?>> left() {
            return TextStylePropertyName.MARGIN_LEFT;
        }
    },
    
    PADDING {
        @Override
        public TextStylePropertyName<Length<?>> top() {
            return TextStylePropertyName.PADDING_TOP;
        }

        @Override
        public TextStylePropertyName<Length<?>> right() {
            return TextStylePropertyName.PADDING_RIGHT;
        }

        @Override
        public TextStylePropertyName<Length<?>> bottom() {
            return TextStylePropertyName.PADDING_BOTTOM;
        }

        @Override
        public TextStylePropertyName<Length<?>> left() {
            return TextStylePropertyName.PADDING_LEFT;
        }
    };
    
    public abstract TextStylePropertyName<Length<?>> top();

    public abstract TextStylePropertyName<Length<?>> right();

    public abstract TextStylePropertyName<Length<?>> bottom();

    public abstract TextStylePropertyName<Length<?>> left();
}
