package com.stereowalker.combat.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class PenetrationEnchantment extends Enchantment {
	
	public PenetrationEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
		super(rarityIn, CEnchantmentType.THROWN, slots);
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
		return 5;
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this enchantment.
	 */
	public boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench);
	}
}