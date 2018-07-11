package io.github.ambauma.events;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventRouterTest {

  private EventRouter eventRouter;
  private static final Object EVENT_A = new Object() {
  };
  private static final Object EVENT_B = new Object() {
  };
  private static final Object EVENT_C = new Object() {
  };
  private static final Object EVENT_D = new Object() {
  };

  class QueryableEventHandler implements EventHandler {

    private int timesCalled;

    @Override
    public <T> void handle(T event) {
      timesCalled++;
    }

    public int getTimesCalled() {
      return timesCalled;
    }

    public void setTimesCalled(int timesCalled) {
      this.timesCalled = timesCalled;
    }
  }

  class EventAHandler extends QueryableEventHandler {

    @Override
    public <T> void handle(T event) {
      System.out.println(event.getClass().getSimpleName());
      if (EVENT_A.getClass().isInstance(event)) {
        super.handle(event);
      }
    }
  }

  class EventBHandler extends QueryableEventHandler {

    @Override
    public <T> void handle(T event) {
      if (EVENT_B.getClass().isInstance(event)) {
        super.handle(event);
      }
    }
  }

  class EventCHandler extends QueryableEventHandler {

    @Override
    public <T> void handle(T event) {
      if (EVENT_A.getClass().isInstance(event)
          || EVENT_B.getClass().isInstance(event)) {
        super.handle(event);
      }
    }
  }

  class EventDHandler extends QueryableEventHandler {

    @Override
    public <T> void handle(T event) {
      if (EVENT_D.getClass().isInstance(event)) {
        super.handle(event);
      }
    }
  }

  private EventAHandler eventAHandler;
  private EventBHandler eventBHandler;
  private EventCHandler eventCHandler;
  private EventDHandler eventDHandler;

  @Before
  public void setup() {
    eventRouter = new EventRouter();
    eventAHandler = new EventAHandler();
    eventBHandler = new EventBHandler();
    eventCHandler = new EventCHandler();
    eventDHandler = new EventDHandler();
    eventRouter.registerHandlers(eventAHandler, eventBHandler, eventCHandler, eventDHandler);
  }

  private void assertEventHandlersHaveNotBeenCalled() {
    assertEquals(0, eventAHandler.getTimesCalled());
    assertEquals(0, eventBHandler.getTimesCalled());
    assertEquals(0, eventCHandler.getTimesCalled());
    assertEquals(0, eventDHandler.getTimesCalled());
  }

  @Test
  public void testFiringEvent_A() {
    assertEventHandlersHaveNotBeenCalled();
    eventRouter.fire(EVENT_A);
    assertEquals(1, eventAHandler.getTimesCalled());
    assertEquals(0, eventBHandler.getTimesCalled());
    assertEquals(1, eventCHandler.getTimesCalled());
    assertEquals(0, eventDHandler.getTimesCalled());
  }

  @Test
  public void testFiringEvent_B() {
    assertEventHandlersHaveNotBeenCalled();
    eventRouter.fire(EVENT_B);
    assertEquals(0, eventAHandler.getTimesCalled());
    assertEquals(1, eventBHandler.getTimesCalled());
    assertEquals(1, eventCHandler.getTimesCalled());
    assertEquals(0, eventDHandler.getTimesCalled());
  }

  @Test
  public void testFiringEventMultipleEvents() {
    assertEventHandlersHaveNotBeenCalled();
    eventRouter.fire(EVENT_A);
    eventRouter.fire(EVENT_B);
    assertEquals(1, eventAHandler.getTimesCalled());
    assertEquals(1, eventBHandler.getTimesCalled());
    assertEquals(2, eventCHandler.getTimesCalled());
    assertEquals(0, eventDHandler.getTimesCalled());
  }

  @Test
  public void testFiringEventNoHandlers() {
    assertEventHandlersHaveNotBeenCalled();
    eventRouter.fire(EVENT_C);
    assertEquals(0, eventAHandler.getTimesCalled());
    assertEquals(0, eventBHandler.getTimesCalled());
    assertEquals(0, eventCHandler.getTimesCalled());
    assertEquals(0, eventDHandler.getTimesCalled());
  }

  @Test
  public void testFiringOneEventHandler() {
    assertEventHandlersHaveNotBeenCalled();
    eventRouter.fire(EVENT_D);
    assertEquals(0, eventAHandler.getTimesCalled());
    assertEquals(0, eventBHandler.getTimesCalled());
    assertEquals(0, eventCHandler.getTimesCalled());
    assertEquals(1, eventDHandler.getTimesCalled());
  }

}
