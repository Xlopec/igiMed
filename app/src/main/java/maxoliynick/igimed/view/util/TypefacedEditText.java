package maxoliynick.igimed.view.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;

import maxoliynick.igimed.R;

/**
 * Created by Максим on 10/29/2016.
 */

public class TypefacedEditText extends AppCompatEditText {

    private static final String TAG = TypefacedEditText.class.getSimpleName();

    public TypefacedEditText(Context context) {
        super(context);
    }

    public TypefacedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TypefacedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String customFont = a.getString(R.styleable.TypefacedTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        Typeface tf;

        try {
            tf = Typefaces.get(ctx, asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
