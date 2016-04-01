/*
 * Copyright 2015 Marvin Schäfer (inventivetalent). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package co.insou.refer.utils.item;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

/**
 * {@link Comparator} for {@link ItemStack}s
 */
public class ItemComparator implements Comparator<ItemStack> {

	private CompareMode mode;

	private boolean ignoreAmount;
	private boolean ignoreDurability;
	private boolean ignoreMeta;

	/**
	 * Construct the default comparator
	 */
	public ItemComparator() {
		mode = CompareMode.TYPE;
		ignoreAmount = false;
		ignoreDurability = false;
		ignoreMeta = false;
	}

	/**
	 * Construct a comparator with the specified CompareMode
	 *
	 * @param mode CompareMode
	 */
	public ItemComparator(CompareMode mode) {
		this();
		this.mode = mode;
	}

	/**
	 * Construct a comparator with the specified CompareMode
	 *
	 * @param mode         CompareMode
	 * @param ignoreAmount if the comparator should ignore the item's amount
	 */
	public ItemComparator(CompareMode mode, boolean ignoreAmount) {
		this(mode);
		this.ignoreAmount = ignoreAmount;
	}

	/**
	 * Construct a comparator with the specified CompareMode
	 *
	 * @param mode             CompareMode
	 * @param ignoreAmount     if the comparator should ignore the item's amount
	 * @param ignoreDurability if the comparator should ignore the item's durability
	 */
	public ItemComparator(CompareMode mode, boolean ignoreAmount, boolean ignoreDurability) {
		this(mode, ignoreAmount);
		this.ignoreDurability = ignoreDurability;
	}

	/**
	 * Construct a comparator with the specified CompareMode
	 *
	 * @param mode             CompareMode
	 * @param ignoreAmount     if the comparator should ignore the item's amount
	 * @param ignoreDurability if the comparator should ignore the item's durability
	 * @param ignoreMeta       if the comparator should ignore the item's {@link org.bukkit.inventory.meta.ItemMeta}
	 */
	public ItemComparator(CompareMode mode, boolean ignoreAmount, boolean ignoreDurability, boolean ignoreMeta) {
		this(mode, ignoreAmount, ignoreDurability);
		this.ignoreMeta = ignoreMeta;
	}

	/**
	 * Compares the two {@link ItemStack}s
	 */
	@Override
	public int compare(ItemStack a, ItemStack b) {
		if (equals(a, b)) { return 0; }

		if (mode != null) {
			switch (mode) {
				case TYPE:
					return b.getTypeId() > b.getTypeId() ? 1 : -1;
				case AMOUNT:
					if (ignoreAmount) { return 0; }
					return b.getAmount() > a.getAmount() ? 1 : -1;
				case DURABILITY:
					if (ignoreDurability) { return 0; }
					return b.getDurability() > a.getDurability() ? 1 : -1;
				case DISPLAY_NAME:
					if (ignoreMeta) { return 0; }
					if (a.hasItemMeta() && b.hasItemMeta()) {
						if (a.getItemMeta().getDisplayName() != null) {
							return a.getItemMeta().getDisplayName().compareTo(b.getItemMeta().getDisplayName());
						}
					}
			}
		}

		return 0;
	}

	/**
	 * Check if two {@link ItemStack}s are equal
	 * This method will not check <i>amount/durability/item meta</i> if they were set to be ignored in the constructor
	 *
	 * @param a First {@link ItemStack}
	 * @param b Second {@link ItemStack} to compare with the first
	 * @return <code>true</code> if the {@link ItemStack} are equal, <code>false</code> otherwise
	 */
	public boolean equals(ItemStack a, ItemStack b) {
		if (a.getType() != b.getType()) { return false; }
		if (!ignoreAmount) {
			if (a.getAmount() != b.getAmount()) { return false; }
		}
		if (!ignoreDurability) {
			if (a.getDurability() != b.getDurability()) { return false; }
		}
		if (!ignoreMeta) {
			if (!Bukkit.getItemFactory().equals(a.getItemMeta(), b.getItemMeta())) { return false; }
		}

		return true;
	}

	/**
	 * Mode to use for {@link ItemStack} comparison in a {@link ItemComparator}
	 */
	public enum CompareMode {
		/**
		 * Compares the item type by id
		 */
		TYPE,
		/**
		 * Compares the item amount
		 */
		AMOUNT,
		/**
		 * Compares the item durability
		 */
		DURABILITY,
		/**
		 * Compares the item display name
		 */
		DISPLAY_NAME
	}

}
