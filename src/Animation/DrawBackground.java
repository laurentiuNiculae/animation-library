package Animation;

import com.raylib.java.core.Color;

import Animation.Lerp.LerpColor;

public class DrawBackground implements Task{
    AnimationContext ctx;
    
    LerpColor color;

    float animationTime;
    float timeElapsed;
    boolean finished;

    public DrawBackground(Color start, Color end, float animationTime) {
        this.color = new LerpColor(start, end);
        this.animationTime = animationTime;
    }

    public DrawBackground setEasingFunc(EasingFunction f) {
        this.color.easingFunc = f;
        return this;
    }

    @Override
    public float Draw(float dt) {
        var drawTime = dt;
        if (timeElapsed + dt > animationTime) {
            drawTime = animationTime - timeElapsed;
            timeElapsed = animationTime;
            this.color.setProgress(1);
        } else {
            timeElapsed += dt;
            this.color.setProgress(timeElapsed/animationTime);
        }

        ctx.backgroundColor.r = color.lerpEnd.r;
        ctx.backgroundColor.g = color.lerpEnd.g;
        ctx.backgroundColor.b = color.lerpEnd.b;
        ctx.backgroundColor.a = color.lerpEnd.a;

        ctx.core.ClearBackground(ctx.backgroundColor);

        return drawTime;
    }

    @Override
    public boolean Finished() {
        return timeElapsed + 1e-06 >= animationTime;
    }

    @Override
    public void Reset() {
        this.color.setProgress(1);
        this.timeElapsed = 0;
        finished = false;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        this.ctx = ctx;
    }
}
