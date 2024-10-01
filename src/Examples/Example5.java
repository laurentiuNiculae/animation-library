package Examples;

import Animation.AnimationContext;
import Animation.AnimationRenderer;
import AnimationParser.AnimationScriptParser;

public class Example5 {
    public void RunExample() {
        var x = new AnimationScriptParser("./test.txt");

        var ctx = new AnimationContext(800, 600, 60);
        var animation = x.ParseFile();
        animation.SetAnimationCtx(ctx);

        var animationRenderer = new AnimationRenderer(ctx, animation);

        animationRenderer.DisplayAnimation();
    }
}
