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

package dev.ultreon.mcgdx.impl.fabric;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.api.GameEnvironment;
import dev.ultreon.mcgdx.api.ModLoader;
import dev.ultreon.mcgdx.impl.Gdx3DRenderable;
import dev.ultreon.mcgdx.impl.GdxBlockEntity;
import dev.ultreon.mcgdx.impl.GdxBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.*;

public class FabricModLoader implements ModLoader {
    private final Map<ResourceLocation, BlockEntity> registeredBlockEntities = new HashMap<>();
    private final List<Item> items = new ArrayList<>();;
    private final ResourceKey<CreativeModeTab> tab = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("mcgdx", "mcgdx"));

    public FabricModLoader() {
        CreativeModeTab build = FabricItemGroup.builder().title(Component.literal("mcGDX")).icon(() -> new ItemStack(Items.ITEM_FRAME)).build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tab, build);

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            GdxMinecraft.app.create();
        });
    }

    @Override
    public FileHandle getConfigDir() {
        return new FileHandle(FabricLoader.getInstance().getConfigDir().toFile());
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public GameEnvironment getEnvironmentType() {
        EnvType envType = FabricLoader.getInstance().getEnvironmentType();
        if (envType == null) return GameEnvironment.UNKNOWN;
        return switch (envType) {
            case CLIENT -> GameEnvironment.CLIENT;
            case SERVER -> GameEnvironment.SERVER;
        };
    }

    @Override
    public void register(ResourceLocation resourceLocation, Gdx3DRenderable source) {
        var ref = new BlockEntityTemp();
        ref.blockEntity = new BlockEntityType<>((blockPos, blockState) -> new GdxBlockEntity(ref.blockEntity, blockPos, blockState), Set.of(ref.block), null);

        Registry.register(BuiltInRegistries.BLOCK, resourceLocation, ref.block);
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, ref.item);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resourceLocation, ref.blockEntity);

        this.items.add(ref.item);

        if (getEnvironmentType() == GameEnvironment.CLIENT) {
            BlockEntityRendererRegistryImpl.register(ref.blockEntity, context -> new GdxBlockEntityRenderer(context, source));
        }

        ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
            entries.accept(ref.item);
        });
    }

    private static class BlockEntityTemp {
        BlockEntityType<GdxBlockEntity> blockEntity = null;
        Block block = new GdxEntityBlock(BlockBehaviour.Properties.of().noCollission(), () -> blockEntity);

        Item item = new BlockItem(block, new Item.Properties());
    }
}
