package Animation;

public class EmptyTask implements Task {
    boolean finished = false;

    @Override
    public void Draw(float dt) {
        finished = true;
        return;
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
