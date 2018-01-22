package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;

public class TreasureCards {
    public static final Card POLE = new BonusWear(R.string.pole, R.string.pole_desc, R.drawable.plain,1, BonusWear.Blocking.TWOHANDS, BonusWear.Size.SMALL);
    public static final Card HELMET = new BonusWear(R.string.helmet, R.string.helmet_desc, R.drawable.helmofcourage, 1, BonusWear.Blocking.HEAD, BonusWear.Size.SMALL);
    public static final Card BOILED_LEATHER = new BonusWear(R.string.boiled_leather, R.string.boiled_leather_desc, R.drawable.tleatherarmor, 1, BonusWear.Blocking.ARMOR, BonusWear.Size.SMALL);
    public static final Card SLIM_ARMOR = new BonusWear(R.string.slim_armor, R.string.slim_armor_desc, R.drawable.plain, 1, BonusWear.Blocking.ARMOR, BonusWear.Size.SMALL);
    public static final Card KNEE = new BonusWear(R.string.knee, R.string.knee_desc, R.drawable.tspikyknies, 1, BonusWear.Blocking.NOTHING, BonusWear.Size.SMALL);
    public static final Card HOT_HELMET = new BonusWear(R.string.hot_helmet, R.string.hot_helmet_desc, R.drawable.thornyhelmet, 1, BonusWear.Blocking.HEAD, BonusWear.Size.SMALL);
    public static final Card ASS_KICK_BOOT = new BonusWear(R.string.ass_kick_boot, R.string.ass_kick_boot_desc, R.drawable.plain, 1, BonusWear.Blocking.SHOES, BonusWear.Size.SMALL);
}
