package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.data.EventRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ExecutorTest {
    private Game game;
    private EventRepository repository;
    private Executor executor;

    @Before
    public void setUp() {
        game = Mockito.mock(Game.class);
        repository = Mockito.mock(EventRepository.class);
        executor = new Executor(game, repository);
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
