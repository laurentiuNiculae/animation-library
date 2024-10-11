package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;
import com.lniculae.Animation.Lerp.LerpColor;

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
        float drawTime = dt;
        if (timeElapsed + dt > animationTime) {
            drawTime = animationTime - timeElapsed;
            timeElapsed = animationTime;
            this.color.setProgress(1);
        } else {
            timeElapsed += dt;
            this.color.setProgress(timeElapsed/animationTime);
        }

        ctx.setBackgroundColor(new Color(
            color.lerpEnd.r,
            color.lerpEnd.g,
            color.lerpEnd.b,
            color.lerpEnd.a
        ));

        // ctx.clearBackground(ctx.getBackgroundColor());

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
