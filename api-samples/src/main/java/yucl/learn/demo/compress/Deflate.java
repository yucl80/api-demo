package yucl.learn.demo.compress;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Deflate implements CompressUtil {
    @Override
    public byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DeflateCompressorOutputStream compressorOutputStream = new DeflateCompressorOutputStream(byteArrayOutputStream);
            compressorOutputStream.write(data);
            compressorOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decompress(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream  (in);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int b = compressorInputStream.read();
            while (b != -1) {
                byteArrayOutputStream.write(b);
                b = compressorInputStream.read();
            }
            compressorInputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
