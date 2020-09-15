package eliasyaoyc.github.io.smtp;

/**
 * {@link MessageHandlerFactory}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface MessageHandlerFactory {

    public MessageHandler create(MessageContext ctx);
}
