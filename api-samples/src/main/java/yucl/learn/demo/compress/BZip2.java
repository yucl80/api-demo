package yucl.learn.demo.compress;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BZip2 implements CompressUtil {
    @Override
    public byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BZip2CompressorOutputStream outputStream = new BZip2CompressorOutputStream (byteArrayOutputStream);
            outputStream.write(data);
            outputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decompress(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            BZip2CompressorInputStream inputStream = new BZip2CompressorInputStream (in);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int b = inputStream.read();
            while (b != -1) {
                byteArrayOutputStream.write(b);
                b = inputStream.read();
            }
            inputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
