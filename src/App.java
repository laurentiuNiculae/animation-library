import com.raylib.java.Raylib;
import com.raylib.java.raymath.Vector2;

import Animation.AnimationRenderer;
import Animation.AnimationScriptParser;
import Animation.DrawLine;
import Animation.Secvential;

public class App {
    public static void main2(String[] args) {
        var scriptParse = new AnimationScriptParser("/home/laur/dev/personal/java-learn/bunica/src/script");

        scriptParse.PrintFileContent();
    }

    public static void main(String[] args) {
        Raylib rlj = new Raylib();

        var task = new Secvential(){{
            addTask(new DrawLine(rlj, new Vector2(100, 100), new Vector2(400, 400), 2));
        }};

        var x = new AnimationRenderer(task);

        try {
            x.RenderToFile("/home/laur/dev/personal/java-learn/bunica/src/wow");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
