package fractal.games.swipe.sorin.petre.nica.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class AutoUpdatableView extends SurfaceView implements SurfaceHolder.Callback {

    protected final String logTag;

    private Thread         thread;

    public AutoUpdatableView(Context context) {
        super(context);
        getHolder().addCallback(this);
        logTag = getClass().getSimpleName();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(logTag, "View background thread started.");
        thread = new Thread(getBehavior());
        thread.setName(logTag);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void drawSurface() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            drawSurface(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected abstract Runnable getBehavior();

    protected abstract void drawSurface(Canvas canvas);

}
