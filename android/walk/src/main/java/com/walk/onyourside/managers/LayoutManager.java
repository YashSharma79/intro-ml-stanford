package com.walk.onyourside.managers;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;

public class LayoutManager {

    ConstraintLayout layout;
    private ConstraintSet set;

    public LayoutManager(ConstraintLayout layout){
        this.layout = layout;
        set = new ConstraintSet();
    }

    //View, horizontal constraints, vertical constraints
    public void PlaceInLayout(View view, int[] h, int[] v){

        layout.addView(view);
        //place in the layout
        set.clone(layout);

        set.connect(view.getId(), h[0], h[1], h[2], h[3]);
        set.connect(view.getId(), v[0], v[1], v[2], v[3]);
        set.applyTo(layout);

    }

}
