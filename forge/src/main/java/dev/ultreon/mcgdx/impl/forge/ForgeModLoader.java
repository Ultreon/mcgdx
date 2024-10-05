/*
 * Copyright (c) 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ultreon.mcgdx.impl.forge;

import com.badlogic.gdx.files.FileHandle;
import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.api.GameEnvironment;
import dev.ultreon.mcgdx.api.ModLoader;
import dev.ultreon.mcgdx.impl.Gdx3DRenderable;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeModLoader implements ModLoader {
    private final DeferredRegister<Block> blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, GdxMinecraft.MOD_ID);
    private final DeferredRegister<BlockEntityType<?>> blockEntityTypeRegistry = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GdxMinecraft.MOD_ID);
    private final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, GdxMinecraft.MOD_ID);
    private final DeferredRegister<CreativeModeTab> creativeModeTabRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GdxMinecraft.MOD_ID);
    private final RegistryObject<CreativeModeTab> tab;

    public ForgeModLoader() {
        tab = creativeModeTabRegistry.register("mcgdx", () -> CreativeModeTab.builder().title(Component.literal("mcGDX")).icon(() -> new ItemStack(Items.ITEM_FRAME)).build());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        blockRegistry.register(modEventBus);
        blockEntityTypeRegistry.register(modEventBus);
        itemRegistry.register(modEventBus);
        creativeModeTabRegistry.register(modEventBus);

        modEventBus.addListener(this::buildCreativeTabs);
    }

    @Override
    public FileHandle getConfigDir() {
        return new FileHandle(FMLPaths.CONFIGDIR.get().toFile());
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public GameEnvironment getEnvironmentType() {
        Dist dist = FMLEnvironment.dist;
        if (dist == null) return GameEnvironment.UNKNOWN;

        return switch (dist) {
            case CLIENT -> GameEnvironment.CLIENT;
            case DEDICATED_SERVER -> GameEnvironment.SERVER;
        };
    }

    @Override
    public void register(ResourceLocation resourceLocation, Gdx3DRenderable source) {

    }

    public void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab1 = event.getTab();
        if (tab.get().equals(tab1)) {
            for (var e : itemRegistry.getEntries()) {
                event.accept(e);
            }
        }
    }
}
