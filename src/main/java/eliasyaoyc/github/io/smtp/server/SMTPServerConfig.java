package eliasyaoyc.github.io.smtp.server;

import eliasyaoyc.github.io.smtp.CommandHandler;
import eliasyaoyc.github.io.smtp.Config;
import eliasyaoyc.github.io.smtp.MessageHandlerFactory;
import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.auth.AuthenticationHandlerFactory;
import io.netty.util.AttributeKey;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * {@link SMTPServerConfig} SMTP server related configuration.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class SMTPServerConfig extends Config {

  public static final AttributeKey attrName = AttributeKey.valueOf("SMTPSession");
  private static final String UNKNOWN_HOSTNAME = "localhost";

  private AuthenticationHandlerFactory authenticationHandlerFactory;
  private int maxConnectionPerIp = 10;
  private int timeoutSeconds = 10;
  public static final int MAX_LINE_LENGTH = 8192;
  private Session session;
  private MessageHandlerFactory messageHandlerFactory;
  private String hostName;
  private int port = 25;

  /**
   * True if this SMTPServer was started. It remains true even if the SMTPServer has been stopped
   * since.
   */
  private boolean started = false;

  /** If true, TLS is enabled */
  private boolean enableTLS = false;
  /** If true, TLS is not announced; ignored if enableTLS=false */
  private boolean hideTLS = false;
  /** If true, a TLS handshake is required; ignored if enableTLS=false */
  private boolean requireTLS = false;

  /** If true, no Received headers will be inserted */
  private boolean disableReceivedHeaders = false;

  /**
   * set a hard limit on the maximum number of connections this server will accept once we reach
   * this limit, the server will gracefully reject new connections. Default is 1000.
   */
  private int maxConnections = 1000;

  /** The timeout for waiting for data on a connection is one minute: 1000 * 60 * 1 */
  private int connectionTimeout = 1000 * 60 * 1;

  /** The maximal number of recipients that this server accepts per message delivery request. */
  private int maxRecipients = 1000;

  /**
   * The maximum size of a message that the server will accept. This value is advertised during the
   * EHLO phase if it is larger than 0. If the message size specified by the client during the MAIL
   * phase, the message will be rejected at that time. (RFC 1870) Default is 0. Note this doesn't
   * actually enforce any limits on the message being read; you must do that yourself when reading
   * data.
   */
  private int maxMessageSize = 0;

  private String softwareName = "MixMicro SMTP Server";

  private CommandHandler commandHandler;

  /** Simple constructor. */
  public SMTPServerConfig(MessageHandlerFactory handlerFactory) {
    this(handlerFactory, null);
  }

  /**
   * Complex constructor.
   *
   * @param authHandlerFact the {@link AuthenticationHandlerFactory} which performs authentication
   *     in the SMTP AUTH command. If null, authentication is not supported. Note that setting an
   *     authentication handler does not enforce authentication, it only makes authentication
   *     possible. Enforcing authentication is the responsibility of the client application, which
   *     usually enforces it only selectively. Use {@link Session#isAuthenticated} to check whether
   *     the client was authenticated in the session.
   */
  public SMTPServerConfig(
      MessageHandlerFactory msgHandlerFact, AuthenticationHandlerFactory authHandlerFact) {
    this.messageHandlerFactory = msgHandlerFact;
    this.authenticationHandlerFactory = authHandlerFact;

    try {
      this.hostName = InetAddress.getLocalHost().getCanonicalHostName();
    } catch (UnknownHostException e) {
      this.hostName = UNKNOWN_HOSTNAME;
    }

    this.commandHandler = new CommandHandler();
  }

  public static String getUnknownHostname() {
    return UNKNOWN_HOSTNAME;
  }

  /**
   * Return the factory for auth handlers, or null if no such factory has been set.
   *
   * @return the factory for auth handlers, or null if no such factory has been set.
   */
  public AuthenticationHandlerFactory getAuthenticationHandlerFactory() {
    return authenticationHandlerFactory;
  }

  public void setAuthenticationHandlerFactory(
      AuthenticationHandlerFactory authenticationHandlerFactory) {
    this.authenticationHandlerFactory = authenticationHandlerFactory;
  }

  public int getMaxConnectionPerIp() {
    return maxConnectionPerIp;
  }

  public void setMaxConnectionPerIp(int maxConnectionPerIp) {
    this.maxConnectionPerIp = maxConnectionPerIp;
  }

  public int getTimeoutSeconds() {
    return timeoutSeconds;
  }

  public void setTimeoutSeconds(int timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
  }

  public static int getMaxLineLength() {
    return MAX_LINE_LENGTH;
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public MessageHandlerFactory getMessageHandlerFactory() {
    return messageHandlerFactory;
  }

  public void setMessageHandlerFactory(MessageHandlerFactory messageHandlerFactory) {
    this.messageHandlerFactory = messageHandlerFactory;
  }

  /**
   * Return the host name that will be reported to SMTP clients.
   *
   * @return the host name that will be reported to SMTP clients.
   */
  public String getHostName() {
    if (this.hostName == null) {
      return UNKNOWN_HOSTNAME;
    } else {
      return this.hostName;
    }
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isEnableTLS() {
    return enableTLS;
  }

  public void setEnableTLS(boolean enableTLS) {
    this.enableTLS = enableTLS;
  }

  public boolean isHideTLS() {
    return hideTLS;
  }

  public void setHideTLS(boolean hideTLS) {
    this.hideTLS = hideTLS;
  }

  public boolean isRequireTLS() {
    return requireTLS;
  }

  public void setRequireTLS(boolean requireTLS) {
    this.requireTLS = requireTLS;
  }

  public boolean isDisableReceivedHeaders() {
    return disableReceivedHeaders;
  }

  public void setDisableReceivedHeaders(boolean disableReceivedHeaders) {
    this.disableReceivedHeaders = disableReceivedHeaders;
  }

  /**
   * Return the maxMessageSize.
   *
   * @return the maxMessageSize.
   */
  public int getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getMaxRecipients() {
    return maxRecipients;
  }

  public void setMaxRecipients(int maxRecipients) {
    this.maxRecipients = maxRecipients;
  }

  public int getMaxMessageSize() {
    return maxMessageSize;
  }

  public void setMaxMessageSize(int maxMessageSize) {
    this.maxMessageSize = maxMessageSize;
  }

  /**
   * The string reported to the public as the software running here. Defaults to SMTP server and the
   * version number.
   *
   * @return software name.
   */
  public String getSoftwareName() {
    return softwareName;
  }

  /**
   * Changes the publicly reported software information.
   *
   * @param softwareName
   */
  public void setSoftwareName(String softwareName) {
    this.softwareName = softwareName;
  }

  public CommandHandler getCommandHandler() {
    return commandHandler;
  }

  public void setCommandHandler(CommandHandler commandHandler) {
    this.commandHandler = commandHandler;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
