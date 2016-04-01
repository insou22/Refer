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

import co.insou.refer.utils.item.util.AccessUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BookMetaBuilder extends MetaBuilder {

	public BookMetaBuilder() {
	}

	public BookMetaBuilder(ItemStack itemStack) {
		super(itemStack);
	}

	public BookMetaBuilder(ItemBuilder itemBuilder) {
		super(itemBuilder);
	}

	protected BookMetaBuilder(MetaBuilder builder) {
		super();
		this.meta = builder.meta;
		this.itemBuilder = builder.itemBuilder;
	}

	private BookMeta getMeta() {
		return (BookMeta) this.meta;
	}

	@Override
	public BookMetaBuilder fromConfig(ConfigurationSection section, boolean translateColors) {
		super.fromConfig(section, translateColors);

		if (section.contains("author")) {
			withAuthor(format(translateColors(section.getString("author"), translateColors)));
		}

		if (section.contains("title")) {
			withTitle(format(translateColors(section.getString("title"), translateColors)));
		}

		if (section.contains("pages")) {
			List<String> pageStrings = section.getStringList("pages");
			if (translateColors || !formatMap.isEmpty()) {
				List<String> translated = new ArrayList<>();
				for (String s : pageStrings) {
					translated.add(format(translateColors(s, translateColors)));
				}
				withPages(translated);
			} else { withPages(pageStrings); }
		}

		return this;
	}

	@Override
	public ConfigurationSection toConfig(ConfigurationSection section) {
		section = super.toConfig(section);

		section.set("author", getMeta().getAuthor());
		section.set("title", getMeta().getTitle());
		section.set("pages", new ArrayList<String>() {
			{
				for (String page : getMeta().getPages()) {
					add(page);
				}
			}
		});

		return section;
	}

	/**
	 * Change the title of the book
	 *
	 * @param title title
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withTitle(String title) {
		validateInit();
		getMeta().setTitle(title);
		return this;
	}

	/**
	 * Change the author of the book
	 *
	 * @param author author
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withAuthor(String author) {
		validateInit();
		getMeta().setAuthor(author);
		return this;
	}

	/**
	 * Change the specific page of the book
	 *
	 * @param page    page index
	 * @param content page content
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPage(int page, String content) {
		validateInit();
		getMeta().setPage(page, content);
		return this;
	}

	/**
	 * Change the specific page of the book
	 *
	 * @param page      page index
	 * @param component {@link BaseComponent} content of the page
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPage(int page, BaseComponent component) {
		validateInit();

		try {
			List<Object> pages = (List<Object>) CraftMetaBook_pages.get(getMeta());
			pages.set(page - 1, deseriliazeMessage(ComponentSerializer.toString(component)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return this;
	}

	/**
	 * Set the pages of the book
	 *
	 * @param pages List of pages
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPages(List<String> pages) {
		validateInit();
		getMeta().setPages(pages);
		return this;
	}

	/**
	 * Add a page to the book
	 *
	 * @param pages Array of pages to add
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPage(String... pages) {
		validateInit();
		getMeta().addPage(pages);
		return this;
	}

	/**
	 * Add a page to the book
	 *
	 * @param components Array of {@link BaseComponent} pages to add
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPage(BaseComponent... components) {
		validateInit();

		try {
			List<Object> pages = (List<Object>) CraftMetaBook_pages.get(getMeta());
			for (BaseComponent component : components) {
				pages.add(deseriliazeMessage(ComponentSerializer.toString(component)));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return this;
	}

	/**
	 * Set the pages of the book
	 *
	 * @param pages Array of pages to set
	 * @return the BookMetaBuilder
	 */
	public BookMetaBuilder withPages(String... pages) {
		validateInit();
		getMeta().setPages(pages);
		return this;
	}

	/**
	 * @return the built {@link BookMeta}
	 */
	@Override
	public BookMeta build() {
		return (BookMeta) super.build();
	}

	String serializeMessage(Object component) {
		String serialized = "{}";
		try {
			serialized = (String) AccessUtil.setAccessible(Reflection.getMethod(ChatSerializer, "a", new Class[]{IChatBaseComponent})).invoke(null, component);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialized;
	}

	Object deseriliazeMessage(String serialized) {
		try {
			return AccessUtil.setAccessible(Reflection.getMethod(ChatSerializer, "a", new Class[] { String.class })).invoke(null, serialized);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	Class<?> CraftMetaBook       = Reflection.getOBCClass("inventory.CraftMetaBook");
	Class<?> IChatBaseComponent  = Reflection.getNMSClass("IChatBaseComponent");
	Class<?> ChatSerializer      = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
	Field    CraftMetaBook_pages = Reflection.getField(CraftMetaBook, "pages");
}
