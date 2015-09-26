package elec332.jsoneffects.modules.hotbar;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.core.module.annotations.ElecModule;
import elec332.core.module.event.SetupModuleEvent;
import elec332.core.util.EventHelper;
import elec332.jsoneffects.handler.EffectHandler;
import elec332.jsoneffects.handler.ItemEffect;
import elec332.jsoneffects.handler.WrappedEffect;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 26-9-2015.
 */
@ElecModule(name = "HotBarModule", enabled = false)
public class HotBarModule {

    private static List<WrappedEffect> handledEffects;

    @ElecModule.EventHandler
    public void setupModule(SetupModuleEvent event){
        handledEffects = EffectHandler.getEffects(ItemEffect.InventoryLocation.HOTBAR);
    }

    @ElecModule.EventHandler
    public void init(FMLInitializationEvent event){
        EventHelper.registerHandlerFML(new HotBarHandler());
    }

    public static class HotBarHandler {

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event){
            if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote){
                if (event.player.worldObj.getTotalWorldTime() % 20 == 0){
                    for (WrappedEffect effect : handledEffects){
                        int i = effect.checkAmount(0, 9, event.player.inventory.mainInventory);
                        if (i > 0){
                            effect.apply((EntityPlayerMP) event.player, i);
                        }
                    }
                }
            }
        }

    }
}
