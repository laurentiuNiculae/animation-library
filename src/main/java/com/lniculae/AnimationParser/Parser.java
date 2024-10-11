package com.lniculae.AnimationParser;

import com.lniculae.Animation.*;
import com.lniculae.Helper.Result;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    String filePath;
    Map<String, Vec2> globalVec2;
    Map<String, Float> globalFloat;

    public Result<Task> ParseTask(String filePath, String text) {
        this.filePath = filePath;
        globalVec2 = new HashMap<>();
        globalFloat = new HashMap<>();

        var tokenizer = new Tokenizer(filePath, text);
        var result = tokenizer.InitTokenizer();
         
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var x = ParseSyncBlock(tokenizer);
        if (!x.Ok()) {
            return new Result<>(x.Err());
        }

        var animationWithBackground = new Syncronous();
        animationWithBackground.addTask(x.Some());

        return new Result<>(animationWithBackground);
    }

    public Result<Task> ParseSyncBlock(Tokenizer tokenizer) {
        if (!tokenizer.Peek().Ok()) {
            return new Result<>(new EmptyTask());
        }
        var keyWord = tokenizer.Pop().Some();

        if (keyWord.Kind != TokenKind.SyncronizationWord) {
            return new Result<>(new Error(
                formatExpectedToken("Syncronization word like 'Syncronous', 'Secvential' or 'Persistent'", "", keyWord)  
            ));
        }

        var result = ParseSyncBlockContent(tokenizer);
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var contentList = result.Some();

        Task task = new EmptyTask();

        switch (keyWord.Content) {
            case KeyWords.Syncronous:
                var sync = new Syncronous();
                contentList.forEach(t -> sync.addTask(t));
                task = sync;
                break;
            case KeyWords.Secvential:
                var secv = new Secvential();
                contentList.forEach(t -> secv.addTask(t));
                task = secv;
                break;
            case KeyWords.Persistent:
                var pers = new PersistentSecvential();
                contentList.forEach(t -> pers.addTask(t));
                task = pers;
                break;
            default:
                return new Result<>(new Error(String.format("Unexpected result '%s'", keyWord.Content)));
        }

        return new Result<>(task);
    }

    public Result<List<Task>> ParseSyncBlockContent(Tokenizer tokenizer) {
        var tasks = new ArrayList<Task>();

        var result = tokenizer.Pop();
        if (!result.Ok()) {
            return new Result<>(new Error(
                formatEndOfFile(TokenKind.OpenCurlyParen.toString(), "{", tokenizer.PeekBack())
            ));
        }

        if (result.Some().Kind != TokenKind.OpenCurlyParen) {
            return new Result<>(new Error(
                formatExpectedToken(TokenKind.OpenCurlyParen.toString(), "{", result.Some())
            ));
        }

        loop:
        while (tokenizer.Peek().Ok()) {
            result = tokenizer.Peek();
            if (!result.Ok()) return new Result<>(new Error(
                formatEndOfFile(TokenKind.ClosedCurlyParen.toString(), "}", tokenizer.PeekBack())
            ));

            var token = result.Some();

            switch (token.Kind) {
                case ClosedCurlyParen:
                    var task = new EmptyTask();
                    tasks.add(task);
                    tokenizer.Pop();
                    return new Result<>(tasks);
                case Ident:
                    switch (token.Content) {
                        case DrawingPrimitives.DrawLine:
                            tokenizer.Pop();
                            var drawLine = ParseDrawLineParams(tokenizer);
                            if (!drawLine.Ok()) {
                                return new Result<>(drawLine.Err());
                            }
                            tasks.add(drawLine.Some());
                            continue loop;
                        // case DrawingPrimitives.DrawNode:
                        //     if (tokens.size() == 0 ) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));
                        //     if (tokens.get(0).Kind != TokenKind.OpenParen) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));

                        //     var drawNode = ParseDrawNodeParams(tokens);
                        //     tasks.add(drawNode);
                        //     tokens = tokens.subList(drawNode.parsedTokens, tokens.size());
                        //     continue loop;
                        default:
                            var newIdent = tokenizer.Pop().Some();
                            
                            var equalResult = PopTokenKind(tokenizer, TokenKind.Equal, "=");
                            if (!equalResult.Ok()) {
                                if (equalResult.Err().getMessage().contains("EndOfFile")) {
                                    return new Result<>(new Error(
                                        formatEndOfFile("", "", newIdent)
                                    ));    
                                }

                                return new Result<>(new Error(
                                    String.format("Stray indent '%s' at %s", newIdent.Content, 
                                        formatFileLocation(newIdent.Line, newIdent.Column))
                                ));
                            }

                            var vec2Result = ParseVec2(tokenizer);
                            if (!vec2Result.Ok()) {
                                return new Result<>(new Error(vec2Result.Err()));
                            }

                            globalVec2.put(newIdent.Content, vec2Result.Some());
                            continue loop;
                    }
                case SyncronizationWord:
                    var syncBlockResult = ParseSyncBlock(tokenizer);
                    if (!syncBlockResult.Ok()) {
                        return new Result<>(syncBlockResult.Err());
                    }

                    tasks.add(syncBlockResult.Some());
                    continue loop;
                default:
                    return new Result<>(new Error(String.format(
                            "Unknown token '%s' of type '%s' at %s",
                                token.Content, token.Kind.toString(), formatFileLocation(token.Line, token.Column))));
            }
        }

        return new Result<>(tasks);
    }

    // private ParsedTask ParseDrawNodeParams(List<Token> tokens) {
    //     // if (tokens.get(0))
    // }

    private Result<Task> ParseDrawLineParams(Tokenizer tokenizer) {
        var result = PopTokenKind(tokenizer, TokenKind.OpenParen, "(");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var start = new Vec2(0, 0);

        var token = tokenizer.Peek();
        if (!token.Ok()) {
            return new Result<>(new Error("Failed to parse vec2: " + token.Err().getMessage()));
        }

        switch (token.Some().Kind) {
            case Ident:
                if (!globalVec2.containsKey(token.Some().Content)) {
                    return new Result<>(new Error(
                        formatUnknownIdentifier(token.Some())
                    ));
                }

                start = globalVec2.get(token.Some().Content);
                tokenizer.Pop();
                break;
            case OpenParen:
                var vec2Result = ParseVec2(tokenizer);
                if (!vec2Result.Ok()) {
                    return new Result<>(new Error("Failed to parse vec2: " + vec2Result.Err().getMessage()));
                }

                start = vec2Result.Some();
                break;
            default:
                return new Result<>(new Error("Failed to parse vec2: wrong type, expected vec2 at " + formatFileLocation(token.Some().Line, token.Some().Column)));
        }
        
        result = PopTokenKind(tokenizer, TokenKind.Comma, ",");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var end = new Vec2(0, 0);

        token = tokenizer.Peek();
        if (!token.Ok()) {
            return new Result<>(new Error("Failed to parse vec2: " + token.Err().getMessage()));
        }

        switch (token.Some().Kind) {
            case Ident:
                if (!globalVec2.containsKey(token.Some().Content)) {
                    return new Result<>(new Error(
                        formatUnknownIdentifier(token.Some())
                    ));
                }

                end = globalVec2.get(token.Some().Content);
                tokenizer.Pop();
                break;
            case OpenParen:
                var vec2Result = ParseVec2(tokenizer);
                if (!vec2Result.Ok()) {
                    return new Result<>(new Error("Failed to parse vec2: " + vec2Result.Err().getMessage()));
                }

                end = vec2Result.Some();
                break;
            default:
            return new Result<>(new Error("Failed to parse vec2: wrong type, expected vec2 at " + formatFileLocation(token.Some().Line, token.Some().Column)));
        }

        result = PopTokenKind(tokenizer, TokenKind.Comma, ",");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }
        
        result = PopTokenKind(tokenizer, TokenKind.Number, "69");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var animationTime = Float.parseFloat(result.Some().Content);

        result = PopTokenKind(tokenizer, TokenKind.ClosedParen, ")");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var drawLine = new DrawLine(start, end, animationTime); 
        
        return new Result<>(drawLine);
    }

    private Result<Vec2> ParseVec2(Tokenizer tokenizer) {
        var result = PopTokenKind(tokenizer, TokenKind.OpenParen, "(");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        result = PopTokenKind(tokenizer, TokenKind.Number, "69");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var x = Float.parseFloat(result.Some().Content);

        result = PopTokenKind(tokenizer, TokenKind.Comma, ",");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        result = PopTokenKind(tokenizer, TokenKind.Number, "69");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        var y = Float.parseFloat(result.Some().Content);

        result = PopTokenKind(tokenizer, TokenKind.ClosedParen, ")");
        if (!result.Ok()) {
            return new Result<>(result.Err());
        }

        return new Result<>(new Vec2(x, y));
    }

    private Result<Token> PopTokenKind(Tokenizer tokenizer, TokenKind expectedKind, String example) {
        if (!tokenizer.Peek().Ok()) return new Result<>(new Error(
            formatEndOfFile(expectedKind.toString(), example, tokenizer.PeekBack())
        ));

        var result = tokenizer.Pop();

        if (result.Some().Kind != expectedKind) return new Result<>(new Error(
            formatExpectedToken(expectedKind.toString(), example, result.Some())
        ));

        return new Result<>(result.Some());
    }

    private String formatExpectedToken(String expectedSymbolName, String expectedSymbol, Token token) {
        String butGot = token.Content;
        int line = token.Line;
        int col = token.Column;
        return String.format("Expected %s '%s' but got '%s' at line %s:%d:%d", expectedSymbolName, expectedSymbol, butGot, filePath, line, col);
    }
    
    private String formatUnknownIdentifier(Token token) {
        String butGot = token.Content;
        int line = token.Line;
        int col = token.Column;
        return String.format("Unknown identifier'%s' at line %s:%d:%d", butGot, filePath, line, col);
    }

    private String formatEndOfFile(String expectedSymbolName, String expectedSymbol, Token lastToken) {
        String butGot = lastToken.Content;
        int line = lastToken.Line;
        int col = lastToken.Column + lastToken.Content.length();
        return String.format("Unexpected EndOfFile: Expected %s '%s' at line %s:%d:%d", expectedSymbolName, expectedSymbol, butGot, filePath, line, col);
    }

    private String formatFileLocation(int line, int col) {
        return String.format("%s:%d:%d", filePath, line, col);
    }
}