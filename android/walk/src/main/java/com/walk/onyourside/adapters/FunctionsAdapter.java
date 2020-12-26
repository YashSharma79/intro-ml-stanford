package com.walk.onyourside.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.walk.onyourside.R;

import java.util.List;

public class FunctionsAdapter extends RecyclerView.Adapter<FunctionsAdapter.FunctionsViewHolder> {

    private List<String> functionNames;
    private List<String> functionIntroductions;

    private FunctionsViewHolder.FunctionsAdapterInterface functionsAdapterInterface;

    public FunctionsAdapter(List<String> functionNames, List<String > functionIntroductions, FunctionsViewHolder.FunctionsAdapterInterface functionsAdapterInterface){
        this.functionNames = functionNames;
        this.functionIntroductions = functionIntroductions;
        this.functionsAdapterInterface = functionsAdapterInterface;
    }


    @Override
    public FunctionsAdapter.FunctionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.function_view, parent, false);
        FunctionsAdapter.FunctionsViewHolder functionsViewHolder = new FunctionsViewHolder(itemView, functionsAdapterInterface);

        return functionsViewHolder;

    }

    //Populate the views
    //RecyclerView calls this when it wants to use/reuse a viewHolder for a new piece of data
    @Override
    public void onBindViewHolder(FunctionsViewHolder holder, int position) {
        holder.functionButton.setText(functionNames.get(position));
        holder.functionIndex = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return functionNames.size();
    }

    public static class FunctionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Button functionButton;
        private int functionIndex;

        private FunctionsAdapterInterface functionsAdapterInterface;


        public interface FunctionsAdapterInterface{
            void onFunctionClick(String functionName, int functionIndex);
        }

        public FunctionsViewHolder(View view, FunctionsAdapterInterface functionsAdapterInterface){
            super(view);
            functionButton = view.findViewById(R.id.function_button);
            this.functionsAdapterInterface = functionsAdapterInterface;
            functionButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            functionsAdapterInterface.onFunctionClick(functionButton.getText().toString(), functionIndex);
        }
    }


}
