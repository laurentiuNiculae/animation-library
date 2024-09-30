package Encoding;

public class FFmpegEncoder implements VideoEncoder {
    Process process;
    String createProcessError;
    boolean renderProcessStarted;

    public FFmpegEncoder() {};

    public void startRendering(String path, float width, float height, int fps) throws Error  {
        if (renderProcessStarted) {
            throw new Error("ERROR: Tried to start render process when it already started. Call finalizeRendering first.");
        }

        String resolution = String.format("%dx%d", (int) width, (int) height);
        String framerate = String.format("%d", (int) fps);

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
            path
        );

        try {
            this.process = processB.start();
            renderProcessStarted = true;
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

    public void finalizeRendering() throws Error {
        if (!renderProcessStarted) {
            throw new Error("ERROR: tried to write when process isn't started: call startRendering first");
        }

        try {
            process.outputWriter().close();
            process.waitFor();
            renderProcessStarted = false;        
            System.out.println("Closed FFMPEG stdin");
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

    public void writeFrame(byte[] imgBytes) throws Error {
        if (!renderProcessStarted) {
            throw new Error("ERROR: tried to write when process isn't started: call startRendering first");
        }

        try {
            process.getOutputStream().write(imgBytes);
            process.getOutputStream().flush();
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }
}
