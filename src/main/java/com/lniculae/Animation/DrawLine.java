package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;
import com.lniculae.Animation.Lerp.LerpVec1;
import com.lniculae.Animation.Lerp.LerpVec2;

public class DrawLine implements Task {
    AnimationContext ctx;
    LerpVec2 position;
    LerpVec1 lineWidth;

    float animationTime;
    float timeElapsed;
    float progress;

    public DrawLine(AnimationContext ctx, Vec2 start, Vec2 end, float animationTime) {
        this(start, end, animationTime);
        this.ctx = ctx;
    }
    
    public DrawLine(Vec2 start, Vec2 end, float animationTime) {
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
        
        ctx.drawLine(position.start, position.lerpEnd, lineWidth.lerpEnd, new Color(240, 240, 240, 255));
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
