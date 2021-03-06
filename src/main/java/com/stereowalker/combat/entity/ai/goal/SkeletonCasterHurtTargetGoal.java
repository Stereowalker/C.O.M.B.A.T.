package com.stereowalker.combat.entity.ai.goal;

import java.util.EnumSet;

import com.stereowalker.combat.entity.monster.SkeletonMinionEntity;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

public class SkeletonCasterHurtTargetGoal extends TargetGoal {
	private final SkeletonMinionEntity tameable;
	private LivingEntity attacker;
	private int timestamp;

	public SkeletonCasterHurtTargetGoal(SkeletonMinionEntity theEntityTameableIn) {
		super(theEntityTameableIn, false);
		this.tameable = theEntityTameableIn;
		this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		LivingEntity livingentity = this.tameable.getOwner();
		if (livingentity == null) {
			return false;
		} else {
			this.attacker = livingentity.getLastAttackedEntity();
			int i = livingentity.getLastAttackedEntityTime();
			return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, livingentity);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.tameable.setAttackTarget(this.attacker);
		LivingEntity livingentity = this.tameable.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastAttackedEntityTime();
		}

		super.startExecuting();
	}
}