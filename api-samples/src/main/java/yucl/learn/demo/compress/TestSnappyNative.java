package yucl.learn.demo.compress;

import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestSnappyNative implements CompressUtil {
    @Override
    public byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            SnappyOutputStream snappyOutputStream = new SnappyOutputStream(byteArrayOutputStream,data.length);
            snappyOutputStream.write(data);
            snappyOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] decompress(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            SnappyInputStream snappyInputStream = new SnappyInputStream(in);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int b = snappyInputStream.read();
            while (b != -1) {
                byteArrayOutputStream.write(b);
                b = snappyInputStream.read();
            }
            snappyInputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected int compressBlock(byte[] uncompressed, byte[] compressBuffer) throws IOException
    {
        return Snappy.compress(uncompressed, 0, uncompressed.length, compressBuffer, 0);
    }

    protected int uncompressBlock(byte[] compressed, byte[] uncompressBuffer) throws IOException
    {
        return Snappy.uncompress(compressed, 0, compressed.length, uncompressBuffer, 0);
    }
}
