package eliasyaoyc.github.io.smtp.common;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SMTPCommandReply}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
public class SMTPCommandReply {
  private final SMTPReplyStatus replyCode;
  private final List<CharSequence> text;

  public SMTPCommandReply(SMTPReplyStatus statusCode, CharSequence text) {
    replyCode = statusCode;
    this.text = new ArrayList<>();
    this.text.add(text);
  }

  public SMTPCommandReply(SMTPReplyStatus statusCode, List<String> lines) {
    replyCode = statusCode;
    this.text = new ArrayList<>();
    this.text.addAll(lines);
  }

  public SMTPReplyStatus getReplyCode() {
    return replyCode;
  }

  public List<CharSequence> getLines() {
    return this.text;
  }
}
