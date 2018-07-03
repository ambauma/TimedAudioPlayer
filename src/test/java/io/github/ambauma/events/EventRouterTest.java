package io.github.ambauma.events;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventRouterTest {

    private EventRouter eventRouter;
    private static final Event EVENT_A = new Event() { };
    private static final Event EVENT_B = new Event() { };
    private static final Event EVENT_C = new Event() { };
    private static final Event EVENT_D = new Event() { };

    class QueryableEventHandler implements EventHandler {

        private int timesCalled;

        @Override
        public void handle(Event event) {
            timesCalled++;
        }

        public int getTimesCalled() {
            return timesCalled;
        }

        public void setTimesCalled(int timesCalled) {
            this.timesCalled = timesCalled;
        }
    }

    private QueryableEventHandler eventAHandler;
    private QueryableEventHandler eventBHandler;

    @Before
    public void setup() {
        eventRouter = new EventRouter();
        eventAHandler = new QueryableEventHandler();
        eventBHandler = new QueryableEventHandler();

        eventRouter.registerHandler(EVENT_A, eventAHandler);
        eventRouter.registerHandler(EVENT_B, eventBHandler);
        eventRouter.registerHandler(EVENT_C, eventAHandler);
        eventRouter.registerHandler(EVENT_C, eventBHandler);
    }

    private void assertEventHandlersHaveNotBeenCalled() {
        assertEquals(0, eventAHandler.getTimesCalled());
        assertEquals(0, eventBHandler.getTimesCalled());
    }

    @Test
    public void testFiringEvent_A() {
        assertEventHandlersHaveNotBeenCalled();
        eventRouter.fire(EVENT_A);
        assertEquals(1, eventAHandler.getTimesCalled());
        assertEquals(0, eventBHandler.getTimesCalled());
    }

    @Test
    public void testFiringEvent_B() {
        assertEventHandlersHaveNotBeenCalled();
        eventRouter.fire(EVENT_B);
        assertEquals(0, eventAHandler.getTimesCalled());
        assertEquals(1, eventBHandler.getTimesCalled());
    }

    @Test
    public void testFiringEventMultipleEvents() {
        assertEventHandlersHaveNotBeenCalled();
        eventRouter.fire(EVENT_C);
        assertEquals(1, eventAHandler.getTimesCalled());
        assertEquals(1, eventBHandler.getTimesCalled());
    }

    @Test
    public void testFireingEventNoHandlers() {
        assertEventHandlersHaveNotBeenCalled();
        eventRouter.fire(EVENT_D);
        assertEquals(0, eventAHandler.getTimesCalled());
        assertEquals(0, eventBHandler.getTimesCalled());
    }

    @Test
    public void testHandlingEvent() {

    }

}
