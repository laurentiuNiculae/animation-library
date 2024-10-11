package com.lniculae.Animation.Context;

import com.lniculae.Animation.Color;
import com.lniculae.Animation.Vec2;

public interface AnimationContext {
    public void drawLine(Vec2 startPos, Vec2 endPos, float thick, Color color);
    public void drawCircle(Vec2 center, float radius, Color color);
    public void clearBackground(Color color);
    public void setBackgroundColor(Color color);
    public Color getBackgroundColor();
    public int getWidth();
    public int getHeight();
    public int getFPS();
}
