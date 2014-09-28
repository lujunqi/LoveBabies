package com.xqj.lovebabies.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
	public RoundImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) { // ���������д�ķ����ˣ��뻭ʲô��״�Լ�����
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// ���߿�
		Rect rec = canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		Paint paint = new Paint();
		paint.setColor(Color.GRAY); // ��ɫ
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawRect(rec, paint);
	}

}
