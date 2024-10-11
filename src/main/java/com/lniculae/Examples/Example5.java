package com.lniculae.Examples;

import com.lniculae.AnimationOutput.JavaAWTRenderer;
import com.lniculae.AnimationParser.AnimationScriptParser;

public class Example5 {
    public void RunExample() {
        var scriptParser = new AnimationScriptParser();

        var animationResult = scriptParser.ParseFile("./animation-script-demo.txt");
        if (!animationResult.Ok()) {
            System.out.println(animationResult.Err().getMessage());
            return;
        }

        var animationRenderer = new JavaAWTRenderer(animationResult.Some(), 600, 600, 60);

        animationRenderer.renderToFile("./nice-drawing.mp4");
    }
}
