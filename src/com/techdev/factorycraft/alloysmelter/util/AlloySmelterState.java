package com.techdev.factorycraft.alloysmelter.util;

public enum  AlloySmelterState
{
    IDLING("IDLING"),
    CRAFTING("CRAFTING");

    private String name;

    AlloySmelterState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
