package Animation;

import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

public class DrawNode implements Task {
    AnimationContext ctx;
    Color color;
    Vector2 center;
    float radius;
    boolean finished = false;

    public DrawNode(AnimationContext ctx, Vector2 center, float radius, Color color) {
        this(center, radius, color);
        this.ctx = ctx;
    }
    
    public DrawNode(Vector2 center, float radius, Color color) {
        this.color = color;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void Draw(float dt) {
        ctx.shapes.DrawCircleV(center, radius, color);
        finished = true;
    }

    @Override
    public boolean Finished() {
        return finished;
    }

    @Override
    public void Reset() {
        finished = false;
        return;
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        this.ctx = ctx;
    }


}
