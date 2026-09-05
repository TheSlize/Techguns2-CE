package techguns.plugins.hats;

import me.ichun.mods.hats.common.core.ApiHandler;
import net.minecraft.entity.EntityLivingBase;
import techguns.entities.npcs.AlienBug;
import techguns.entities.npcs.GenericNPC;
import techguns.entities.npcs.Ghastling;
import techguns.entities.npcs.SuperMutantBasic;
import techguns.entities.npcs.TGDummySpawn;

public class TGHatsPlugin {

	public static void init() {
		ApiHandler.registerHelper(new NPCHatHelper());
		ApiHandler.registerHelper(new SuperMutantHatHelper());
		ApiHandler.registerHelper(new AlienBugHatHelper());
		ApiHandler.registerHelper(new GhastlingHatHelper());
	}

	public static class NPCHatHelper extends TGHatHelper {

		public NPCHatHelper() {
			super(GenericNPC.class, MODEL_ORIGIN, 0.0f, 0.5f, 1.0f);
		}

		@Override
		public boolean canWearHat(EntityLivingBase entity) {
			return super.canWearHat(entity) && !(entity instanceof TGDummySpawn);
		}

	}

	public static class SuperMutantHatHelper extends TGHatHelper {

		public SuperMutantHatHelper() {
			super(SuperMutantBasic.class, MODEL_ORIGIN, 0.0f, 0.5f, 1.0f);
		}

		@Override
		public float getRotatePointVert(EntityLivingBase entity) {
			return MODEL_ORIGIN + (float) ((SuperMutantBasic) entity).getModelHeightOffset();
		}

		@Override
		public float getOffsetPointVert(EntityLivingBase entity) {
			return 0.5f * ((SuperMutantBasic) entity).getModelScale();
		}

		@Override
		public float getHatScale(EntityLivingBase entity) {
			return ((SuperMutantBasic) entity).getModelScale();
		}

	}

	public static class AlienBugHatHelper extends TGHatHelper {

		public AlienBugHatHelper() {
			super(AlienBug.class, 0.7890625f, 0.0625f, 0.15625f, 0.7f);
		}

	}

	public static class GhastlingHatHelper extends TGHatHelper {

		public GhastlingHatHelper() {
			super(Ghastling.class, 2.1078125f, 0.0f, 0.0f, 2.0f);
		}

		@Override
		public float getPrevRotationYaw(EntityLivingBase entity) {
			return entity.prevRenderYawOffset;
		}

		@Override
		public float getRotationYaw(EntityLivingBase entity) {
			return entity.renderYawOffset;
		}

		@Override
		public float getPrevRotationPitch(EntityLivingBase entity) {
			return 0.0f;
		}

		@Override
		public float getRotationPitch(EntityLivingBase entity) {
			return 0.0f;
		}

	}

}
