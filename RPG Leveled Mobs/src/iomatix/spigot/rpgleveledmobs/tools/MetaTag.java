package iomatix.spigot.rpgleveledmobs.tools;

public enum MetaTag {

	RPGmob("RPGMob"), Level("RPGMobLevel"), DamageMod("RPGMobDamageMod"), DefenseMod("RPGMobDefenseMod"),
	ExpMod("RPGMobExpMod"), RecentKill("RPGMobRecentKill"), ArenaExpMod("RPGMobArenaXpMod");

	private String tag;

	private MetaTag(final String tag) {
		this.tag = tag;
	}
}
