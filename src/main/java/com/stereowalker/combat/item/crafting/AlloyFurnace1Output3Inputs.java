package com.stereowalker.combat.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class AlloyFurnace1Output3Inputs extends AbstractAlloyFurnaceRecipe{

	public AlloyFurnace1Output3Inputs(ResourceLocation recipeIdIn, Ingredient ingredient1In,
			Ingredient ingredient2In, Ingredient ingredient3In, ItemStack result1In,
			ItemStack result2In, float experienceIn, int cookTimeIn) {
		super(recipeIdIn, ingredient1In, ingredient2In, ingredient3In, result1In, result2In,
				experienceIn, cookTimeIn);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return CRecipeSerializer.ALLOY_SMELTING_1OU_3IN;
	}

	@Override
	public boolean has2Ingredients() {
		return false;
	}

	@Override
	public boolean has1Result() {
		return true;
	}

}
