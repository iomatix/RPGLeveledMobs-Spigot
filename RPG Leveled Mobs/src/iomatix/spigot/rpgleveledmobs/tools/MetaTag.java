package iomatix.spigot.rpgleveledmobs.tools;

public enum MetaTag {

	RPGmob("RPGMob"), Level("RPGMobLevel"), DamageMod("RPGMobDamageMod"), DefenseMod("RPGMobDefenseMod"),
	ExpMod("RPGMobExpMod"), RecentKill("RPGMobRecentKill"),RecentKillAddon("RPGMobRecentKillAddon"), ArenaExpMod("RPGMobArenaXpMod"), MoneyMod("RPGMobMoneyMod"),
	MoneyDrop("RPGMobMoneyDrop"), MoneyRandomizer("RPGMobMoneyRandomizer"), CustomName("RPGMobCustomName"),
	BaseAdditionalHealth("RPGMobBaseHealth"), HealthMod("RPGMobHealthMod"),
	ExpAddon("RPGMobAdditionalExpValue"),HealthAddon("RPGMobAdditionalHpValue"),
	DefenseAddon("RPGMobAdditionalDefenseValue"), DamageAddon("RPGMobAdditionalDamageValue");
	

	private String tag;

	private MetaTag(final String tag) {
		this.tag = tag;
	}
}
