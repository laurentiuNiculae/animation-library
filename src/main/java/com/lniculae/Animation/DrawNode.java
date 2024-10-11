package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;
import com.lniculae.Animation.Context.RaylibAnimationContext;
import com.lniculae.Animation.Lerp.LerpVec1;

public class DrawNode implements Task {
    AnimationContext ctx;

    Color color;
    Vec2 center;
    LerpVec1 radius;

    float animationTime;
    float timeElapsed;
    float progress;
    boolean finished = false;

    public DrawNode(RaylibAnimationContext ctx, Vec2 center, float radius, Color color, float animationTime) {
        this(center, radius, color, animationTime);
        this.ctx = ctx;
    }
    
    public DrawNode(Vec2 center, float radius, Color color, float animationTime) {
        this.color = color;
        this.center = center;
        this.animationTime = animationTime;
        this.radius = new LerpVec1(radius, radius);
    }

    public DrawNode setCenter(Vec2 center) {
        this.center.x = center.x;
        this.center.y = center.y;
        
        return this;
    }

    public Vec2 getCenter() {
        return center;
    }

    public DrawNode radius(float from, float to) {
        this.radius = new LerpVec1(from, to);
        return this;
    }

    public DrawNode radius(float from, float to, EasingFunction f) {
        this.radius = new LerpVec1(from, to, f);
        return this;
    }

    @Override
    public float Draw(float dt) {
        var drawTime = (float) 0.0;
        if (timeElapsed + dt >= animationTime) {
            drawTime = animationTime - timeElapsed;
            timeElapsed = animationTime;
            this.setProgress(1);
        } else {
            drawTime = dt;
            timeElapsed += dt;
            this.setProgress(timeElapsed/animationTime);
        }

        ctx.drawCircle(center, radius.lerpEnd, color);
        return drawTime;
    }

    public DrawNode setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        }

        this.timeElapsed = this.animationTime * progress;
        this.progress = progress;
        this.radius.setProgress(progress);
        return this;
    }

    @Override
    public boolean Finished() {
        return timeElapsed + 1e-06 >= animationTime;
    }

    @Override
    public void Reset() {
        progress = 0;
        timeElapsed = 0;
        radius.lerpEnd = radius.end;
        finished = false;
        return;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        this.ctx = ctx;
    }
}
