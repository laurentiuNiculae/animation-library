package com.lniculae.Animation;

import com.lniculae.Animation.Context.AnimationContext;

public class EmptyTask implements Task {
    boolean finished = false;

    @Override
    public float Draw(float dt) {
        finished = true;
        return 0;
    }

    @Override
    public boolean Finished() {
        return finished;
    }

    @Override
    public void Reset() {
        return;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        return;
    }

}
