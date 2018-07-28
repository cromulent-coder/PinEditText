package com.rohitupreti.pinedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class UEditText extends LinearLayout{

    private static final String TAG = UEditText.class.getSimpleName();

    private ImageView mClearButton;
    private CustomEditText[] mEditTexts;
    private List<FilledListener> filledListenerList;

    private int length;

    private int inputType;
    private static final int NUMBER = 0;
    private static final int STRING = 1;

    private static final int MAX_CHARS = 1;
    private static final int MAX_LINES = 1;
    private int textSize;
    private String hint;
    private int boxWidth;


    public UEditText(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public UEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.UEditText,
                0, 0);
        initializeViews(context,a);
    }

    public UEditText(Context context,
                          AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.UEditText,
                0, 0);
        initializeViews(context, a);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context, TypedArray arr) {
        this.inputType = arr.getInt(R.styleable.UEditText_input, 0);
        this.length = arr.getInt(R.styleable.UEditText_length, 3);
        this.textSize = arr.getDimensionPixelSize(R.styleable.UEditText_size, dpToPx(getContext(), 12));
        this.hint = arr.getString(R.styleable.UEditText_hint);
        this.hint = correctHintLength(hint, length);
        this.boxWidth = arr.getDimensionPixelSize(R.styleable.UEditText_width, dpToPx(getContext(), 20));

        this.mEditTexts = new CustomEditText[this.length];

        int hintCharLength = hint.length()/length;

        Log.d(TAG, "Length: " + this.length);

        Log.d(TAG, this.inputType + " : " + this.length);
        this.setOrientation(HORIZONTAL);
        int px = dpToPx(getContext(), 8);
        int pxSmall =dpToPx(getContext(), 4);
        this.setPadding(px, px, px, px);


        LayoutParams params = new LayoutParams(
                boxWidth,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(pxSmall, 0, 0, 0);

        for(int i=0; i<length; i++) {
            mEditTexts[i] = new CustomEditText(getContext());
            mEditTexts[i].setBackgroundResource(R.drawable.background_line);
            mEditTexts[i].setMaxLines(MAX_LINES);
            mEditTexts[i].setFilters(setMaxLength(MAX_CHARS));
            mEditTexts[i].setGravity(Gravity.CENTER);
            mEditTexts[i].setHint(hint.substring(i*hintCharLength, i*hintCharLength + hintCharLength));
            mEditTexts[i].setState(CustomEditText.State.EMPTY);
            mEditTexts[i].setCursorVisible(false);
            mEditTexts[i].setSelectAllOnFocus(true);
            mEditTexts[i].setHighlightColor(Color.alpha(0));
            mEditTexts[i].setState(CustomEditText.State.EMPTY);
            /*mEditTexts[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if()
                    your_edittext.setSelection(your_edittext.getText().length());
                }
            });*/
            mEditTexts[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mEditTexts[i].setInputType(inputType==NUMBER?InputType.TYPE_CLASS_NUMBER:InputType.TYPE_CLASS_TEXT);
            if(i==length-1){
                params.setMargins(pxSmall, 0, pxSmall, 0);
            }
            mEditTexts[i].setLayoutParams(params);
            this.addView(mEditTexts[i]);
        }
    }

    private String correctHintLength(String hint, int length) {
        int hintLength = hint.length();

        if(hintLength%length!=0){
            int d = hintLength/length;
            d = d+1;
            //whitespaces
            d = d*length - hintLength;
            StringBuilder builder = new StringBuilder(hint);
            for(int i=0; i<d; i++){
                builder.append(" ");
            }
            hint = builder.toString();
        }
        return hint;
    }


    public InputFilter[] setMaxLength(int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        return filterArray;
    }



    private int dpToPx(Context context, int dp){
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        /*mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    mRootView.setBackgroundResource(R.drawable.background_selected);                }
                else{
                    mRootView.setBackground(null);
                }
            }
        });*/

        // If the text changes, show or hide the X (clear) button.
        //to be used for filtering options
        for(int i=0; i<length; i++){
            final int curr = i;

            mEditTexts[i].setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                    if(keyCode == KeyEvent.KEYCODE_DEL) {
                        //this is for backspace
                        if(curr!=0){
                            mEditTexts[curr-1].requestFocus();
                        }
                    }
                    return false;
                }
            });

            mEditTexts[curr].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(charSequence.toString().length()==MAX_CHARS) {
                        Log.d(TAG, "filled state: " + curr);
                        mEditTexts[curr].setState(CustomEditText.State.FILLED);

                        if (curr == length - 1) {
                            //do nothing
                            informListeners(getText());
                            UEditText.this.setFocusableInTouchMode(true);
                            UEditText.this.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
                            UEditText.this.requestFocus();
                        } else {
                            mEditTexts[curr + 1].requestFocus();
                        }
                        mEditTexts[curr].refreshDrawableState();
                    }else if(charSequence.toString().length()==0){
                        Log.d(TAG, curr + " length is zero");
                        mEditTexts[curr].setState(CustomEditText.State.EMPTY);
                        if(curr!=0) {
                            mEditTexts[curr - 1].requestFocus();
                        }
                        mEditTexts[curr].refreshDrawableState();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }

            });
        }
    }


    public void setText(String text){
        for(int i=0; i<text.length() && i<length; i++){
            this.mEditTexts[i].setText(text.charAt(i));
        }
    }

    public String getText(){
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<length; i++){
            builder.append(this.mEditTexts[i].getText().toString());
        }
        return builder.toString();
    }

    public void addFilledListener(FilledListener filledListener){
        if(this.filledListenerList==null){
            filledListenerList = new ArrayList<>();
        }
        filledListenerList.add(filledListener);
    }

    public void removeFilledListener(FilledListener filledListener){
        if(filledListenerList!=null){
            this.filledListenerList.remove(filledListener);
        }
    }

    private void informListeners(String content){
        if(filledListenerList==null){
            return;
        }
        for(FilledListener filledListener: filledListenerList){
            filledListener.onFilled(content);
        }
    }

    interface FilledListener{
        void onFilled(String content);
    }

}
