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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionMetaBuilder extends MetaBuilder {

	public PotionMetaBuilder() {
	}

	public PotionMetaBuilder(ItemStack itemStack) {
		super(itemStack);
	}

	public PotionMetaBuilder(ItemBuilder itemBuilder) {
		super(itemBuilder);
	}

	protected PotionMetaBuilder(MetaBuilder builder) {
		super();
		this.meta = builder.meta;
		this.itemBuilder = builder.itemBuilder;
	}

	private PotionMeta getMeta() {
		return (PotionMeta) this.meta;
	}

	/**
	 * Change the main effect of the potion
	 *
	 * @param mainEffect Main {@link PotionEffectType}
	 * @return the PotionMetaBuilder
	 */
	public PotionMetaBuilder withMainEffect(PotionEffectType mainEffect) {
		validateInit();
		getMeta().setMainEffect(mainEffect);
		return this;
	}

	/**
	 * Add a custom effect
	 *
	 * @param effect    {@link PotionEffect}
	 * @param overwrite overwrite
	 * @return the PotionMetaBuilder
	 */
	public PotionMetaBuilder withCustomEffect(PotionEffect effect, boolean overwrite) {
		validateInit();
		getMeta().addCustomEffect(effect, overwrite);
		return this;
	}

	/**
	 * @return the built {@link PotionMeta}
	 */
	@Override
	public PotionMeta build() {
		return (PotionMeta) super.build();
	}
}
