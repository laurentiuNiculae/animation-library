package Animation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AnimationRenderer implements Task {
    Task task;
    int width;
    int height;
    int fps;

    public AnimationRenderer(Task task, int width, int height, int fps) {
        this.task = task;
        this.width = width;
        this.height = height;
        this.fps = fps;
    }

    Process process;
    String createProcessError;
    private void createRenderProcess() throws IOException  {
        String resolution = "800x600";
        String framerate = "60";

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

    public void RenderToFile(String filePath) throws IOException, InterruptedException {
        createRenderProcess();

        while (!task.Finished()) {

            task.Draw((float)1/fps);

        }

        char[] x = {0, 1, 2, 3, 4, 5};

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(x);
        writer.newLine();
        writer.flush();
        writer.close();

        // Read the output from the Go program's stdout
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
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
}
