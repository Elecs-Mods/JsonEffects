package elec332.jsoneffects.modules.armour;

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
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Created by Elec332 on 25-9-2015.
 */
@ElecModule(name = "Armour", enabled = false)
public class ArmourModule {

    public static Logger logger;
    public static Configuration config;
    private static List<WrappedEffect> handledEffects;

    @ElecModule.EventHandler
    public void setupModule(SetupModuleEvent event){
        logger = event.getModuleLog();
        config = event.getConfiguration();
        handledEffects = EffectHandler.getEffects(ItemEffect.InventoryLocation.ARMOUR);
    }

    @ElecModule.EventHandler
    public void init(FMLInitializationEvent event){
        EventHelper.registerHandlerFML(new ArmourHandler());
    }

    public static class ArmourHandler {

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event){
            if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote){
                if (event.player.worldObj.getTotalWorldTime() % 20 == 0){
                    for (WrappedEffect effect : handledEffects){
                        int i = effect.checkAmount(event.player.inventory.armorInventory);
                        if (i > 0){
                            effect.apply((EntityPlayerMP) event.player, i);
                        }
                    }
                }
            }
        }

    }

}
