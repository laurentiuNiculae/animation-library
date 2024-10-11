package com.lniculae.Animation;

public class Vec2 {
    public float x;
    public float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vec2 Rotate(Vec2 v, float angle) {
      Vec2 result = new Vec2(0, 0);
      float cosres = (float)Math.cos((double)angle);
      float sinres = (float)Math.sin((double)angle);
      result.x = v.x * cosres - v.y * sinres;
      result.y = v.x * sinres + v.y * cosres;
      return result;
    }

   public static Vec2 Lerp(Vec2 v1, Vec2 v2, float amount) {
      Vec2 result = new Vec2(0,0);
      result.x = v1.x + amount * (v2.x - v1.x);
      result.y = v1.y + amount * (v2.y - v1.y);
      return result;
   }

  @Override
  public String toString() {
    return "Vec2 [x=" + x + ", y=" + y + "]";
  }

}
