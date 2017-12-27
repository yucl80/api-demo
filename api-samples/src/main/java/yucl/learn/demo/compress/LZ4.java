package yucl.learn.demo.compress;


import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class LZ4 implements CompressUtil {
    static LZ4Factory lz4= LZ4Factory.fastestInstance();
    static  LZ4FastDecompressor decompressor = lz4.fastDecompressor();
    static LZ4Compressor fastCompressor=lz4.fastCompressor();

    @Override
    public byte[] compress(byte[] data) {
        if(data==null||data.length==0)
        {
            return new byte[0];
        }
        /*try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            LZ4FrameOutputStream compressorOutputStream = new LZ4FrameOutputStream (byteArrayOutputStream);
            compressorOutputStream.write(data);
            compressorOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        return   fastCompressor.compress(data);
    }

    @Override
    public byte[] decompress(byte[] data) {
        try {

            byte[] restored = new byte[1024*1024];

           // int maxCompressedLength = LZ4Factory.nativeInstance().highCompressor().maxCompressedLength(decompressedLength);
            return  decompressor.decompress(data,335);
            /*ByteArrayInputStream in = new ByteArrayInputStream(data);
            LZ4FrameInputStream compressorInputStream = new LZ4FrameInputStream (in);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int b = compressorInputStream.read();
            while (b != -1) {
                byteArrayOutputStream.write(b);
                b = compressorInputStream.read();
            }
            compressorInputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
