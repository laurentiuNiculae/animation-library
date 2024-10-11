package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;

public interface Task {
    public float Draw(float dt);
    public boolean Finished();
    public void Reset();
    public void SetAnimationCtx(AnimationContext ctx);
}
