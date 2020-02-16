package ru.kolesnikovdmitry.lesson7firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivity extends AppCompatActivity {
// Перед тем, как отправлять сообщения в базу данных, нужно не забыть в разделе "правила" на сайте firebase
// установить параметры "rules": {
//    ".read": true,
//    ".write": true
// в параметрах правил, чтобы датабейс вообще реагировала на наши отправки и запросы))0)


    final int REQUEST_CODE_REGISTRATION_ACTIVITY = 228;
    CoordinatorLayout mCoordLay;
    View mViewHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            mCoordLay   = findViewById(R.id.coordinatorLayMain);
            mViewHelper = mCoordLay;
        }catch (Throwable th) {
            Toast.makeText(getApplicationContext(), th.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        //если пользователь не авторизован, то...
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                    REQUEST_CODE_REGISTRATION_ACTIVITY);
        }
        else {
            Snackbar.make(mViewHelper, "Вы успешно вошли!", Snackbar.LENGTH_SHORT).show();
            displayAllMessages();
        }

        ImageView emojiButton = findViewById(R.id.emojiButton);
        EmojiconEditText editText = findViewById(R.id.editTextMessageField);
        EmojIconActions emojIconActions = new EmojIconActions(getApplicationContext(), mCoordLay, editText, emojiButton);
        emojIconActions.ShowEmojIcon();
    }


    private void displayAllMessages() {
        ListView listOfMess = findViewById(R.id.listOfMessages);
        //ImageButton  imgBtnLikeImg    = new ImageButton(getApplicationContext());
        //boolean      imgBtnId         = model.getLikeMessage();
        //lay.addView(imgBtnLikeImg);
        /*v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //imgBtnLikeImg.setVisibility(View.VISIBLE);
                        //v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(getApplicationContext(), model.getTextMessage().toString(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });*/
        /*if(model.getLikeMessage()) {
                    imgBtnLikeImg.setImageResource(R.drawable.ic_favorite_black_24dp);
                }*/
        FirebaseListAdapter<Message> messAdapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.message_item,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, final Message model, final int position) {
                TextView textViewUserName = v.findViewById(R.id.textNameMessageItem);
                BubbleTextView textViewText = v.findViewById(R.id.textMessageItem);
                TextView textViewTime = v.findViewById(R.id.timeMessageItem);
                //ImageButton  imgBtnLikeImg    = new ImageButton(getApplicationContext());
                //boolean      imgBtnId         = model.getLikeMessage();
                LinearLayout lay = v.findViewById(R.id.itemMessageLayout);
                //lay.addView(imgBtnLikeImg);

                /*v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //imgBtnLikeImg.setVisibility(View.VISIBLE);
                        //v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(getApplicationContext(), model.getTextMessage().toString(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });*/

                /*if(model.getLikeMessage()) {
                    imgBtnLikeImg.setImageResource(R.drawable.ic_favorite_black_24dp);
                }*/
                textViewUserName.setText(model.getUserName());
                textViewText.setText(model.getTextMessage());
                textViewTime.setText(DateFormat.format("dd-mm-yyyy\nHH:mm:ss", model.getTime()));
            }
        };

        /*listOfMess.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //ImageButton imgBtnHeart = view.findViewById(R.id.imageButtonLikeMessageItem);
                //imgBtnHeart.setFocusable(false);
                //String kek = String.valueOf(position);
                //Toast.makeText(getApplicationContext(),  , Toast.LENGTH_SHORT).show();
                //imgBtnHeart.setVisibility(View.VISIBLE);
                return true;
            }
        });*/
        listOfMess.setAdapter(messAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_REGISTRATION_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    Snackbar.make(mViewHelper, "Вы авторизовались", Snackbar.LENGTH_SHORT);
                    displayAllMessages();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Ты лох, гуд бай", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    public void onClickMainActivity(View view) {
        switch (view.getId()) {
            case R.id.buttonSend:
                EditText mEditTextMessage = findViewById(R.id.editTextMessageField);
                if(mEditTextMessage.getText().toString().equals("")) {
                    break;
                }
                String userName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();  //берем адрес электронной почты текущего пользователя
                String text = mEditTextMessage.getText().toString(); //берем текст из поля едит текст
                if(userName == null) {
                    userName = "No Name";
                }
                Message messSend = new Message(userName, text);

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //String key = db.child("lesson-7firebase").push().getKey();

                FirebaseDatabase.getInstance().getReference().push().setValue(messSend);
                mEditTextMessage.setText("");
                break;
            default:
                break;
        }
    }
}
