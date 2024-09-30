package Encoding;

public interface VideoEncoder {
    public void startRendering(String path, float width, float height, int fps);
    public void writeFrame(byte[] imgBytes);
    public void finalizeRendering();
}
