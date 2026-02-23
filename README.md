[![Build Status](https://github.com/mP1/walkingkooka-tree-text/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/alkingkooka-tree-text/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree-text/badge.svg)](https://coveralls.io/github/mP1/walkingkooka-tree-text)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree-text.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-text/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree-text.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-text/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-tree-text)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

A rich graph representation of a document along with styling of individual nodes, similar to the DOM concepts used by a life HTML document within a browser.  Some powerful extras include immutability and significant helpers to assist with querying and modifications. The representation is technology agnostic but translate to representing a browser DOM easily.

Additional converters to assist converting simple types like String to [TextNode](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/TextNode.java) or [TextStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/TextStyle.java) are available, along with functions enabling CRUD operations and more.

- Note that many but not all CSS properties are represented as this is intentional, and more may be added in the future, see [TextStylePropertyName](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/TextStylePropertyName.java)
- Because [TextNode](https://github.com/mP1/walkingkooka/blob/master/Node.md) is a [Node], technologies found in other repos support XPATH, XPOINTER etc.

## Java sample

A simple sample below is presented which demonstrates building of a TextNode graph and conversion to HTML.

```text
final TextNode node = TextNode.styleName(TextStyleName.with("HTML"))
    .appendChild(
        TextNode.styleName(
            TextStyleName.with("head")
        ).appendChild(
            TextNode.styleName(
                TextStyleName.with("TITLE")
            ).appendChild(
                TextNode.text("title123")
            )
        )
    ).appendChild(TextNode.styleName(TextStyleName.with("BODY"))
        .appendChild(
            TextNode.hyperlink(
                Url.parseAbsolute("https://example.com/hello")
            ).appendChild(TextNode.text("hyper link text 123"))
        ).appendChild(
            TextNode.image(
                Url.parse("https://example.com/image.png")
            )
        ).appendChild(TextNode.text("before"))
        .appendChild(TextNode.text("something gray")
            .setAttributes(
                Maps.of(
                    TextStylePropertyName.COLOR,
                    Color.parse("#678")
                )
            )
        ).appendChild(TextNode.text("after"))
    );

final StringBuilder html = new StringBuilder();
final LineEnding eol = LineEnding.SYSTEM;
final IndentingPrinter printer = Printers.stringBuilder(html, eol)
        .indenting(Indentation.SPACES2);

// very simple html printer
new FakeTextNodeVisitor() {
    @Override
    protected Visiting startVisit(final TextNode node) {
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextNode node) {
    }

    @Override
    protected Visiting startVisit(final TextStyleNode node) {
        printer.lineStart();
        printer.print("<SPAN style=\"");

        node.attributes()
            .forEach((p, v) -> {
                printer.print(p + ": " + v + ";");
            });

        printer.print("\">" + eol);
        printer.indent();
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextStyleNode node) {
        printer.outdent();
        printer.lineStart();
        printer.print("</SPAN>" + eol);
    }

    @Override
    protected Visiting startVisit(final Hyperlink node) {
        this.beginElement("A href=\"" + node.url() + "\"");
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final Hyperlink node) {
        this.endElement("A");
    }

    @Override
    protected Visiting startVisit(final TextStyleNameNode node) {
        this.beginElement(node.styleName().value());
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextStyleNameNode node) {
        this.endElement(node.styleName().value());
    }

    @Override
    protected void visit(final Flag node) {
        printer.print(node.toHtml()); // prints flag emoji
    }
    
    @Override
    protected void visit(final Image node) {
        printer.print(node.toHtml());
    }

    @Override
    protected void visit(final Text node) {
        printer.print(node.value());
    }

    private void beginElement(final String element) {
        printer.lineStart();
        printer.print("<" + element + ">" + eol);
        printer.indent();
    }

    private void endElement(final String element) {
        printer.outdent();
        printer.lineStart();
        printer.print("</" + element + ">" + eol);
    }
}.accept(node);
```

prints

```text
<HTML>
  <head>
    <TITLE>
      title123
    </TITLE>
  </head>
  <BODY>
    <A href="https://example.com/hello">
      hyper link text 123
    </A>
    <IMG src="https://example.com/image.png"/>before
    <SPAN style="color: #667788;">
      something gray
    </SPAN>
    after
  </BODY>
</HTML>
```

### [Converters](https://github.com/mP1/walkingkooka-convert/blob/master/src/main/java/walkingkooka/convert/Converter.java)

These `Converters` that convert function parameter that may be `String` to other types like [TextStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/TextStyle.java)

- [has-text-node](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/HasTextNodeConverter.java)
- [has-text-style](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/HasTextStyleConverter.java)
- [text-to-text-node](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/TextToTextNodeConverter.java)
- [text-to-text-style](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/TextToTextStyleConverter.java)
- [text-to-text-style-property-name](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/TextToTextStylePropertyNameConverter.java)
- [to-styleable](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/ToStyleableConverter.java)
- [url-to-hyperlink](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/TreeTextConverterUrlToHyperlink.java)
- [url-to-image](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/convert/UrlToImageConverter.java)

### [Functions](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

Functions that will be useful to create a rich text display, by building individual components with text, styling and more.
These functions are intended to support creation of custom HTML for individual cell formatting in [walkingkooka-spreadsheet](https://github.com/mP1/walkingkooka-spreadsheet)

- [badge](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionBadge.java)
- [getStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionGetStyle.java)
- [getTextNode](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionGetTextNode.java)
- [hyperlink](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionHyperlink.java)
- [image](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionImage.java)
- [mergeStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionMergeStyle.java)
- [setText](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionSetText.java)
- [setStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionSetStyle.java)
- [style](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyle.java)
- [styleGet](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleGet.java)
- [styleRemove](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleRemove.java)
- [styleSet](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleSet.java)
- [styledText](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionStyledText.java)
