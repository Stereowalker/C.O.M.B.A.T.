package com.stereowalker.combat.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.stereowalker.combat.Combat;
import com.stereowalker.combat.api.spell.AbstractRaySpell;
import com.stereowalker.combat.entity.magic.RayEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RayRenderer extends EntityRenderer<RayEntity>{
	public static final ResourceLocation RAY_TEXTURES = Combat.getInstance().location("textures/entity/ray.png");

	protected RayRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	private Vector3d getPosition(Entity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
		double d0 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosX, entityLivingBaseIn.getPosX());
		double d1 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosY, entityLivingBaseIn.getPosY()) + p_177110_2_;
		double d2 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.lastTickPosZ, entityLivingBaseIn.getPosZ());
		return new Vector3d(d0, d1, d2);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void render(RayEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int p_225623_6_) {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, p_225623_6_);
		LivingEntity owner = entity.getOwner();
		if (entity != null) {
			float f1 = (float)owner.world.getGameTime() + partialTicks;
			float f2 = f1 * 0.5F % 1.0F;
			float f3 = owner.getEyeHeight() - 0.2F;
			matrixStackIn.push();
			double x = owner.getPosX() - entity.getPosX();
			double y = owner.getPosY() - entity.getPosY();
			double z = owner.getPosZ() - entity.getPosZ();
			matrixStackIn.translate(x, y + f3, z);
			Vector3d vec3d = this.getPosition(entity, 0.0D, partialTicks);
			Vector3d vec3d1 = this.getPosition(owner, f3, partialTicks);
			Vector3d vec3d2 = vec3d.subtract(vec3d1);
	         float f4 = (float)(vec3d2.length() + 1.0D);
	         vec3d2 = vec3d2.normalize();
	         float f5 = (float)Math.acos(vec3d2.y);
	         float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
	         matrixStackIn.rotate(Vector3f.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
	         matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
	         float f7 = f1 * 0.05F * -1.5F;
	         int j = (int) (entity.getSpell().getSpell().getCategory().getrCOlor()*255); 
	         int k = (int) (entity.getSpell().getSpell().getCategory().getgCOlor()*255);
	         int l = (int) (entity.getSpell().getSpell().getCategory().getbCOlor()*255);
	         float f11 = MathHelper.cos(f7 + 2.3561945F) * 0.282F;
	         float f12 = MathHelper.sin(f7 + 2.3561945F) * 0.282F;
	         float f13 = MathHelper.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
	         float f14 = MathHelper.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
	         float f15 = MathHelper.cos(f7 + 3.926991F) * 0.282F;
	         float f16 = MathHelper.sin(f7 + 3.926991F) * 0.282F;
	         float f17 = MathHelper.cos(f7 + 5.4977875F) * 0.282F;
	         float f18 = MathHelper.sin(f7 + 5.4977875F) * 0.282F;
	         float f19 = MathHelper.cos(f7 + (float)Math.PI) * 0.2F;
	         float f20 = MathHelper.sin(f7 + (float)Math.PI) * 0.2F;
	         float f21 = MathHelper.cos(f7 + 0.0F) * 0.2F;
	         float f22 = MathHelper.sin(f7 + 0.0F) * 0.2F;
	         float f23 = MathHelper.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
	         float f24 = MathHelper.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
	         float f25 = MathHelper.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
	         float f26 = MathHelper.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
	         float f29 = -1.0F + f2;
	         float f30 = f4 * 2.5F + f29;
	         IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(((AbstractRaySpell)entity.getSpell().getSpell()).rayTexture()));
	         MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
	         Matrix4f matrix4f = matrixstack$entry.getMatrix();
	         Matrix3f matrix3f = matrixstack$entry.getNormal();
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
	         float f31 = 0.0F;
	         if (owner.ticksExisted % 2 == 0) {
	            f31 = 0.5F;
	         }

	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
	         func_229108_a_(ivertexbuilder, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
	         matrixStackIn.pop();
		}
	}

	private static void func_229108_a_(IVertexBuilder p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_) {
		p_229108_0_.pos(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).tex(p_229108_9_, p_229108_10_).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
	}


	@Override
	public ResourceLocation getEntityTexture(RayEntity entity) {
		return RAY_TEXTURES;
	}

}
