package com.reversecraft.reversecraftingtable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverseCraftingTableMod implements ModInitializer {
    public static final String MOD_ID = "reverse-crafting-table";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block REVERSE_CRAFTING_TABLE = new ReverseCraftingTableBlock(
            AbstractBlock.Settings.create().strength(2.5f)
    );

    public static final BlockItem REVERSE_CRAFTING_TABLE_ITEM = new BlockItem(
            REVERSE_CRAFTING_TABLE, new Item.Settings()
    );

    public static BlockEntityType<ReverseCraftingTableBlockEntity> REVERSE_CRAFTING_TABLE_BLOCK_ENTITY;

    public static ScreenHandlerType<ReverseCraftingTableScreenHandler> REVERSE_CRAFTING_TABLE_SCREEN_HANDLER;

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "reverse_crafting_table"), REVERSE_CRAFTING_TABLE);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "reverse_crafting_table"), REVERSE_CRAFTING_TABLE_ITEM);

        REVERSE_CRAFTING_TABLE_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(MOD_ID, "reverse_crafting_table"),
                BlockEntityType.Builder.create(ReverseCraftingTableBlockEntity::new, REVERSE_CRAFTING_TABLE).build()
        );

        REVERSE_CRAFTING_TABLE_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of(MOD_ID, "reverse_crafting_table"),
                new ScreenHandlerType<>(ReverseCraftingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(REVERSE_CRAFTING_TABLE_ITEM);
        });

        LOGGER.info("Reverse Crafting Table mod initialized!");
    }
}
