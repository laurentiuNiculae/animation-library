package com.lniculae.Encoding;

import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public interface VideoEncoder {
    public Result<Empty> startRendering(String path, float width, float height, int fps);
    public Result<Empty> writeFrame(byte[] imgBytes);
    public Result<Empty> finalizeRendering();
}
