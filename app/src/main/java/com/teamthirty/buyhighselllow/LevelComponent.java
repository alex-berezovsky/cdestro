package com.teamthirty.buyhighselllow;

public interface LevelComponent extends IComponent {
    /**
     * Increases the level of the tower
     */
    public void levelUp();

    /**
     * Increases the fire rate of the tower
     */
    public void fireRateIncrease();

    /**
     * Increases the damage output of the tower
     */
    public void damageIncrease();

    /**
     * Increases the upgrade cost of the tower
     */
    public void upgradeCostIncrease();
}