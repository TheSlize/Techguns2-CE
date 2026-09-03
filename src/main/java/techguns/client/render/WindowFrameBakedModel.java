package techguns.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.blocks.BlockTGWindowFrame;
import techguns.client.ClientProxy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WindowFrameBakedModel implements IBakedModel {

	private final IBakedModel frame;
	private final int facingIndex;

	public WindowFrameBakedModel(IBakedModel frame, int facingIndex) {
		this.frame = frame;
		this.facingIndex = facingIndex;
	}

	@Nullable
	private IBakedModel getPane(@Nullable IBlockState state) {
		if (!(state instanceof IExtendedBlockState)) return null;
		Integer index = ((IExtendedBlockState) state).getValue(BlockTGWindowFrame.PANE);
		return index == null ? null : ClientProxy.getWindowPaneModel(index, this.facingIndex);
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		IBakedModel pane = this.getPane(state);
		if (layer == null) {
			if (pane == null) return this.frame.getQuads(state, side, rand);
			List<BakedQuad> quads = new ArrayList<>(this.frame.getQuads(state, side, rand));
			quads.addAll(pane.getQuads(state, side, rand));
			return quads;
		}
		if (layer == BlockRenderLayer.TRANSLUCENT) {
			return pane == null ? Collections.emptyList() : pane.getQuads(state, side, rand);
		}
		return this.frame.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.frame.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.frame.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public @NotNull TextureAtlasSprite getParticleTexture() {
		return this.frame.getParticleTexture();
	}

	@Override
	public @NotNull ItemCameraTransforms getItemCameraTransforms() {
		return this.frame.getItemCameraTransforms();
	}

	@Override
	public @NotNull ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
