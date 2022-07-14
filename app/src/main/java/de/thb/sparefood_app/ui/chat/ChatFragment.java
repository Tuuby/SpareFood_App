package de.thb.sparefood_app.ui.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentChatBinding;

import de.thb.sparefood_app.ui.chat.ChatClient;



public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    public List<Message> MessageList;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel ChatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        MessageList = new ArrayList<>();

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);

        MaterialButton backButton = binding.backButton;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_notifications);
            }
        });

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        navView.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.hide();


        try {
            ChatClient dummyClient = new ChatClient();
            dummyClient.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMessageRecycler = (RecyclerView) binding.recyclerGchat;
        MessageList.add(new Message("luedrick@th-brandenburg.de", "Hi Christian, Ich würd gern was von deinem Curry abholen kommen."));
        MessageList.add(new Message("trompbell@th-brandenburg.de", "Hey Roman, Cool! Kannst du ne feste Dose ohne Löcher mitbringen?"));
        MessageList.add(new Message("luedrick@th-brandenburg.de", "Jo kann ich machen"));
        MessageList.add(new Message("trompbell@th-brandenburg.de", "Ich bin erst um 17 Uhr Zuhause, passt heute Abend 17.30 Uhr für dich?"));
        MessageList.add(new Message("luedrick@th-brandenburg.de", "Das passt. Kannst du mir bitte die Adresse sagen ?"));
        MessageList.add(new Message("trompbell@th-brandenburg.de", "Milchstraße 43 10249 Berlin"));
        MessageList.add(new Message("luedrick@th-brandenburg.de", "danke, bis dann"));

        mMessageAdapter = new MessageListAdapter(getActivity(), MessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessageRecycler.setAdapter(mMessageAdapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        navView.setVisibility(View.VISIBLE);
        bottomAppBar.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.show();
    }


    public class MessageListAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

        private Context mContext;
        private List<Message> mMessageList;

        public MessageListAdapter(Context context, List<Message> messageList) {
            mContext = context;
            mMessageList = messageList;
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        // Determines the appropriate ViewType according to the sender of the message.
        @Override
        public int getItemViewType(int position) {
            Message message = (Message) mMessageList.get(position);

            if (isSender(message.getRecipient())) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        private boolean isSender(String Recipient) {
            if(Recipient == "luedrick@th-brandenburg.de") {
                return true;
            } else {
                return false;
            }
        }

        // Inflates the appropriate layout according to the ViewType.
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new MessageListAdapter.SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new MessageListAdapter.ReceivedMessageHolder(view);
            }

            return null;
        }

        // Passes the message object to a ViewHolder so that the contents can be bound to UI.
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message message = (Message) mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((MessageListAdapter.SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((MessageListAdapter.ReceivedMessageHolder) holder).bind(message);
            }
        }

        private class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, dateText, timeStampText;

            SentMessageHolder(View itemView) {
                super(itemView);


                messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
                dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_me);
                timeStampText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            }

            void bind(Message message) {
            messageText.setText(message.getContent());

                // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                dateText.setText(currentDate);
                timeStampText.setText(currentTime);
            }
        }

        private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, dateText, timeStampText, nameText;
//            ImageView profileImage;

            ReceivedMessageHolder(View itemView) {
                super(itemView);

                messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
                dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_other);
                timeStampText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
//                nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
//                profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            }

            void bind(Message message) {
                messageText.setText(message.getContent());

                // Format the stored timestamp into a readable String using method.
//            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
//                timeText.setText(Calendar.getInstance().getTime().toString());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                dateText.setText(currentDate);
                timeStampText.setText(currentTime);


//            nameText.setText(message.getSender().getNickname());
//                nameText.setText("SenderNickname");

                // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
            }
        }
    }


}