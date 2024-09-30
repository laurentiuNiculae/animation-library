package Animation;

import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

import Animation.Lerp.LerpVec1;
import Animation.Lerp.LerpVec2;

public class DrawLine implements Task {
    AnimationContext ctx;
    LerpVec2 position;
    LerpVec1 lineWidth; 

    float animationTime;
    float timeElapsed;
    float progress;

    public DrawLine(AnimationContext ctx, Vector2 start, Vector2 end, float animationTime) {
        this(start, end, animationTime);
        this.ctx = ctx;
    }
    
    public DrawLine(Vector2 start, Vector2 end, float animationTime) {
        this.position = new LerpVec2(start, end);
        this.lineWidth = new LerpVec1(1, 1);
        this.animationTime = animationTime;
        this.timeElapsed = 0;
        this.progress = 0;
    }

    public DrawLine setLineWidth(float from, float to) {
        this.lineWidth = new LerpVec1(from, to);
        return this;
    }
    
    public DrawLine setLineWidth(float from, float to, EasingFunction easingFunc) {
        this.lineWidth = new LerpVec1(from, to, easingFunc);
        return this;
    }
    
    public DrawLine setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        }

        this.timeElapsed = this.animationTime * progress;
        this.progress = progress;
        this.position.setProgress(progress);
        this.lineWidth.setProgress(progress);
        return this;
    }
    
    public DrawLine setTimeElapsed(float timeElapsed) {
        if (timeElapsed > animationTime) {
            timeElapsed = animationTime;
        }
        
        this.timeElapsed = timeElapsed;
        this.progress = timeElapsed/animationTime;
        this.position.setProgress(progress);

        return this;
    }

    public DrawLine setEasingFunction(EasingFunction easingFunc) {
        this.position.easingFunc = easingFunc;
        return this;
    }

    public float Draw(float dt) {
        var drawTime = dt;
        if (timeElapsed + dt > animationTime) {
            drawTime = animationTime - timeElapsed;
            timeElapsed = animationTime;
            this.setProgress(1);
        } else {
            timeElapsed += dt;
            this.setProgress(timeElapsed/animationTime);
        }
        
        ctx.shapes.DrawLineEx(position.start, position.lerpEnd, lineWidth.lerpEnd, Color.RAYWHITE);
        return drawTime;
    }

    @Override
    public boolean Finished() {
        return timeElapsed + 1e-06 >= animationTime;
    }

    @Override
    public void Reset() {
        position.setProgress(1);
        timeElapsed = 0;
        progress = 0;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        this.ctx = ctx;
    }
}
