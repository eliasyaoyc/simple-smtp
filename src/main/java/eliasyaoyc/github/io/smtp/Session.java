package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.server.SMTPServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.cert.Certificate;

/**
 * {@link Session}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class Session implements MessageContext {

  private static final Logger logger = LoggerFactory.getLogger(Session.class);

  private SMTPServerConfig config;

  /** the last command executed. */
  private CharSequence lastCommand;

  /** Uniquely identifies this session within an extended time period, useful for logging. */
  private String sessionId;

  /** Might exist if the client has successfully authenticated */
  private AuthenticationHandler authenticationHandler;

  /** Might exist if the client is giving us a message */
  private MessageHandler messageHandler;

  /** The module that an in-memory queue where message are stored. */
  private MailTransfer mailTransfer;

  /** Some state information */
  private String helo;

  private boolean hasMailFrom;

  private int recipientCount;
  /**
   * The recipient address in the first accepted RCPT command, but only if there is exactly one such
   * accepted recipient. If there is no accepted recipient yet, or if there are more than one, then
   * this value is null. This information is useful in the construction of the FOR clause of the
   * Received header.
   */
  private String singleRecipient;

  /**
   * If the client told us the size of the message, this is the value. If they didn't, the value
   * will be 0.
   */
  private int declaredMessageSize = 0;

  /** Some more state information */
  private boolean tlsStarted;

  private Certificate[] tlsPeerCertificates;

  /** Netty Channel object. */
  private Channel channel;
  /**
   * Creates the Session object.
   *
   * @param config a link to {@link SMTPServerConfig}
   * @throws IOException
   */
  public Session(SMTPServerConfig config) throws IOException {
    this.config = config;
  }

  /**
   * Returns an identifier of the session which is reasonably unique within an extended time period.
   */
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public SMTPServerConfig getServerConfig() {
    return this.config;
  }

  @Override
  public Channel getChannel() {
    return this.channel;
  }

  /**
   * Return the current message handler
   *
   * @return
   */
  public MessageHandler getMessageHandler() {
    return this.messageHandler;
  }

  public String getHelo() {
    return this.helo;
  }

  public void setHelo(String value) {
    this.helo = value;
  }

  public boolean getHasMailFrom() {
    return this.hasMailFrom;
  }

  public void setHasMailFrom(boolean value) {
    this.hasMailFrom = value;
  }

  public void addRecipient(String recipientAddress) {
    this.recipientCount++;
    this.singleRecipient = this.recipientCount == 1 ? recipientAddress : null;
  }

  public int getRecipientCount() {
    return this.recipientCount;
  }

  /**
   * Returns the first accepted recipient if there is exactly one accepted recipient, otherwise it
   * returns null.
   */
  public String getSingleRecipient() {
    return singleRecipient;
  }

  public boolean isAuthenticated() {
    return this.authenticationHandler != null;
  }

  public AuthenticationHandler getAuthenticationHandler() {
    return this.authenticationHandler;
  }

  /**
   * This is called by the AuthCommand when a session is successfully authenticated. The handler
   * will be an object created by the AuthenticationHandlerFactory.
   */
  public void setAuthenticationHandler(AuthenticationHandler handler) {
    this.authenticationHandler = handler;
  }

  /**
   * Return the maxMessageSize.
   *
   * @return the maxMessageSize
   */
  public int getDeclaredMessageSize() {
    return this.declaredMessageSize;
  }

  /**
   * Set the maxMessageSize.
   *
   * @param declaredMessageSize the size that the client says the message will be
   */
  public void setDeclaredMessageSize(int declaredMessageSize) {
    this.declaredMessageSize = declaredMessageSize;
  }

  /**
   * Reset the SMTP protocol to the initial state, which is the state after a server issues a 220
   * service ready greeting.
   */
  public void resetSMTPProtocol() {
    resetMessageState();
    this.helo = null;
  }

  /**
   * Some state is associated with each particular message (senders, recipients, the message
   * handler). Some state is not; seeing hello, TLS, authentication.
   */
  public void resetMessageState() {
    this.endMessageHandler();
    this.messageHandler = null;
    this.hasMailFrom = false;
    this.recipientCount = 0;
    this.singleRecipient = null;
    this.declaredMessageSize = 0;
  }

  public void startMailTransaction() {
    if (this.messageHandler != null)
      throw new IllegalStateException("Mail transaction is already in progress");
    this.messageHandler = config.getMessageHandlerFactory().create(this);
  }

  /** Safely calls done() on a message hander, if one exists */
  protected void endMessageHandler() {
    if (this.messageHandler != null) {
      try {
        this.messageHandler.done();
      } catch (Throwable ex) {
        logger.error("done() threw exception", ex);
      }
    }
  }

  /**
   * True when the TLS handshake was completed, false otherwise.
   *
   * @return
   */
  public boolean isTLSStarted() {
    return tlsStarted;
  }

  /**
   * Set the TLS handshake was completed.
   *
   * @param tlsStarted true when the TLS handshake was completed, false otherwise.
   */
  public void setTlsStarted(boolean tlsStarted) {
    this.tlsStarted = tlsStarted;
  }

  public void setTlsPeerCertificates(Certificate[] tlsPeerCertificates) {
    this.tlsPeerCertificates = tlsPeerCertificates;
  }

  public Certificate[] getTlsPeerCertificates() {
    return tlsPeerCertificates;
  }

  public void setChannel(ChannelHandlerContext ctx) {
    this.channel = ctx.channel();
  }

  public CharSequence getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(CharSequence lastCommand) {
    this.lastCommand = lastCommand;
  }

  public MailTransfer getMailTransfer() {
    return mailTransfer;
  }

  public void setMailTransfer(MailTransfer mailTransfer) {
    this.mailTransfer = mailTransfer;
  }
}
