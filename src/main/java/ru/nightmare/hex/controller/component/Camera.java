package ru.nightmare.hex.controller.component;

public class Camera {
    public static float x = 5;
    public static float y = 5;
    public static float speedX = 0;
    public static float speedY = 0;
    public static float speed = 0;
    public static float zoom = 1;
    public static float zoomSpeed = 0;
    public static final float minRadius = 3f;
    public static final float radius = 5f;
    public static final float SelectedRadius = 8f;

    public static void setPosition(float x, float y) {
        Camera.x  = x;
        Camera.y  = y;
        speedX    = 0;
        speedY    = 0;
        speed     = 0;
        zoomSpeed = 0;
    }

    public static void stop() {
        speedX    = 0;
        speedY    = 0;
        speed     = 0;
        zoomSpeed = 0;
    }
    public static void resetZoom() {
        zoomSpeed = 0;
        zoom      = 1;
    }

    public static void step() {
        x         +=speedX;
        y         +=speedY;
        move(-speedX/50, -speedY/50);
        zoom      +=zoomSpeed;
        zoomSpeed /=2;
        if (Math.abs(speed)<0.1) {
            speed  = 0;
            speedX = 0;
            speedY = 0;
        }
        if (Math.abs(zoomSpeed)<0.1)
            zoomSpeed = 0;
    }
    public static void move(float x, float y) {
        Camera.speed = Math.abs(Camera.speedX+x)+Math.abs(Camera.speedY+y);
        Camera.speedX+=x;
        Camera.speedY+=y;
    }
    public static void zooming(float v) {
        if (v>0) {
            if (zoomSpeed>0) {
                zoomSpeed*=v;
            } else {
                if (zoomSpeed<-0.1) {
                    zoomSpeed /= v;
                } else {
                    zoomSpeed = 0.1f;
                }
            }
        } else {
            if (zoomSpeed<0) {
                zoomSpeed*=Math.abs(v);
            } else {
                if (zoomSpeed>0.1) {
                    zoomSpeed /= Math.abs(v);
                } else {
                    zoomSpeed = -0.1f;
                }
            }
        }
    }
    public static void resetPosition() {
        x      = 5;
        y      = 5;
        speedY = 0;
        speedX = 0;
        speed  = 0;
    }
    public static void reset() {
        resetPosition();
        resetZoom();
    }
}
