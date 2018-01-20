package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.data.EventRepository;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.junit.JUnitRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MatchTest {
    private Match match;
    private Player myself = new Player(Integer.MAX_VALUE);
    private EventRepository repository;
    private Executor executor;

    @Before
    public void setUp() {
        repository = Mockito.mock(EventRepository.class);
        match = new Match(myself, repository);
    }

    @Test
    public void joinAsFirstPlayer() {
        match.start(6);

        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(repository).push(argument.capture());

        Event event = argument.getValue();


        Assert.assertEquals(Action.JOIN_PLAYER, event.getAction());
        Assert.assertEquals(myself, match.getCurrentPlayer().getValue());

        Robolectric.flushForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
    }

    @Test
    public void joinMorePlayers() {
        match.start(6);

//        Assert.assertEquals(6, match.getPlayers().getValue().size());
    }
}
