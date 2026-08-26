package techguns.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import techguns.items.armors.GenericArmorMultiCamo;

public class RenderArmorItem extends RenderItemBase {

	protected ModelBiped modelBiped;
	
	public RenderArmorItem(ModelBiped model, ResourceLocation texture, EntityEquipmentSlot armorSlot) {
		super(null, texture);
		this.modelBiped=model;
		
		modelBiped.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
		modelBiped.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;

		modelBiped.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || armorSlot == EntityEquipmentSlot.LEGS;
		modelBiped.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		modelBiped.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
		modelBiped.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
		modelBiped.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
		
		this.scale_thirdp = 1.0f;
		this.scale_ego = 1.0f;
		this.scale_gui = 1.5f;
		this.scale_itemframe = 1.5f;
		
		if (armorSlot == EntityEquipmentSlot.HEAD) {
			this.scale_ground = 1.25f;
		} else {
			this.scale_ground = 1.5f;
		}
		
		//this.translateBase[1]=0;
		switch(armorSlot) {
		case HEAD:
			this.translateBase[1]=-0.54f;
			break;
		case CHEST:
			this.translateBase[1]= -0.9f;
			break;
		case LEGS:
			this.translateBase[1]= -1.29f;
			break;
		case FEET:
			this.translateBase[1]= -1.42f;
			break;
		default:
			break;
		}
		
		this.translateType[4][2] = 0.07f; //Item Frame, Z
		
		this.translateType[0][1] = 0.1f;
		this.translateType[1][1] = 0.04f;
		
	}

	protected ResourceLocation getTexture(ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof GenericArmorMultiCamo armorCamo) {
			return armorCamo.getCamoItemTexture(stack);
		}
		return this.texture;
	}

	@Override
	public void renderItem(@NotNull TransformType transform, @NotNull ItemStack stack, EntityLivingBase elb, boolean leftHanded) {
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5f, 0.5f, 0.5f);

		Minecraft.getMinecraft().getTextureManager().bindTexture(this.getTexture(stack));

		this.applyTranslation(transform);

		if (transform == TransformType.GUI) {
			GlStateManager.rotate(180.0f, 0, 1f, 0);
		} else if (transform == TransformType.FIRST_PERSON_LEFT_HAND || transform == TransformType.THIRD_PERSON_LEFT_HAND) {
			GlStateManager.translate(-1f, 0f, 0f);
		}

		this.setBaseScale(elb,transform);
		this.setBaseRotation(transform);
		this.applyBaseTranslation();

		modelBiped.render(elb, 0, 0, 0, 0, 0, SCALE);

		GlStateManager.popMatrix();

	}
	
}
