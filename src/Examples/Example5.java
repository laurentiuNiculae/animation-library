package Examples;

import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import Animation.AnimationContext;
import Animation.AnimationRenderer;
import Animation.EmptyTask;
import Animation.Task;
import Graph.Graph;

public class Example5 {
    static int Width = 800;
    static int Height = 600;

    Graph graph;

    public Example5() {
        graph = new Graph() {
            {
                setNode(1, 5);
                setNode(2, 25);
                setNode(3, 50);
                setNode(4, 100);
                setNode(5, 200);
                setNode(6, 250);
                setEdge(1, 2);
                setEdge(1, 3);
                setEdge(1, 4, 32);
                setEdge(2, 3);
                setEdge(4, 6);
            }
        };
    }

    private Task GetAnimation(AnimationContext ctx) {
        return new EmptyTask();
    }

    public void RunExample() {
        var ctx = new AnimationContext(Width, Height, 60);
        var animation = GetAnimation(ctx);
        var animationRenderer = new AnimationRenderer(ctx, animation);
        // var background = new Color(18, 18, 18, 255);
        var background = Color.RED;
        
        ctx.core.InitWindow(Width, Height, "Raylib-J Example");
        ctx.core.SetTargetFPS(60);
        
        boolean canStart = false;

        int frameCount = 0;

        while (!ctx.core.WindowShouldClose()) {
            ctx.core.BeginDrawing();
            ctx.core.ClearBackground(background);
            
            // float dt = ctx.core.GetFrameTime();
            
            if (ctx.core.IsKeyDown(Keyboard.KEY_E)) {
                canStart = true;
                animation.Reset();
                ctx.core.ClearBackground(background);
            }

            if (canStart) {
                try {
                    animation.Draw((float)1/ctx.getFPS());
                    animationRenderer.RenderFrameToFile((float)1/ctx.getFPS());
                    System.out.println(frameCount);
                    frameCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (animationRenderer.Finished()) {
                try {
                    animationRenderer.EndRenderFrameToFile();
                    System.out.println("Ending");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ctx.core.EndDrawing();
        }
    }

    static public float easeOutBounce(float x) {
        float n1 = (float) 7.5625;
        float d1 = (float) 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return (float) (n1 * (x -= 1.5 / d1) * x + 0.75);
        } else if (x < 2.5 / d1) {
            return (float) (n1 * (x -= 2.25 / d1) * x + 0.9375);
        } else {
            return (float) (n1 * (x -= 2.625 / d1) * x + 0.984375);
        }
    }
}
