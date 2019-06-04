package com.syswin.temail.usermail.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HashUtils {

  /**
   * MurMurHash算法，是非加密HASH算法，性能很高， 比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
   * 等HASH算法要快很多，而且据说这个算法的碰撞率很低. http://murmurhash.googlepages.com/
   */
  public static Long hash(String key) {
    ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
    int seed = 0x1234ABCD;
    ByteOrder byteOrder = buf.order();
    buf.order(ByteOrder.LITTLE_ENDIAN);
    long m = 0xc6a4a7935bd1e995L;
    int r = 47;
    long h = seed ^ (buf.remaining() * m);
    long k;
    while (buf.remaining() >= 8) {
      k = buf.getLong();
      k *= m;
      k ^= k >>> r;
      k *= m;
      h ^= k;
      h *= m;
    }
    if (buf.remaining() > 0) {
      ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
      finish.put(buf).rewind();
      h ^= finish.getLong();
      h *= m;
    }
    h ^= h >>> r;
    h *= m;
    h ^= h >>> r;
    buf.order(byteOrder);
    return h;
  }

  public static long hash64(final byte[] data, int length, int seed) {
    final long m = 0xc6a4a7935bd1e995L;
    final int r = 47;

    long h = (seed & 0xffffffffl) ^ (length * m);

    int length8 = length / 8;

    for (int i = 0; i < length8; i++) {
      final int i8 = i * 8;
      long k = ((long) data[i8 + 0] & 0xff) + (((long) data[i8 + 1] & 0xff) << 8)
          + (((long) data[i8 + 2] & 0xff) << 16) + (((long) data[i8 + 3] & 0xff) << 24)
          + (((long) data[i8 + 4] & 0xff) << 32) + (((long) data[i8 + 5] & 0xff) << 40)
          + (((long) data[i8 + 6] & 0xff) << 48) + (((long) data[i8 + 7] & 0xff) << 56);

      k *= m;
      k ^= k >>> r;
      k *= m;

      h ^= k;
      h *= m;
    }

    switch (length % 8) {
      case 7:
        h ^= (long) (data[(length & ~7) + 6] & 0xff) << 48;
      case 6:
        h ^= (long) (data[(length & ~7) + 5] & 0xff) << 40;
      case 5:
        h ^= (long) (data[(length & ~7) + 4] & 0xff) << 32;
      case 4:
        h ^= (long) (data[(length & ~7) + 3] & 0xff) << 24;
      case 3:
        h ^= (long) (data[(length & ~7) + 2] & 0xff) << 16;
      case 2:
        h ^= (long) (data[(length & ~7) + 1] & 0xff) << 8;
      case 1:
        h ^= (long) (data[length & ~7] & 0xff);
        h *= m;
    }

    h ^= h >>> r;
    h *= m;
    h ^= h >>> r;

    return h;
  }

  /**
   * Generates 64 bit hash from byte array with default seed value.
   *
   * @param data byte array to hash
   * @param length length of the array to hash
   * @return 64 bit hash of the given string
   */
  public static long hash64(final byte[] data, int length) {
    return hash64(data, length, 0xe17a1465);
  }

  /**
   * Generates 64 bit hash from a string.
   *
   * @param text string to hash
   * @return 64 bit hash of the given string
   */
  public static long hash64(final String text) {
    final byte[] bytes = text.getBytes();
    return hash64(bytes, bytes.length);
  }

  /**
   * Generates 64 bit hash from a substring.
   *
   * @param text string to hash
   * @param from starting index
   * @param length length of the substring to hash
   * @return 64 bit hash of the given array
   */
  public static long hash64(final String text, int from, int length) {
    return hash64(text.substring(from, from + length));
  }
}
