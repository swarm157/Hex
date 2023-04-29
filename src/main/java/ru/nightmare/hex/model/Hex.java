package ru.nightmare.hex.model;

public class Hex {
    int ownerId;
    Biomes biome;
    Type type;
    int health;
    int level;
    boolean hidden;

    public Hex(int ownerId, Biomes biome, Type type, int health, int level, boolean hidden) {
        this.ownerId = ownerId;
        this.biome = biome;
        this.type = type;
        this.health = health;
        this.level = level;
        this.hidden = hidden;
    }

    public Hex(Biomes biome, Type type) {
        this.biome = biome;
        this.type = type;
        ownerId = -1;
        hidden = false;
        health = 0;
        level = 0;
    }

    public Hex() {
        ownerId = -1;
        biome = Biomes.empty;
        hidden = false;
        health = 0;
        level = 0;
        this.type = Type.empty;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Biomes getBiome() {
        return biome;
    }

    public void setBiome(Biomes biome) {
        this.biome = biome;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
