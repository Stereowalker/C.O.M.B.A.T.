package com.stereowalker.rankup.events;

import java.util.List;

import com.google.common.collect.Lists;
import com.stereowalker.combat.config.Config;
import com.stereowalker.rankup.AccessoryModifiers;
import com.stereowalker.rankup.AccessoryStats;
import com.stereowalker.rankup.api.stat.Stat;
import com.stereowalker.rankup.stat.StatProfile;
import com.stereowalker.unionlib.UnionLib;
import com.stereowalker.unionlib.item.AccessoryItem;
import com.stereowalker.unionlib.item.AccessoryItem.AccessorySlotType;
import com.stereowalker.unionlib.util.ModHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class UnionEvents {
	static List<AccessorySlotType> useable_ids = Lists.newArrayList(AccessorySlotType.RING, AccessorySlotType.NECKLACE);

	@SubscribeEvent
	public static void addEffectsToAccessories(LivingUpdateEvent event) {
		if (!ModHelper.isCuriosLoaded() && Config.RPG_COMMON.enableLevelingSystem.get()) {
			if (event.getEntityLiving() instanceof PlayerEntity) {
				addEffectsToAccessory((PlayerEntity) event.getEntityLiving());
			}
		}
	}

	@SubscribeEvent
	public static void addEffectsToChestItems(PlayerContainerEvent.Open event) {
		if (Config.RPG_COMMON.enableLevelingSystem.get()) {
			if (event.getEntityLiving() instanceof PlayerEntity) {
				AccessoryStats.addEffectsToChestItems(event.getContainer());
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void modifierTooltip(ItemTooltipEvent event) {
		if (Config.RPG_COMMON.enableLevelingSystem.get()) {
			if (event.getEntityLiving() instanceof PlayerEntity) {
				AccessoryStats.modifierTooltip(event.getToolTip(), event.getItemStack());
			}
		}
	}

	@SubscribeEvent
	public static void addModifiers(LivingUpdateEvent event) {
		if (!ModHelper.isCuriosLoaded() && Config.RPG_COMMON.enableLevelingSystem.get()) {
			if (event.getEntityLiving() instanceof PlayerEntity) {
				addModifiers((PlayerEntity) event.getEntityLiving());
			}
		}
	}
	
	public static void addEffectsToAccessory(PlayerEntity player) {
		if (player != null) {
			if (!UnionLib.getAccessoryInventory(player).getFirstRing().isEmpty())
				AccessoryStats.addModifier(UnionLib.getAccessoryInventory(player).getFirstRing());
			if (!UnionLib.getAccessoryInventory(player).getSecondRing().isEmpty())
				AccessoryStats.addModifier(UnionLib.getAccessoryInventory(player).getSecondRing());
			if (player.getHeldItem(Hand.OFF_HAND).getItem() instanceof AccessoryItem && useable_ids.contains(((AccessoryItem)player.getHeldItem(Hand.OFF_HAND).getItem()).getAccessoryType())) {
				AccessoryStats.addModifier(player.getHeldItem(Hand.OFF_HAND));
			} else if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof AccessoryItem && useable_ids.contains(((AccessoryItem)player.getHeldItem(Hand.MAIN_HAND).getItem()).getAccessoryType())) {
				AccessoryStats.addModifier(player.getHeldItem(Hand.MAIN_HAND));
			} else {
				for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
					ItemStack itemstack = player.inventory.getStackInSlot(i);
					if (itemstack.getItem() instanceof AccessoryItem && useable_ids.contains(((AccessoryItem)itemstack.getItem()).getAccessoryType())) {
						AccessoryStats.addModifier(itemstack);
					}
				}
			}
		}
	}

	public static void addModifiers(PlayerEntity player) {
		if (player != null) {
			for (AccessoryModifiers modifier : AccessoryModifiers.values()) {
				if (modifier != AccessoryModifiers.NONE && modifier != null) {
					int level = 0;
					if (modifier.equals(AccessoryStats.getModifier(UnionLib.getAccessoryInventory(player).getFirstRing()))) level++;
					if (modifier.equals(AccessoryStats.getModifier(UnionLib.getAccessoryInventory(player).getSecondRing()))) level++;
					if (modifier.equals(AccessoryStats.getModifierDebuff(UnionLib.getAccessoryInventory(player).getFirstRing()))) level++;
					if (modifier.equals(AccessoryStats.getModifierDebuff(UnionLib.getAccessoryInventory(player).getSecondRing()))) level++;

					Stat stat = modifier.getStat();
					if (level > 0) {
						StatProfile.addModifier(player, stat, modifier.name(), level * modifier.getAmount());
					}
					else {
						StatProfile.removeModifier(player, stat, modifier.name());
					}
				}
			}
		}
	}
}
