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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base class for any measure.
 */
public abstract class Length<V> {

    /**
     * Parses text that contains a support measurement mostly a number and unit.
     */
    public static Length<?> parse(final String text) {
        checkText(text);

        return NoneLength.TEXT.equals(text) ?
                NoneLength.INSTANCE :
                NormalLength.TEXT.equals(text) ?
                        NormalLength.INSTANCE :
                        LengthUnit.tryAllParse(text);
    }

    private static void checkText(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");
    }

    /**
     * Parses text that contains a normal literal.
     */
    public static NoneLength parseNone(final String text) {
        checkText(text);

        return NoneLength.parseNone0(text);
    }

    /**
     * Parses text that contains a normal literal.
     */
    public static NormalLength parseNormal(final String text) {
        checkText(text);

        return NormalLength.parseNormal0(text);
    }

    /**
     * Parses text that contains a number measurement.
     */
    public static NumberLength parseNumber(final String text) {
        checkText(text);

        return NumberLength.parseNumber0(text);
    }

    /**
     * Parses text that contains a pixel measurement, note the unit is required.
     */
    public static PixelLength parsePixels(final String text) {
        checkText(text);

        return PixelLength.parsePixels0(text);
    }

    /**
     * {@see NoneLength}
     */
    public static NoneLength none() {
        return NoneLength.INSTANCE;
    }

    /**
     * {@see NormalLength}
     */
    public static NormalLength normal() {
        return NormalLength.INSTANCE;
    }

    /**
     * {@see NumberLength}
     */
    public static NumberLength number(final long value) {
        return NumberLength.with(value);
    }

    /**
     * {@see PixelLength}
     */
    public static PixelLength pixel(final Double value) {
        return PixelLength.with(value);
    }

    /**
     * Package private to limit subclassing
     */
    Length() {
    }

    /**
     * Returns this length in pixels or throws a {@link UnsupportedOperationException}.
     */
    abstract public double pixelValue();

    abstract double doubleValue();

    abstract long longValue();

    /**
     * The unit portion of this length.
     */
    abstract public Optional<LengthUnit<V, Length<V>>> unit();

    // is ..............................................................................................................

    /**
     * Only {@link NoneLength} returns true.
     */
    public final boolean isNone() {
        return this instanceof NoneLength;
    }

    /**
     * Only {@link NormalLength} returns true.
     */
    public final boolean isNormal() {
        return this instanceof NormalLength;
    }

    /**
     * Only {@link NumberLength} returns true.
     */
    public final boolean isNumber() {
        return this instanceof NumberLength;
    }

    /**
     * Only {@link PixelLength} returns true.
     */
    public final boolean isPixel() {
        return this instanceof PixelLength;
    }

    // parameter checking...............................................................................................

    abstract void pixelOrFail();

    final void pixelOrFail0() {
        throw new IllegalArgumentException("Expected pixel length but got " + this);
    }

    abstract void normalOrPixelOrFail();

    final void normalOrPixelOrFail0() {
        throw new IllegalArgumentException("Expected normal or pixel length but got " + this);
    }

    abstract void numberFail();

    final void numberFail0() {
        throw new IllegalArgumentException("Expected number but got " + this);
    }

    // LengthVisitor....................................................................................................

    abstract void accept(final LengthVisitor visitor);

    // Object ..........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    abstract boolean equals0(final Length other);

    // JsonNodeContext..................................................................................................

    /**
     * Accepts a json string holding a number and unit.
     */
    static Length unmarshall(final JsonNode node,
                             final JsonNodeUnmarshallContext context) {
        return unmarshall0(node, Length::parse);
    }

    /**
     * Accepts a json string holding a {@link NoneLength}
     */
    static NoneLength unmarshallNone(final JsonNode node) {
        return unmarshall0(node, Length::parseNone);
    }

    /**
     * Accepts a json string holding a {@link NormalLength}
     */
    static NormalLength unmarshallNormal(final JsonNode node) {
        return unmarshall0(node, Length::parseNormal);
    }

    /**
     * Accepts a json string holding a {@link NumberLength}
     */
    static NumberLength unmarshallNumber(final JsonNode node) {
        return unmarshall0(node, Length::parseNumber);
    }

    /**
     * Accepts a json string holding a {@link PixelLength}.
     */
    static PixelLength unmarshallPixel(final JsonNode node) {
        return unmarshall0(node, Length::parsePixels);
    }

    private static <L extends Length<?>> L unmarshall0(final JsonNode node,
                                                       final Function<String, L> factory) {
        Objects.requireNonNull(node, "node");

        return factory.apply(node.stringOrFail());
    }

    final JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static {
        JsonNodeContext.register("length",
                Length::unmarshall,
                Length::marshall,
                Length.class, NoneLength.class, NormalLength.class, NumberLength.class, PixelLength.class);
    }
}
