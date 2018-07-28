package com.rohitupreti.pinedittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditText extends AppCompatEditText {

    public enum State{
        FILLED,
        EMPTY
    }

    private State state;

    private static final int[] STATE_FILLED = {R.attr.state_filled};
    private static final int[] STATE_EMPTY = {R.attr.state_empty};

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] def = super.onCreateDrawableState(extraSpace+1);
        if(getState().equals(State.FILLED)){
            mergeDrawableStates(def, STATE_FILLED);
        }
        return def;
    }


    public State getState(){
        if(state==null){
            state = State.EMPTY;
        }
        return state;
    }

    public void setState(State state){
        this.state = state;
    }

}
