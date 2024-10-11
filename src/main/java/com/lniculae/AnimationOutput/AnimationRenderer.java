package com.lniculae.AnimationOutput;

import com.lniculae.Helper.Result;
import com.lniculae.Helper.Result.Empty;

public interface AnimationRenderer {
    public Result<Empty> renderToScreen();
}
