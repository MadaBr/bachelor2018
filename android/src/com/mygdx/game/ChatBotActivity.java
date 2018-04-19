package com.mygdx.game;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatBotActivity extends AppCompatActivity {
   // public EditText myMessage;
    //public Button sendToServerButton, dialogButton;
    public  AIService aiService;
    public AIDataService aiDataService;

    private RecyclerView recyclerView;
    private EditText userMessageET;
    private ImageView sendIMV;

    public DatabaseReference ref;
    private static List<ChatMessage> messages;
    public String userMessage;
    public FirebaseRecyclerAdapter<ChatMessage,RecyclerAdapter.RecyclerViewHolder> adapter;
    public AIConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);


        
        messages = new ArrayList<>();
        GetChatBotConnection Bot = new GetChatBotConnection();

        recyclerView = (RecyclerView) findViewById(R.id.chatbotRV);
        userMessageET = (EditText) findViewById(R.id.userMessageET) ;
        sendIMV = (ImageView) findViewById(R.id.sendIMV);

        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager LLayoutManager = new LinearLayoutManager(ChatBotActivity.this);
        //LLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(LLayoutManager);


        if(LoginActivity.studyingLanguage.equals("kor")) {
           config = new AIConfiguration("953eaff270be4a92a9486405b7d74599 ",
                    AIConfiguration.SupportedLanguages.Korean,
                    AIConfiguration.RecognitionEngine.System);
        }
        if(LoginActivity.studyingLanguage.equals("en")) {
           config = new AIConfiguration("953eaff270be4a92a9486405b7d74599 ",
                    AIConfiguration.SupportedLanguages.English,
                    AIConfiguration.RecognitionEngine.System);
        }
        if(LoginActivity.studyingLanguage.equals("fr")) {
            config = new AIConfiguration("953eaff270be4a92a9486405b7d74599 ",
                    AIConfiguration.SupportedLanguages.French,
                    AIConfiguration.RecognitionEngine.System);
        }
        if(LoginActivity.studyingLanguage.equals("de")) {
            config = new AIConfiguration("953eaff270be4a92a9486405b7d74599 ",
                    AIConfiguration.SupportedLanguages.German,
                    AIConfiguration.RecognitionEngine.System);
        }
        if(LoginActivity.studyingLanguage.equals("ro")) {
            config = new AIConfiguration("953eaff270be4a92a9486405b7d74599 ",
                    AIConfiguration.SupportedLanguages.Spanish, //instead of Romanian
                    AIConfiguration.RecognitionEngine.System);
        }

        aiService = AIService.getService(getApplicationContext(), config);
        aiService.setListener(Bot);
        aiDataService = new AIDataService(getApplicationContext(), config);


        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("AIChat").child(LoginActivity.current_user.getUid()).removeValue();
         adapter = new FirebaseRecyclerAdapter<ChatMessage, RecyclerAdapter.RecyclerViewHolder>(ChatMessage.class,R.layout.messages_layout,RecyclerAdapter.RecyclerViewHolder.class,ref.child("AIChat").child(LoginActivity.current_user.getUid())) {
            @Override
            protected void populateViewHolder(RecyclerAdapter.RecyclerViewHolder holder, ChatMessage model, int position) {
                if(model.getParticipant().equals("user")){
                    holder.userMessage.setVisibility(View.VISIBLE);
                    holder.userMessage.setText(model.getMessage());
                    holder.botMessage.setVisibility(View.INVISIBLE);
                    Log.wtf("TAG", "showed user message");
                }
                else{
                    holder.userMessage.setVisibility(View.INVISIBLE);
                    holder.botMessage.setVisibility(View.VISIBLE);
                    holder.botMessage.setText(model.getMessage());
                    Log.wtf("TAG", "showed bot message");
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCounter = adapter.getItemCount();
                int lastVisiblePosition = LLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCounter - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                 }
              }
        });
        recyclerView.setAdapter(adapter);


        sendIMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMessage = userMessageET.getText().toString();
                if( !userMessage.equals("")){
                    userMessageET.setText(""); //.child(LoginActivity.current_user.getUid())
                    ref.child("AIChat").child(LoginActivity.current_user.getUid()).push().setValue(new ChatMessage(userMessage, "user"));
                    AIRequest aiRequest = new AIRequest();
                    aiRequest.setQuery(userMessage);

                    GetChatBotConnection Bot = new GetChatBotConnection();
                    Log.wtf("TAG", "sending message");
                    Bot.execute(aiRequest);
                }
            }
        });

        /*sendToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageChatBot bot = new MessageChatBot();
                bot.execute(myMessage.getText().toString());
            }
        });*/

       /* dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AIRequest aiRequest = new AIRequest();
                aiRequest.setQuery(myMessage.getText().toString());
                Bot.execute(aiRequest);
            }
        });*/


    }




    public class GetChatBotConnection extends  AsyncTask<AIRequest, Void , AIResponse> implements AIListener{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            if (aiResponse != null) {

                Result result = aiResponse.getResult();
                String reply = result.getFulfillment().getSpeech();
                Log.wtf("TAG", reply);
                ref.child("AIChat").child(LoginActivity.current_user.getUid()).push().setValue(new ChatMessage(reply, "bot"));
                Log.wtf("TAG", "pushed reply to firebase");
                //botMessage.setText(reply);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests){
            try {
                Log.wtf("TAG", "here bg");
                aiService.textRequest(aiRequests[0]);
                AIResponse aiResponse  = aiDataService.request(aiRequests[0]);

                return aiResponse;

            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onResult(AIResponse result) {
            Result res = result.getResult();
            Log.wtf("TAG", res.getParameters().toString());
            String parameterString = "";
            if (res.getParameters() != null && !res.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : res.getParameters().entrySet()) {
                    parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";

                }
            }
        }


        @Override
        public void onError(AIError error) {

        }

        @Override
        public void onAudioLevel(float level) {

        }

        @Override
        public void onListeningStarted() {

        }

        @Override
        public void onListeningCanceled() {

        }

        @Override
        public void onListeningFinished() {

        }

    }


    /*  public class MessageChatBot extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                botMessage.setText(s);
            }
            else{
                botMessage.setText("null");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder builder = null;
            Log.wtf("tag", "async");
            try {
                URL url = new URL("https://tw2017-braha-madalina-madakattlyne.c9users.io:8081/chat");
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                Log.wtf("tag", "got conn");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("charset", "utf-8");
                conn.setDoOutput(true);
                DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
                String d = "{ \"text\": \"따뜻하다 " + strings[0] + "\"}";
                byte[] data = d.getBytes("UTF-8");
                ds.write(data);
                ds.flush();
                ds.close();

                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bfr = new BufferedReader(isr);
                String line="";
                builder = new StringBuilder();
                while((line=bfr.readLine())!=null){
                    builder.append(line);
                }
                bfr.close();
                isr.close();
                is.close();
                conn.disconnect();

            } catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            return builder.toString();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf("chatBOTACTIVITY","on destroy called");
        if(isFinishing()){
            Log.wtf("chatBOTACTIVITY","finish_called");
        }
        else{
            Log.wtf("chatBOTACTIVITY","system finished");
        }
    }
}
