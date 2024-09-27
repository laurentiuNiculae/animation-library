package Animation;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Raymath;
import com.raylib.java.raymath.Vector2;

public class RenderLine {
    Raylib raylib;
    Vector2 start;
    Vector2 end;
    Vector2 lerpEnd;
    float animationTime;
    float timeElapsed;
    float progress;

    public RenderLine(Raylib rlj, Vector2 start, Vector2 end, float animationTime) {
        this.raylib = rlj;
        this.start = start;
        this.lerpEnd = start;
        this.end = end;
        this.animationTime = animationTime;
        this.timeElapsed = 0;
        this.progress = 0;
    }
    
    public RenderLine(Raylib rlj, Vector2 start, Vector2 end, float animationTime, float progress) {
        this.raylib = rlj;
        this.start = start;
        this.lerpEnd = start;
        this.end = end;
        this.animationTime = animationTime;
        this.timeElapsed = animationTime * progress;
        this.progress = progress;
    }

    public void Draw(float dt) {
        timeElapsed += dt;
        if (timeElapsed <= animationTime) {
            progress = animationTime/timeElapsed;
            lerpEnd = Raymath.Vector2Lerp(start, end, progress);
        }
        raylib.shapes.DrawLineV(start, lerpEnd, Color.RAYWHITE);
    }

    public void Reset() {
        lerpEnd = start;
        timeElapsed = 0;
        progress = 0;
    }
}
