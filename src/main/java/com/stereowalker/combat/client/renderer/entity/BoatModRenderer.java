package com.stereowalker.combat.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.stereowalker.combat.Combat;
import com.stereowalker.combat.client.renderer.entity.model.BoatModModel;
import com.stereowalker.combat.entity.item.BoatModEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoatModRenderer extends EntityRenderer<BoatModEntity> {
	private static final ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[]{Combat.getInstance().location("textures/entity/boat/dead_oak.png"), Combat.getInstance().location("textures/entity/boat/ausldine.png"), Combat.getInstance().location("textures/entity/boat/monoris.png")};
	protected BoatModModel modelBoat = new BoatModModel();

	public BoatModRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(BoatModEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		matrixStackIn.translate(0.0D, 0.375D, 0.0D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
		float f = (float)entityIn.getTimeSinceHit() - partialTicks;
		float f1 = entityIn.getDamageTaken() - partialTicks;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f > 0.0F) {
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float)entityIn.getForwardDirection()));
		}

		float f2 = entityIn.getRockingAngle(partialTicks);
		if (!MathHelper.epsilonEquals(f2, 0.0F)) {
			matrixStackIn.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entityIn.getRockingAngle(partialTicks), true));
		}

		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
		this.modelBoat.setRotationAngles(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelBoat.getRenderType(this.getEntityTexture(entityIn)));
		this.modelBoat.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getWaterMask());
		this.modelBoat.func_228245_c_().render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	public ResourceLocation getEntityTexture(BoatModEntity entity) {
		return BOAT_TEXTURES[entity.getBoatType().ordinal()];
	}
}