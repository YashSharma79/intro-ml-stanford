package com.walk.onyourside.model;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget. Toast;

import com.walk.onyourside.fragments.ConceptComplete;
import com.walk.onyourside.fragments.FirstScreen;
import com.walk.onyourside.fragments.FunctionFragment;
import com.walk.onyourside.fragments.AlgorithmFragment;
import com.walk.onyourside.fragments.LearnIntroDialog;
import com.walk.onyourside.interfaces.ConstructionState;
import com.walk.onyourside.fragments.FunctionIntroduction;
import com.walk.onyourside.fragments.Operators;
import com.walk.onyourside.R;
import com.walk.onyourside.concepts.Concept;
import com.walk.onyourside.concepts.LinkedList;
import com.walk.onyourside.concepts.QuickSort;



public class AlgorithmBuilder extends AppCompatActivity implements AlgorithmFragment.OnFragmentInteractionListener, Operators.OnFragmentInteractionListener, FunctionFragment.OnFragmentInteractionListener, View.OnClickListener, Concept.BuildingBlockCallback, ConstructionState, FunctionIntroduction.OnFragmentInteractionListener, FunctionFragment.OnFunctionSelectListener,LearnIntroDialog.OnFragmentInteractionListener, FirstScreen.OnFragmentInteractionListener,ConceptComplete.OnFragmentInteractionListener {

    ConstraintLayout layout;

    TextView prevInstruction;
    TextView currentInstruction;
    EditText dataInput;

    TextView stateText;
    TextView functionName;
    TextView introduction;

    AlgorithmFragment algorithmFragment;
    FunctionFragment functionFragment;
    Operators operatorFragment;

    ConceptController conceptController;

    Concept concept;
    FragmentManager fragmentManager;
    Button nextFunction;

    String conceptName;

    FragmentTransaction fragmentTransaction;
    FunctionIntroduction functionIntroduction;


    Fragment currentFragment;

    LearnIntroDialog learnIntroDialog;
    Fragment firstScreenFragment;

