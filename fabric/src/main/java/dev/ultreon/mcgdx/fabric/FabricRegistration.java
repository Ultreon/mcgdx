package dev.ultreon.mcgdx.fabric;

import dev.ultreon.mods.xinexlib.platform.XinexPlatform;
import dev.ultreon.mods.xinexlib.registrar.Registrar;
import dev.ultreon.mods.xinexlib.registrar.RegistrarManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static dev.ultreon.mcgdx.GdxMinecraft.MOD_ID;

public class FabricRegistration {
    public static final RegistrarManager REGISTRAR_MANAGER = XinexPlatform.getRegistrarManager(MOD_ID);
    public static final Registrar<Block> BLOCK = REGISTRAR_MANAGER.getRegistrar(Registries.BLOCK);
    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITY = REGISTRAR_MANAGER.getRegistrar(Registries.BLOCK_ENTITY_TYPE);
    public static final Registrar<EntityType<?>> ENTITY = REGISTRAR_MANAGER.getRegistrar(Registries.ENTITY_TYPE);
    public static final Registrar<Item> ITEM = REGISTRAR_MANAGER.getRegistrar(Registries.ITEM);

    public static void load() {
        BLOCK.load();
        BLOCK_ENTITY.load();
        ENTITY.load();
        ITEM.load();
    }
}
