package com.lniculae.AnimationOutput;

import com.lniculae.Animation.Task;
import com.lniculae.Animation.Context.JavaAWTAnimationContext;
import com.lniculae.Encoding.FFmpegEncoder;
import com.lniculae.Encoding.VideoEncoder;
import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public class JavaAWTRenderer implements AnimationFileRenderer {
    JavaAWTAnimationContext ctx;
    Task task;
    VideoEncoder videoEncoder;

    public JavaAWTRenderer(Task task, int width, int height, int fps) {
        this.ctx = new JavaAWTAnimationContext(width, height, fps);
        task.SetAnimationCtx(this.ctx);
        this.task = task;
        this.videoEncoder = new FFmpegEncoder();
    }

    @Override
    public Result<Empty> renderToFile(String path) {
        var result = videoEncoder.startRendering(path, ctx.getWidth(), ctx.getHeight(), ctx.getFPS());
        if (!result.Ok()) {
            return result;
        }

        float dt = (float)1/ctx.getFPS();
        int i = 0;

        while (!task.Finished()) {
            ctx.drawTaskToImage(task, dt);

            result = videoEncoder.writeFrame(ctx.getImageBytes());
            if (!result.Ok()) {
                return result;
            }

            System.out.println(i);
            i++;
        }

        result = videoEncoder.finalizeRendering();
        if (!result.Ok()) {
            return result;
        }

        task.Reset();

        return Result.None;
    }
    
}
