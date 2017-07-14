package io.octet.android.octetsdk.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * A port of the blockies fork found at
 * https://github.com/ethereum/blockies/blob/master/blockies.js
 */
public class Blockies {
    private int[] randseed = new int[4];

    private void seedRandom(String seed) {
        for (int i = 0; i < randseed.length; i++) {
            randseed[i] = 0;
        }

        for (int i = 0; i < seed.length(); i++) {
            char c = seed.charAt(i);
            randseed[i%4] = ((randseed[i%4] << 5) - randseed[i%4]) + c;
        }
    }

    private float rand() {
        int t = randseed[0] ^ (randseed[0] << 11);

        randseed[0] = randseed[1];
        randseed[1] = randseed[2];
        randseed[2] = randseed[3];
        randseed[3] = (randseed[3] ^ (randseed[3] >> 19) ^ t ^ (t >> 8));

        float temp = (float)(randseed[3] >>> 0) / ((1 << 31) >>> 0);
        return temp;
    }

    private float[] createColor() {
        float h = (float) Math.floor(rand() * 360);

        float s = ((rand() * 60f) + 40f);

        float l = ((rand() + rand() + rand() + rand()) * 25f);

        return new float[] { h, s, l };
    }

    private Integer[] createImageData(float size) {
        int width = (int) size;
        int height = (int) size;

        int dataWidth = (int) Math.ceil(width / 2f);
        int mirrorWidth = width - dataWidth;

        ArrayList<Integer> data = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            int[] row = new int[dataWidth];
            for (int x = 0; x < dataWidth; x++) {
                row[x] = (int) Math.floor(rand()*2.3);
            }

            int[] r = new int[mirrorWidth];
            System.arraycopy(row, 0, r, 0, mirrorWidth);

            //reverse r
            for (int i = 0; i < r.length / 2; i++) {
                int temp = r[i];
                r[i] = r[r.length - i - 1];
                r[r.length - i - 1] = temp;
            }

            //concat r
            int[] newRow = new int[row.length + r.length];
            System.arraycopy(row, 0, newRow, 0, row.length);
            System.arraycopy(r, 0, newRow, row.length, r.length);

            row = newRow;

            for (int aRow : row) {
                data.add(aRow);
            }
        }

        return data.toArray(new Integer[data.size()]);
    }

    private Bitmap renderIcon(Options options) {
        if (options == null)
            options = new Options();

        seedRandom(options.getSeed());
        options.updateColors();

        Integer[] imageData = createImageData(options.size);
        int width = (int) Math.sqrt(imageData.length);

        int w, h;
        w = h = (int) (options.size * options.scale);

        Bitmap bmp = Bitmap.createBitmap(w, h, options.config);
        Canvas canvas = new Canvas(bmp);
        Paint myPaint = new Paint();

        int bgColor = Color.HSVToColor(options.backgroundColor);
        canvas.drawColor(bgColor);
        myPaint.setColor(bgColor);
        canvas.drawRect(0f, 0f, (float)w, (float)h, myPaint);

        int color = Color.HSVToColor(options.color);
        int spotColor = Color.HSVToColor(options.spotColor);

        canvas.drawColor(color);
        myPaint.setColor(color);

        for (int i = 0; i < imageData.length; i++) {
            int pixel = imageData[i].intValue();

            if (pixel > 0) {
                int row = (int) Math.floor(i / width);
                int col = i % width;

                int drawColor = pixel == 1 ? color : spotColor;

                canvas.drawColor(drawColor);
                myPaint.setColor(drawColor);

                canvas.drawRect(col * options.scale, row * options.scale, options.scale, options.scale, myPaint);
            }
        }

        return bmp;
    }

    public Bitmap createIcon(String address) {
        Options o = new Options();
        o.seed = address;

        return createIcon(o);
    }

    public Bitmap createIcon(String address, int size) {
        Options o = new Options();
        o.seed = address;
        o.size = size;

        return createIcon(o);
    }

    public Bitmap createIcon(String address, int size, int scale) {
        Options o = new Options();
        o.seed = address;
        o.size = size;
        o.scale = scale;

        return createIcon(o);
    }

    public Bitmap createIcon(Options options) {
        return renderIcon(options);
    }

    public Bitmap createIcon() {
        return createIcon(new Options());
    }

    public class Options {
        private float size = 8;
        private float scale = 4;
        private String seed = Long.toHexString(
                Double.doubleToRawLongBits(
                    Math.floor((Math.random()*Math.pow(10,16)))
                )
        );
        private float[] color = createColor();
        private float[] backgroundColor = createColor();
        private float[] spotColor = createColor();
        private Bitmap.Config config = Bitmap.Config.ARGB_8888;

        public Options() { }

        public Options(float size) {
            this.size = size;
        }

        public Options(float size, float scale) {
            this.size = size;
            this.scale = scale;
        }

        public Options(float size, float scale, String seed) {
            this.size = size;
            this.scale = scale;
            this.seed = seed;
        }

        public Options(float size, float scale, String seed,
                       float[] color) {
            this.size = size;
            this.scale = scale;
            this.seed = seed;
            this.color = color;
        }

        public Options(float size, float scale, String seed, float[] color, float[] backgroundColor) {
            this.size = size;
            this.scale = scale;
            this.seed = seed;
            this.color = color;
            this.backgroundColor = backgroundColor;
        }

        public Options(float size, float scale, String seed,
                       float[] color, float[] backgroundColor, float[] spotColor) {
            this.size = size;
            this.scale = scale;
            this.seed = seed;
            this.color = color;
            this.backgroundColor = backgroundColor;
            this.spotColor = spotColor;
        }

        public Options(float size, float scale, String seed,
                       float[] color, float[] backgroundColor, float[] spotColor,
                       Bitmap.Config config) {
            this.size = size;
            this.scale = scale;
            this.seed = seed;
            this.color = color;
            this.backgroundColor = backgroundColor;
            this.spotColor = spotColor;
            this.config = config;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public String getSeed() {
            return seed;
        }

        public void setSeed(String seed) {
            this.seed = seed;
        }

        public float[] getColor() {
            return color;
        }

        public void setColor(float[] color) {
            this.color = color;
        }

        public float[] getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(float[] backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public float[] getSpotColor() {
            return spotColor;
        }

        public void setSpotColor(float[] spotColor) {
            this.spotColor = spotColor;
        }

        public Bitmap.Config getConfig() {
            return config;
        }

        public void setConfig(Bitmap.Config config) {
            this.config = config;
        }

        public void updateColors() {
            color = createColor();
            backgroundColor = createColor();
            spotColor = createColor();
        }
    }
}
