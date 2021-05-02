package com.stereowalker.combat.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class NoCooldownEnchantment extends Enchantment {
	
	public NoCooldownEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
		super(rarityIn, CEnchantmentType.WAND, slots);
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	public int getMinEnchantability(int enchantmentLevel) {
		return 5 + enchantmentLevel * 7;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this enchantment.
	 */
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != CEnchantments.COOLDOWN_REDUCTION;
	}
}