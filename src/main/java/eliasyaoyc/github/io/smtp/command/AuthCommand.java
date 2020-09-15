package eliasyaoyc.github.io.smtp.command;

import eliasyaoyc.github.io.smtp.AuthenticationHandler;
import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.auth.AuthenticationHandlerFactory;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.exception.RejectException;
import eliasyaoyc.github.io.smtp.utils.CRLFTerminatedReader;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link AuthCommand}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class AuthCommand extends AbstractCommand {
  public static final String VERB = "AUTH";
  public static final String AUTH_CANCEL_COMMAND = "*";

  /** Creates a new instance of AuthCommand */
  public AuthCommand() {
    super(
        VERB,
        "Authentication service",
        VERB
            + " <mechanism> [initial-response] \n"
            + "\t mechanism = a string identifying a SASL authentication mechanism,\n"
            + "\t an optional base64-encoded response");
  }

  /** */
  @Override
  public SMTPCommandReply execute(String commandString, Session session) throws IOException {
    if (session.isAuthenticated()) {
      return new SMTPCommandReply(SMTPReplyStatus.R503, "Refusing any other AUTH command.");
    }

    AuthenticationHandlerFactory authFactory =
        session.getServerConfig().getAuthenticationHandlerFactory();

    if (authFactory == null) {
      return new SMTPCommandReply(SMTPReplyStatus.R502, "Authentication not supported.");
    }

    AuthenticationHandler authHandler = authFactory.create();

    String[] args = this.getArgs(commandString);
    // Let's check the command syntax
    if (args.length < 2) {
      return new SMTPCommandReply(
          SMTPReplyStatus.R501, "Syntax : " + VERB + " mechanism [initial-response]");
    }

    // Let's check if we support the required authentication mechanism
    String mechanism = args[1];
    if (!authFactory
        .getAuthenticationMechanisms()
        .contains(mechanism.toUpperCase(Locale.ENGLISH))) {
      return new SMTPCommandReply(
          SMTPReplyStatus.R504, "The requested authentication mechanism is not supported.");
    }
    // OK, let's go trough the authentication process.
    try {
      // The authentication process may require a series of challenge-responses
//      CRLFTerminatedReader reader = session.getReader();
      CRLFTerminatedReader reader = null;

      SMTPCommandReply response = authHandler.auth(commandString);
      if (response != null) {
        // challenge-response iteration
        return response;
      }

      while (response != null) {
        String clientInput = reader.readLine();
        if (clientInput.trim().equals(AUTH_CANCEL_COMMAND)) {
          // RFC 2554 explicitly states this:
          return new SMTPCommandReply(SMTPReplyStatus.R501, "Authentication canceled by client.");
        } else {
          response = authHandler.auth(clientInput);
          if (response != null) {
            // challenge-response iteration
            return response;
          }
        }
      }
      session.setAuthenticationHandler(authHandler);
      return new SMTPCommandReply(SMTPReplyStatus.R235, "Authentication successful.");
    } catch (RejectException authFailed) {
      return new SMTPCommandReply(SMTPReplyStatus.R502, authFailed.getErrorResponse());
    }
  }
}
