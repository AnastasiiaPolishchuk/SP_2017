package com.example.admin.ezmunchkin;

/**
 * Created by Falco on 26.10.2017.
 */

public class Curse extends Card {

    public enum CurseName {
        WIRKLICHBESCHISSENDERFLUCH,
        LOSELEVEL,
        LOSEARMOR,
        LOSESHOES,
        LOSEHAT,
        LOSERACE,
        DUCK,
        LOSESMALL,
        LOSEBIG,
        LOSERACEORLEVEL,
        LOSERACETOHUMAN;

    }

    public CurseName curseName;
    public String description;

    public Curse (String name, CurseName curseName, String description) {
        super(Membership.DOOR, name);
        this.description = description;
        this.curseName = curseName;
    }

    /**
     * Computes the execution of the curse
     * @param currentPlayer
     */
    public void execution(Player currentPlayer, Game currentGame){
        if (currentPlayer.searchForWunschringAndRemove(currentGame)){
            //Wunschring des Players wird falls er einen hat in der Methode hatWunschring abgelegt
            currentPlayer.discardDoor(this, currentGame);
            currentGame.fightDeck.remove(this);
            return;
        }
        else {
            switch (this.curseName){
                case WIRKLICHBESCHISSENDERFLUCH: currentPlayer.searchAndDiscardBiggestBonus(); break;
                case LOSELEVEL: currentPlayer.levelDown(); break;
                case LOSEARMOR: currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.ARMOR); break;
                case LOSESHOES: currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.SHOES); break;
                case LOSEHAT: currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.HEAD); break;
                case LOSERACE: currentPlayer.getRaceCardFormTableAndDiscard(); break;
                case DUCK: currentPlayer.levelDown(); currentPlayer.levelDown(); break;
                case LOSESMALL: currentPlayer.discardTableOfPlayerOnlyBonusWearSize(BonusWear.Size.SMALL); break;
                case LOSEBIG: currentPlayer.discardTableOfPlayerOnlyBonusWearSize(BonusWear.Size.BIG); break;
                case LOSERACEORLEVEL: if(currentPlayer.playerRace != Player.PlayerRace.HUMAN){
                    currentPlayer.getRaceCardFormTableAndDiscard();
                }
                else {
                    currentPlayer.levelDown();
                }
                    break;
                case LOSERACETOHUMAN: if(currentPlayer.playerRace != Player.PlayerRace.HUMAN){
                    currentPlayer.getRaceCardFormTableAndDiscard();
                }
                    break;
                default: //TODO: error ausgeben
                    break;
            }
        }
    }
}
