package com.lniculae.AnimationOutput;

import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public interface AnimationFileRenderer {
    public Result<Empty> renderToFile(String path);
}
