package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.di.AppComponent;
import com.annapol04.munchkin.di.AppModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static org.junit.Assert.assertEquals;

/**
 * Created by chris_000 on 08.12.2017.
 */

public class EventExecuterTest {
    private Game game;

    @Rule
    public DaggerMockRule<AppComponent> mockitoRule =
            new DaggerMockRule<>(AppComponent.class, new AppModule());

    @Mock
    EventRepository repository;

    @InjectFromComponent
    EventExecuter executer;

    @Test
    public void handlesPlayers() throws Exception {
        assertEquals(1, 2);
    }

    @Before
    public void init() {
        game = new Game();
    }
}
