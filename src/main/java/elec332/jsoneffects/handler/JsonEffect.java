package elec332.jsoneffects.handler;

import com.google.common.collect.Lists;
import elec332.core.effects.api.ElecCoreAbilitiesAPI;
import elec332.core.effects.api.ability.WrappedAbility;
import elec332.core.effects.api.util.AbilityHelper;
import elec332.core.player.InventoryHelper;
import elec332.jsoneffects.JsonEffects;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by Elec332 on 25-9-2015.
 */
public class JsonEffect {

    public JsonEffect(ItemEffect effect){
        if (effect == null || effect.items.length == 0 || effect.validLocations.length == 0 || effect.effects.length == 0)
            throw new IllegalArgumentException();
        validStacks = Lists.newArrayList(effect.items);
        effects = Lists.newArrayList(effect.effects);
        doesStack = effect.doesEffectStack;
        checkEffects(effects);
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
            AbilityHelper.addEffectToEntity(player, new WrappedAbility(ElecCoreAbilitiesAPI.getApi().getEffectFromName(effect.effect), JsonEffects.timeCheck + 6, strength));
        }
    }


    public int checkAmount(int minSlot, int maxSlot, ItemStack... stacks){
        int ret = 0;
        for (int i = minSlot; i < maxSlot; i++) {
            ItemStack inSlot = stacks[i];
            for (ItemStack stack : validStacks){
                if (InventoryHelper.areEqualNoSize(inSlot, stack) && inSlot.stackSize >= stack.stackSize){
                    float diff = (float)inSlot.stackSize/stack.stackSize;
                    ret += MathHelper.floor_float(diff);
                }
            }
        }
        return ret;
    }

    private static void checkEffects(List<ItemEffect.Effect> effects){
        for (ItemEffect.Effect effect : effects){
            if (ElecCoreAbilitiesAPI.getApi().getEffectFromName(effect.effect) == null)
                throw new IllegalArgumentException("ERROR: Ability with name "+effect.effect+" doesn't exist!");
        }
    }
}
