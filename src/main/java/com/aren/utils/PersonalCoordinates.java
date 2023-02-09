package com.aren.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PersonalCoordinates {

    public static Vector rotateFunction(Vector vector, Location location) {
        double yawR = location.getYaw() / 180*Math.PI;
        double pitchR = location.getPitch() / 180*Math.PI;

        vector = rotateAboutX(vector, pitchR);
        vector = rotateAboutY(vector, -yawR);

        return vector;
    }

    public static Vector rotateAboutX (Vector vec, double a) {
        double y = Math.cos(a) * vec.getY() - Math.sin(a) * vec.getZ();
        double z = Math.sin(a) * vec.getY() + Math.cos(a) * vec.getZ();

        return vec.setY(y).setZ(z);
    }

    public static Vector rotateAboutY (Vector vec, double b) {
        double x = Math.cos(b) * vec.getX() + Math.sin(b) * vec.getZ();
        double z = -Math.sin(b) * vec.getX() + Math.cos(b) * vec.getZ();

        return vec.setX(x).setZ(z);
    }

    public static Vector rotateAboutZ (Vector vec, double c) {
        double x = Math.cos(c) * vec.getX() - Math.sin(c) * vec.getY();
        double y = Math.sin(c) * vec.getX() + Math.cos(c) * vec.getY();

        return vec.setX(x).setY(y);
    }

}
