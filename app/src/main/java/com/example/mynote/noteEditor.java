package com.example.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mynote.constants.Constants;
import com.example.mynote.dao.DBhandler;
import com.example.mynote.models.Note;

import static android.content.ClipDescription.MIMETYPE_TEXT_HTML;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;


public class noteEditor extends AppCompatActivity{
    EditText content;
    EditText title;
    Button savebtn;
    TextView date;
    Button discard;
    Button dltbtn;
    Button sharebtn;
    Switch modebtn;
    Button copybtn;
    Button pastebtn;
    private int ID;
    private boolean isnew;
    private String DT;
    KeyListener keyon;

    public class notemaking extends AsyncTask<Void,Void,Void>{
        private String title;
        private String content;

        public notemaking(String title,String content){
            this.content=content;
            this.title= title;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DBhandler db = new DBhandler(noteEditor.this);
            Note note = new Note(ID,title,content,DT);
            Log.d("time",": "+DT);
            if(isnew)
            {
                db.addNote(note);
            }
            else{
                boolean result= db.updateNote(note);
                if (result){Log.d("editor","note updated "+ ID);
                }}
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(noteEditor.this, "Saved Sucessfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        final Intent intent = getIntent();
        ID = intent.getIntExtra("id",0);
        isnew = intent.getBooleanExtra("isnew",false);
        content = findViewById(R.id.noteEditorContent);
        title = findViewById(R.id.noteEditorTitle);
        DT = Constants.getTime();
        modebtn = findViewById(R.id.mode);
        savebtn = findViewById(R.id.save);
        discard = findViewById(R.id.discard);
        dltbtn = findViewById(R.id.delete);
        sharebtn = findViewById(R.id.share);
        copybtn = findViewById(R.id.copy);
        pastebtn = findViewById(R.id.paste);
        keyon = content.getKeyListener();

        if(isnew)
        {
            modebtn.setAlpha(0.0f);
            modebtn.setEnabled(false);
            sharebtn.setEnabled(false);
            copybtn.setEnabled(false);
            change_mode(true);
        }
        else{
            title.setText(intent.getStringExtra("title"));
            content.setText(intent.getStringExtra("content"));
            content.setKeyListener(null);
            title.setKeyListener(null);
            date = findViewById(R.id.date);
            date.setText(intent.getStringExtra("datetime"));
            change_mode(false);
        }
        final String previous_title = title.getText().toString();
        final String previous_content = content.getText().toString();
        modebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //mode true  : edit mode ON
         // mode false : edit mode OFF
                if(modebtn.isChecked()) {
                    change_mode(true);
                    modebtn.setText("Edit Mode");
                }
                else {
                    change_mode(false);
                    modebtn.setText("View Mode");
                }
                }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_title = title.getText().toString();
                String current_content = content.getText().toString();
                if (current_title.length()!=0 || current_content.length()!=0 ) {
                    if ((previous_title.equals(current_title)) && (previous_content.equals(current_content)))
                        Toast.makeText(noteEditor.this, "N0 Change", Toast.LENGTH_SHORT).show();
                     else{
                        notemaking maker = new notemaking(current_title, current_content);
                        maker.execute();
                        change_activity();
                       // Log.d("new","new note from editor");
                    }
                }else
                    Toast.makeText(noteEditor.this, "Note Must Not Be Empty!", Toast.LENGTH_SHORT).show();
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modebtn.isChecked()) {
                    content.setText(previous_content);
                    title.setText(previous_title);
                    modebtn.setChecked(false);

                }
               else
                   change_activity();
            }
        });

        dltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","delete"+ID);
                DBhandler db = new DBhandler(noteEditor.this);
                db.deleteNote(ID);
                change_activity();
            }
        });
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharing = new Intent(Intent.ACTION_SEND);
                sharing.setType("text/plain");
                sharing.putExtra(Intent.EXTRA_SUBJECT,title.getText().toString());
                sharing.putExtra(Intent.EXTRA_TEXT,content.getText().toString());
                startActivity(Intent.createChooser(sharing,"Share Via"));
            }
        });

        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copy = title.getText().toString() + "\n"+ content.getText().toString();
                if(copy.length()!=0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",copy);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(noteEditor.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(noteEditor.this, "Nothing to Copy! ", Toast.LENGTH_SHORT).show();
            }
        });
        pastebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip() && (clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN) || clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_HTML)))
                {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    String topaste = content.getText().toString() + item.getText().toString();
                    content.setText(topaste);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
           change_activity();
        }
        return super.onKeyDown(keyCode, event);
    }
    void change_activity()
    {
        Intent intent = new Intent(noteEditor.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    void change_mode(boolean mode)
    {
        if(mode){
            title.setKeyListener(keyon);
            content.setKeyListener(keyon);
            sharebtn.setEnabled(false);
            discard.setEnabled(true);
            pastebtn.setEnabled(true);
        }
        else{
            title.setKeyListener(null);
            content.setKeyListener(null);
            sharebtn.setEnabled(true);
            discard.setEnabled(false);
            pastebtn.setEnabled(false);
        }
    }

}
