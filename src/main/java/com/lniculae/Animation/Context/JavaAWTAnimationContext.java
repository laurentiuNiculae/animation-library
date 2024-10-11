package com.lniculae.Animation.Context;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.lniculae.Animation.Color;
import com.lniculae.Animation.Task;
import com.lniculae.Animation.Vec2;

public class JavaAWTAnimationContext implements AnimationContext {
    BufferedImage image;
    Graphics2D g2d;

    int width, height, fps;
    Color backgroundColor;

    public JavaAWTAnimationContext(int width, int height, int fps, Color backgroundColor) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = backgroundColor;
    }

    public JavaAWTAnimationContext(int width, int height, int fps) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        this.width = width;
        this.height = height;
        this.fps = fps;
        this.backgroundColor = new Color(0, 0, 0, 255);
    }

    public void drawLine(Vec2 startPos, Vec2 endPos, float thick, Color color) {
        g2d.setStroke(new BasicStroke(thick));
        g2d.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        g2d.drawLine((int) startPos.x, (int) startPos.y, (int) endPos.x, (int) endPos.y);
    }

    public void drawCircle(Vec2 center, float radius, Color color) {
        int diameter = Math.round(radius*2);

        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        g2d.fillOval((int) (center.x - radius), (int) (center.y - radius), diameter, diameter);
    }

    public void clearBackground(Color color) {
        setBackgroundColor(color);
    }

    public void drawTaskToImage(Task task, float dt) {
        final Color c = backgroundColor;
        g2d.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
        g2d.fillRect(0, 0, width, height);
        task.Draw(dt);
    }

    public byte[] getImageBytes() {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        byte[] rawBytes = new byte[pixels.length * 4];

        for (int i = 0; i < pixels.length; i++) {
            rawBytes[i * 4 + 0] = (byte) ((pixels[i] >> 16) & 0xFF);
            rawBytes[i * 4 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);
            rawBytes[i * 4 + 2] = (byte) ((pixels[i] >> 0) & 0xFF);
            rawBytes[i * 4 + 3] = (byte) ((pixels[i] >> 24) & 0xFF);
        }

        return rawBytes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFPS() {
        return fps;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public Color getBackgroundColor() {
       return backgroundColor;
    }

}
