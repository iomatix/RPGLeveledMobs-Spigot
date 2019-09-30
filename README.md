# RPGLeveledMobs-Spigot
Leveled mobs with scaling attributes compatible with many RPG plugins.

[Release](https://www.spigotmc.org/resources/rpg-leveled-mobs.71301/)



[I really appreciate your work and want to help you keep going!](https://www.paypal.me/iomatix)



## API for Developers:

All leveled mobs use Metadata "MetaTag" from iomatix.spigot.rpgleveledmobs.tools package.

### Usage:

**Add** `import iomatix.spigot.rpgleveledmobs.tools.MetaTag;` **to use the API** or use string syntax instead.
*Remember to add the [RPGLeveledMobs] to dependency in your plugin.yml and add the extern .jar*
*Is recommended to create a module loader which contains the information is the RPGLeveledMobs loaded correctly.*


Add `import org.bukkit.metadata.MetadataValue;` to use the Metadatable values.
To access MetaTag get the [LivingEntity](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/LivingEntity.html) first.

Example will explain how to get the Mob's *level* but the idea is the same for each enum.
Check the *RPGMob* tag by `RPGmob` enum first.
`if(LivingEntity.hasMetadata(MetaTag.RPGmob.toString()) && LivingEntity.hasMetadata(MetaTag.Level.toString()))`
Get the level of the Mob to the variable.
`final int TheLevel = LivingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt;`
Use the local variable in your own method.

Example:
```
double YourAwesomeFunction(LivingEntity mob, double valueOrigin){ // YourAwesomeFunction in most cases is an event listener.

// Start of your awesome code 

double awesomeValue = valueOrigin * awesomeMultiplier;

if(mob.hasMetadata(MetaTag.RPGmob.toString()) && mob.hasMetadata(MetaTag.Level.toString()))
{ 
final int TheLevel = mob.getMetadata(MetaTag.Level.toString()).get(0).asInt;
awesomeValue = awesomeValue * The Level;
}

return awesomeValue; //return your awesome output
}
```

More MetaData Methods: [Metadatable](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/metadata/Metadatable.html)

### MetaTags list:

| **Enum**        | **Meta Data Tag**           | **Type**  | **Description**  |
| :-------------: | :---------------------: | :------------: | :--------------------------: |
| `RPGmob` | *RPGMob* | **Boolean** | Is the mob loaded by RPG Leveled mobs plugin? |
| `Level` | *RPGMobLevel* | **Integer** | The mob's level|
| `DamageMod` | *RPGMobDamageMod* | **Double** | Per level damage modifier. |
| `DamageAddon` | *RPGMobAdditionalDamageValue* | **Double** | Additional flat damage scaled by level. |
| `DefenseMod` | *RPGMobDefenseMod* | **Double** | Per level defense modifier. |
| `DefenseAddon` | *RPGMobAdditionalDefenseValue* | **Double** | Base defense value for all mobs and the additional flat scaled by level. |
| `ExpMod` | *RPGMobExpMod* | **Double** | Per level experience modifier. |
| `ExpAddon` | *RPGMobAdditionalExpValue* | **Double** | Additional flat experience scaled by level. |
| `RecentKill` | *RPGMobRecentKill* | **LinkedList** | `Level` multiplied by `ExpMod` values list of the recent killed mobs. |
| `RecentKillAddon` | *RPGMobRecentKillAddon* | **LinkedList** | `Level` multiplied by `ExpAddon` values list of the recent killed mobs. |
| `ArenaExpMod` | *RPGMobArenaXpMod* | **Double** | [MobArena](https://www.spigotmc.org/resources/mobarena.34110/) experience modifier. |
| `MoneyMod` | *RPGMobMoneyMod* | **Double** | Per level money modifier. |
| `MoneyDrop` | *RPGMobMoneyDrop* | **Double** | Base money value of the mob. |
| `MoneyRandomizer` | *RPGMobMoneyRandomizer* | **Double** | Money randomizer added to the final money output. |
| `CustomName` | *RPGMobCustomName* | **String** | Contains mob's custom name. |
| `BaseAdditionalHealth` | *RPGMobBaseHealth* | **Double** | Mob's vanilla base health without multipliers. |
| `HealthMod` | *RPGMobHealthMod* | **Double** | Per level health modifier. |
| `HealthAddon` | *RPGMobAdditionalHpValue* | **Double** | Additional flat health scaled by level. |

### Events:

[Events Source Code](https://github.com/iomatix/RPGLeveledMobs-Spigot/tree/master/RPG%20Leveled%20Mobs/src/iomatix/spigot/rpgleveledmobs/events)

Package: `package iomatix.spigot.rpgleveledmobs.events;`

| **Event**        | **Method** | **Description**  |
| :-------------: | :---------------------: | :--------------------------: |
| `RPGMobsGainExperience` | `RPGMobsGainExperience(double exp,Player who)` | (**CANCELLABE**) Event is called every time when [Player](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Player.html) gains experience from leveled mobs. `who` is the Player, `exp` is the experience amount. |
| `RPGMobsGainExperience` | `Player getEntity()` | Returns [Player](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Player.html) involved in the event. |
| `RPGMobsGainExperience` | `Double getExp()` | Returns Experience amount. |
| `RPGMobsGainExperience` | `boolean isSkillAPIModuleON()` | Returns true if SkillAPI is loaded correctly. |
| `RPGMobsGainMoney` | `RPGMobsGainMoney(double money,Player who, Economy economyHandler)` | (**CANCELLABE**) Event is called every time when [Player](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Player.html) gains money from leveled mobs. `who` is the Player, `money` is the money amount, `economyHandler` is an vault's [Economy](https://milkbowl.github.io/VaultAPI/net/milkbowl/vault/economy/Economy.html). |
| `RPGMobsGainMoney` | `Player getEntity()` | Returns [Player](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Player.html) involved in the event. |
| `RPGMobsGainMoney` | `Double getMoney()` | Returns (double) money amount. |
| `RPGMobsGainMoney` | `void transaction()` | Deposits the money amount to the [Player](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/Player.html)'s economy account. |