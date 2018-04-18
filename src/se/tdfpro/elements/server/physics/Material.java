package se.tdfpro.elements.server.physics;

import org.newdawn.slick.Color;

public interface Material {
    float getRestitution();
    float getFriction();
    
    Color getColor();
}
