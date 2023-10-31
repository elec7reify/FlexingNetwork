package com.flexingstudios.flexingnetwork.api.entity;

import net.minecraft.server.v1_12_R1.EntityLiving;

public interface IEntity {
    void setMovementSpeed(EntityLiving entity, double speed);
}
