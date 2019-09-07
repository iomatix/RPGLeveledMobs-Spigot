package iomatix.spigot.rpgleveledmobs.spawnsController;

import java.util.HashMap;

import org.bukkit.entity.EntityType;

import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.Language;

public class MobNamesMap {
	private static HashMap<Language, HashMap<EntityType, String>> languageMapping;

	public static String getMobName(final Language lang, final EntityType mobType) {
		return MobNamesMap.languageMapping.get(lang).get(mobType);
	}

	static {
		MobNamesMap.languageMapping = new HashMap<Language, HashMap<EntityType, String>>();
		for (final Language lang : Language.values()) {
			MobNamesMap.languageMapping.put(lang, new HashMap<EntityType, String>());
		}
		try {
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.BAT, "Nietoperz");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.BLAZE, "P³omyk");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.CAVE_SPIDER, "Paj¹k Jaskiniowy");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.CHICKEN, "Kurczak");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.COW, "Krowa");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.CREEPER, "Creeper");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.DONKEY, "Osio³");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ELDER_GUARDIAN, "Starszy Stra¿nik");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ENDER_DRAGON, "Smok");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ENDERMAN, "Enderman");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ENDERMITE, "Endermite");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.EVOKER, "Przywo³ywacz");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.GHAST, "Ghast");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.GIANT, "Olbrzym");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.GUARDIAN, "Stra¿nik");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.HORSE, "Koñ");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.HUSK, "Posuch");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.IRON_GOLEM, "Golem");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.MAGMA_CUBE, "Kostka Magmy");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.LLAMA, "Lama");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.MULE, "Mu³");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.MUSHROOM_COW, "Zmutowana Krowa");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.OCELOT, "Ocelot");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PIG, "Œwinia");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PIG_ZOMBIE, "Zombie Pigman");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.POLAR_BEAR, "NiedŸwiedŸ Polarny");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.RABBIT, "Królik");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SHEEP, "Owca");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SHULKER, "Shulker");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SILVERFISH, "Silverfish");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SKELETON, "Szkielet");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SKELETON_HORSE, "Koœciany Koñ");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SLIME, "Slime");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SNOWMAN, "Lodowy Golem");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SPIDER, "Paj¹k");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SQUID, "Ka³amarnica");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.STRAY, "Zb³¹kany");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.VEX, "Vex");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.VILLAGER, "Osadnik");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.VINDICATOR, "Obroñca");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.WITCH, "WiedŸma");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.WITHER, "Wither");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.WITHER_SKELETON, "Obumar³y Szkielet");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.WOLF, "Wilk");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ZOMBIE, "Zombie");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ZOMBIE_HORSE, "Koñ Zombie");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.ZOMBIE_VILLAGER, "Zombie");
			// 1.13:
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PARROT, "Papuga");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.DOLPHIN, "Delfin");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.DROWNED, "Topielec");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PHANTOM, "Fantom");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.COD, "Dorsz");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.SALMON, "£osoœ");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PUFFERFISH, "Ryba Dymka");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.TROPICAL_FISH, "Ryba Tropikalna");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.TURTLE, "¯ó³w");
			// 1.14
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.CAT, "Kot");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.FOX, "Lis");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PANDA, "Panda");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.PILLAGER, "Grabie¿ca");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.RAVAGER, "Niszczyciel");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.TRADER_LLAMA, "Lama Handlarza");
			MobNamesMap.languageMapping.get(Language.POLISH).put(EntityType.WANDERING_TRADER, "Wêdrowny Handlarz");
			/////ENG
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.BAT, "Bat");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.BLAZE, "Blaze");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.CAVE_SPIDER, "Cave Spider");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.CHICKEN, "Chicken");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.COW, "Cow");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.CREEPER, "Creeper");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.DONKEY, "Donkey");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ELDER_GUARDIAN, "Elder Guardian");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ENDER_DRAGON, "Dragon");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ENDERMAN, "Enderman");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ENDERMITE, "Endermite");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.EVOKER, "Evoker");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.GHAST, "Ghast");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.GIANT, "Giant Zombie");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.GUARDIAN, "Guardian");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.HORSE, "Horse");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.HUSK, "Husk");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.IRON_GOLEM, "Golem");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.MAGMA_CUBE, "Magma Cube");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.LLAMA, "Llama");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.MULE, "Mule");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.MUSHROOM_COW, "Mushroom Cow");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.OCELOT, "Ocelot");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PIG, "Pig");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PIG_ZOMBIE, "Pig Zombie");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.POLAR_BEAR, "Bear");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.RABBIT, "Rabbit");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SHEEP, "Sheep");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SHULKER, "Shulker");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SILVERFISH, "Silverfish");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SKELETON, "Skeleton");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SKELETON_HORSE, "Skeleton Horse");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SLIME, "Slime");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SNOWMAN, "Snowman");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SPIDER, "Spider");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SQUID, "Squid");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.STRAY, "Stray");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.VEX, "Vex");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.VILLAGER, "Villager");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.VINDICATOR, "Vindicator");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.WITCH, "Witch");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.WITHER, "Wither");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.WITHER_SKELETON, "Wither Skeleton");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.WOLF, "Wolf");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ZOMBIE, "Zombie");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ZOMBIE_HORSE, "Zombie Horse");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.ZOMBIE_VILLAGER, "Zombie");
			// 1.13:
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PARROT, "Parrot");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.DOLPHIN, "Dolphin");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.DROWNED, "Drowned");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PHANTOM, "Phantom");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.COD, "Cod");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.SALMON, "Salmon");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PUFFERFISH, "Pufferfish");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.TROPICAL_FISH, "Tropical Fish");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.TURTLE, "Turtle");
			// 1.14
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.CAT, "Cat");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.FOX, "Fox");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PANDA, "Panda");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.PILLAGER, "Pillager");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.RAVAGER, "Ravager");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.TRADER_LLAMA, "Llama");
			MobNamesMap.languageMapping.get(Language.ENGLISH).put(EntityType.WANDERING_TRADER, "Wandering Trader");
		} catch (NoSuchFieldError e) {
			LogsModule.warning("Upgrade server to the 1.14.4 for full language support.");
		}
	}

}
