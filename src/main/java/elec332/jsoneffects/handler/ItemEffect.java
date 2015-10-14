package elec332.jsoneffects.handler;

import net.minecraft.item.ItemStack;

import java.io.Serializable;

/**
 * Created by Elec332 on 25-9-2015.
 */
public class ItemEffect implements Serializable{

    public ItemStack[] items = new ItemStack[0];
    public Effect[] effects = new Effect[0];
    public boolean doesEffectStack = false;
    public InventoryLocation[] validLocations = new InventoryLocation[0];

    public static class Effect {

        public String displayName = "";
        public String effect = "";
        public int strength = 0;

    }

    public enum InventoryLocation{
        ARMOUR, HOTBAR, INVENTORY
    }

}
