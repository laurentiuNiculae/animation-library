import java.util.HashMap;
import com.raylib.java.Raylib;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;

import Animation.AnimationScriptParser;

import com.raylib.java.raymath.Raymath;

public class App {
    static int Width = 800;
    static int Height = 600;

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
        
    public static void main(String[] args){
        var scriptParse = new AnimationScriptParser("/home/laur/dev/personal/java-learn/bunica/src/script");

        scriptParse.PrintFileContent();
    }

    public static void main2(String[] args){
        var graph = new Graph() {{
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
            setEdge(4, 1);
        }};

        System.out.println("Edges: ");
    
        for (var edge : graph.Edges()) {
            System.out.println("  - " + edge.toString());
        }

        
        System.out.println("Nodes: ");
        for (var node : graph.Nodes()) {
            System.out.println("  - " + node.toString());
        }

        graph.PrintNodes();
        System.err.println("");
        graph.PrintEdges();

        Raylib rlj = new Raylib();
        // Raylib rayMath = new Raymath();
        
        rlj.core.InitWindow(800, 600, "Raylib-J Example");
        int nodeWidth = 20;
        var centerPoint = new Vector2(Width/2, Height/2);
        int radius = Height/3;

        var background = new Color(18, 18, 18, 255);

        float angle = 0;
        float deltaAngle = 2*Raymath.PI/graph.nodeCount();
        var startPoint = new Vector2(radius, 0);
        var nodePositions = new HashMap<Integer, Vector2>();

        for (var node : graph.Nodes()) {
            var point = Raymath.Vector2Rotate(startPoint, angle);
            nodePositions.put(node.Id, new Vector2(point.x + centerPoint.x, point.y + centerPoint.y));

            angle += deltaAngle;
        }

        boolean canStart = false;
        var start = new Vector2(100, 100);
        var end = new Vector2(300, 300);
        float currProgress = 0;
        float animationTime = 2;

        rlj.core.SetTargetFPS(60);

        while (!rlj.core.WindowShouldClose()){
            rlj.core.BeginDrawing();
            rlj.core.ClearBackground(background);
            
            float dt = rlj.core.GetFrameTime();
            
            if (rlj.core.IsKeyDown(Keyboard.KEY_E)) {
                canStart = true;
                currProgress = 0;
            }

            if (canStart) {
                float progress = easeOutBounce(currProgress/animationTime);
                if (progress < 1) {
                    currProgress += dt;
                }
                var lerpEnd = Raymath.Vector2Lerp(start, end, (float)Math.sqrt(progress));
                rlj.shapes.DrawLineV(start, lerpEnd, Color.RAYWHITE);
            }


            // for (var edge : graph.Edges()) {
            //     rlj.shapes.DrawLineV(nodePositions.get(edge.From.Id), nodePositions.get(edge.To.Id), Color.WHITE);
            // }

            // for (var node : graph.Nodes()) {
            //     var color = DrawingUtils.GetNodeColor(node);
            //     rlj.shapes.DrawCircleV(nodePositions.get(node.Id), nodeWidth, color);
            // }

            rlj.core.EndDrawing();
        }
    }
}
