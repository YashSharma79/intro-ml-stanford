package com.walk.onyourside.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.walk.onyourside.concepts.Concept;
import com.walk.onyourside.concepts.LinkedList;
import com.walk.onyourside.fragments.FunctionFragment;
import com.walk.onyourside.fragments.GameIntroDialog;
import com.walk.onyourside.managers.LayoutManager;
import com.walk.onyourside.fragments.Operators;
import com.walk.onyourside.R;
import com.walk.onyourside.fragments.WYDIWYS;

import java.util.List;


public class GameOfFunctions extends AppCompatActivity implements FunctionFragment.OnFragmentInteractionListener, Operators.OnFragmentInteractionListener, WYDIWYS.OnFragmentInteractionListener, FunctionFragment.OnFunctionSelectListener, GameIntroDialog.OnFragmentInteractionListener, Concept.BuildingBlockCallback{

    FragmentManager fragmentManager;
    FunctionFragment functionFragment;
    Operators operatorFragment;
    WYDIWYS wydiwys;

    Fragment currentFragment;
    LayoutManager layoutManager;

    ConceptController conceptController;
    String conceptName;

    private View lastView;
    private Game currentGame;

    private TextView gameName;
    private TextView algorithmText;
    Concept concept;

    GameIntroDialog gameIntroDialog;
    private Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_functions);

        Intent intent = getIntent();
        conceptName = intent.getStringExtra("concept_name");

        gameName = findViewById(R.id.gameTask);
        algorithmText = findViewById(R.id.algorithm_text);

        new BackgroundTask(this).execute();

        //Fragments should be dynamic
        fragmentManager = getSupportFragmentManager();
        functionFragment = (FunctionFragment) getSupportFragmentManager().findFragmentById(R.id.function_shelf);
        operatorFragment = (Operators) getSupportFragmentManager().findFragmentById(R.id.operator_shelf);
        wydiwys = (WYDIWYS) getSupportFragmentManager().findFragmentById(R.id.fragment_wydiwys);

        functionFragment.RegisterFunctionSelectListener(this::onFunctionSelect);
        HideFragment(functionFragment);
        HideFragment(operatorFragment);
        layoutManager = new LayoutManager(findViewById(R.id.game_of_functions_layout));
        SetLastView(findViewById(R.id.game_of_functions_layout));

        //overlay

        if(isFirstTime()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            gameIntroDialog = GameIntroDialog.newInstance("", "");
            fragmentTransaction.add(R.id.game_of_functions_layout, gameIntroDialog, "GameIntro");
            fragmentTransaction.commit();
        }

    }

    void InitWYS(){

        //The WYS is concept based
        //It has to be called after the details have been fetched
        //Or maybe it should be left to the concept to take care of this
        //The concept provides all the images, all the operations needed for this
        //Whatever we see in the WYD should come from the editor


    }


    void ShowFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .show(fragment)
                .commit();

        currentFragment = fragment;
    }

    void HideFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .hide(fragment)
                .commit();

        currentFragment = null;

    }

    void SetLastView(View lastView){
        this.lastView = lastView;
    }

    @Override
    public void onFunctionSelect(String functionName, int functionIndex) {

        //Place the function in WYD editor
        String fullAlgorithmText = algorithmText.getText().toString() + "\n" + functionName;
        algorithmText.setText(fullAlgorithmText);

        FunctionForGame functionForGame = conceptController.getFunctionForGame(functionIndex);

        TextView parameter = new TextView(this);
        parameter.setId(View.generateViewId());

        if(functionForGame.getParameters() != null) {
            parameter.setText(functionForGame.getParameters().get(0));
            parameter.setTextColor(Color.BLUE);
            Toast.makeText(this, "Function selected = " + functionForGame.getParameters().get(0), Toast.LENGTH_SHORT ).show();
        }

        int[] constraintsH = new int[]{ConstraintSet.LEFT, algorithmText.getId(), ConstraintSet.RIGHT, 30};
        int[] constraintsV = new int[]{ConstraintSet.TOP, lastView.getId(), ConstraintSet.TOP, 80};

        layoutManager.PlaceInLayout(parameter, constraintsH, constraintsV);

        concept.MakeFunctionCall(functionForGame.getName());

    }

    private boolean isFirstTime(){
        if(firstTime == null){
            SharedPreferences sharedPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = sharedPreferences.getBoolean("first_time", true);

            if(firstTime){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("first_time", false);
                editor.apply();
            }

        }

        return firstTime;
    }


    public void onClick(View view){

        switch (view.getId()){

            case R.id.functionShelfButton:
                ToggleFunctionShelf(functionFragment);
                break;

            case R.id.operatorShelfButton:
                ToggleOperatorShelf(operatorFragment);
                break;

            case R.id.game_intro_ui:
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(gameIntroDialog);
                fragmentTransaction.commit();
                break;

            default:
                break;

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    void ToggleOperatorShelf(Operators f){

        if(currentFragment instanceof Operators){
            HideFragment(f);
            return;
        }

        ShowFragment(f);

    }

    void ToggleFunctionShelf(FunctionFragment f){

        if(currentFragment instanceof FunctionFragment){
            HideFragment(f);
            return;
        }

        ShowFragment(f);

    }

    void PostBackgroundTask(){

        //Populate all the views here
        System.out.println("Size of functionForGames = " + conceptController.getAllFunctions().size());

        gameName.setText(currentGame.name);
        List<FunctionForGame> f = conceptController.getAllFunctions();

        for (int i = 0; i < conceptController.getAllFunctions().size(); i++){

            functionFragment.AddFunction(f.get(i).getName(), f.get(i).getIntroduction());

        }

        concept = new LinkedList(this,GameOfFunctions.this, findViewById(R.id.fragment_wydiwys));

        concept.InitWYDIWYS();
        concept.RegisterCallback(this);
        concept.PreGameLevel(currentGame.state);

    }

    @Override
    public void CallbackAfterConcept() {

    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        private final Context mContext;

        public BackgroundTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            conceptController = new ConceptController(mContext, conceptName);
            currentGame = conceptController.getGame(0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PostBackgroundTask();
        }
    }
}
