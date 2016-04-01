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

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder extends Formattable {

	ItemStack itemStack;

	/**
	 * Constructs a new ItemBuilder, without any content
	 */
	public ItemBuilder() {
	}

	/**
	 * Constructs a new ItemBuilder
	 *
	 * @param itemStack {@link ItemStack to build}
	 */
	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	/**
	 * Constructs a new ItemBuilder
	 *
	 * @param typeId ID of the item
	 */
	public ItemBuilder(int typeId) {
		this.withTypeId(typeId);
	}

	/**
	 * Construct a new ItemBuilder
	 *
	 * @param material Type of the item
	 */
	public ItemBuilder(Material material) {
		this.withType(material);
	}

	/**
	 * Construct a new ItemBuilder
	 *
	 * @param material Type of the item
	 * @param amount   Item amount
	 */
	public ItemBuilder(Material material, int amount) {
		this(material);
		this.withAmount(amount);
	}

	/**
	 * Construct a new ItemBuilder
	 *
	 * @param material   Type of the item
	 * @param amount     Item amount
	 * @param durability Item durability/damage value
	 */
	public ItemBuilder(Material material, int amount, int durability) {
		this(material, amount);
		this.withDurability(durability);
	}

	private void initItem(int id) {
		if (this.itemStack == null) { this.itemStack = new ItemStack(id); }
	}

	void validateInit() {
		if (itemStack == null) { throw new IllegalStateException("Item is not yet initiated (Missing material)"); }
	}

	/**
	 * Loads the item from a configuration section
	 * See <link>https://paste.inventivetalent.org/wivacelunu.yml</link> for an example configuration
	 *
	 * @param section Section of the configuration containing the item
	 * @return the ItemBuilder
	 */
	public ItemBuilder fromConfig(ConfigurationSection section) {
		if (section == null) { throw new IllegalArgumentException("section cannot be null"); }
		if (section.contains("type")) {
			if (section.isInt("type")) {
				withTypeId(section.getInt("type"));
			}
			if (section.isString("type")) {
				Material material = null;
				try {
					material = Material.valueOf(section.getString("type").toUpperCase());
				} catch (Exception e) {
				}
				if (material != null) { withType(material); }
			}
		}

		if (section.contains("amount")) {
			if (section.isInt("amount")) {
				withAmount(section.getInt("amount"));
			}
		}

		if (section.contains("durability")) {
			if (section.isInt("durability")) {
				withDurability(section.getInt("durability"));
			}
		}

		if (section.contains("meta") && section.isConfigurationSection("meta")) {
			ConfigurationSection meta = section.getConfigurationSection("meta");

			MetaBuilder metaBuilder = buildMeta();
			metaBuilder.withFormat(formatMap);
			metaBuilder.fromConfig(meta);
			withMeta(metaBuilder);
		}

		return this;
	}

	/**
	 * Writes the item to a {@link ConfigurationSection}
	 *
	 * @param section {@link ConfigurationSection}
	 * @return return the written {@link ConfigurationSection}
	 */
	public ConfigurationSection toConfig(ConfigurationSection section) {
		if (section == null) { throw new IllegalArgumentException("section cannot be null"); }
		section.set("type", itemStack.getType().name());
		section.set("amount", itemStack.getAmount());
		section.set("durability", itemStack.getDurability());

		ConfigurationSection metaSection = section.getConfigurationSection("meta");
		if (metaSection == null) { metaSection = section.createSection("meta"); }

		MetaBuilder metaBuilder = buildMeta();
		section.set("meta", metaBuilder.toConfig(metaSection));

		return section;
	}

	/**
	 * Adds a string replacement for text being loaded by {@link #fromConfig(ConfigurationSection)}
	 *
	 * @param key   string to be replaced
	 * @param value replacement
	 * @return the ItemBuilder
	 */
	@Override
	public ItemBuilder withFormat(String key, String value) {
		super.withFormat(key, value);
		return this;
	}

	//Material

	/**
	 * Changes the type ID of the item
	 *
	 * @param id Item ID
	 * @return the ItemBuilder
	 */
	public ItemBuilder withTypeId(int id) {
		initItem(id);
		return this;
	}

	/**
	 * Changes the type of the item
	 *
	 * @param material Item {@link Material}
	 * @return the ItemBuilder
	 */
	public ItemBuilder withType(Material material) {
		initItem(material.getId());
		return this;
	}

	//Amount

	/**
	 * Changes the item amount
	 *
	 * @param amount Amount
	 * @return the ItemBuilder
	 */
	public ItemBuilder withAmount(int amount) {
		validateInit();
		itemStack.setAmount(amount);
		return this;
	}

	//Data

	/**
	 * Changes the item durability
	 *
	 * @param durability Durability
	 * @return the ItemBuilder
	 */
	public ItemBuilder withDurability(int durability) {
		validateInit();
		itemStack.setDurability((short) durability);
		return this;
	}

	/**
	 * Changes the Material data
	 *
	 * @param data {@link MaterialData} of the item
	 * @return the ItemBuilder
	 */
	public ItemBuilder withData(MaterialData data) {
		validateInit();
		itemStack.setData(data);
		return this;
	}

	//Item meta

	/**
	 * Creates a new {@link MetaBuilder} for this item
	 *
	 * @return new {@link MetaBuilder}
	 */
	public MetaBuilder buildMeta() {
		return new MetaBuilder(this).convert(itemStack.getItemMeta().getClass());
	}

	/**
	 * Creates a new {@link MetaBuilder} for this item
	 *
	 * @param metaClazz {@link MetaBuilder} class to return
	 * @return new {@link MetaBuilder}
	 */
	public <T extends MetaBuilder> T buildMeta(Class<T> metaClazz) {
		return buildMeta().convertBuilder(metaClazz);
	}

	/**
	 * Changes the ItemMeta of the item
	 *
	 * @param meta {@link ItemMeta}
	 * @return the ItemBuilder
	 */
	public ItemBuilder withMeta(ItemMeta meta) {
		validateInit();
		itemStack.setItemMeta(meta);
		return this;
	}

	/**
	 * Changes the ItemMeta of the item
	 *
	 * @param meta {@link MetaBuilder}
	 * @return the ItemBuilder
	 */
	public ItemBuilder withMeta(MetaBuilder meta) {
		validateInit();
		itemStack.setItemMeta(meta.build());
		return this;
	}

	//Build

	/**
	 * Builds this item
	 *
	 * @return the built {@link ItemStack}
	 */
	public ItemStack build() {
		validateInit();
		return this.itemStack;
	}

}
