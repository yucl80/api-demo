package yucl.learn.demo.compress;

import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class TestSnappy implements CompressUtil{
    public  byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            SnappyCompressorOutputStream compressorOutputStream = new SnappyCompressorOutputStream(byteArrayOutputStream,data.length);
            compressorOutputStream.write(data);
            compressorOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  byte[] decompress(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            SnappyCompressorInputStream compressorInputStream = new SnappyCompressorInputStream(in);
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
