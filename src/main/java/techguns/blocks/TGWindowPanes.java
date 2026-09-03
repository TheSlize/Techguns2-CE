package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Th3_Sl1ze: all that shit is made for model baking, and yes I don't want to use TESR for window frames
public class TGWindowPanes {

	private static final List<ItemStack> PANES = new ArrayList<>();
	private static final Map<String, Integer> INDICES = new HashMap<>();

	public static void init() {
		PANES.clear();
		INDICES.clear();

		List<Block> panes = new ArrayList<>();
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof BlockPane && block.getRegistryName() != null) panes.add(block);
		}
		panes.sort(Comparator.comparing(b -> b.getRegistryName().toString()));

		NonNullList<ItemStack> stacks = NonNullList.create();
		for (Block block : panes) {
			Item item = Item.getItemFromBlock(block);
			if (item == Items.AIR) continue;

			stacks.clear();
			try {
				item.getSubItems(CreativeTabs.SEARCH, stacks);
			} catch (Exception e) {
				stacks.clear();
				stacks.add(new ItemStack(block));
			}

			for (ItemStack stack : stacks) {
				if (stack.isEmpty()) continue;
				ItemStack pane = stack.copy();
				pane.setCount(1);
				if (INDICES.putIfAbsent(getKey(pane), PANES.size() + 1) == null) PANES.add(pane);
			}
		}
	}

	private static String getKey(ItemStack stack) {
		return stack.getItem().getRegistryName() + "@" + stack.getMetadata();
	}

	public static boolean isPaneStack(ItemStack stack) {
		return !stack.isEmpty() && INDICES.containsKey(getKey(stack));
	}

	public static int getIndex(ItemStack stack) {
		if (stack.isEmpty()) return 0;
		Integer index = INDICES.get(getKey(stack));
		return index == null ? 0 : index;
	}

	public static ItemStack getPane(int index) {
		if (index <= 0 || index > PANES.size()) return ItemStack.EMPTY;
		return PANES.get(index - 1).copy();
	}

	@Nullable
	public static IBlockState getPaneState(int index) {
		ItemStack stack = getPane(index);
		if (stack.isEmpty()) return null;
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block == Blocks.AIR) return null;
		return block.getStateFromMeta(stack.getItem().getMetadata(stack.getMetadata()));
	}
}
