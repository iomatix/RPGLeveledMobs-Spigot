package iomatix.spigot.rpgleveledmobs.userInterface;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Button {
	private onButtonPressedListener buttonPressedListener;
	private ArrayList<String> lore;
	protected ItemStack itemStack;
	private static Enchantment glow;

	public Button() {
		this.lore = new ArrayList<String>();
		this.itemStack = new ItemStack(Material.GLASS);
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public void setIcon(final Material material) {
		this.itemStack.setType(material);
		final ItemMeta meta = this.itemStack.getItemMeta();
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		this.itemStack.setItemMeta(meta);
	}

	public void setOnPressedListener(final onButtonPressedListener listener) {
		this.buttonPressedListener = listener;
	}

	public void setAmount(final int amount) {
		this.itemStack.setAmount(amount);
	}

	public void setName(final String name) {
		final ItemMeta meta = this.itemStack.getItemMeta();
		meta.setDisplayName(name);
		this.itemStack.setItemMeta(meta);
	}

	public void hasGlowEffect(final boolean showEffect) {
		if (showEffect) {
			this.itemStack.addEnchantment(this.getGlow(), 1);
		} else {
			this.itemStack.removeEnchantment(this.getGlow());
		}
	}

	public void buttonPressed(final MenuInteractionEvent event) {
		if (this.buttonPressedListener != null) {
			this.buttonPressedListener.onButtonPressed(event);
		}
	}

	public void removeLore() {
		this.lore.clear();
		this.itemStack.getItemMeta().setLore((List) this.lore);
	}

	public onButtonPressedListener getOnButtonPressedListener() {
		return this.buttonPressedListener;
	}

	public void setLore(final ArrayList<String> lore) {
		final ItemMeta meta = this.itemStack.getItemMeta();
		meta.setLore((List) lore);
		this.itemStack.setItemMeta(meta);
	}

	public void addLoreLine(final String line) {
		this.lore.add(line);
		final ItemMeta meta = this.itemStack.getItemMeta();
		meta.setLore((List) this.lore);
		this.itemStack.setItemMeta(meta);
	}

	public ArrayList<String> getLore() {
		return this.lore;
	}

	public String getName() {
		return this.itemStack.getItemMeta().getDisplayName();
	}

	public void removeLastLoreLine() {
		final ItemMeta meta = this.itemStack.getItemMeta();
		this.lore.remove(this.lore.size() - 1);
		meta.setLore((List) this.lore);
	}

	private Enchantment getGlow() {
		if (Button.glow != null) {
			return Button.glow;
		}
		try {
			final Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Button.glow = (Enchantment) new EnchantGlow("vanishing_curse");
		try {
			Enchantment.registerEnchantment(Button.glow);
		} catch (IllegalArgumentException ex) {

		}
		return Button.glow;
	}

	private class EnchantGlow extends EnchantmentWrapper {
		public EnchantGlow(final String id) {
			super(id);
		}

		public boolean canEnchantItem(final ItemStack item) {
			return true;
		}

		public boolean conflictsWith(final Enchantment other) {
			return false;
		}

		public EnchantmentTarget getItemTarget() {
			return null;
		}

		public int getMaxLevel() {
			return 10;
		}

		public String getName() {
			return "Glow";
		}

		public int getStartLevel() {
			return 1;
		}
	}

	public interface onButtonPressedListener {
		void onButtonPressed(final MenuInteractionEvent p0);
	}
}
