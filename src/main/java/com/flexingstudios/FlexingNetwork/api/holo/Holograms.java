package com.flexingstudios.FlexingNetwork.api.holo;

import com.flexingstudios.FlexingNetwork.api.geom.Vec3f;

import java.util.List;

public interface Holograms {
    TextHologram createText(Vec3f paramVec3f, String... paramVarArgs);
    Hologram get(int paramInt);
    void remove(int paramInt);
    void remove(Hologram paramHologram);
    void reset();
    List<Hologram> getAll();
}
