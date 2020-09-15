package eliasyaoyc.github.io.smtp.common;

/**
 * {@link SMTPReplyStatus}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
public class SMTPReplyStatus {
  private short status;

  public SMTPReplyStatus(short status) {
    this.setStatus(status);
  }

  public static final SMTPReplyStatus R211 = new SMTPReplyStatus((short) 211);
  public static final SMTPReplyStatus R214 = new SMTPReplyStatus((short) 214);
  public static final SMTPReplyStatus R220 = new SMTPReplyStatus((short) 220);
  public static final SMTPReplyStatus R221 = new SMTPReplyStatus((short) 221);
  public static final SMTPReplyStatus R235 = new SMTPReplyStatus((short) 235);

  public static final SMTPReplyStatus R250 = new SMTPReplyStatus((short) 250);

  public static final SMTPReplyStatus R354 = new SMTPReplyStatus((short) 354);
  public static final SMTPReplyStatus R450 = new SMTPReplyStatus((short) 450);
  public static final SMTPReplyStatus R452 = new SMTPReplyStatus((short) 452);

  public static final SMTPReplyStatus R454 = new SMTPReplyStatus((short) 454);
  public static final SMTPReplyStatus R500 = new SMTPReplyStatus((short) 500);
  public static final SMTPReplyStatus R501 = new SMTPReplyStatus((short) 501);
  public static final SMTPReplyStatus R502 = new SMTPReplyStatus((short) 502);
  public static final SMTPReplyStatus R503 = new SMTPReplyStatus((short) 503);
  public static final SMTPReplyStatus R504 = new SMTPReplyStatus((short) 504);
  public static final SMTPReplyStatus R530 = new SMTPReplyStatus((short) 530);
  public static final SMTPReplyStatus R552 = new SMTPReplyStatus((short) 552);
  public static final SMTPReplyStatus R553 = new SMTPReplyStatus((short) 553);

  public short getStatus() {
    return status;
  }

  public void setStatus(short status) {
    // TODO: check for valid status codes
    this.status = status;
  }
}
