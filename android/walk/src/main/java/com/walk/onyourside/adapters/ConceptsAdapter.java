package com.walk.onyourside.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.walk.onyourside.model.AlgorithmBuilder;
import com.walk.onyourside.model.GameOfFunctions;
import com.walk.onyourside.R;

public class ConceptsAdapter extends RecyclerView.Adapter<ConceptsAdapter.ConceptsViewHolder> {


    private Context context;
    //Tell the adapter what data it should work with
    private String[] conceptNames;
    private String[] description;


    public static class ConceptsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView conceptName;
        private TextView conceptDescription;

        private Button learn;
        private Button play;


        private ConceptCardClick mOnClickListener;
        private ConceptCardClick playClickListener;

        public ConceptsViewHolder(View view, ConceptCardClick listener){

            super(view);
            mOnClickListener = listener;
            conceptName = view.findViewById(R.id.concept_name);
            conceptDescription = view.findViewById(R.id.concept_description);
            learn = view.findViewById(R.id.learn);
            play = view.findViewById(R.id.play);

            learn.setOnClickListener(this);
            play.setOnClickListener(this);
            

        }

        public interface ConceptCardClick{
            void onConceptLearn(String tag);
            void onConceptPlay(String tag);
        }
        

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.learn:
                    mOnClickListener.onConceptLearn(v.getTag().toString());
                    break;

                case R.id.play:
                    mOnClickListener.onConceptPlay(v.getTag().toString());
                    break;

                    default:
                        break;
            }

        }

    }

    public ConceptsAdapter(Context context, String[] conceptNames, String[] description){
        this.context = context;
        this.conceptNames = conceptNames;
        this.description = description;
    }


    //this function gets called when the recycler view needs to create a new viewHolder
    @Override
    public ConceptsAdapter.ConceptsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.concept_view, parent, false);


        ConceptsAdapter.ConceptsViewHolder vh = new ConceptsViewHolder(itemView, new ConceptsAdapter.ConceptsViewHolder.ConceptCardClick(){
            @Override
            public void onConceptLearn(String tag) {
//                Toast.makeText(parent.getContext(), tv.getText(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, AlgorithmBuilder.class);
                intent.putExtra("concept_name", tag);
                context.startActivity(intent);

            }

            @Override
            public void onConceptPlay(String tag){
                Intent intent = new Intent(context, GameOfFunctions.class);
                intent.putExtra("concept_name", tag);
                context.startActivity(intent);
            }

            
        });


        return vh;

    }

    //Populate the views
    //RecyclerView calls this when it wants to use/reuse a viewHolder for a new piece of data
    @Override
    public void onBindViewHolder(ConceptsViewHolder holder, int position) {

        holder.conceptName.setText(conceptNames[position]);
        holder.conceptDescription.setText(description[position]);
        holder.learn.setTag(conceptNames[position]);
        holder.play.setTag(conceptNames[position]);
    }

    //Need to tell the adapter how many data items are there
    @Override
    public int getItemCount(){
        return conceptNames.length;
    }


}
