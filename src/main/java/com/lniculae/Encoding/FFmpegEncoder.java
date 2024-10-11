package com.lniculae.Encoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public class FFmpegEncoder implements VideoEncoder {
    Process process;
    String createProcessError;
    boolean renderProcessStarted;

    public FFmpegEncoder() {};

    public Result<Empty> startRendering(String path, float width, float height, int fps)  {
        if (renderProcessStarted) {
            return new Result<>(
                new Error("ERROR: Tried to start render process when it already started. Call finalizeRendering first.")
            );
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
            return new Result<>(new Error(e.getMessage()));
        }

        return Result.None;
    }

    public Result<Empty> finalizeRendering() {
        if (!renderProcessStarted) {
            return new Result<>(
                new Error("ERROR: tried to write when process isn't started: call startRendering first")
            );
        }

        try {
            process.outputWriter().close();

            // Handle the output and error streams
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            process.waitFor();
            renderProcessStarted = false;        
            System.out.println("Closed FFMPEG stdin");
        } catch (Exception e) {
            return new Result<>(new Error(e.getMessage()));
        }

        return Result.None;
    }

    public Result<Empty> writeFrame(byte[] imgBytes) throws Error {
        if (!renderProcessStarted) {
            return new Result<>(
                new Error("ERROR: tried to write when process isn't started: call startRendering first")
            );
        }

        try {
            process.getOutputStream().write(imgBytes);
            process.getOutputStream().flush();
        } catch (Exception e) {
            // Handle the output and error streams
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return new Result<>(new Error(e.getMessage()));
        }

        return Result.None;
    }
}
