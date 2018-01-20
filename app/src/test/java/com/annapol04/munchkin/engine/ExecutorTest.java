package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.data.EventRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExecutorTest {
    private Match match;
    private Game game;
    private EventRepository repository;
    private Executor executor;

    @Before
    public void setUp() {
        match = Mockito.mock(Match.class);
        game = Mockito.mock(Game.class);
        repository = Mockito.mock(EventRepository.class);
        executor = new Executor(match, game, repository);
    }

    @Test
    public void handlesPlayers() throws Exception {
     //   executor.onNewEvent(new Event(Scope.GAME, Action.JOIN_PLAYER,0, "Falco"));
/*
        ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
        verify(game).join(argument.capture());

        assertEquals(1, argument.getAllValues().size());
        assertEquals("Falco", argument.getValue().getName());*/
    }
}
