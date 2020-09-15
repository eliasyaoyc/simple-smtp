package eliasyaoyc.github.io.smtp.listener;

import eliasyaoyc.github.io.smtp.MessageContext;
import eliasyaoyc.github.io.smtp.MessageHandler;
import eliasyaoyc.github.io.smtp.MessageHandlerFactory;
import eliasyaoyc.github.io.smtp.command.DataCommand;
import eliasyaoyc.github.io.smtp.exception.RejectException;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link SimpleMessageListenerAdapter} MessageHandlerFactory implementation which adapter to a
 * collection of MessageListeners.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class SimpleMessageListenerAdapter implements MessageHandlerFactory {

  /**
   * 5 megs by default. The server will buffer incoming messages to disk when they hit this limit in
   * the {@link DataCommand}
   */
  private static int DEFAULT_DATA_DEFERRED_SIZE = 1024 * 1024 * 5;

  private Collection<SimpleMessageListener> listeners;
  private int dataDeferredSize;

  /**
   * Initializes this factory with a single listener.
   *
   * <p>Default data deferred size is 5 megs.
   *
   * @param listeners
   */
  public SimpleMessageListenerAdapter(SimpleMessageListener listeners) {
    this(Collections.singleton(listeners), DEFAULT_DATA_DEFERRED_SIZE);
  }

  /**
   * Initializers this factory with a collections listeners.
   *
   * <p>Default data deferred size is 5 megs.
   *
   * @param listeners
   */
  public SimpleMessageListenerAdapter(Collection<SimpleMessageListener> listeners) {
    this(listeners, DEFAULT_DATA_DEFERRED_SIZE);
  }

  /**
   * Initializers this factory witch a collections listeners and dataDeferredSize.
   *
   * @param listeners
   * @param dataDeferredSize the server will buffer incoming messages to disk when they hit this
   *     limit in the DATA received.
   */
  public SimpleMessageListenerAdapter(
      Collection<SimpleMessageListener> listeners, int dataDeferredSize) {
    this.listeners = listeners;
    this.dataDeferredSize = dataDeferredSize;
  }

  @Override
  public MessageHandler create(MessageContext ctx) {
    return new Handler(ctx);
  }

  /** Needed by this class to track which listeners and delivery. */
  static class Delivery {

    SimpleMessageListener listener;
    String recipient;

    public Delivery(SimpleMessageListener listener, String recipient) {
      this.listener = listener;
      this.recipient = recipient;
    }

    public String getRecipient() {
      return recipient;
    }

    public SimpleMessageListener getListener() {
      return listener;
    }
  }

  /** Class which implements the actual handler interface. */
  class Handler implements MessageHandler {
    MessageContext ctx;
    String from;
    List<Delivery> deliveries = new ArrayList<>();

    public Handler(MessageContext ctx) {
      this.ctx = ctx;
    }

    public void from(String from) throws RejectException {
      this.from = from;
    }

    public void recipient(String recipient) throws RejectException {
      boolean addedListener = false;

      for (SimpleMessageListener listener : SimpleMessageListenerAdapter.this.listeners) {
        if (listener.accept(this.from, recipient)) {
          this.deliveries.add(new Delivery(listener, recipient));
          addedListener = true;
        }
      }
      if (!addedListener) {
        throw new RejectException(553, "<" + recipient + "> address unknown");
      }
    }

    public void data(MimeMessage data) throws IOException, MessagingException {
      if (!CollectionUtils.isEmpty(this.deliveries)) {
        for (Delivery delivery : this.deliveries) {
          delivery.getListener().deliver(this.from, delivery.getRecipient(), data);
        }
      }
    }

    public void done() {}
  }
}
