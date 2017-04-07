package com.example.destan.kezintagame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.zagum.switchicon.SwitchIconView;
import com.tomer.fadingtextview.FadingTextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    int duration;
    float screenWidth;
    boolean musicFlag;
    float inputTextSize;

    ArrayList wordCollection;//It stores the all of words
    List<String> wordListView;//It stores the all of the word that come from user
    List<ImageView> keyboardTextViews;

    MediaPlayer music;

    TextView inputText;

    FadingTextView fTextViewInfos;

    LinearLayout keyboardView;
    LinearLayout inputView;

    private SwitchIconView switchIcon1;
    private SwitchIconView switchIcon2;

    //Initializing view components,ArrayLists and some methods(read from raw,generate random word for start,find last char of first word)
    public void init(){

        inputText = (TextView)findViewById(R.id.textInput);
        inputTextSize = inputText.getTextSize();

        switchIcon1 = (SwitchIconView) findViewById(R.id.switchIconView1);
        switchIcon2 = (SwitchIconView) findViewById(R.id.switchIconView2);

        fTextViewInfos = (FadingTextView) findViewById(R.id.fadingTextView);

        keyboardView = (LinearLayout)findViewById(R.id.keyboardLayout);
        inputView = (LinearLayout)findViewById(R.id.inputLayout);

        keyboardTextViews = new ArrayList<>();

        keyboardTextViews.add((ImageView)findViewById(R.id.q));
        keyboardTextViews.add((ImageView)findViewById(R.id.w));
        keyboardTextViews.add((ImageView)findViewById(R.id.e));
        keyboardTextViews.add((ImageView)findViewById(R.id.r));
        keyboardTextViews.add((ImageView)findViewById(R.id.t));
        keyboardTextViews.add((ImageView)findViewById(R.id.y));
        keyboardTextViews.add((ImageView)findViewById(R.id.u));
        keyboardTextViews.add((ImageView)findViewById(R.id.ii));
        keyboardTextViews.add((ImageView)findViewById(R.id.o));
        keyboardTextViews.add((ImageView)findViewById(R.id.p));
        keyboardTextViews.add((ImageView)findViewById(R.id.gi));
        keyboardTextViews.add((ImageView)findViewById(R.id.ui));
        keyboardTextViews.add((ImageView)findViewById(R.id.a));
        keyboardTextViews.add((ImageView)findViewById(R.id.s));
        keyboardTextViews.add((ImageView)findViewById(R.id.d));
        keyboardTextViews.add((ImageView)findViewById(R.id.f));
        keyboardTextViews.add((ImageView)findViewById(R.id.g));
        keyboardTextViews.add((ImageView)findViewById(R.id.h));
        keyboardTextViews.add((ImageView)findViewById(R.id.j));
        keyboardTextViews.add((ImageView)findViewById(R.id.k));
        keyboardTextViews.add((ImageView)findViewById(R.id.l));
        keyboardTextViews.add((ImageView)findViewById(R.id.si));
        keyboardTextViews.add((ImageView)findViewById(R.id.i));
        keyboardTextViews.add((ImageView)findViewById(R.id.z));
        keyboardTextViews.add((ImageView)findViewById(R.id.x));
        keyboardTextViews.add((ImageView)findViewById(R.id.c));
        keyboardTextViews.add((ImageView)findViewById(R.id.v));
        keyboardTextViews.add((ImageView)findViewById(R.id.b));
        keyboardTextViews.add((ImageView)findViewById(R.id.n));
        keyboardTextViews.add((ImageView)findViewById(R.id.m));
        keyboardTextViews.add((ImageView)findViewById(R.id.oi));
        keyboardTextViews.add((ImageView)findViewById(R.id.ci));

        wordCollection = new ArrayList<>();
        wordListView = new ArrayList<>();

        musicFlag = false;

        readFromRaw();
    }

    //Check the word if it exist and it's not repeated
    public boolean checkWord(String word){

        if(wordCollection.contains(word) && !wordListView.contains(word))
            return true;
        else
            return false;

    }


    //Read the .txt file line by line and store in wordCollection which type is ArrayList
    public void readFromRaw(){

        try   {
            InputStream fis = this.getResources().openRawResource(R.raw.turkish_db);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = br.readLine()) != null)
                wordCollection.add(line);

            //listWords.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wordCollection));
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /*
    * runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    // change UI elements here
                    tv.setTextSize(currentTextSize - 1);
                }
            });
     */

    private String backSpace(String str){
        char[] ch = str.toCharArray();
        str = "";
        for(int i = 0; i < ch.length - 1 ; i++)
            str += ch[i];

        return str;
    }

    private void increaseTextSize(TextView tv){
        float currentTextSize = tv.getTextSize();
        if(currentTextSize < inputTextSize)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentTextSize + 3.5F);
    }

    private void decreaseTextSize(TextView tv){
        float currentTextSize = tv.getTextSize();
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentTextSize - 3.5F);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        this.init();

        Toast.makeText(MainActivity.this,"Screen width:" + screenWidth,Toast.LENGTH_SHORT).show();

        switchIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIcon1.switchState();
            }
        });

        switchIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIcon2.switchState();
                if(!switchIcon2.isIconEnabled() && music.isPlaying()){
                    duration = music.getCurrentPosition();
                    music.pause();
                }else{
                    music.seekTo(duration);
                    music.start();
                }

            }
        });

        //This listener is used to delete last character.
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.i("User Action","Delete the last char");
                    inputText.setText(backSpace(inputText.getText().toString()));
                    increaseTextSize(inputText);
            }
        });

        //This listener is used to listen keys to write on board.
        for (final ImageView Image : keyboardTextViews){
            /*textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("User action","Appending: " + textView.getText().toString());
                    inputText.setText(inputText.getText().toString() + textView.getText().toString());
                    decreaseTextSize(inputText);
                }
            });*/
        }

