package iomatix.spigot.rpgleveledmobs.tools;

public enum MetaTag {

	RPGmob("RPGMob"), Level("RPGMobLevel"), DamageMod("RPGMobDamageMod"), DefenseMod("RPGMobDefenseMod"),
	ExpMod("RPGMobExpMod"), RecentKill("RPGMobRecentKill"), ArenaExpMod("RPGMobArenaXpMod"),
	MoneyMod("RPGMobMoneyMod"), MoneyDrop("RPGMobMoneyDrop");

	private String tag;

	private MetaTag(final String tag) {
		this.tag = tag;
	}
}
