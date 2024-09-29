package Animation;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Raymath;
import com.raylib.java.raymath.Vector2;

public class DrawLine implements Task {
    Raylib raylib;
    Vector2 start;
    Vector2 end;
    Vector2 lerpEnd;
    float animationTime;
    float timeElapsed;
    float progress;
    EasingFunction easing;

    public DrawLine(Raylib rlj, Vector2 start, Vector2 end, float animationTime) {
        this.raylib = rlj;
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.animationTime = animationTime;
        this.timeElapsed = 0;
        this.progress = 0;

        this.easing = x -> x;
    }
    
    public DrawLine setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        }

        this.timeElapsed = this.animationTime * progress;
        this.progress = progress;
        this.lerpEnd = Raymath.Vector2Lerp(start, end, progress);
        return this;
    }
    
    public DrawLine setTimeElapsed(float timeElapsed) {
        if (timeElapsed > animationTime) {
            timeElapsed = animationTime;
        }
        
        this.timeElapsed = timeElapsed;
        this.progress = easing.apply(timeElapsed/animationTime);
        this.lerpEnd = Raymath.Vector2Lerp(start, end, progress);

        return this;
    }

    public DrawLine setEasingFunction(EasingFunction easingFunc) {
        this.easing = easingFunc;
        return this;
    }

    public void Draw(float dt) {
        timeElapsed += dt;
        if (timeElapsed > animationTime) {
            timeElapsed = animationTime;
        }

        progress = easing.apply(timeElapsed/animationTime);
        lerpEnd = Raymath.Vector2Lerp(start, end, progress);

        raylib.shapes.DrawLineV(start, lerpEnd, Color.RAYWHITE);
    }

    @Override
    public boolean Finished() {
        return timeElapsed >= animationTime;
    }

    @Override
    public void Reset() {
        lerpEnd = end;
        timeElapsed = 0;
        progress = 0;
    }
}
