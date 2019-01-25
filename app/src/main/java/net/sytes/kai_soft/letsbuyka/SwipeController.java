package net.sytes.kai_soft.letsbuyka;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;


import static android.support.v7.widget.helper.ItemTouchHelper.*;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeController extends Callback {
    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private SwipeControllerActions buttonsActions = null;
    private int direct = -1;
    private static final float buttonWidth = 720;
    private Context context;

    public SwipeController(SwipeControllerActions buttonsActions, Context context) {
        this.buttonsActions = buttonsActions;
        this.context = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == RIGHT){
            buttonsActions.onLeftSwiped(viewHolder.getAdapterPosition());
        }else if (direction == LEFT){
            buttonsActions.onRightSwiped(viewHolder.getAdapterPosition());
        }
    }

    public void reInit(){
        buttonShowedState = ButtonsState.GONE;
        currentItemViewHolder = null;
        direct = -1;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    /*public void setNeedSwipeBack(boolean needSwipeBack){
        this.needSwipeBack = needSwipeBack;
    }*/

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //buttonWidth = viewHolder.itemView.getRight() - viewHolder.itemView.getLeft();
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        onDraw(c);
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = (viewHolder.itemView.getRight() - viewHolder.itemView.getLeft()) / 2 > Math.abs(dX);
                if (dX < -1) direct = LEFT;
                else if (dX > 1) direct = RIGHT;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    /*if (buttonShowedState != ButtonsState.GONE) {
                        setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }*/
                }
                return false;
            }
        });
    }

    /*private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }*/

    /*private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowedState = ButtonsState.GONE;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }*/

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {

        float corners = 20;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        RectF rightButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());

        if (direct == RIGHT){
            p.setColor(Color.BLUE);
            c.drawRoundRect(leftButton, corners, corners, p);;
            drawIcons(c, leftButton, p);
        }
        else if(direct == LEFT){
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawIcons(c, rightButton, p);
        }

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {

            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {

            buttonInstance = rightButton;
        }
    }


    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        float textMargin = 15;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(text);
        if (direct == LEFT){
            c.drawText(text, button.right - textWidth - textMargin, button.centerY()+(textSize/2), p);
        } else if (direct == RIGHT){
            c.drawText(text, button.left + textMargin, button.centerY()+(textSize/2), p);
        }
    }

    private void drawIcons(Canvas c, RectF button, Paint p){
        float bitmapWidth = context.getResources().getDrawable(R.drawable.ic_delete_black_50dp).getIntrinsicWidth();
        float bitmapMargin = 15;
        p.setColor(Color.WHITE);
        Bitmap bitmapSource;
        if (direct == LEFT){
            bitmapSource = getBitmapFromDrawable(context.getResources().getDrawable(R.drawable.ic_delete_black_50dp));
            c.drawBitmap(bitmapSource, button.right - bitmapWidth - bitmapMargin, button.top, p);
        } else if (direct == RIGHT){
            bitmapSource = getBitmapFromDrawable(context.getResources().getDrawable(R.drawable.ic_edit_black_50dp));
            c.drawBitmap(bitmapSource, button.left + bitmapMargin, button.top, p);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}
