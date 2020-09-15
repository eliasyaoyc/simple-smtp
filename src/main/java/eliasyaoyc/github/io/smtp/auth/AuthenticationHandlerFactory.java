package eliasyaoyc.github.io.smtp.auth;

import eliasyaoyc.github.io.smtp.AuthenticationHandler;

import java.util.List;

/**
 * {@link AuthenticationHandlerFactory} The factory interface for creating authentication handlers.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface AuthenticationHandlerFactory {

  /**
   * If your handler supports RFC 2554 at some degree, then it must return all the supported
   * mechanisms here. <br>
   * The order you use to populate the list will be preserved in the output of the EHLO command.
   * <br>
   *
   * @return the supported authentication mechanisms as List, names are in upper case.
   */
  public List<String> getAuthenticationMechanisms();

  /** Create a fresh instance of your handler. */
  public AuthenticationHandler create();
}
