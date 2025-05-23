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

import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

/**
 * Value class that holds a text-overflow
 */
public abstract class TextOverflow implements Value<Optional<String>>,
    HasText {

    final static String CLIP_TEXT = "clip";

    /**
     * A constant that text-overflow should be clipped
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextOverflow CLIP = TextOverflowNonString.constant(CLIP_TEXT);

    final static String ELLIPSIS_TEXT = "ellipsis";

    /**
     * A constant that text-overflow should be clipped with three ellipsis.
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextOverflow ELLIPSIS = TextOverflowNonString.constant(ELLIPSIS_TEXT);

    /**
     * The inverse of {@link #text()}, parses the text and returns a {@link TextOverflow}.
     * <br>
     * This supports a token holding "clip" or "eclipse" without the quotes or a single quoted string.
     */
    public static TextOverflow parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        final int MODE_SKIP_INITIAL_WHITESPACE = 1;

        final int MODE_UNQUOTED_TOKEN = 2;

        final int MODE_QUOTED_INSIDE = 3;
        final int MODE_QUOTED_ESCAPING = 4;
        final int MODE_QUOTED_OUTSIDE = 5;

        int mode = MODE_SKIP_INITIAL_WHITESPACE;

        final StringBuilder b = new StringBuilder();

        int i = 0;
        for (char c : text.toCharArray()) {
            switch (mode) {
                case MODE_SKIP_INITIAL_WHITESPACE:
                    if (Character.isWhitespace(c)) {
                        break;
                    }
                    switch (c) {
                        case '\"':
                            mode = MODE_QUOTED_INSIDE;
                            break;
                        default:
                            mode = MODE_UNQUOTED_TOKEN;
                            b.append(c);
                            break;
                    }
                    break;
                case MODE_UNQUOTED_TOKEN:
                    b.append(c);
                    break;
                case MODE_QUOTED_INSIDE:
                    switch (c) {
                        case '\\':
                            mode = MODE_QUOTED_ESCAPING;
                            break;
                        case '\"':
                            mode = MODE_QUOTED_OUTSIDE;
                            break;
                        default:
                            b.append(c);
                            break;
                    }
                    break;
                case MODE_QUOTED_ESCAPING:
                    mode = MODE_QUOTED_INSIDE;
                    b.append(c);
                    break;
                case MODE_QUOTED_OUTSIDE:
                    if (Character.isWhitespace(c)) {
                        break;
                    }
                    throw new InvalidCharacterException(
                        text,
                        i
                    );
                default:
                    NeverError.unhandledCase(
                        mode
                    );
            }

            i++;
        }

        final TextOverflow textOverflow;

        switch (mode) {
            case MODE_UNQUOTED_TOKEN:
                switch (text) {
                    case CLIP_TEXT:
                        textOverflow = TextOverflow.CLIP;
                        break;
                    case ELLIPSIS_TEXT:
                        textOverflow = TextOverflow.ELLIPSIS;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid text");
                }
                break;
            case MODE_QUOTED_INSIDE:
            case MODE_QUOTED_ESCAPING:
                throw new IllegalArgumentException("Missing closing quote");
            case MODE_QUOTED_OUTSIDE:
                textOverflow = TextOverflow.string(
                    b.toString()
                );
                break;
            default:
                textOverflow = null;
                NeverError.unhandledCase(
                    mode,
                    MODE_SKIP_INITIAL_WHITESPACE,
                    MODE_UNQUOTED_TOKEN,
                    MODE_QUOTED_INSIDE,
                    MODE_QUOTED_ESCAPING,
                    MODE_QUOTED_OUTSIDE
                );
        }

        return textOverflow;
    }
    
    /**
     * Factory that creates a {@link TextOverflow}.
     */
    public static TextOverflow string(final String value) {
        return TextOverflowString.with(value);
    }

    /**
     * Package private to limit sub classing.
     */
    TextOverflow() {
        super();
    }

    /**
     * Getter that returns any text if present.
     */
    @Override
    public abstract Optional<String> value();

    public abstract boolean isClip();

    public abstract boolean isEllipse();

    public final boolean isString() {
        return this instanceof TextOverflowString;
    }

    @Override
    public abstract String toString();

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link TextOverflow} from the given node.
     */
    static TextOverflow unmarshall(final JsonNode node,
                                   final JsonNodeUnmarshallContext context) {
        return parse(
            node.stringOrFail()
        );
    }

    abstract JsonNode marshall(final JsonNodeMarshallContext context);

    static {
        JsonNodeContext.register("text-overflow",
            TextOverflow::unmarshall,
            TextOverflow::marshall,
            TextOverflow.class, TextOverflowNonString.class, TextOverflowString.class);
    }
}
