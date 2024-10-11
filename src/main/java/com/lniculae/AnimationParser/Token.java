package com.lniculae.AnimationParser;

public class Token {
    public String Content;
    public int Line;
    public int Column;
    public TokenKind Kind;

    public Token(String Content, int Line, int Column, TokenKind Kind) {
        this.Content = Content;
        this.Line = Line+1;
        this.Column = Column+1;
        this.Kind = Kind;
    }

    public String toString() {
        return String.format("Token[%s %s]", Content, Kind.toString());
    }
}
