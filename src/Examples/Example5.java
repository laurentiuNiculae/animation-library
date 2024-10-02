package Examples;

import Animation.AnimationContext;
import Animation.AnimationRenderer;
import AnimationParser.AnimationScriptParser;

public class Example5 {
    public void RunExample() {
        var x = new AnimationScriptParser("./animationScript.txt");

        var ctx = new AnimationContext(600, 600, 60);
        var animationResult = x.ParseFile();
        if (!animationResult.Ok()) {
            System.out.println(animationResult.Err().getMessage());
            return;
        }

        var animationRenderer = new AnimationRenderer(ctx, animationResult.Some());

        // animationRenderer.DisplayAnimation();
        animationRenderer.RenderAnimationToFile("./nice-drawing.mp4");
    }
}
