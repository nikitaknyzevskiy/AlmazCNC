package com.rokobit.almaz.psd.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.rokobit.almaz.psd.parser.layer.Channel;

import java.nio.IntBuffer;
import java.util.List;

public class BufferedImageBuilder {

    private final List<Channel> channels;
    private final byte[][] uncompressedChannels;
    private final int width;
    private final int height;
    private int opacity = -1;

    public BufferedImageBuilder(List<Channel> channels, int width, int height) {
        this.uncompressedChannels = null;
        this.channels = channels;
        this.width = width;
        this.height = height;
    }

    public BufferedImageBuilder(byte[][] channels, int width, int height) {
        this.uncompressedChannels = channels;
        this.channels = null;
        this.width = width;
        this.height = height;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public Bitmap makeImage() {
        if (width == 0 || height == 0) {
            return null;
        }
        byte[] aChannel = getChannelData(Channel.ALPHA);
        byte[] rChannel = getChannelData(Channel.RED);
        byte[] gChannel = getChannelData(Channel.GREEN);
        byte[] bChannel = getChannelData(Channel.BLUE);
//        applyOpacity(aChannel);

        Bitmap bitmap = null;
        try {
            bitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e){
            Log.e("Error", "BufferedImageBuilder");
        }

        if (bitmap == null) return null;

        int[] data = new int[width * height];
        int n = width * height - 1;
        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        while (n >= 0) {
            a = aChannel[n] & 0xff;
            r = rChannel[n] & 0xff;
            g = gChannel[n] & 0xff;
            b = bChannel[n] & 0xff;
            data[n] = a << 24 | r << 16 | g << 8 | b;
            n--;
        }
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(data));
        return bitmap;
    }

    private void applyOpacity(byte[] a) {
        Log.d("opacity", "opacity" + a);
        if (opacity != -1) {
            double o = (opacity & 0xff) / 256.0;
            for (int i = 0; i < a.length; i++) {
                a[i] = (byte) ((a[i] & 0xff) * o);
            }
        }
    }

    private byte[] getChannelData(int channelId) {
        if (uncompressedChannels == null) {
            for (Channel c : channels) {
                if (channelId == c.getId() && c.getCompressedData() != null) {
                    ChannelUncompressor uncompressor = new ChannelUncompressor();
                    byte[] uncompressedChannel = uncompressor.uncompress(c.getCompressedData(), width, height);
                    if (uncompressedChannel != null) {
                        return uncompressedChannel;
                    }
                }
            }
        } else {
            if (channelId >= 0 && uncompressedChannels[channelId] != null) {
                return uncompressedChannels[channelId];
            }
        }
        return fillBytes(width * height, (byte) (channelId == Channel.ALPHA ? 255 : 0));
    }

    private byte[] fillBytes(int size, byte value) {
        byte[] result = new byte[size];
        if (value != 0) {
            for (int i = 0; i < size; i++) {
                result[i] = value;
            }
        }
        return result;
    }

}