    View firstScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_builder);


        Intent intent = getIntent();
        conceptName = intent.getStringExtra("concept_name");

        new BackgroundTask(this).execute();
        layout = findViewById(R.id.algorithm_builder_layout);
        prevInstruction = findViewById(R.id.prev_instruction);
        currentInstruction = findViewById(R.id.c_instruction);
        dataInput = findViewById(R.id.dataInput);

        stateText = findViewById(R.id.stateText);
        functionName = findViewById(R.id.function_name);
        introduction = findViewById(R.id.introduction);
        nextFunction = findViewById(R.id.nextStep);

        functionFragment = (FunctionFragment) getSupportFragmentManager().findFragmentById(R.id.function_shelf);
        operatorFragment = (Operators) getSupportFragmentManager().findFragmentById(R.id.operator_shelf);

        fragmentManager = getSupportFragmentManager();
        SetKeyboardInput(dataInput);
        firstScreen = findViewById(R.id.first_screen_layout);

        Init();
        learnIntroDialog = LearnIntroDialog.newInstance("", "");
        AddFragment(learnIntroDialog, "LearnIntro");

        firstScreenFragment = FirstScreen.newInstance("", "");
        AddFragment(firstScreenFragment, "FirstScreen");

    }

    void Init(){

        algorithmFragment = (AlgorithmFragment) getSupportFragmentManager().findFragmentById(R.id.algorithm_fragment);
        operatorFragment= (Operators) getSupportFragmentManager().findFragmentById(R.id.operator_fragment);
        functionFragment = (FunctionFragment) getSupportFragmentManager().findFragmentById(R.id.function_shelf);

        functionFragment.RegisterFunctionSelectListener(this::onFunctionSelect);
        HideFragment(operatorFragment);
        HideFragment(functionFragment);

        if(conceptName.equals("Linked List")){
            concept = new LinkedList(this,AlgorithmBuilder.this, layout);

        }

        else if(conceptName.equals("Quick Sort")){
            concept = new QuickSort(this,AlgorithmBuilder.this, layout);
        }

        concept.InitConstruction();
        concept.RegisterCallback(this);
        concept.RegisterFunctionCallback(this);


        //after async task is done

    }

    void PostBackgroundTask(){

        SetupNewFunctionUI();
        SetInstruction( conceptController.NextInstruction() );
        concept.SetBuildingBlocksBehaviour(conceptController.NextBlockBehaviour());
        conceptController.RegisterCallback(this);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.nextStep:
                ChangeFunction();
                break;

            case R.id.add:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                break;

            case R.id.multiply:
                Toast.makeText(this, "Multiply", Toast.LENGTH_SHORT).show();
                break;

            case R.id.divide:
                Toast.makeText(this, "Divide", Toast.LENGTH_SHORT).show();
                break;

            case R.id.subtract:
                Toast.makeText(this, "Subtract", Toast.LENGTH_SHORT).show();
                break;

            case R.id.not_equal:
                Toast.makeText(this, "Not equal", Toast.LENGTH_SHORT).show();
                break;

            case R.id.less_than:
                Toast.makeText(this, "Less than", Toast.LENGTH_SHORT).show();
                break;

            case R.id.greater_than:
                Toast.makeText(this, "Greater than", Toast.LENGTH_SHORT).show();
                break;

            case R.id.assignment:
                Toast.makeText(this, "Assign", Toast.LENGTH_SHORT).show();
                HideFragment(operatorFragment);
                break;

            case R.id.start_function:
                RemoveFragment(functionIntroduction);
                break;


            case R.id.functionShelfButton:
                ToggleFunctionShelf(functionFragment);
                break;

            case R.id.operatorShelfButton:
                ToggleOperatorShelf(operatorFragment);
                break;


            case R.id.learn_intro_done:
                RemoveFragment(learnIntroDialog);
                algorithmFragment.HideTitle();
                break;

            case R.id.start_algorithm:
                RemoveFragment(firstScreenFragment);
                break;

            case R.id.back_to_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            default:
                    break;
        }

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


    //After the concept has performed the action of construction
    @Override
    public void CallbackAfterConcept() {

        algorithmFragment.UpdateAlgorithm( conceptController.NextBlock() );
        SetInstruction( conceptController.NextInstruction() );
        concept.SetBuildingBlocksBehaviour(conceptController.NextBlockBehaviour());
    }

    @Override
    public void OnFunctionFinish() {

        concept.OnFunctionFinish(conceptController.getCurrentFunctionName());
//        ChangeFunction();
        //Add previous function to functionFragment
        functionFragment.AddFunction(conceptController.getCurrentFunctionName(), conceptController.getCurrentFunctionIntroduction());

        /*Snackbar snackbar = Snackbar.make(layout, "Good engineering", Snackbar.LENGTH_SHORT);
        snackbar.show();*/

        nextFunction.setVisibility(View.VISIBLE);

    }

    @Override
    public void ChangeFunction() {

        int functionReturn  = conceptController.ChangeFunction(conceptController.getCurrentFunctionIndex() + 1);

        if(functionReturn == -1)
        {
            //Algorithm finished
            /*Toast.makeText(this, "Well done, you have learned LinkedList", Toast.LENGTH_SHORT).show();
            ConceptComplete conceptComplete = ConceptComplete.newInstance("", "");
            AddFragment(conceptComplete, "ConceptComplete");*/
            setContentView(R.layout.fragment_concept_complete);
            return;
        }

        SetupNewFunctionUI();
        BeginFunction();
        nextFunction.setVisibility(View.INVISIBLE);

    }

    @Override
    public void SetDataInputUI(int visibility) {
        dataInput.setVisibility(visibility);
    }



    void BeginFunction(){

        concept.PreFunction(conceptController.getCurrentFunctionName());
        SetInstruction( conceptController.NextInstruction() );
        algorithmFragment.ClearAlgorithm();
        concept.SetBuildingBlocksBehaviour(conceptController.NextBlockBehaviour());

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        functionIntroduction = FunctionIntroduction.newInstance(conceptController.getCurrentFunctionName(), conceptController.getCurrentFunctionIntroduction());
        fragmentTransaction.add(layout.getId(),functionIntroduction, "FunctionGirl");
        fragmentTransaction.commit();

    }

    void SetupNewFunctionUI(){

        SetInstruction("");
        functionName.setText(conceptController.getCurrentFunctionName());
        introduction.setText(conceptController.getCurrentFunctionIntroduction());
        algorithmFragment.setSequentialPseudocode(conceptController.getFunctionSequentialType());

    }

    void SetInstruction(String constructionStep){

        prevInstruction.setText(currentInstruction.getText());
        currentInstruction.setText(constructionStep);

    }

    void SetKeyboardInput(final EditText dataInput){

        dataInput.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId  == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE)
                {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    dataInput.setVisibility(View.INVISIBLE);
                    concept.PopulateView( dataInput.getText().toString());
                    CallbackAfterConcept();
                    return true;

                }

                return false;

            }

        });
    }


    void HideFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .hide(fragment)
                .commit();

        currentFragment = null;
    }

    void ShowFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .show(fragment)
                .commit();

        currentFragment = fragment;

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){

        /*super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hideSystemUI();
        }*/

    }

    private void hideSystemUI(){

        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.

    private void showSystemUI() {

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    @Override
    public void onFunctionSelect(String functionName, int functionIndex) {
        concept.MakeFunctionCall(functionName);
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void>{

        private final Context mContext;

        public BackgroundTask(Context context){
            mContext = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            conceptController = new ConceptController(mContext, conceptName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PostBackgroundTask();
        }
    }


    void RemoveFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    void AddFragment(Fragment fragment, String tag){

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.algorithm_builder_layout, fragment, tag);
        fragmentTransaction.commit();
    }
}
