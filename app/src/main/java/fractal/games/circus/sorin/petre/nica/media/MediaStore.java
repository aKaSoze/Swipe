package fractal.games.circus.sorin.petre.nica.media;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class MediaStore {

    private interface ResourceLoader<R> {
        R loadResource();
    }

    public static Context      context;
    public static AssetManager assetManager;

    private static final Map<String, Object> resources = new HashMap<String, Object>();

    public static Bitmap getBitmap(final Integer bitmapResourceId) {
        return loadResource("bitmap_" + bitmapResourceId, new ResourceLoader<Bitmap>() {
            @Override
            public Bitmap loadResource() {
                return BitmapFactory.decodeResource(context.getResources(), bitmapResourceId);
            }
        });
    }

    public static Bitmap getScaledBitmap(final Integer bitmapResourceId, final Integer width, final Integer height) {
        return loadResource("bitmap_" + bitmapResourceId + "_" + width + "x" + height, new ResourceLoader<Bitmap>() {
            @Override
            public Bitmap loadResource() {
                return Bitmap.createScaledBitmap(getBitmap(bitmapResourceId), width, height, true);
            }
        });
    }

    public static MediaPlayer getSound(final Integer soundId) {
        return loadResource("sound_" + soundId, new ResourceLoader<MediaPlayer>() {
            @Override
            public MediaPlayer loadResource() {
                return MediaPlayer.create(context, soundId);
            }
        });
    }

    public static Typeface getTypeFace(final String typeFaceName) {
        return loadResource("font_" + typeFaceName, new ResourceLoader<Typeface>() {
            @Override
            public Typeface loadResource() {
                return Typeface.createFromAsset(assetManager, typeFaceName);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <R> R loadResource(String resourceId, ResourceLoader<R> resourceLoader) {
        if (!resources.containsKey(resourceId)) {
            resources.put(resourceId, resourceLoader.loadResource());
        }
        return (R) resources.get(resourceId);
    }
}
