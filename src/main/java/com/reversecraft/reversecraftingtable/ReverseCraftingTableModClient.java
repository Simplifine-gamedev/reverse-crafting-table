package com.reversecraft.reversecraftingtable;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ReverseCraftingTableModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ReverseCraftingTableMod.REVERSE_CRAFTING_TABLE_SCREEN_HANDLER, ReverseCraftingTableScreen::new);
    }
}
