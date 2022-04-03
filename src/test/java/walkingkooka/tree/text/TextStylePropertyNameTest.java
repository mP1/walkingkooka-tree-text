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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.FieldAttributes;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextStylePropertyNameTest extends TextNodeNameNameTestCase<TextStylePropertyName<?>> {

    @Test
    public void testConstants() {
        this.checkEquals(Lists.empty(),
                Arrays.stream(TextStylePropertyName.class.getDeclaredFields())
                        .filter(FieldAttributes.STATIC::is)
                        .filter(f -> f.getType() == TextStylePropertyName.class)
                        .filter(TextStylePropertyNameTest::constantNotCached)
                        .collect(Collectors.toList()),
                "");
    }

    private static boolean constantNotCached(final Field field) {
        try {
            final TextStylePropertyName<?> name = Cast.to(field.get(null));
            return name != TextStylePropertyName.with(name.value());
        } catch (final Exception cause) {
            throw new AssertionError(cause.getMessage(), cause);
        }
    }

    @Test
    public void testConstantValue() {
        this.checkEquals(Lists.empty(),
                Arrays.stream(TextStylePropertyName.class.getDeclaredFields())
                        .filter(FieldAttributes.STATIC::is)
                        .filter(f -> f.getType() == TextStylePropertyName.class)
                        .map(TextStylePropertyNameTest::checkConstantAndValueCompatible)
                        .filter(v -> null != v)
                        .collect(Collectors.toList()),
                "");
    }

    private static String checkConstantAndValueCompatible(final Field field) {
        try {
            final String fieldName = field.getName();
            final String expectedConstantValue = fieldName.replace('_', '-').toLowerCase();

            field.setAccessible(true);

            final TextStylePropertyName<?> constant = Cast.to(field.get(null));
            final String value = constant.value();
            return expectedConstantValue.equals(value) ?
                    null :
                    fieldName + "=" + value;

        } catch (final Exception cause) {
            throw new Error(cause.getMessage(), cause);
        }
    }

    @Test
    public void testValues() {
        this.checkEquals(
                TextStylePropertyName.values(),
                Arrays.stream(TextStylePropertyName.class.getDeclaredFields())
                        .filter(FieldAttributes.STATIC::is)
                        .filter(f -> f.getType() == TextStylePropertyName.class)
                        .map(TextStylePropertyNameTest::getField)
                        .collect(Collectors.toCollection(Sets::sorted))
        );
    }

    /**
     * Wraps a call to {@link Field} wrapping any checked reflection exceptions.
     */
    private static TextStylePropertyName<?> getField(final Field field) {
        try {
            return Cast.to(field.get(null));
        } catch (final Exception cause) {
            throw new Error(cause.getMessage());
        }
    }

    @Test
    public void testConstantNameBackgroundColor() {
        this.constantNameAndCheck(TextStylePropertyName.BACKGROUND_COLOR, "BACKGROUND_COLOR");
    }

    @Test
    public void testConstantBorderBottomColor() {
        this.constantNameAndCheck(TextStylePropertyName.BORDER_BOTTOM_COLOR, "BORDER_BOTTOM_COLOR");
    }

    @Test
    public void testConstantColor() {
        this.constantNameAndCheck(TextStylePropertyName.COLOR, "COLOR");
    }

    private void constantNameAndCheck(final TextStylePropertyName<?> property,
                                      final String constantName) {
        this.checkEquals(
                constantName,
                property.constantName(),
                () -> property + ".constantName()"
        );
    }

    @Test
    public void testEnumType() {
        this.checkEquals((Object)
                        TextStylePropertyName.values()
                                .stream()
                                .filter(n -> n.enumType().isPresent())
                                .collect(Collectors.toCollection(Sets::sorted)),
                TextStylePropertyName.values()
                        .stream()
                        .filter(n -> n.handler instanceof TextStylePropertyValueHandlerEnum)
                        .collect(Collectors.toCollection(Sets::sorted))
        );
    }

    @Test
    public void testJsonNodeNameCached() {
        final TextStylePropertyName<?> propertyName = this.createObject();
        assertSame(propertyName.marshallName(), propertyName.marshallName());
    }

    @Override
    public TextStylePropertyName<?> createName(final String name) {
        return TextStylePropertyName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public TextStylePropertyName<?> unmarshall(final JsonNode jsonNode,
                                               final JsonNodeUnmarshallContext context) {
        return TextStylePropertyName.unmarshall(jsonNode, context);
    }

    @Override
    public Class<TextStylePropertyName<?>> type() {
        return Cast.to(TextStylePropertyName.class);
    }
}
