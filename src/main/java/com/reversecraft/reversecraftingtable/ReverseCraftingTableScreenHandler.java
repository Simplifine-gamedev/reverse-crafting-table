package com.reversecraft.reversecraftingtable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ReverseCraftingTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final World world;
    private final Random random = new Random();

    public ReverseCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(10));
    }

    public ReverseCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ReverseCraftingTableMod.REVERSE_CRAFTING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.world = playerInventory.player.getWorld();
        checkSize(inventory, 10);

        this.addSlot(new InputSlot(inventory, 0, 26, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new OutputSlot(inventory, 1 + row * 3 + col, 98 + col * 18, 17 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private void processReverseCraft() {
        ItemStack inputStack = inventory.getStack(0);

        for (int i = 1; i < 10; i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }

        if (inputStack.isEmpty() || world.isClient) {
            return;
        }

        ServerWorld serverWorld = (ServerWorld) world;
        List<RecipeEntry<CraftingRecipe>> recipes = serverWorld.getRecipeManager()
                .listAllOfType(RecipeType.CRAFTING);

        for (RecipeEntry<CraftingRecipe> recipeEntry : recipes) {
            CraftingRecipe recipe = recipeEntry.value();
            ItemStack output = recipe.getResult(serverWorld.getRegistryManager());

            if (ItemStack.areItemsEqual(output, inputStack)) {
                List<ItemStack> ingredients = extractIngredients(recipe);

                List<ItemStack> materialsToReturn = applyRecyclingLoss(ingredients);

                int slotIndex = 1;
                for (ItemStack material : materialsToReturn) {
                    if (slotIndex < 10) {
                        inventory.setStack(slotIndex, material);
                        slotIndex++;
                    }
                }
                break;
            }
        }
    }

    private List<ItemStack> extractIngredients(CraftingRecipe recipe) {
        List<ItemStack> ingredients = new ArrayList<>();

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            for (Ingredient ingredient : shapedRecipe.getIngredients()) {
                if (!ingredient.isEmpty()) {
                    ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                    if (matchingStacks.length > 0) {
                        ingredients.add(matchingStacks[0].copy());
                    }
                }
            }
        } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            for (Ingredient ingredient : shapelessRecipe.getIngredients()) {
                if (!ingredient.isEmpty()) {
                    ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                    if (matchingStacks.length > 0) {
                        ingredients.add(matchingStacks[0].copy());
                    }
                }
            }
        }

        return ingredients;
    }

    private List<ItemStack> applyRecyclingLoss(List<ItemStack> ingredients) {
        if (ingredients.isEmpty()) {
            return ingredients;
        }

        int totalItems = ingredients.size();
        int itemsToRemove = (int) Math.ceil(totalItems * 0.25);

        if (itemsToRemove == 0 && totalItems > 0) {
            if (random.nextDouble() < 0.25) {
                itemsToRemove = 1;
            }
        }

        List<ItemStack> result = new ArrayList<>(ingredients);

        if (itemsToRemove > 0 && !result.isEmpty()) {
            Collections.shuffle(result, random);
            for (int i = 0; i < itemsToRemove && !result.isEmpty(); i++) {
                result.remove(result.size() - 1);
            }
        }

        return result;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (slotIndex < 10) {
                if (!this.insertItem(originalStack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex < 46) {
                if (!this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.getWorld().isClient) {
            ItemStack inputStack = inventory.getStack(0);
            if (!inputStack.isEmpty()) {
                player.dropItem(inputStack, false);
                inventory.setStack(0, ItemStack.EMPTY);
            }
            for (int i = 1; i < 10; i++) {
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }
    }

    private class InputSlot extends Slot {
        public InputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            processReverseCraft();
        }

        @Override
        public void setStack(ItemStack stack) {
            super.setStack(stack);
            processReverseCraft();
        }
    }

    private class OutputSlot extends Slot {
        public OutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            super.onTakeItem(player, stack);
            ItemStack inputStack = inventory.getStack(0);
            if (!inputStack.isEmpty()) {
                inputStack.decrement(1);
                if (inputStack.isEmpty()) {
                    inventory.setStack(0, ItemStack.EMPTY);
                }
            }
            for (int i = 1; i < 10; i++) {
                inventory.setStack(i, ItemStack.EMPTY);
            }
            processReverseCraft();
        }
    }
}
