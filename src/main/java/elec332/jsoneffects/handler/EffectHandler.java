package elec332.jsoneffects.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import elec332.core.json.JsonHandler;
import elec332.core.server.ServerHelper;
import elec332.jsoneffects.JsonEffects;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 25-9-2015.
 */
public class EffectHandler {

    private static File jsonFile;

    private static Map<ItemEffect.InventoryLocation, List<WrappedEffect>> registry = Maps.newHashMap();

    public static void loadFile(File file){
        jsonFile = file;
        reloadFile();
    }

    public static void reloadFile(){
        if (jsonFile == null)
            throw new IllegalStateException("Cannot reload file before file has been set!");
        if (!jsonFile.exists()) {
            JsonArray element = JsonHandler.newJsonArray();
            element.add(JsonHandler.toJsonElement(getExample(), ItemEffect.class));
            JsonHandler.toFile(new File(JsonEffects.configDir, "example.json"), JsonHandler.newJsonObject(element, "effects"));
        }
        List<ItemEffect> effectList;
        try {
            effectList = _reloadFile();
        } catch (IOException e){
            throw new RuntimeException("Error loading Json file!", e);
        }
        for (ItemEffect effect : effectList){
            for (ItemEffect.InventoryLocation location : effect.validLocations){
                registry.get(location).add(new WrappedEffect(effect));
            }
        }
    }

    public static List<WrappedEffect> getEffects(ItemEffect.InventoryLocation inventoryLocation){
        return registry.get(inventoryLocation);
    }

    private static List<ItemEffect> _reloadFile() throws IOException{
        try {
            return JsonHandler.getDataAsList(JsonHandler.getMainFileObject(jsonFile), "effects", ItemEffect.class);
        } catch (FileNotFoundException e){
            ServerHelper.createFile(jsonFile);
            return Lists.newArrayList();
        }
    }

    private static ItemEffect getExample(){
        ItemEffect ret = new ItemEffect();
        ret.doesEffectStack = true;
        ItemEffect.Effect effect = new ItemEffect.Effect();
        effect.effectID = 2;
        effect.strength = 1;
        ret.effects = new ItemEffect.Effect[]{effect};
        ret.validLocations = new ItemEffect.InventoryLocation[]{ItemEffect.InventoryLocation.ARMOUR};
        ret.items = new ItemStack[]{new ItemStack(Items.iron_helmet), new ItemStack(Items.golden_helmet)};
        return ret;
    }

    static {
        for (ItemEffect.InventoryLocation location : ItemEffect.InventoryLocation.values()){
            registry.put(location, Lists.<WrappedEffect>newArrayList());
        }
    }
}
