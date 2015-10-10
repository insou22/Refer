package co.insou.utils;

public class Replace {

	public static String replace (String s) {
		return s.replaceAll("&", "§");
	}
	
	public static String replace (String s, ReferPlayer p) {
		return replace(s).replaceAll("%player%", p.getName()).replaceAll("%refers%", p.getReferences() + "");
	}
	
}
