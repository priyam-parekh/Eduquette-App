package com.eduquette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.eduquette.model.IndexDeck;
import com.eduquette.model.User;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;

/**
 * Activity class for the Splash screen
 */
public class SplashActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "SharedPreference";

    // Instance variables for all the activity components
    private RelativeLayout rlWelcome, rlNewUser;
    private LinearLayout llAskName;
    private TextView tvWelcome;
    private EditText etUserName;
    private User user;

    /**
     * Overridden method that is called when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button bNewUser, bLetsGo;

        // Initialize all the buttons and panel components
        rlWelcome = findViewById(R.id.rl_welcome);
        rlNewUser = findViewById(R.id.rl_new_user);
        llAskName = findViewById(R.id.ll_ask_name);
        tvWelcome = findViewById(R.id.tv_welcome);
        etUserName = findViewById(R.id.et_user_name);
        bNewUser = findViewById(R.id.b_new_user);
        bLetsGo = findViewById(R.id.b_lets_go);

        // Load the user and the resources
        loadUser();
        loadAllSubjectDataAndInitialize(R.raw.class.getDeclaredFields());

        // Configure the listeners for the buttons and text box
        bNewUser.setOnClickListener(new NewUserListener());
        bLetsGo.setOnClickListener(new LetsGoListener());
        etUserName.setOnEditorActionListener(new NewUserActionListener());

        // Show the splash screen for 3 seconds
        Handler handler = new Handler();
        handler.postDelayed(new SplashRunnable(), 3000);
    }

    /**
     * This method loads all the raw resources (subjects and index cards) from the project files
     */
    private void loadAllSubjectDataAndInitialize(Field[] subjects) {
        Resources resources = getResources();
        for (int i = 0; i < subjects.length; i++) {
            Field subject = subjects[i];
            InputStream raw = resources.openRawResource(resources.getIdentifier(subject.getName(), "raw", getPackageName()));
            Reader reader = new InputStreamReader(raw);

            Gson gson = new Gson();
            // Create a index deck from the file
            IndexDeck deck = gson.fromJson(reader, IndexDeck.class);
            deck.setThumbnail(resources.getIdentifier(deck.getImage(), "drawable", this.getPackageName()));
            deck.refreshCount();

            // Add the deck to the data controller
            DataController.getIndexDecks().put(deck.getSubject(), deck);
        }
    }

    /**
     * This method closes the keyboard
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * This method loads the user object from the shared preferences
     */
    private void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // Create a new User object based on the name in the shared preferences
        user = new User(sharedPreferences.getString(User.ID, "Stranger"));
        if (user.getName().compareToIgnoreCase("stranger") == 0) {
            tvWelcome.setText(R.string.welcome);
            etUserName.setText("");
        } else {
            tvWelcome.setText(String.format(getString(R.string.welcome_user_format), user.getName()));
            etUserName.setText(user.getName());
        }
    }

    /**
     * This method saves the user object based on the text in the name text box
     */
    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (etUserName.getText().toString().isEmpty()) {
            editor.putString(User.ID, getString(R.string.default_name));
            user.setName(getString(R.string.default_name));
        } else {
            editor.putString(User.ID, etUserName.getText().toString());
            user.setName(etUserName.getText().toString());
        }
        editor.apply();

    }

    /**
     * Method that is called when the screen comes back in focus
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Update the welcome text
        if (user.getName().compareToIgnoreCase("stranger") == 0) {
            // If there is no user name then show the askName panel
            tvWelcome.setText(R.string.welcome);
            llAskName.setVisibility(View.VISIBLE);
        } else {
            // If there is a user name then hide the askName panel
            tvWelcome.setText(String.format(getString(R.string.welcome_user_format), user.getName()));
            llAskName.setVisibility(View.GONE);
        }
    }

    /**
     * Class to handle the click action of the LetsGo button
     */
    public class LetsGoListener implements View.OnClickListener {

        /**
         * Overriden method that is called when the user clicks on the letsgo button
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // Close the keyboard
            closeKeyboard();

            // Save the user object
            saveUser();

            // Hide the AskName panel
            llAskName.setVisibility(View.GONE);

            // Start the IndexDeck activity
            Intent myIntent = new Intent(v.getContext(), IndexDeckActivity.class);
            myIntent.putExtra(User.ID, user);
            startActivityForResult(myIntent, 0);
        }
    }

    /**
     * Class to handle the click action of the new user button
     */
    public class NewUserListener implements View.OnClickListener {

        /**
         * Overriden method that is called when the user clicks on the new user button
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            tvWelcome.setText(R.string.welcome);
            llAskName.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Class to handle the new user name entry
     */
    private class NewUserActionListener implements TextView.OnEditorActionListener {

        /**
         * Overriden method that is called when the user enters a new user name
         *
         * @param v
         * @param actionId
         * @param event
         * @return
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // Close the keyboard
            if ((actionId == EditorInfo.IME_ACTION_DONE) || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    /**
     * Class to handle the splash screen
     */
    private class SplashRunnable implements Runnable {

        /**
         * Overriden method that is called when the thread runs
         */
        @Override
        public void run() {
            rlWelcome.setVisibility(View.VISIBLE);
            rlNewUser.setVisibility(View.VISIBLE);
        }
    }
}
