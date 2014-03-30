package fractal.games.swipe.sorin.petre.nica.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class TimerView extends AutoUpdatableView {

    private final Integer width;

    private final Integer height;

    private Long          minutes;

    private Long          seconds;

    private Long          elapsedTime = 0L;

    private final Long    startTime   = System.currentTimeMillis();

    private Boolean       isTimerOn   = false;

    private Paint         paint;

    public TimerView(Context context, Integer size) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(size);
        width = size * 3;
        height = (size * 84) / 100;
    }

    public TimerView(Context context) {
        this(context, 30);
    }

    @Override
    protected Runnable getBehavior() {
        return new Runnable() {
            @Override
            public void run() {
                isTimerOn = true;
                while (isTimerOn) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    minutes = elapsedTime / 60000;
                    seconds = (elapsedTime % 60000) / 1000;
                    drawSurface();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isTimerOn = false;
        super.surfaceDestroyed(holder);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSurface(canvas);
    }

    @Override
    protected void drawSurface(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        canvas.drawText(evaluateDispalyString(), 0, (height * 92) / 100, paint);
    }

    private String evaluateDispalyString() {
        String displayString = (minutes < 10 ? "0" : "") + minutes + " : " + (seconds < 10 ? "0" : "") + seconds;
        return displayString;
    }

}
