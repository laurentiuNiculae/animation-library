package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;

public class DrawWait implements Task {
    float animationTime;
    float timeElapsed;

    public DrawWait(float waitSeconds) {
        animationTime = waitSeconds;
    }
    
    @Override
    public float Draw(float dt) {
        timeElapsed += dt; 
        return dt;
    }

    @Override
    public boolean Finished() {
        return timeElapsed + 1e-06 >= animationTime;
    }

    @Override
    public void Reset() {
        timeElapsed = 0;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        return;
    }
}
