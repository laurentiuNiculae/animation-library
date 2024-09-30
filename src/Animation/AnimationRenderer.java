package Animation;

import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.textures.RenderTexture;
import com.raylib.java.textures.Texture2D;

import Encoding.FFmpegEncoder;
import Encoding.VideoEncoder;

public class AnimationRenderer {
    AnimationContext ctx;
    RenderTexture renderTarget;
    Task task;
    VideoEncoder videoEncoder;

    public AnimationRenderer(AnimationContext ctx, Task task) {
        this.task = task;
        this.ctx = ctx;
        this.videoEncoder = new FFmpegEncoder();
        

        task.SetAnimationCtx(ctx);
    }

    public RenderTexture RenderFrameToTexture(float dt) {
        if (renderTarget == null) {
            this.renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());
        }

        ctx.core.BeginTextureMode(renderTarget);
            ctx.core.ClearBackground(Color.RED);
            task.Draw(dt);
        ctx.core.EndTextureMode();

        return renderTarget;
    }

    public RenderTexture RenderFrameToTexture() {
        if (renderTarget == null) {
            this.renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());
        }

        ctx.core.BeginTextureMode(renderTarget);
            ctx.core.ClearBackground(Color.RED);
            task.Draw(0);
        ctx.core.EndTextureMode();

        return renderTarget;
    }

    public void DisplayAnimation() {
        ctx.core.InitWindow(ctx.getWidth(), ctx.getHeight(), "Raylib-J Example");
        ctx.core.SetTargetFPS(ctx.getFPS());

        boolean canStart = false;

        while (!ctx.core.WindowShouldClose()) {
            ctx.core.BeginDrawing();
            
            if (ctx.core.IsKeyPressed(Keyboard.KEY_E)) {
                canStart = true;
                this.Reset();
            }

            if (canStart) {
                try {
                    task.Draw((float)1/ctx.getFPS());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ctx.core.EndDrawing();
        }
    }

    // "/home/laur/dev/personal/java-learn/bunica/src/output.mp4"
    public void RenderAnimationToFile(String path) {
        ctx.core.InitWindow(ctx.getWidth(), ctx.getHeight(), "Raylib-J Example");
        renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());

        videoEncoder.startRendering(path, ctx.getWidth(), ctx.getHeight(), ctx.getFPS());

        float dt = (float)1/ctx.getFPS();
        int i = 0;

        while (!task.Finished()) {
            ctx.core.BeginTextureMode(renderTarget);
                task.Draw(dt);
            ctx.core.EndTextureMode();

            var img = ctx.textures.LoadImageFromTexture(renderTarget.texture);
            ctx.textures.ImageFlipVertical(img);

            videoEncoder.writeFrame(img.getData());

            System.out.println(i);
            i++;
        }

        videoEncoder.finalizeRendering();
        task.Reset();
    }

    private void renderTextureToScreen(Texture2D texture) {
        var rec = new Rectangle(0, 0, renderTarget.texture.width, -renderTarget.texture.height);
        ctx.textures.DrawTextureRec(renderTarget.texture, rec, new Vector2(0, 0), Color.RAYWHITE);
    }

    public void Reset() {
        task.Reset();
    }
}
