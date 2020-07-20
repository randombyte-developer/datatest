package de.randombyte.datatest;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

@ConfigSerializable public class Config {
    public @Setting ItemStackSnapshot itemStackSnapshot;

    public Config() {

    }

    public Config(ItemStackSnapshot itemStackSnapshot) {
        this.itemStackSnapshot = itemStackSnapshot;
    }
}
