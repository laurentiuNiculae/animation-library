package com.lniculae.AnimationParser;

import java.util.List;
import java.util.ArrayList;
import java.lang.Error;

import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public class Tokenizer {
    String filepath;
    String text;
    
    List<Token> tokens;
    int index = 0;

    public Tokenizer(String filepath, String text) {
        this.filepath = filepath;
        this.text = text;
    }

    public Result<Empty> InitTokenizer() {
        var tokensResult = GetTokens(filepath, text);
        if (!tokensResult.Ok()) {
            return new Result<>(tokensResult.Err());
        }

        this.tokens = tokensResult.Some();

        return Result.None;
    }

    public Result<Token> Peek() {
        if (index >= tokens.size()) {
            return new Result<>(new Error("NoMoreTokens"));
        }

        return new Result<>(tokens.get(index));
    }

    public Result<Token> Pop() {
        if (index >= tokens.size()) {
            return new Result<>(new Error("NoMoreTokens"));
        }

        var rez = new Result<>(tokens.get(index));
        index++;

        return rez;
    }

    public Token PeekBack() {
        if (tokens.size() == 0) {
            return new Token("", 0, 0, TokenKind.NoToken);
        }

        if (index == 0) {
            return tokens.get(0);
        }

        return tokens.get(index-1);
    }

    public Result<List<Token>> GetTokens(String filepath, String text) {
        var tokens = new ArrayList<Token>();
        int line = 0;
        int column = 0;

        int i = 0;
        
        loop:
        while (i < text.length()) {
            int j = i;
            
            if (Character.isWhitespace(text.charAt(j))) {
                column++;
                if (text.charAt(j) == '\n') {
                    line++;
                    column=0;
                }

                i++; 
                continue;
            }

            switch (text.charAt(j)) {
                case '{':
                    tokens.add(new Token("{", line, column, TokenKind.OpenCurlyParen));
                    i++; column++;
                    continue loop;
                case '}':
                    tokens.add(new Token("}", line, column, TokenKind.ClosedCurlyParen));
                    i++; column++;
                    continue loop;
                case '(':
                    tokens.add(new Token("(", line, column, TokenKind.OpenParen));
                    i++; column++;
                    continue loop;
                case ')':
                    tokens.add(new Token(")", line, column, TokenKind.ClosedParen));
                    i++; column++;
                    continue loop;
                case ',':
                    tokens.add(new Token(",", line, column, TokenKind.Comma));
                    i++; column++;
                    continue loop;
                case '=':
                    tokens.add(new Token("=", line, column, TokenKind.Equal));
                    i++; column++;
                    continue loop;
                case '#':
                    i++; column++;
                    j++;
                    while (j < text.length() && 
                        (Character.isAlphabetic(text.charAt(j)) || Character.isDigit(text.charAt(j)) || text.charAt(j) == '-'))
                    {
                        j++;
                        column++;
                    }

                    var content = text.substring(i, j);
                    tokens.add(new Token(content, line, column, TokenKind.Tag));
                    i = j;
                    continue loop;
            }

            if (Character.isDigit(text.charAt(j))) {
                while (j < text.length() && Character.isDigit(text.charAt(j))) {
                    j++;
                    column++;
                }

                if (j < text.length() && Character.isAlphabetic(text.charAt(j))) {
                    return new Result<>(
                        new Error(String.format("Lexer error at line: %s column: %s. Can't have identifier start with numbers", line, column))
                    );
                }

                var number = text.substring(i, j);
                tokens.add(new Token(number, line, column, TokenKind.Number));
                i = j;
                continue;
            }

            if (Character.isAlphabetic(text.charAt(j))) {
                for (var keyWord : new ArrayList<String>(){{
                    add("Secvential"); add("Persistent"); add("Syncronous");
                }}) {
                    if (!text.substring(i).startsWith(keyWord)) {
                        continue;
                    }

                    j += keyWord.length(); 
                    column += keyWord.length();
                    i = j;
                    tokens.add(new Token(keyWord, line, column, TokenKind.SyncronizationWord));
                    continue loop;
                }

                while (j < text.length() && 
                    (Character.isAlphabetic(text.charAt(j)) || Character.isDigit(text.charAt(j)))
                ) {
                    j++;
                    column++;
                }

                var content = text.substring(i, j);
                tokens.add(new Token(content, line, column, TokenKind.Ident));
                i = j;
                continue;
            }

            return new Result<>(
                new Error(String.format("Lexer error at %s:%d:%d. unexpected character '%c'", filepath, line+1, column+1, text.charAt(j)))
            );
        }

        return new Result<>(tokens);
    }
}
