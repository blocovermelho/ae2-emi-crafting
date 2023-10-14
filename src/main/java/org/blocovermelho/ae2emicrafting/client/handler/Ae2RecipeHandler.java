package org.blocovermelho.ae2emicrafting.client.handler;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.FillCraftingGridFromRecipePacket;
import appeng.menu.SlotSemantics;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.items.CraftingTermMenu;
import com.google.common.base.Preconditions;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.*;

public class Ae2RecipeHandler<T extends CraftingTermMenu> implements StandardRecipeHandler<T> {

    private final Comparator<GridInventoryEntry> BY_COUNT = Comparator.comparing(GridInventoryEntry::getStoredAmount);

    @Override
    public List<Slot> getInputSources(T handler) {
        IClientRepo clientRepo = handler.getClientRepo();

        if (clientRepo == null) {
            return new ArrayList<>();
        }

        List<Slot> sources = new ArrayList<>(clientRepo.getAllEntries()
                .stream()
                .filter((s) -> s.getWhat() != null)
                .filter((s) -> s.getStoredAmount() > 0 || !s.isCraftable())
                .map((s) -> {
                    ItemStack stack = s.getWhat().wrapForDisplayOrFilter();
                    long amount = s.getStoredAmount();
                    int clamped = (int) (amount > 64 ? 64 : amount);
                    stack.setCount(clamped);

                    return new Slot(new SimpleInventory(stack), 0, 0, 0);
                }).toList());

        sources.addAll(handler.getSlots(SlotSemantics.PLAYER_INVENTORY));
        sources.addAll(handler.getSlots(SlotSemantics.PLAYER_HOTBAR));
        sources.addAll(handler.getSlots(SlotSemantics.CRAFTING_GRID));

        return sources;
    }

    @Override
    public List<Slot> getCraftingSlots(T handler) {
        return handler.getSlots(SlotSemantics.CRAFTING_GRID);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        if(recipe instanceof EmiCraftingRecipe craftingRecipe) {
            Identifier recipeId = craftingRecipe.getId();
            DefaultedList<ItemStack> candidates = findTemplateCandidates(craftingRecipe, context.getScreenHandler());

            // Sends a packet to the server, informing that a crafting was requested. Since that's how AE2 handles crafting requests.
            NetworkHandler.instance().sendToServer(new FillCraftingGridFromRecipePacket(recipeId, candidates, false));
            return true;
        }

        return false;
    }

    @Override
    public Slot getOutputSlot(T handler) {

        List<Slot> slots = handler.getSlots(SlotSemantics.CRAFTING_RESULT);
        if(slots.isEmpty()) {
            return null;
        }

        return slots.get(0);
    }


    private DefaultedList<ItemStack> findTemplateCandidates(EmiRecipe recipe, T menu) {
        Map<AEKey, Integer> prioritizedInventory = prioritize(menu);
        DefaultedList<ItemStack> list = DefaultedList.ofSize(9, ItemStack.EMPTY);
        DefaultedList<EmiIngredient> normalizedMatrix = ensureSize(recipe);

        for (int i = 0; i < normalizedMatrix.size(); i++) {
            EmiIngredient ingredient = normalizedMatrix.get(i);
            if (ingredient.isEmpty()) continue;

            // Get the best viable stack for this crafting, handling tags and taking items from the ME Inventory first.
            ItemStack stack = prioritizedInventory.entrySet().stream()
                    .filter((e) ->
                            e.getKey() instanceof AEItemKey itemKey
                                    && EmiIngredient.areEqual(ingredient, EmiStack.of(itemKey.toStack()))
                    )
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(e -> ((AEItemKey) e.getKey()).toStack())
                    .orElse(ingredient.getEmiStacks().get(0).getItemStack());

            list.set(i, stack);
        }

        return list;
    }

    private Map<AEKey, Integer> prioritize(T menu){
        var result = new HashMap<AEKey, Integer>();

        if (menu.getClientRepo() == null) {
            return result;
        }

        List<AEKey> aeKeys = menu.getClientRepo().getAllEntries().stream()
                .filter((s) -> s.getWhat() != null)
                .sorted(BY_COUNT)
                .map(GridInventoryEntry::getWhat)
                .toList();

        for (int i = 0; i < aeKeys.size(); i++) {
            result.put(aeKeys.get(i), i);
        }

        for (ItemStack stack : menu.getPlayerInventory().main) {
            AEItemKey itemKey = AEItemKey.of(stack);

            if (itemKey != null) {
                result.putIfAbsent(itemKey, -1);
            }
        }

        return result;
    }

    private DefaultedList<EmiIngredient> ensureSize(EmiRecipe recipe) {
        DefaultedList<EmiIngredient> list = DefaultedList.ofSize(9, EmiIngredient.of(Ingredient.EMPTY));
        List<EmiIngredient> ingredients = recipe.getInputs();

        Preconditions.checkArgument(ingredients.size() <= 9);

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();

            Preconditions.checkArgument(width <= 3 && height <= 3);

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    int source = w + h * width;
                    var target = w + h * 3;

                    EmiIngredient ingredient = ingredients.get(source);
                    list.set(target, ingredient);
                }
            }
        } else {
            for (int i = 0; i < ingredients.size(); i++) {
                list.set(i, ingredients.get(i));
            }
        }

        return list;
    }
}
