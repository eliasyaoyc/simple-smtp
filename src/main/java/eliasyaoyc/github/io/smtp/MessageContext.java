package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.server.SMTPServerConfig;
import io.netty.channel.Channel;

import java.security.cert.Certificate;

/**
 * {@link MessageContext} Interface which provides context to the message handlers.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface MessageContext {

    /**
     * Return the SMTPServerConfig object.
     *
     * @return
     */
    public SMTPServerConfig getServerConfig();


    public Channel getChannel();

    /**
     * Return the handler instance that was used to authenticate.
     *
     * @return
     */
    public AuthenticationHandler getAuthenticationHandler();

    /**
     * Return the host name or address literal the client supplied in the HELO or EHLO command,
     * or null if neither of these commands were received yet.
     * @return
     */
    public String getHelo();

    /**
     * Returns the identity of the peer which was established as part of the TLS handshake
     * as defined by {@link javax.net.ssl.SSLSession#getPeerCertificates()}.
     * <p/>
     *
     * @return an ordered array of peer certificates, with the peer's own certificate first followed
     *         by any certificate authorities, or null when no such information is available
     * @see javax.net.ssl.SSLSession#getPeerCertificates()
     */
    Certificate[] getTlsPeerCertificates();
}
