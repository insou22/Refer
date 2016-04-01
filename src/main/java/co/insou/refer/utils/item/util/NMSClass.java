package co.insou.refer.utils.item.util;

import co.insou.refer.utils.item.Reflection;

import java.lang.reflect.Field;

public abstract class NMSClass {

	private static boolean	initialized;

	public static Class<?>	WorldServer;
	public static Class<?>	World;
	public static Class<?>	TileEntitySkull;
	public static Class<?>	NBTBase;
	public static Class<?>	NBTTagCompound;
	public static Class<?>	GameProfileSerializer;

	static {
		if (!initialized) {
			for (Field f : NMSClass.class.getDeclaredFields()) {
				if (f.getType().equals(Class.class)) {
					try {
						f.set(null, Reflection.getNMSClassWithException(f.getName()));
					} catch (Exception e) {
					}
				}
			}
		}
	}

}