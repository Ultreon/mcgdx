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

package dev.ultreon.mcgdx.neoforge;

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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForgeModLoader implements ModLoader {
    private final DeferredRegister<Block> blockRegistry = DeferredRegister.create(Registries.BLOCK, GdxMinecraft.MOD_ID);
    private final DeferredRegister<BlockEntityType<?>> blockEntityTypeRegistry = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GdxMinecraft.MOD_ID);
    private final DeferredRegister<Item> itemRegistry = DeferredRegister.create(Registries.ITEM, GdxMinecraft.MOD_ID);
    private final DeferredRegister<CreativeModeTab> creativeModeTabRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GdxMinecraft.MOD_ID);
    private final DeferredHolder<CreativeModeTab, CreativeModeTab> tab;

    public ForgeModLoader(ModContainer mod, IEventBus modEventBus) {
        tab = creativeModeTabRegistry.register("mcgdx", () -> CreativeModeTab.builder().title(Component.literal("mcGDX")).icon(() -> new ItemStack(Items.ITEM_FRAME)).build());

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

    @Override
    public void load() {

    }

    public void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab1 = event.getTab();
        if (tab.get().equals(tab1)) {
            for (var e : itemRegistry.getEntries()) {
                event.accept(e.get());
            }
        }
    }
}
