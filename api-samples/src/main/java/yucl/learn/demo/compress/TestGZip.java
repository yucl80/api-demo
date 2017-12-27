package yucl.learn.demo.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TestGZip implements CompressUtil{
    public  byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            GZIPOutputStream gzout = new GZIPOutputStream(o);
            gzout.write(data);
            gzout.close();
            return o.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  byte[] decompress(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            GZIPInputStream gzin = new GZIPInputStream(in);
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            int b = gzin.read();
            while (b != -1) {
                o.write(b);
                b = gzin.read();
            }
            gzin.close();
            o.close();
            return o.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
