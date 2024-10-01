package AnimationParser;

import com.raylib.java.raymath.Vector2;

public class ParsedVec2 {
    public Vector2 vec2;
    public int parsedTokens;

    public ParsedVec2(Vector2 task, int parsedTokens) {
        this.vec2 = task;
        this.parsedTokens = parsedTokens;
    }
}
