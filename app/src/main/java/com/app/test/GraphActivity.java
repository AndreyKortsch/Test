package com.app.test;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;

public class GraphActivity extends Activity {
    float[] name;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
         name= (float[]) arguments.getFloatArray("answers");
        setContentView(new SurafaceClass(this));


    }
    public class SurafaceClass extends View implements
            SurfaceHolder.Callback {
        Bitmap mBitmap;
        Paint paint =new Paint();
        public SurafaceClass(Context context) {
            super(context);
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a1);
           // TODO Auto-generated constructor stub
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onDraw(Canvas canvas) {
           canvas.drawColor(Color.GREEN);
           paint.setColor(Color.RED);
        // толщина линии = 10
            paint.setStrokeWidth(10);
            canvas.drawBitmap(mBitmap, 0, 500, paint);
            canvas.drawLine(105,1380-90*name[9],170,1420-59*name[10],paint);
            canvas.drawLine(240,1490-35*name[11],170,1420-59*name[10],paint);
            canvas.drawLine(345,1510-44*name[1],420,1540-46*name[2],paint);
            canvas.drawLine(510,1684-42*name[3],420,1540-46*name[2],paint);
            canvas.drawLine(510,1684-42*name[3],585,1765-28*name[4],paint);
            canvas.drawLine(675,1528-67*name[5],585,1765-28*name[4],paint);
            canvas.drawLine(675,1528-67*name[5],760,1068+62*15-62*name[6],paint);
            canvas.drawLine(845,1280+12*44-44*name[7],760,1068+62*15-62*name[6],paint);
            canvas.drawLine(845,1280+12*44-44*name[7],925,1290+65*6-65*name[8],paint);
        }

    }
}
