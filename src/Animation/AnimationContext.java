package Animation;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class AnimationContext extends Raylib {
    int width, height, fps;
    Color backgroundColor;

    public AnimationContext(int width, int height, int fps) {
        super();
        this.config.setSupportTracelog(false);
        this.config.setSupportTracelogDebug(false);

        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = Color.RED; 
    }
    public AnimationContext(int width, int height, int fps, Color backgroundColor) {
        super();
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = backgroundColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFPS() {
        return fps;
    }
}
