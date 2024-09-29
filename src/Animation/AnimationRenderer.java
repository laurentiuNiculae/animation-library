package Animation;

import java.io.IOException;
import com.raylib.java.core.Color;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.textures.RenderTexture;

public class AnimationRenderer implements Task {
    AnimationContext ctx;
    RenderTexture renderTarget;
    Task task;

    public AnimationRenderer(Task task, int width, int height, int fps) {
        this.task = task;
        this.ctx = new AnimationContext(width, height, fps);
    }

    public AnimationRenderer(AnimationContext ctx, Task task) {
        this.task = task;
        this.ctx = ctx;

        task.SetAnimationCtx(ctx);
    }

    public RenderTexture RenderFrameToTexture(float dt) {
        if (renderTarget == null) {
            this.renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());
        }

        ctx.core.BeginTextureMode(renderTarget);
            ctx.core.ClearBackground(new Color(18, 18, 18, 255));
            task.Draw(dt);
        ctx.core.EndTextureMode();

        return renderTarget;
    }

    Process process;
    String createProcessError;
    private void createRenderProcess() throws IOException  {
        // String resolution = "800x600";
        String resolution = "800x600";
        String framerate = "10";

        var processB = new ProcessBuilder("ffmpeg", 
            "-loglevel", "verbose",
            "-y",

            "-f", "rawvideo",
            "-pix_fmt", "rgba",
            "-s", resolution,
            "-r", framerate,
            "-i", "-",

            "-c:v", "libx264",
            "-vb", "2500k",
            "-c:a", "aac",
            "-ab", "200k",
            "-pix_fmt", "yuv420p",
            "/home/laur/dev/personal/java-learn/bunica/src/output.mp4"
        );

        this.process = processB.start();
    }

    public void RenderFrameToFile(float dt) throws IOException, InterruptedException {
        if (process == null) {
            createRenderProcess();
        }

        if (renderTarget == null) {
            this.renderTarget = ctx.textures.LoadRenderTexture(ctx.getWidth(), ctx.getHeight());
        }

        ctx.core.BeginTextureMode(renderTarget);
            ctx.core.ClearBackground(new Color(18, 18, 18, 255));
            task.Draw(dt);
        ctx.core.EndTextureMode();

        var rec = new Rectangle(0, 0, renderTarget.texture.width, -renderTarget.texture.height);
        ctx.textures.DrawTextureRec(renderTarget.texture, rec, new Vector2(0, 0), Color.RAYWHITE);


        var img = ctx.textures.LoadImageFromTexture(renderTarget.texture);
        ctx.textures.ImageFlipVertical(img);
        var imgBytes = img.getData();
        System.out.printf("imgBytes.length: %d\n", imgBytes.length);

        process.getOutputStream().write(imgBytes);
        process.getOutputStream().write(imgBytes);
        process.getOutputStream().write(imgBytes);
        process.getOutputStream().write(imgBytes);
        process.getOutputStream().write(imgBytes);
        process.getOutputStream().flush();
    }
    
    public void EndRenderFrameToFile() throws IOException, InterruptedException {
        process.outputWriter().close();
        process.waitFor();
        System.out.println("Closed FFMPEG stdin");
    }

    @Override
    public void Draw(float dt) {
        task.Draw(dt);
    }

    @Override
    public boolean Finished() {
        return task.Finished();
    }

    @Override
    public void Reset() {
        task.Reset();
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SetAnimationCtx'");
    }
}
