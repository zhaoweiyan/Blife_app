package com.blife.blife_app.index.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.blife.blife_app.utils.logcat.L;

/**
 * Created by w on 2016/9/28.
 */
public class KeyWordEditLayout extends FrameLayout implements View.OnClickListener {

    private Context context;

    private EditText editText;
    private KeyWordsEditText keyWordsEditText;


    public KeyWordEditLayout(Context context) {
        super(context);
        init(context);
    }

    public KeyWordEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyWordEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOnClickListener(this);
        initView();
    }

    private void initView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        editText = new EditText(context);
        editText.addTextChangedListener(watcher);
        editText.setTextSize(0.01f);
        editText.setGravity(Gravity.TOP);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setCursorVisible(false);
        addView(editText);
        keyWordsEditText = new KeyWordsEditText(context);
        addView(keyWordsEditText, layoutParams);
    }

    public void setKeyWordLength(int length) {
        if (editText != null)
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        if (keyWordsEditText != null) {
            keyWordsEditText.setKeywordsNum(length);
        }
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (keyWordsEditText != null)
                keyWordsEditText.setText(s.toString().trim());
        }
    };

    public void clear() {
        if (keyWordsEditText != null)
            keyWordsEditText.setText("");
        if (editText != null)
            editText.setText("");
    }

    public String getInputText() {
        return editText.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        if (editText != null) {
            editText.requestFocus();
            String text = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                editText.setSelection(text.length());
            }
        }
    }
}
