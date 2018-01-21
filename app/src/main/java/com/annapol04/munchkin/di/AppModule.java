package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.data.AndroidMessageBook;
import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.FakeMatch;
import com.annapol04.munchkin.engine.MessageBook;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.PlayClientDummy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ViewModelModule.class,
        SignInActivityModule.class,
        MainActivityModule.class,
        PlayDeskActivityModule.class,
})
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    public AppDb provideDb(Application app) {
        return Room.databaseBuilder(app, AppDb.class, "munchkin_app.db").build();
    }

    @Singleton
    @Provides
    public EventDao provideEventDao(AppDb db) {
        return db.eventDao();
    }

    @Singleton
    @Provides
    public PlayClient providePlayClient(/*Application app*/) {
        return new PlayClientDummy();// new GooglePlayClient(app);
    }

    @Singleton
    @Provides
    @Named("myself")
    public Player provideMyself(Application app, Game game, EventRepository repository) {
        Player p = new Player(Integer.MAX_VALUE, game, repository);//new Random().nextLong());//new Player(GoogleSignIn.getLastSignedInAccount(app).getDisplayName()));
        p.rename("Marvin");
        return p;
    }

    @Provides
    @Named("doorDeck")
    public List<Card> providesDoorDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(Card.LEPERCHAUN);
        deck.add(Card.SABBERNDERSCHLEIM);
        deck.add(Card.LAHMERGOBLIN);
        deck.add(Card.HAMMERRATTE);
        deck.add(Card.TOPFPFLANZE);
        deck.add(Card.FLIEGENDEFROESCHE);
        deck.add(Card.GALLERTOKTAEDER);
        deck.add(Card.HUHN);
        return deck;
    }

    @Provides
    @Named("treasureDeck")
    public List<Card> providesTreasureDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(Card.STANGE);
        deck.add(Card.HELM);
        deck.add(Card.LEDERRUESTUNG);
        deck.add(Card.SCHLEIMIGERUESTUNG);
        deck.add(Card.KNIE);
        deck.add(Card.GEILERHELM);
        deck.add(Card.ARSCHTRITTSTIEFEL);
//        deck.add(Card.HELM_OF_COURAGE);
//        deck.add(Card.BROADSWORD);
        return deck;
    }

    @Singleton
    @Provides
    public Match providesMatch(Game game, @Named("myself") Player player, EventRepository repository) {
        return new FakeMatch(game, player, repository);
    }

    @Provides
    public MessageBook providesMessageBook(Application application) {
        return new AndroidMessageBook(application);
    }
}
