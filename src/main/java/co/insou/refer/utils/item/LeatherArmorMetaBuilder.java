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

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeatherArmorMetaBuilder extends MetaBuilder {

	final Pattern RGB_PATTERN = Pattern.compile("R:([0-9]+) G:([0-9]+) B:([0-9]+)");

	public LeatherArmorMetaBuilder() {
	}

	public LeatherArmorMetaBuilder(ItemStack itemStack) {
		super(itemStack);
	}

	public LeatherArmorMetaBuilder(ItemBuilder itemBuilder) {
		super(itemBuilder);
	}

	protected LeatherArmorMetaBuilder(MetaBuilder builder) {
		super();
		this.meta = builder.meta;
		this.itemBuilder = builder.itemBuilder;
	}

	private LeatherArmorMeta getMeta() {
		return (LeatherArmorMeta) this.meta;
	}

	@Override
	public LeatherArmorMetaBuilder fromConfig(ConfigurationSection section, boolean translateColors) {
		super.fromConfig(section, translateColors);

		if (section.contains("color")) {
			String colorString = section.getString("color");

			DyeColor dyeColor = null;
			try {
				dyeColor = DyeColor.valueOf(colorString.toUpperCase());
			} catch (Exception e) {
			}

			Color color = null;

			if (dyeColor != null) {
				color = dyeColor.getColor();
			} else {
				Matcher matcher = RGB_PATTERN.matcher(colorString);
				if (matcher.groupCount() == 3) {
					int r = Integer.parseInt(matcher.group(0));
					int g = Integer.parseInt(matcher.group(1));
					int b = Integer.parseInt(matcher.group(2));

					color = Color.fromRGB(r, g, b);
				}
			}

			if (color != null) {
				withColor(color);
			}
		}

		return this;
	}

	@Override
	public ConfigurationSection toConfig(ConfigurationSection section) {
		section = super.toConfig(section);

		Color color = getMeta().getColor();
		section.set("color", "R:" + color.getRed() + " G:" + color.getGreen() + " B:" + color.getBlue());

		return section;
	}

	/**
	 * Change the color of the Armor
	 *
	 * @param color {@link Color}
	 * @return the LeatherArmorMetaBuilder
	 */
	public LeatherArmorMetaBuilder withColor(Color color) {
		validateInit();
		getMeta().setColor(color);
		return this;
	}

	/**
	 * @return the built {@link LeatherArmorMeta}
	 */
	@Override
	public LeatherArmorMeta build() {
		return (LeatherArmorMeta) super.build();
	}
}
