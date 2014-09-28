package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Scroller;

public class SwipeListView extends ListView {
    private Boolean mIsHorizontal;

    private View mPreItemView;

    private View mCurrentItemView;

    private float mFirstX;

    private float mFirstY;

    private int mRightViewWidth;

    // private boolean mIsInAnimation = false;
    private final int mDuration = 100;

    private final int mDurationStep = 10;

    private boolean mIsShown;
    
//    private Scroller scroller;

    public SwipeListView(Context context) {
        this(context,null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,  
                R.styleable.swipelistviewstyle);  
        
//        scroller = new Scroller(context);
      //��ȡ�Զ������Ժ�Ĭ��ֵ  
      mRightViewWidth = (int) mTypedArray.getDimension(R.styleable.swipelistviewstyle_right_width, 200);   
      
      mTypedArray.recycle();  
     
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                System.out.println("onInterceptTouchEvent----->ACTION_DOWN");
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int)mFirstX, (int)mFirstY);
                System.out.println("onInterceptTouchEvent  getPosition --> "+motionPosition);
                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }else{// �����ListView�Ŀհ״�
                	if(mCurrentItemView!=null && mIsShown){
                		hiddenRight(mCurrentItemView);
                	}
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("onInterceptTouchEvent----->ACTION_UP");
                if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * ���һ��
                     * <p>
                     * һ��Item���ұ߲����Ѿ���ʾ��
                     * <p>
                     * ��ʱ��������һ��item, ��ô�Ǹ��ұ߲�����ʾ��item�������ұ߲���
                     */
                    hiddenRight(mPreItemView);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;

        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else {
            canJudge = false;
        }

        return canJudge;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        int motionPosition = pointToPosition((int)mFirstX, (int)mFirstY);
        if(motionPosition<0){
        	return super.onTouchEvent(ev);
        }
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("onTouchEvent---->ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                // confirm is scroll direction
                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                        break;
                    }
                }

                if (mIsHorizontal) {
                    if (mIsShown && mPreItemView != mCurrentItemView) {
                        System.out.println("2---> hiddenRight");
                        /**
                         * �������
                         * <p>
                         * һ��Item���ұ߲����Ѿ���ʾ��
                         * <p>
                         * ��ʱ�����һ�������һ��item,�Ǹ��ұ߲�����ʾ��item�������ұ߲���
                         * <p>
                         * ���󻬶�ֻ��������������һ������ᴥ�������
                         */
                        hiddenRight(mPreItemView);
                    }

                    if (mIsShown && mPreItemView == mCurrentItemView) {
                        dx = dx - mRightViewWidth;
                        System.out.println("======dx " + dx);
                    }

                    // can't move beyond boundary
                    if (dx < 0 && dx > -mRightViewWidth && mCurrentItemView!=null) {
                        mCurrentItemView.scrollTo((int)(-dx), 0);
                    }

                    return true;
                } else {
                    if (mIsShown) {
                        System.out.println("3---> hiddenRight");
                        /**
                         * �������
                         * <p>
                         * һ��Item���ұ߲����Ѿ���ʾ��
                         * <p>
                         * ��ʱ�����¹���ListView,��ô�Ǹ��ұ߲�����ʾ��item�������ұ߲���
                         */
                        hiddenRight(mPreItemView);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("onTouchEvent============ACTION_UP");
                clearPressedState();
                if (mIsShown) {
                    System.out.println("4---> hiddenRight");
                    /**
                     * ����ģ�
                     * <p>
                     * һ��Item���ұ߲����Ѿ���ʾ��
                     * <p>
                     * ��ʱ�����һ�����ǰһ��item,�Ǹ��ұ߲�����ʾ��item�������ұ߲���
                     */
                    hiddenRight(mPreItemView);
                }

                if (mIsHorizontal != null && mIsHorizontal) {
                    if (mFirstX - lastX > mRightViewWidth / 2) {
                    	System.out.println("5---> showRight");
                        showRight(mCurrentItemView);
                    } else {
                        System.out.println("5---> hiddenRight");
                        /**
                         * ����壺
                         * <p>
                         * ���һ���һ��item,�һ����ľ��볬�����ұ�View�Ŀ�ȵ�һ�룬����֮��
                         */
                        hiddenRight(mCurrentItemView);
                    }

                    return true;
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        // TODO current item is still has background, issue
    	if(mCurrentItemView!=null){
    		mCurrentItemView.setPressed(false);
            setPressed(false);
            refreshDrawableState();
    	}
        // invalidate();
    }

    private void showRight(View view) {
        System.out.println("showRight function=========showRight");

       if(view!=null){
//    	   int startX = view.getScrollX();
//    	   int startY = view.getScrollY();
//    	   int dx = mRightViewWidth - startX;
//    	   int dy = 0;
//    	   ScrollParams params = new ScrollParams(startX,startY,dx,dy); 
    	   Message msg = new MoveHandler().obtainMessage();
           msg.obj = view;
//    	   msg.obj = params;
           msg.arg1 = view.getScrollX();
           msg.arg2 = mRightViewWidth;
           msg.sendToTarget();
       }

        mIsShown = true;
    }

    private void hiddenRight(View view) {
        System.out.println("hiddenRight function=========hiddenRight");
        if (mCurrentItemView == null) {
            return;
        }
        if(view!=null){
//        	int startX = view.getScrollX();
//     	   int startY = view.getScrollY();
//     	   int dx = 0 - startX;
//     	   int dy = 0;
//     	   ScrollParams params = new ScrollParams(startX,startY,dx,dy); 
        	Message msg = new MoveHandler().obtainMessage();//
            msg.obj = view;
//        	msg.obj = params;
            msg.arg1 = view.getScrollX();
            msg.arg2 = 0;
            msg.sendToTarget();
        }

        mIsShown = false;
    }

    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;
        
//        ScrollParams params;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View)msg.obj;
//                params = (ScrollParams)msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int)((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
//                	scroller.startScroll(toX, params.startY, (-toX), 0, 500);
//                	System.out.println("scroller.startScroll("+toX+", "+params.startY+", "+(-toX)+", 0, 500);");
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
//            scroller.startScroll(fromX, params.startY, (-fromX), 0, 500);
//        	System.out.println("scroller.startScroll("+fromX+", "+params.startY+", "+(-fromX)+", 0, 500);");

            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }
    
    /**
     * �ⲿ����ɾ���������
     */
    public void hideRightView(){
    	hideRightHandler.sendEmptyMessage(0);
    }
    /**
     * �ⲿ����ɾ������Handler
     */
    private Handler hideRightHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (mIsShown && mCurrentItemView!=null) {
                System.out.println("hideRightHandler---> hiddenRight");
                hiddenRight(mCurrentItemView);
            }
		}
    };
    
//    class ScrollParams{
//    	public int startX;
//    	public int startY;
//    	public int dx;
//    	public int dy;
//    	public ScrollParams(int startX,int startY,int dx, int dy){
//    		this.startX = startX;
//    		this.startY = startY;
//    		this.dx = dx;
//    		this.dy = dy;
//    	}
//    	
//    }
}
