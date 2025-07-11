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
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextStyleTestCase<T extends TextStyle> implements ClassTesting2<TextStyle>,
    HasTextTesting,
    StyleableTesting<TextStyle>,
    CanBeEmptyTesting,
    HashCodeEqualsDefinedTesting2<TextStyle>,
    JsonNodeMarshallingTesting<TextStyle>,
    ToStringTesting<TextStyle>,
    TreePrintableTesting {

    TextStyleTestCase() {
        super();
    }

    // isEmpty...........................................................................................................

    @Test
    public final void testIsEmpty() {
        final TextStyle textStyle = this.createObject();

        this.isEmptyAndCheck(
            textStyle,
            textStyle.value().isEmpty()
        );
    }

    // setValues........................................................................................................

    @Test
    public final void testSetValuesWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setValues(null)
        );
    }

    @Test
    public final void testSetValuesWithInvalidValueFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> this.createObject()
                .setValues(
                    Maps.of(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        "hello"
                    )
                )
        );
        this.checkEquals(
            "Property \"background-color\": Expected color but got String",
            thrown.getMessage(),
            () -> thrown.toString()
        );
    }

    @Test
    public final void testSetValuesWithWildcardFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> this.createObject()
                .setValues(
                    Maps.of(
                        TextStylePropertyName.WILDCARD,
                        "hello"
                    )
                )
        );
        this.checkEquals(
            "Property \"*\": Values not supported",
            thrown.getMessage(),
            () -> thrown.toString()
        );
    }

    @Test
    public final void testSetValuesSame() {
        final TextStyle textStyle = this.createObject();
        assertSame(
            textStyle,
            textStyle.setValues(
                textStyle.value()
            )
        );
    }

    @Test
    public final void testSetValuesDifferent() {
        final TextStyle before = this.createObject();


        final Map<TextStylePropertyName<?>, Object> newValues = Maps.of(
            TextStylePropertyName.COLOR,
            Color.parse("#234")
        );

        final Map<TextStylePropertyName<?>, Object> expected = Maps.sorted();
        expected.putAll(before.value());
        expected.putAll(
            newValues
        );

        this.setValuesAndCheck(
            before,
            expected,
            TextStyle.EMPTY.setValues(
                expected
            )
        );
    }

    private void setValuesAndCheck(final TextStyle textStyle,
                                   final Map<TextStylePropertyName<?>, Object> values,
                                   final TextStyle expected) {
        this.checkEquals(
            expected,
            textStyle.setValues(values),
            () -> textStyle + " setValues " + values
        );
    }

    // merge............................................................................................................

    @Test
    public final void testMergeNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .merge(null)
        );
    }

    @Test
    public final void testMergeEmpty() {
        final TextStyle textStyle = this.createObject();
        final TextStyle empty = TextStyle.EMPTY;
        assertSame(
            textStyle,
            textStyle.merge(empty),
            () -> textStyle + " merge EMPTY"
        );
    }

    final void mergeAndCheck(final TextStyle textStyle,
                             final TextStyle other,
                             final TextStyle expected) {
        this.checkEquals(
            expected,
            textStyle.merge(other),
            () -> textStyle + " merge " + other
        );
    }

    // replace...........................................................................................................

    @Test
    public final void testReplaceNullTextNodeFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().replace(null));
    }

    final void replaceAndCheck(final TextStyle textStyle,
                               final TextNode textNode) {
        assertSame(textNode,
            textStyle.replace(textNode),
            () -> textStyle + " replace " + textNode);
    }

    final void replaceAndCheck(final TextStyle textStyle,
                               final TextNode textNode,
                               final TextNode expected) {
        final TextNode actual = textStyle.replace(textNode);
        this.checkEquals(expected,
            actual,
            () -> textStyle + " replace " + textNode + "\nEXPECTED.root\n" + expected.root() + "\nACTUAL.root\n" + actual.root() + '\n');
    }

    // get..............................................................................................................

    @Test
    public final void testGetNullFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().get(null));
    }

    @Test
    public final void testGetUnknown() {
        this.getAndCheck(this.createObject(),
            TextStylePropertyName.HYPHENS,
            null);
    }

    final <TT> void getAndCheck(final TextStyle textStyle,
                                final TextStylePropertyName<TT> propertyName,
                                final TT value) {
        this.checkEquals(Optional.ofNullable(value),
            textStyle.get(propertyName),
            () -> textStyle + " get " + propertyName);
    }

    // set..............................................................................................................

    @Test
    public final void testSetInvalidPropertyValueFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                final TextStylePropertyName<?> propertyName = TextStylePropertyName.FONT_FAMILY;
                this.createObject().set(propertyName, Cast.to("invalid"));
            }
        );
    }

    @Test
    public final void testSetWildcardFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                final TextStylePropertyName<?> propertyName = TextStylePropertyName.WILDCARD;
                this.createObject()
                    .set(
                        propertyName,
                        Cast.to("Invalid")
                    );
            }
        );
    }

    // remove...........................................................................................................

    @Test
    public final void testRemoveNullFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().remove(null));
    }

    @Test
    public final void testRemoveUnknown() {
        final TextStyle textStyle = this.createObject();
        assertSame(textStyle, textStyle.remove(TextStylePropertyName.HYPHENS));
    }

    // setBorder........................................................................................................

    @Test
    public final void testSetBorderWithNullColorFails() {
        this.setBorderWithNullFails(
            null,
            BorderStyle.DASHED,
            Length.none()
        );
    }

    @Test
    public final void testSetBorderWithNullStyleFails() {
        this.setBorderWithNullFails(
            Color.BLACK,
            null,
            Length.none()
        );
    }

    @Test
    public final void testSetBorderWithNullWidthFails() {
        this.setBorderWithNullFails(
            Color.BLACK,
            BorderStyle.DASHED,
            null
        );
    }

    private void setBorderWithNullFails(final Color color,
                                        final BorderStyle style,
                                        final Length<?> length) {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setBorder(
                color,
                style,
                length
            )
        );
    }

    // helpers .........................................................................................................

    final <TT extends TextNode> TT makeStyleNameParent(final TT child) {
        return this.styleName("parent-style-123")
            .setChildren(Lists.of(child))
            .children()
            .get(0)
            .cast();
    }

    final TextPlaceholderNode placeholder(final String placeholderName) {
        return TextNode.placeholder(TextPlaceholderName.with(placeholderName));
    }

    final TextStyleNameNode styleName(final String styleName) {
        return TextNode.styleName(TextStyleName.with(styleName));
    }

    final List<TextNode> children() {
        return Lists.of(TextNode.text("a1"), TextNode.text("b2"));
    }

    // Styleable........................................................................................................

    @Override
    public final TextStyle createStyleable() {
        return this.createObject();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextStyle> type() {
        return Cast.to(this.textStyleType());
    }

    abstract Class<T> textStyleType();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public final TextStyle unmarshall(final JsonNode from,
                                      final JsonNodeUnmarshallContext context) {
        return TextStyle.unmarshall(from, context);
    }

    @Override
    public final TextStyle createJsonNodeMarshallingValue() {
        return this.createObject();
    }
}
