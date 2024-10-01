package Animation.Lerp;

import com.raylib.java.raymath.Raymath;
import com.raylib.java.raymath.Vector2;

import Animation.EasingFunction;

public class LerpVec2 {
    public Vector2 start;
    public Vector2 end;
    public Vector2 lerpEnd;
    public EasingFunction easingFunc;

    public LerpVec2(Vector2 start, Vector2 end) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = x -> x;
    }

    public LerpVec2(Vector2 start, Vector2 end, EasingFunction easingFunc) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = easingFunc;
    }

    public Vector2 setProgress(float progress) {
        progress = easingFunc.apply(progress);
        
        lerpEnd = Raymath.Vector2Lerp(start, end, progress);
        return lerpEnd;
    }
}