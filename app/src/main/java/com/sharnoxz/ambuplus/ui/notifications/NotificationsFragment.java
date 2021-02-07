package com.sharnoxz.ambuplus.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.sharnoxz.ambuplus.R;
import com.sharnoxz.ambuplus.adapter.NotificationAdapter;
import com.sharnoxz.ambuplus.data.NotificationData;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private final String TAG = "NotificationFragment";

    public static String documentId;
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final ListView listView = root.findViewById(R.id.NotificationListView);
        final TextView notificationText = root.findViewById(R.id.NotificationText);
        db.collection("Notification")
                //.whereEqualTo("userUid",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        NotificationData notificationData;
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            notificationData = new NotificationData(documentSnapshot.get("userProfile").toString(), documentSnapshot.get("notificationType").toString(),documentSnapshot.get("notificationText").toString(),documentSnapshot.get("notificationTime").toString(),documentSnapshot.getString("userUid"));
                            notificationDataArrayList.add(notificationData);
                        }

                        if(!notificationDataArrayList.isEmpty()) {
                            notificationText.setVisibility(View.GONE);
                            listView.setAdapter(new NotificationAdapter(root.getContext(), notificationDataArrayList));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        String notification = notificationDataArrayList.get(i).getNotificationText();
                                        notification = notification.substring(11);
                                        StringBuilder str = new StringBuilder();
                                        str.append(notification);
                                        notification = str.reverse().toString();
                                        notification = notification.substring(11);
                                        StringBuilder str1 = new StringBuilder();
                                        str1.append(notification);
                                        notification = str1.reverse().toString();
                                        Log.d(TAG, notification);
                                        Toast.makeText(root.getContext(),notification,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification));
                                        startActivity(intent);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        else{
                            notificationText.setText("No new Notification available");
                            notificationText.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
        return root;
    }
}