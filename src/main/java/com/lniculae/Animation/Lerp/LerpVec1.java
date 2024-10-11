package com.lniculae.Animation.Lerp;

import com.lniculae.Animation.EasingFunction;

public class LerpVec1 {
    public float start;
    public float end;
    public float lerpEnd;
    public EasingFunction easingFunc;

    public LerpVec1(float start, float end) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = x -> x;
    }

    public LerpVec1(float start, float end, EasingFunction easingFunc) {
        this.start = start;
        this.lerpEnd = end;
        this.end = end;
        this.easingFunc = easingFunc;
    }

    public float setProgress(float progress) {
        progress = this.easingFunc.apply(progress);
        lerpEnd = (1- progress) * start + (progress) * end;
        return lerpEnd;
    }
}
