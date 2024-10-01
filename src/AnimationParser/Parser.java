package AnimationParser;

import Animation.*;
import java.util.List;

import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

import java.util.ArrayList;

public class Parser {
    public Task ParseTask(String text) {
        var tokens = new Tokenizer().GetTokens(text);

        var x = ParseSyncBlock(tokens);

        return x.task;
    }

    public ParsedTask ParseSyncBlock(List<Token> tokens) throws Error {
        if (tokens.size() == 0) {
            return new ParsedTask(new EmptyTask(), 0);
        }

        var firstToken = tokens.get(0);

        if (firstToken.Kind != TokenKind.SyncronizationWord) {
            throw new Error(
                    String.format(
                            "Expected a Syncronization word like 'Syncronous', 'Secvential' or 'Persistent' but got '%s' at line %d col: %d",
                            firstToken.Content, firstToken.Line, firstToken.Column));
        }

        if (tokens.size() == 1) {
            throw new Error(
                    String.format(
                            "Expected a Open Curly Paren '{' at line %s col: %s", firstToken.Line,
                            firstToken.Column + firstToken.Content.length()));
        }

        var token = tokens.get(1);

        if (token.Kind != TokenKind.OpenCurlyParen) {
            throw new Error(
                    String.format(
                            "Expected a Open Curly Paren '{' at line %s col: %s",
                            token.Line, token.Column));
        }

        if (tokens.size() == 2) {
            throw new Error(
                    String.format(
                            "Expected a Close Curly Paren '}' at line %s col: %s",
                            token.Line, token.Column + 1));
        }

        tokens = tokens.subList(2, tokens.size());

        var content = ParseSyncBlockContent(tokens);
        int totalTokensParsed = content.stream().map(t -> t.parsedTokens).reduce(0, (t, u) -> t + u);

        Task task = new EmptyTask();

        switch (firstToken.Content) {
            case KeyWords.Syncronous:
                var sync = new Syncronous();
                content.forEach(t -> sync.addTask(t.task));
                task = sync;
                break;
            case KeyWords.Secvential:
                var secv = new Syncronous();
                content.forEach(t -> secv.addTask(t.task));
                task = secv;
                break;
            case KeyWords.Persistent:
                var pers = new Syncronous();
                content.forEach(t -> pers.addTask(t.task));
                task = pers;
                break;
            default:
                throw new Error(String.format("Unexpected result '%s'", token.Content));
        }

        var finalA = new Syncronous();
        finalA.addTask(new DrawBackground(Color.BLACK, Color.DARKGRAY, 1));
        finalA.addTask(task);

        return new ParsedTask(finalA, totalTokensParsed + 2);
    }

    public List<ParsedTask> ParseSyncBlockContent(List<Token> tokens) {
        var tasks = new ArrayList<ParsedTask>();

        loop:
        while (tokens.size() > 0) {
            var token = tokens.get(0);
            var fix = tokens;
            tokens = tokens.subList(1, tokens.size());

            switch (token.Kind) {
                case ClosedCurlyParen:
                    var task = new EmptyTask();
                    tasks.add(new ParsedTask(task, 1));
                    return tasks;
                case Ident:
                    switch (token.Content) {
                        case DrawingPrimitives.DrawLine:
                            if (tokens.size() == 0 ) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));
                            if (tokens.get(0).Kind != TokenKind.OpenParen) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));

                            var drawLine = ParseDrawLineParams(token, tokens.subList(1, tokens.size()));
                            tasks.add(drawLine);
                            tokens = tokens.subList(drawLine.parsedTokens, tokens.size());
                            continue loop;
                        // case DrawingPrimitives.DrawNode:
                        //     if (tokens.size() == 0 ) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));
                        //     if (tokens.get(0).Kind != TokenKind.OpenParen) throw new Error(String.format("Expected Open Paren '(' at line %s col: %s", token.Line, token.Column));

