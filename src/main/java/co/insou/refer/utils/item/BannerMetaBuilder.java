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

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public class BannerMetaBuilder extends MetaBuilder {

	public BannerMetaBuilder() {
		super();
	}

	public BannerMetaBuilder(ItemStack itemStack) {
		super(itemStack);
	}

	public BannerMetaBuilder(ItemBuilder itemBuilder) {
		super(itemBuilder);
	}

	protected BannerMetaBuilder(MetaBuilder builder) {
		super();
		this.meta = builder.meta;
		this.itemBuilder = builder.itemBuilder;
	}

	private BannerMeta getMeta() {
		return (BannerMeta) this.meta;
	}

	@Override
	public BannerMetaBuilder fromConfig(ConfigurationSection section, boolean translateColors) {
		super.fromConfig(section, translateColors);

		if (section.contains("patterns")) {
			List<String> patternStrings = section.getStringList("patterns");

			for (String s : patternStrings) {
				String[] split = s.split("-");
				if (split.length != 2) { continue; }
				String patternName = split[0];
				String colorName = split[1];

				PatternType patternType = null;
				try {
					patternType = PatternType.valueOf(patternName.toUpperCase());
				} catch (Exception e) {
				}

				DyeColor color = null;
				try {
					color = DyeColor.valueOf(colorName);
				} catch (Exception e) {
				}

				if (patternType != null && color != null) {
					Pattern pattern = new Pattern(color, patternType);

					withPattern(pattern);
				}
			}
		}

		return this;
	}

	@Override
	public ConfigurationSection toConfig(ConfigurationSection section) {
		section = super.toConfig(section);

		section.set("patterns", new ArrayList<String>() {
			{
				for (Pattern pattern : getMeta().getPatterns()) {
					add(pattern.getPattern().name() + "-" + pattern.getColor().name());
				}
			}
		});

		return section;
	}

	/**
	 * Change the base color of the banner
	 *
	 * @param baseColor base {@link DyeColor}
	 * @return the BannerMetaBuilder
	 */
	public BannerMetaBuilder withBaseColor(DyeColor baseColor) {
		validateInit();
		getMeta().setBaseColor(baseColor);
		return this;
	}

	/**
	 * Add a pattern to the banner
	 *
	 * @param patterns Array of {@link Pattern}s to add
	 * @return the BannerMetaBuilder
	 */
	public BannerMetaBuilder withPattern(Pattern... patterns) {
		validateInit();
		for (Pattern pattern : patterns) {
			getMeta().addPattern(pattern);
		}
		return this;
	}

	/**
	 * @return the built {@link BannerMeta}
	 */
	@Override
	public BannerMeta build() {
		return (BannerMeta) super.build();
	}
}
