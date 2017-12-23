package com.example.admin.ezmunchkin;

/**
 * Created by Falco on 26.10.2017.
 */

public class Bonus extends Card {

    public enum BonusName{
        RIESIGERFELS,
        VERDUNKELUNGSUMHANG,
        HAMMER,
        SCHARFERSTREITKOLBEN,
        SCHILDD,
        HELLEBARDE,
        BOGEN,
        NAPALMSTAB;
    }

    public int bonus;
    BonusWear.Size size;
    public BonusName bonusName;
    public BonusWear.Blocking blocking; //0 = nothing, 1 = 1 hand, 2 = 2 hands

    public Bonus(String name, BonusName bonusName, BonusWear.Blocking blocking, int bonus, BonusWear.Size size) {
        super(Membership.DOOR, name);
        this.bonusName = bonusName;
        this.name = name;
        this.blocking = blocking;
        this.size = size;
    }
}