/*

        ArrayAdapter<String> adapterGrid = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, letters);
        final ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,R.layout.my_text,R.id.textItem,wordListView);

        gridKeyboard.setAdapter(adapterGrid);
        listViewInputs.setAdapter(adapterList);

        textGuess.setText("");

        gridKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!letters[position].equals("←") && !letters[position].equals("↪")) {

                    guess += letters[position];
                    guess = guess.toLowerCase();
                    textGuess.setText(guess);


                } else if (letters[position].equals("←")) {

                    if (!guess.equals("")) {

                        guess = guess.replaceFirst(String.valueOf(guess.charAt(guess.length() - 1)), "");
                        textGuess.setText(guess);

                    } else {

                        Toast.makeText(MainActivity.this, "No letter to delete", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(!guess.equals("")){
                        if(guess.startsWith(findLastChar(wordListView)) && checkWord(guess)){

                            wordListView.add(guess);
                            listViewInputs.setAdapter(adapterList);
                            textGuess.setText("");

                            Toast.makeText(MainActivity.this,
                                    "RIGHT " + "Last Char:" + findLastChar(wordListView),
                                     Toast.LENGTH_LONG).show();

                            guess = "";



                        }else{
                            Toast.makeText(MainActivity.this,
                                    "WRONG " + "Last Char:" + findLastChar(wordListView),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(MainActivity.this, "I can see no word", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        */
    }

    @Override
    protected void onStart(){
        super.onStart();
        music = MediaPlayer.create(MainActivity.this,R.raw.gamemod2);
        music.setLooping(true);
        music.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        duration = music.getCurrentPosition();
        music.pause();
        musicFlag = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(musicFlag) {
            music.seekTo(duration);
            music.start();
        }
    }
    @Override
    public void onBackPressed(){
        Intent goToMenuActivity = new Intent(MainActivity.this, MenuActivity.class);
        MainActivity.this.finish();
        startActivity(goToMenuActivity);
    }
}
