package com.flexingstudios.FlexingNetwork.api.holo;

import com.flexingstudios.FlexingNetwork.api.geom.Vec3f;

public interface Hologram {
    void move(Vec3f paramVec3f);
    void update();
    void hide();
    void show();
    void remove();
    int getId();
}
