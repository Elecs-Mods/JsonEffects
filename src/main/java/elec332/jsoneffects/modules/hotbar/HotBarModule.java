package elec332.jsoneffects.modules.hotbar;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.core.module.annotations.ElecModule;
import elec332.core.module.event.SetupModuleEvent;
import elec332.core.util.EventHelper;
import elec332.jsoneffects.JsonEffects;
import elec332.jsoneffects.handler.ItemEffect;
import elec332.jsoneffects.handler.JsonEffect;
import elec332.jsoneffects.handler.JsonEffectHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

/**
 * Created by Elec332 on 26-9-2015.
 */
@ElecModule(name = "HotBarModule", enabled = false)
public class HotBarModule {

    private static List<JsonEffect> handledEffects;

    @ElecModule.EventHandler
    public void setupModule(SetupModuleEvent event){
        handledEffects = JsonEffectHandler.getEffects(ItemEffect.InventoryLocation.HOTBAR);
    }

    @ElecModule.EventHandler
    public void init(FMLInitializationEvent event){
        EventHelper.registerHandlerFML(new HotBarHandler());
    }

    public static class HotBarHandler {

        private int i = 0;

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event){
            if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote){
                i--;
                if (i <= 0){
                    i = JsonEffects.timeCheck;
                    for (JsonEffect effect : handledEffects){
                        int j = effect.checkAmount(0, 9, event.player.inventory.mainInventory);
                        if (j > 0){
                            effect.apply((EntityPlayerMP) event.player, j);
                        }
                    }
                }
            }
        }

    }

}
