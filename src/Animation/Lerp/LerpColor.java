package Animation.Lerp;

import com.raylib.java.core.Color;
import com.raylib.java.raymath.Raymath;

import Animation.EasingFunction;

public class LerpColor {
    public Color start;
    public Color end;
    public Color lerpEnd;
    public EasingFunction easingFunc; 
    
    public LerpColor(Color start, Color end) {
        this.start = new Color(start.r, start.g, start.b, start.a);
        this.end = new Color(end.r, end.g, end.b, end.a);
        this.lerpEnd = new Color(end.r, end.g, end.b, end.a);
        this.easingFunc = x -> x;
    }
    
    public LerpColor(Color start, Color end, EasingFunction easingFunc) {
        this.start = start;
        this.end = end;
        this.lerpEnd = end;
        this.easingFunc = easingFunc;
    }

    public Color setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        }

        progress = this.easingFunc.apply(progress);
        
        lerpEnd.r = (int) Raymath.Lerp(start.r, end.r, progress); ;
        lerpEnd.g = (int) Raymath.Lerp(start.g, end.g, progress); ;
        lerpEnd.b = (int) Raymath.Lerp(start.b, end.b, progress); ;

        return lerpEnd;
    }
}
