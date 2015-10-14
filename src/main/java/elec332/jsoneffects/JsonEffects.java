package elec332.jsoneffects;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import elec332.core.effects.api.ElecCoreAbilitiesAPI;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModInfo;
import elec332.core.module.ModuleHandler;
import elec332.jsoneffects.handler.JsonEffectHandler;
import elec332.jsoneffects.modules.armour.ArmourModule;
import elec332.jsoneffects.modules.hotbar.HotBarModule;
import elec332.jsoneffects.modules.inventory.InventoryModule;
import elec332.jsoneffects.proxies.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = JsonEffects.ModID, name = JsonEffects.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
@SuppressWarnings("all")
public class JsonEffects {

    public static final String ModName = "JsonEffects";
    public static final String ModID = "JsonEffects";

    @SidedProxy(clientSide = "elec332.jsoneffects.proxies.ClientProxy", serverSide = "elec332.jsoneffects.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static JsonEffects instance;
    public static File configDir;
    public static Configuration config;
    public static ModuleHandler moduleHandler;
    public static Logger logger;
    public static int timeCheck;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configDir = FileHelper.getCustomConfigFolderElec(event, "JsonEffects");
        config = new Configuration(new File(configDir, "main.cfg"));
        config.load();
        timeCheck = config.getInt("timeCheck", Configuration.CATEGORY_GENERAL, 40, 10, 100, "How many ticks between every check. (If players have items in their inventory)");
        moduleHandler = new ModuleHandler(config);
        moduleHandler.registerModule(new ArmourModule());
        moduleHandler.registerModule(new HotBarModule());
        moduleHandler.registerModule(new InventoryModule());
        ElecCoreAbilitiesAPI.getApi().requestActivation();
        //setting up mod stuff

        moduleHandler.preInit(event);
        if (config.hasChanged())
            config.save();
        MCModInfo.CreateMCModInfo(event, "Created by Elec332",
                "mod description",
                "website link", "logo",
                new String[]{"Elec332"});
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleHandler.init(event);
        JsonEffectHandler.loadFile(new File(configDir, "effects.json"));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        moduleHandler.postInit(event);
    }

}
