package Animation;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

public class DrawNode implements Task {
    Raylib raylib;
    Color color;
    Vector2 center;
    float radius;
    boolean finished = false;

    public DrawNode(Raylib rlj, Vector2 center, float radius, Color color) {
        this.raylib = rlj;
        this.color = color;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void Draw(float dt) {
        raylib.shapes.DrawCircleV(center, radius, color);
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


}
