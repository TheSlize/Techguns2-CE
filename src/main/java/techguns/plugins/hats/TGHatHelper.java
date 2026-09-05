package techguns.plugins.hats;

import me.ichun.mods.hats.api.RenderOnEntityHelper;
import net.minecraft.entity.EntityLivingBase;

public class TGHatHelper extends RenderOnEntityHelper {

	public static final float MODEL_ORIGIN = 1.5078125f;

	protected final Class<? extends EntityLivingBase> entityClass;
	protected final float rotatePointVert;
	protected final float rotatePointHori;
	protected final float offsetPointVert;
	protected final float hatScale;

	public TGHatHelper(Class<? extends EntityLivingBase> entityClass, float rotatePointVert, float rotatePointHori, float offsetPointVert, float hatScale) {
		this.entityClass = entityClass;
		this.rotatePointVert = rotatePointVert;
		this.rotatePointHori = rotatePointHori;
		this.offsetPointVert = offsetPointVert;
		this.hatScale = hatScale;
	}

	@Override
	public Class helperForClass() {
		return this.entityClass;
	}

	@Override
	public float getRotatePointVert(EntityLivingBase entity) {
		return this.rotatePointVert;
	}

	@Override
	public float getRotatePointHori(EntityLivingBase entity) {
		return this.rotatePointHori;
	}

	@Override
	public float getOffsetPointVert(EntityLivingBase entity) {
		return this.offsetPointVert;
	}

	@Override
	public float getHatScale(EntityLivingBase entity) {
		return this.hatScale;
	}

}
