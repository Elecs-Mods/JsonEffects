package elec332.jsoneffects.handler;

import com.google.common.collect.Lists;
import elec332.core.player.InventoryHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by Elec332 on 25-9-2015.
 */
public class WrappedEffect {

    public WrappedEffect(ItemEffect effect){
        if (effect == null || effect.items.length == 0 || effect.validLocations.length == 0 || effect.effects.length == 0)
            throw new IllegalArgumentException();
        validStacks = Lists.newArrayList(effect.items);
        effects = Lists.newArrayList(effect.effects);
        doesStack = effect.doesEffectStack;
    }

    private List<ItemStack> validStacks;
    private List<ItemEffect.Effect> effects;
    private boolean doesStack;

    public void apply(EntityPlayerMP player, int amount){
        if (amount < 1)
            return;
        if (!doesStack)
            amount = 1;
        for (ItemEffect.Effect effect : effects){
            int strength = effect.strength * amount;
            player.addPotionEffect(new PotionEffect(effect.effectID, 39, strength));
        }
    }


    public int checkAmount(ItemStack... stacks){
        int ret = 0;
        for (ItemStack inSlot : stacks) {
            //ItemStack inSlot = inventory.getStackInSlot(i);
            for (ItemStack stack : validStacks){
                if (InventoryHelper.areEqualNoSize(inSlot, stack) && inSlot.stackSize >= stack.stackSize){
                    float diff = (float)inSlot.stackSize/stack.stackSize;
                    ret += MathHelper.floor_float(diff);
                }
            }
        }
        return ret;
    }
}
