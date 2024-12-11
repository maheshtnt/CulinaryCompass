package com.culinarycompass.hci.Fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.culinarycompass.hci.Adapters.MessageAdapter;
import com.culinarycompass.hci.Model.DishinfoModel;
import com.culinarycompass.hci.Model.Message;
import com.culinarycompass.hci.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatBotDialog extends BottomSheetDialogFragment {

    private static final String API_URL = "https://culinarybot.onrender.com/chatbot";

    private DishinfoModel dishInfo;
    private RecyclerView chatRecyclerView;
    private EditText userInput;
    private ImageButton sendButton;
    private Button presetButton;
    private ProgressBar progressBar;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private boolean isFirstMessage = true;

    public ChatBotDialog(DishinfoModel dishInfo) {
        this.dishInfo = dishInfo;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {

                DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
                int height = (int) (displayMetrics.heightPixels * 0.8);
                bottomSheet.getLayoutParams().height = height;

                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                bottomSheet.requestLayout();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot_dialog, container, false);

        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        userInput = view.findViewById(R.id.user_input);
        sendButton = view.findViewById(R.id.send_button);
        presetButton = view.findViewById(R.id.preset_button);
        progressBar = view.findViewById(R.id.loading_indicator);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(messageAdapter);
        presetButton.setText("");
        if(this.dishInfo != null) {
            presetButton.setText("Is this dish healthy for diabetics?");

            presetButton.setOnClickListener(v -> {
                String presetMessage = "Is this dish healthy for diabetics?";
                addMessage(new Message(presetMessage, Message.TYPE_USER));
                makeApiRequest(presetMessage);
                presetButton.setVisibility(View.GONE);
            });
        }
        else {
            presetButton.setText("What are some diabetic friendly Italian dishes?");

            presetButton.setOnClickListener(v -> {
                String presetMessage = "What are some diabetic friendly Italian dishes?";
                addMessage(new Message(presetMessage, Message.TYPE_USER));
                makeApiRequest(presetMessage);
                presetButton.setVisibility(View.GONE);
            });
        }

        sendButton.setOnClickListener(v -> {
            String query = userInput.getText().toString().trim();
            if (!query.isEmpty()) {
                addMessage(new Message(query, Message.TYPE_USER));
                userInput.setText("");
                makeApiRequest(query);
            }
        });

        return view;
    }

    private void makeApiRequest(String query) {
        showLoading(true);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("session_id", "user1268");
            requestBody.put("query", query);

            if (isFirstMessage && dishInfo != null) {
                JSONObject context = new JSONObject();
                context.put("name", dishInfo.getName());
                context.put("description", dishInfo.getDescription());
                context.put("ingredients", dishInfo.getIngredients());
                context.put("diabetic_rec", dishInfo.isDiabetic_rec());
                context.put("note", dishInfo.getNote());

                JSONObject nutritionalInfo = new JSONObject();
                nutritionalInfo.put("Calories", dishInfo.getNutritional_info().getCalories());
                nutritionalInfo.put("Protein", dishInfo.getNutritional_info().getProtein());
                nutritionalInfo.put("Carbohydrates", dishInfo.getNutritional_info().getCarbohydrates());
                nutritionalInfo.put("Fat", dishInfo.getNutritional_info().getFat());

                context.put("nutritional_info", nutritionalInfo);
                requestBody.put("context", context);

                isFirstMessage = false;
            }

        } catch (JSONException e) {
            addMessage(new Message("Error: Unable to create request", Message.TYPE_BOT));
            showLoading(false);
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                requestBody,
                response -> {
                    showLoading(false);
                    try {
                        if (response.has("response")) {
                            String botResponse = response.getString("response");
                            addMessage(new Message(botResponse, Message.TYPE_BOT));
                        } else {
                            addMessage(new Message("Bot: No response key in API response", Message.TYPE_BOT));
                        }
                    } catch (JSONException e) {
                        addMessage(new Message("Bot: Error parsing response", Message.TYPE_BOT));
                    }
                },
                error -> {
                    showLoading(false);
                    addMessage(new Message("Error: " + error.getMessage(), Message.TYPE_BOT));
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            sendButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            sendButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


    private void addMessage(Message message) {
        messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }
}