                        //     var drawNode = ParseDrawNodeParams(tokens);
                        //     tasks.add(drawNode);
                        //     tokens = tokens.subList(drawNode.parsedTokens, tokens.size());
                        //     continue loop;
                        default:
                            throw new Error(
                                String.format(
                                    "Unknown identifier '%s' at line %s col: %s",
                                        token.Content, token.Line, token.Column));
                    }
                case SyncronizationWord:
                    var syncBlock = ParseSyncBlock(fix);
                    tasks.add(syncBlock);
                    tokens = tokens.subList(syncBlock.parsedTokens, tokens.size());
                    continue loop;
                default:
                    throw new Error(
                        String.format(
                            "Unknown token '%s' of type '%s' at line %s col: %s",
                                token.Content, token.Kind.toString(), token.Line, token.Column));
            }
        }

        return tasks;
    }

    // private ParsedTask ParseDrawNodeParams(List<Token> tokens) {
    //     // if (tokens.get(0))
    // }

    private ParsedTask ParseDrawLineParams(Token lastToken, List<Token> tokens) {
        if (tokens.size() == 0 ) throw new Error(String.format("Unexpected end of file. Expected a vec2 like '(1, 2)' at line %s col: %s", lastToken.Line, lastToken.Column));

        var start = ParseVec2(tokens);
        tokens = tokens.subList(start.parsedTokens, tokens.size());
        if (tokens.size() == 0) throw new Error(String.format("Unexpected end of file. Expected a comma ',' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));

        lastToken = tokens.get(0);
        if (lastToken.Kind != TokenKind.Comma) throw new Error(String.format("Expected a comma ',' but got '%s' at line %s col: %s", lastToken.Line, lastToken.Column, lastToken.Content));
        
        tokens = tokens.subList(1, tokens.size());
        
        var end = ParseVec2(tokens);
        tokens = tokens.subList(end.parsedTokens, tokens.size());
        if (tokens.size() == 0) throw new Error(String.format("Unexpected end of file. Expected a comma ',' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));

        lastToken = tokens.get(0);
        if (lastToken.Kind != TokenKind.Comma) throw new Error(String.format("Expected a comma ',' but got '%s' at line %s col: %s", lastToken.Line, lastToken.Column, lastToken.Content));
        
        tokens = tokens.subList(1, tokens.size());
        if (tokens.size() == 0) throw new Error(String.format("Unexpected end of file. Expected a comma ')' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));

        lastToken = tokens.get(0);
        if (lastToken.Kind != TokenKind.Number) throw new Error(String.format("Expected a comma ')' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));
        
        float animationTime = Float.parseFloat(lastToken.Content);

        tokens = tokens.subList(1, tokens.size());
        if (tokens.size() == 0) throw new Error(String.format("Unexpected end of file. Expected a close paren ')' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));

        lastToken = tokens.get(0);
        if (lastToken.Kind != TokenKind.ClosedParen) throw new Error(String.format("Expected a closed paren ')' at line %s col: %s", lastToken.Line, lastToken.Column + start.parsedTokens));

        return new ParsedTask(new DrawLine(start.vec2, end.vec2, animationTime), 1 + start.parsedTokens + 1 + end.parsedTokens + 1 + 1 + 1);
    }

    private ParsedVec2 ParseVec2(List<Token> tokens) {
        int totalTokensParsed = 0;

        var token = tokens.get(0);

        if (token.Kind != TokenKind.OpenParen)  throw new Error(String.format("Expected open paren but got '%s' at line %s col: %s", token.Line, token.Column, token.Content));
        
        tokens = tokens.subList(1, tokens.size());
        totalTokensParsed++;
        token = tokens.get(0);
        if (token.Kind != TokenKind.Number) throw new Error(String.format("Expected number like '69' but got '%s' at line %s col: %s", token.Line, token.Column, token.Content));

        float x = Float.parseFloat(token.Content);
        
        tokens = tokens.subList(1, tokens.size());
        if (tokens.size() == 0 ) throw new Error(String.format("Unexpected end of file. Expected a comma ',' at line %s col: %s", token.Line, token.Column));
        totalTokensParsed++;

        token = tokens.get(0);
        if (token.Kind != TokenKind.Comma) throw new Error(String.format("Expected a comma ',' but got '%s' at line %s col: %s", token.Line, token.Column, token.Content));

        tokens = tokens.subList(1, tokens.size());
        if (tokens.size() == 0 ) throw new Error(String.format("Unexpected end of file. Expected a vec2 like '(1, 2)' at line %s col: %s", token.Line, token.Column));
        totalTokensParsed++;

        token = tokens.get(0);
        if (token.Kind != TokenKind.Number) throw new Error(String.format("Expected number like '69' but got '%s' at line %s col: %s", token.Line, token.Column, token.Content));
        float y = Float.parseFloat(token.Content);

        tokens = tokens.subList(1, tokens.size());
        if (tokens.size() == 0 ) throw new Error(String.format("Unexpected end of file. Expected Close Paren ')' at line %s col: %s", token.Line, token.Column));
        totalTokensParsed++;

        token = tokens.get(0);
        if (token.Kind != TokenKind.ClosedParen) throw new Error(String.format("Expected Close Paren ')' but got '%s' at line %s col: %s", token.Line, token.Column, token.Content));

        // for the close paren
        totalTokensParsed++;

        return new ParsedVec2(new Vector2(x, y), totalTokensParsed);
    }
}