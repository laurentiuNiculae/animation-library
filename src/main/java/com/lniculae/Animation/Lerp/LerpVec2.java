package com.lniculae.Animation.Lerp;

import com.lniculae.Animation.EasingFunction;
import com.lniculae.Animation.Vec2;

public class LerpVec2 {
    public Vec2 start;
    public Vec2 end;
    public Vec2 lerpEnd;
    public EasingFunction easingFunc;

    public LerpVec2(Vec2 start, Vec2 end) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = x -> x;
    }

    public LerpVec2(Vec2 start, Vec2 end, EasingFunction easingFunc) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = easingFunc;
    }

    public Vec2 setProgress(float progress) {
        progress = easingFunc.apply(progress);
        
        lerpEnd = Vec2.Lerp(start, end, progress);
        
        return lerpEnd;
    }
}