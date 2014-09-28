package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ContactsSideBar extends View {
	public static String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private TextView mTextDialog;
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public ContactsSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ContactsSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContactsSideBar(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		try {
			int canvas_width = getWidth();
			int canvas_height = getHeight();
			int letter_height = canvas_height / letters.length;

			for (int i = 0; i < letters.length; i++) {
				paint.setColor(Color.rgb(33, 65, 98));
				// paint.setColor(Color.CYAN);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				paint.setAntiAlias(true);
				paint.setTextSize(20);
				// 选中的状态
				if (i == choose) {
					paint.setColor(Color.parseColor("#3399ff"));
					paint.setFakeBoldText(true);
				}
				// x坐标等于中间-字符串宽度的一半.
				float xPos = canvas_width / 2 - paint.measureText(letters[i]) / 2;
				float yPos = letter_height * i + letter_height;
				canvas.drawText(letters[i], xPos, yPos, paint);
				paint.reset();// 重置画笔
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * letters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.shape_widget_contacts_side_bar_bg);
			if (oldChoose != c) {
				if (c >= 0 && c < letters.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(letters[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(letters[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}
