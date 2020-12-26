package com.walk.onyourside.managers;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

public class DragDropManager {

    private static Context mContext = null;

    public static void SetContext(final Context context){
        mContext = context;
    }

    public static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Drawable shadow;


        public MyDragShadowBuilder(View imageView) {
            super(imageView);
            shadow = new ColorDrawable(Color.LTGRAY);

        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            int width, height;

            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;

            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter
            outShadowSize.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            outShadowTouchPoint.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().

        @Override
        public void onDrawShadow(Canvas canvas) {
            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

    public static class MyDragEventListener implements View.OnDragListener {

        public MyDragEventListener(){

        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            // Defines a variable to store the action type for the incoming event
            final int action = dragEvent.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // applies a blue color tint to the View to indicate that it can accept data.
                        view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);


                        //invalidate to force redraw in the new tint
                        view.invalidate();


                        //returns true to indicate the view can accept dragged data
                        return true;


                    }


                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.

                    view.getBackground().setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.MULTIPLY);

                    // Invalidate the view to force a redraw in the new tint
                    view.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    view.getBackground().setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.MULTIPLY);

                    // Invalidate the view to force a redraw in the new tint
                    view.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    String dragData = item.getText().toString();

                    // Displays a message containing the dragged data.
                    Toast.makeText( mContext, "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    // Turns off any color tints
                    view.getBackground().clearColorFilter();

                    // Invalidates the view to force a redraw
                    view.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    view.getBackground().clearColorFilter();

                    // Invalidates the view to force a redraw
                    view.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (dragEvent.getResult()) {
                        Toast.makeText( mContext, "The drop was handled.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText( mContext, "The drop didn't work.", Toast.LENGTH_LONG).show();

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;

            }
            return false;
        }
    }
}
