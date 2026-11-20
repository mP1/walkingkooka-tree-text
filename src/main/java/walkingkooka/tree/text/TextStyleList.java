package walkingkooka.tree.text;

import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.HasCsvStringList;
import walkingkooka.collect.list.HasTsvStringList;
import walkingkooka.collect.list.ImmutableListDefaults;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.list.TsvStringList;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link List} that holds nullable {@link TextStyle textStyle}.
 */
public final class TextStyleList extends AbstractList<TextStyle>
    implements ImmutableListDefaults<TextStyleList, TextStyle>,
    HasCsvStringList,
    HasTsvStringList {

    public static TextStyleList fromCsvStringList(final CsvStringList csvStringList) {
        Objects.requireNonNull(csvStringList, "csvStringList");

        return EMPTY.setElements(
            csvStringList.stream()
                .map(
                    (String string) -> TextStyle.fromStringLikeList(
                        CsvStringList.parse(string)
                    )
                ).collect(Collectors.toList())
        );
    }

    public final static TextStyleList EMPTY = new TextStyleList(Lists.empty());

    // @VisibleForTesting
    TextStyleList(final List<TextStyle> list) {
        super();
        this.list = list;
    }

    @Override
    public TextStyle get(final int index) {
        return this.list.get(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public TextStyleList setElements(final Collection<TextStyle> collection) {
        TextStyleList currencyList;

        if (collection instanceof TextStyleList) {
            currencyList = (TextStyleList) collection;
        } else {
            final List<TextStyle> copy = Lists.array();
            copy.addAll(collection);

            if (copy.equals(this)) {
                currencyList = this;
            } else {
                currencyList = copy.isEmpty() ?
                    EMPTY :
                    new TextStyleList(copy);
            }
        }

        return currencyList;
    }

    @Override
    public void elementCheck(final TextStyle currency) {
        // null is allowed
    }

    private final List<TextStyle> list;

    // HasCsvString.....................................................................................................

    /**
     * Returns a {@link CsvStringList}, with each value a single {@link TextStyle} after converting to CSV.
     */
    @Override
    public CsvStringList csvStringList() {
        return CsvStringList.makeCsvStringList(
            this.list,
            (final TextStyle textStyle) -> null == textStyle ?
                "" :
                textStyle.csvStringList()
                    .text()
        );
    }

    // HasTsvString.....................................................................................................

    /**
     * Returns a {@link TsvStringList}, with each value a single {@link TextStyle} after converting to TSV.
     */
    @Override
    public TsvStringList tsvStringList() {
        return TsvStringList.makeTsvStringList(
            this.list,
            (final TextStyle textStyle) -> null == textStyle ?
                "" :
                textStyle.tsvStringList()
                    .text()
        );
    }
}
