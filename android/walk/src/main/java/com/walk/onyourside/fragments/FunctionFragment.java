package com.walk.onyourside.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walk.onyourside.R;
import com.walk.onyourside.adapters.FunctionsAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FunctionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FunctionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunctionFragment extends Fragment implements FunctionsAdapter.FunctionsViewHolder.FunctionsAdapterInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView.Adapter functionsAdapter;
    RecyclerView.LayoutManager functionsLayoutManager;

    private List<String> functionNames;
    private List<String> functionIntroductions;
    private String functionToCall;
    private OnFunctionSelectListener functionSelectListener;

    public FunctionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FunctionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FunctionFragment newInstance(String param1, String param2) {
        FunctionFragment fragment = new FunctionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public void onStart(){
        super.onStart();

        View view = getView();

        if(view != null) {

            RecyclerView functionsRecycler = view.findViewById(R.id.functionsRecycler);
            functionsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            functionsRecycler.setLayoutManager(functionsLayoutManager);
//            functionsRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            //specify adapter
            functionNames = new ArrayList<>();
            functionIntroductions = new ArrayList<>();

            functionsAdapter = new FunctionsAdapter(functionNames, functionIntroductions, this);
            functionsRecycler.setAdapter(functionsAdapter);

        }

    }


    public interface OnFunctionSelectListener{
        void onFunctionSelect(String functionName, int functionIndex);
    }

    public void RegisterFunctionSelectListener(OnFunctionSelectListener functionSelectListener){
        this.functionSelectListener = functionSelectListener;
    }

    public void AddFunction(String functionName, String functionIntroduction){
        functionNames.add(functionName);
        functionIntroductions.add(functionIntroduction);
        functionsAdapter.notifyItemInserted(functionNames.size()-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_function, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFunctionClick(String functionName, int functionIndex) {
        this.functionToCall = functionName;
        functionSelectListener.onFunctionSelect(functionName, functionIndex);
    }

    public String FunctionToCall(){
        return functionToCall;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}
