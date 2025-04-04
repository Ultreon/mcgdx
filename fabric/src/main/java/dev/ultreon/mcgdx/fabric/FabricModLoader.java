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

package dev.ultreon.mcgdx.fabric;

import com.badlogic.gdx.files.FileHandle;
import dev.ultreon.mcgdx.GdxMinecraft;
import dev.ultreon.mcgdx.api.GameEnvironment;
import dev.ultreon.mcgdx.api.ModLoader;
import dev.ultreon.mcgdx.impl.Gdx3DRenderable;
import dev.ultreon.mcgdx.impl.GdxBlockEntity;
import dev.ultreon.mcgdx.impl.GdxBlockEntityRenderer;
import dev.ultreon.mods.xinexlib.platform.XinexPlatform;
import dev.ultreon.mods.xinexlib.registrar.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
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
    private final ResourceKey<CreativeModeTab> tab = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("mcgdx", "mcgdx"));
    private final List<Runnable> clientLoaders = new ArrayList<>();

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
        var ref = new BlockEntityTemp(resourceLocation);
        ref.blockEntity = (RegistrySupplier<BlockEntityType<GdxBlockEntity>, BlockEntityType<GdxBlockEntity>>)(RegistrySupplier) FabricRegistration.BLOCK_ENTITY.register(resourceLocation.getPath(), () -> new BlockEntityType<GdxBlockEntity>((blockPos, blockState) -> new GdxBlockEntity(ref.blockEntity.get(), blockPos, blockState), Set.of(ref.block.get()), null));

        this.clientLoaders.add(() -> {
            if (getEnvironmentType() == GameEnvironment.CLIENT) {
                XinexPlatform.client().entityRenderers().register(ref.blockEntity, context -> new GdxBlockEntityRenderer(source));
            }
        });

        ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
            entries.accept(ref.item.get());
        });
    }

    @Override
    public void load() {
        FabricRegistration.load();

        for (Runnable runnable : this.clientLoaders) {
            runnable.run();
        }
    }

    private static class BlockEntityTemp {
        RegistrySupplier<BlockEntityType<GdxBlockEntity>, BlockEntityType<GdxBlockEntity>> blockEntity = null;
        RegistrySupplier<GdxEntityBlock, Block> block;

        RegistrySupplier<BlockItem, Item> item;

        private BlockEntityTemp(ResourceLocation name) {
            block = FabricRegistration.BLOCK.register(name.getPath(), () -> new GdxEntityBlock(BlockBehaviour.Properties.of().noCollission(), () -> blockEntity.get()));
            item = FabricRegistration.ITEM.register(name.getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }
    }
}
