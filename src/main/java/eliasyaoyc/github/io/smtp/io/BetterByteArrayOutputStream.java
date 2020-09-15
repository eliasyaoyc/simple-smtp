package eliasyaoyc.github.io.smtp.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Adds a getInputStream() method which does not need to make a copy of the underlying array.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
class BetterByteArrayOutputStream extends ByteArrayOutputStream {
  public BetterByteArrayOutputStream() {}

  public BetterByteArrayOutputStream(int size) {
    super(size);
  }

  /** Does not make a copy of the internal buffer. */
  public InputStream getInputStream() {
    return new ByteArrayInputStream(this.buf, 0, this.count);
  }
}
