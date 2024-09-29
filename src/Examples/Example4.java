package Examples;

import java.util.HashMap;

import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.raymath.Raymath;
import com.raylib.java.raymath.Vector2;

import Animation.AnimationContext;
import Animation.AnimationRenderer;
import Animation.DrawNode;
import Animation.PersistentSecvential;
import Animation.Syncronous;
import Animation.Task;
import Graph.Graph;

public class Example4 {
    static int Width = 800;
    static int Height = 600;

    Graph graph;

    public Example4() {
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
        int nodeWidth = 20;
        var centerPoint = new Vector2(Width / 2, Height / 2);
        int radius = Height / 3;

        float angle = 0;
        float deltaAngle = 2 * Raymath.PI / graph.nodeCount();
        var startPoint = new Vector2(radius, 0);
        var nodePositions = new HashMap<Integer, Vector2>();
        
        var animation = new Syncronous(ctx);

        var drawNodes = new Syncronous();
        for (var node : graph.Nodes()) {
            var rotationOffset = Raymath.Vector2Rotate(startPoint, angle);
            var point = new Vector2(rotationOffset.x + centerPoint.x, rotationOffset.y + centerPoint.y);
            nodePositions.put(node.Id, point);
            drawNodes.addTask(new DrawNode(point, nodeWidth, DrawingUtils.GetNodeColor(node)));

            angle += deltaAngle;
        }

        var drawLinesSync = new PersistentSecvential();

        for (var edge : graph.Edges()) {
            var drawLine = new Animation.DrawLine(
            nodePositions.get(edge.From.Id),
            nodePositions.get(edge.To.Id),
            (float) 0.7
            ).setEasingFunction(Example2::easeOutBounce);
            
            drawLinesSync.addTask(drawLine);
        }

        animation.addTask(drawLinesSync);
        animation.addTask(drawNodes);

        return animation;
    }

    public void RunExample() {
        var ctx = new AnimationContext(Width, Height, 60);
        var animation = GetAnimation(ctx);
        var animationRenderer = new AnimationRenderer(ctx, animation);
        var background = new Color(18, 18, 18, 255);
        
        ctx.core.InitWindow(Width, Height, "Raylib-J Example");
        ctx.core.SetTargetFPS(60);
        
        boolean canStart = false;

        int frameCount = 0;

        while (!ctx.core.WindowShouldClose()) {
            ctx.core.BeginDrawing();
            ctx.core.ClearBackground(background);
            
            if (ctx.core.IsKeyDown(Keyboard.KEY_E)) {
                canStart = true;
                animation.Reset();
                ctx.core.ClearBackground(background);
            }

            if (canStart) {
                try {
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
