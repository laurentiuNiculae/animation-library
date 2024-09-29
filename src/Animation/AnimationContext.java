package Animation;

import com.raylib.java.Raylib;

public class AnimationContext extends Raylib {
    int width, height, fps;
    

    public AnimationContext(int width, int height, int fps) {
        super();
        this.width = width;
        this.height = height;
        this.fps = fps;
    }

    public int getwidth() {
        return width;
    }

    public int getheight() {
        return height;
    }

    public int getFPS() {
        return fps;
    }
}
