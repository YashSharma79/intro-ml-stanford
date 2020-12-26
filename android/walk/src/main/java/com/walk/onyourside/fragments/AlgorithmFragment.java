package com.walk.onyourside.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.walk.onyourside.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlgorithmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlgorithmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlgorithmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private TextView pseudoCodeBlock;

    private boolean sequentialPseudocode;
    private int sequenceIndex = 0;

    public AlgorithmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlgorithmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlgorithmFragment newInstance(String param1, String param2) {
        AlgorithmFragment fragment = new AlgorithmFragment();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_algorithm, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        if(view != null){
            pseudoCodeBlock = view.findViewById(R.id.pseudoCodeBlock);
        }
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

    public void UpdateAlgorithm(String pseudoCode){

        Toast.makeText(getActivity(), pseudoCode, Toast.LENGTH_LONG).show();
        CharSequence text = pseudoCodeBlock.getText();
        int previousLength = text.length();

        if(!sequentialPseudocode){

            String s = text.toString();
            String[] split = s.split("\\n");

            if(split.length > (sequenceIndex + 1)){

                StringBuilder stringBuilder = new StringBuilder();

                split[++sequenceIndex] = pseudoCode;

                for (int i = 0; i < split.length; i++) {
                    stringBuilder.append(split[i]);
                    stringBuilder.append("\n");
                }

                pseudoCodeBlock.setText(stringBuilder.toString());
                return;
            }


            pseudoCodeBlock.setText(pseudoCode);
            return;
        }

        //sequential
        String pseudocodeBlock = String.valueOf(text) + "\n" + pseudoCode;

        SpannableString spannableString = new SpannableString(pseudocodeBlock);
        spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#b58900")), previousLength, pseudocodeBlock.length(), 0);

        pseudoCodeBlock.setText(spannableString);


    }

    public void ClearAlgorithm(){

        pseudoCodeBlock.setText("");
        sequenceIndex = 0;
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

    public void setSequentialPseudocode(boolean sequentialPseudocode) {
        this.sequentialPseudocode = sequentialPseudocode;
    }

    public boolean isSequentialPseudocode() {
        return sequentialPseudocode;
    }

    public void HideTitle(){
        TextView title = getView().findViewById(R.id.title);
        title.setVisibility(View.INVISIBLE);
    }
}
