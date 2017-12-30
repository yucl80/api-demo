package yucl.learn.demo.seria;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonSeria {
    static {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
    }

    public static <T> byte[] toByteArray(T message) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JsonStream.serialize(message, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static <T> T toObject(byte[] bytes, Class<T> cls) {
        try {
            return (T) JsonIterator.deserialize(bytes, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Object[] toObjects(InputStream inputStream, Class<?> ... cls ) {
        Object[] objs = new Object[cls.length];
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            IOUtils.copy(inputStream,byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<cls.length;i++) {
             objs[i] = (T) JsonIterator.deserialize(byteArrayOutputStream.toByteArray(), cls[i]);
        }
        return objs;
    }
}
