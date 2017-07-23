package com.phannguyen.statusshare.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phannguyen.statusshare.datamodel.service.StatusModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phannguyen on 4/12/17.
 */

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static FirebaseHelper instance;
    private static final Object lock = new Object();
    private DatabaseReference fibDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth fibAuth = FirebaseAuth.getInstance();
    public static FirebaseHelper Instance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new FirebaseHelper();
            }
        }
        return instance;
    }

    public FirebaseHelper(){
       //
    }

    public DatabaseReference getNewStatusRef(){
        return fibDatabase.child(FirebaseConstant.NEW_STATUS_NODE);
    }

    public DatabaseReference getDeletedStatusRef(){
        return fibDatabase.child(FirebaseConstant.DELETED_STATUS_NODE);
    }

    public boolean isLogin(){
        return fibAuth.getCurrentUser() != null;
    }

    public String getUserNickName(){
        if(fibAuth.getCurrentUser()!=null){
            return fibAuth.getCurrentUser().getDisplayName();
        }
        return "Unknown user";
    }

    public String getUserEmail(){
        if(fibAuth.getCurrentUser()!=null){
            return fibAuth.getCurrentUser().getEmail();
        }
        return null;
    }

    public boolean isCurrentUser(String userEmail){
        return fibAuth.getCurrentUser()!=null
                && fibAuth.getCurrentUser().getEmail()!=null
                && fibAuth.getCurrentUser().getEmail().equals(userEmail);
    }
    public  void firebaseLogin(String email,String password,FibCallback<String> callback){
        fibAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Log.d(TAG,"Login ok");
                if(callback!=null)
                    callback.onSuccess("Login ok");
            }else{
                Log.d(TAG,"Login fail");
                if(task.getException()!=null && task.getException().getMessage()!=null) {
                    if(callback!=null)
                        callback.onError(task.getException().getMessage());
                }else {
                    if(callback!=null)
                        callback.onError("Login fail");
                }
            }
        });

    }

    public void logout(){
        fibAuth.signOut();
    }

    public void firebaseSignupAccount(final String nickname, String email, String password,FibCallback<String> callback){
        fibAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Log.d(TAG,"Sign up ok");
                if(fibAuth.getCurrentUser()!=null) {
                    //update nickname for this user
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .build();
                    fibAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(t->{
                                if (t.isSuccessful()) {
                                    Log.d(TAG, "User nickname updated.");
                                    if(callback!=null)
                                        callback.onSuccess("SignUp Ok");
                                }
                            });
                }

            }else{
                Log.d(TAG,"Sign up fail");
                if(task.getException()!=null && task.getException().getMessage()!=null) {
                    if(callback!=null)
                        callback.onError(task.getException().getMessage());
                }else {
                    if(callback!=null)
                        callback.onError("Create account fail!");
                }
            }
        });
    }

    public  String createNewStatus(StatusModel statusModel,FibCallback<StatusModel> callback){
        DatabaseReference statusRef = fibDatabase.child(FirebaseConstant.NEW_STATUS_NODE).push();
        statusModel.setStatusKeyId(statusRef.getKey());
        statusRef.setValue(statusModel.toMap(),(databaseError,databaseReference)-> {
            if(databaseError!=null) {
                Log.i(TAG, "save status error " + databaseError.getMessage());
                callback.onError(databaseError.getMessage());

            }else {
                Log.i(TAG, "save status complete ");
                databaseReference.child(FirebaseConstant.CREATED_SERVER_STAMP_FIELD).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                statusModel.setCreatedServerStamp(dataSnapshot.getValue(Long.class));
                                callback.onSuccess(statusModel);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        }
                );

            }
        });
        Log.d(TAG, "save status id "+statusModel.getStatusKeyId());
        return statusModel.getStatusKeyId();
    }

    public void deleteStatus(StatusModel statusModel){
        fibDatabase.child(FirebaseConstant.NEW_STATUS_NODE).child(statusModel.getStatusKeyId()).removeValue((databaseError,databaseReference)->{
            if(databaseError!=null){
                Log.i(TAG, "delete status error " + databaseError.getMessage());
            }else{
                Log.i(TAG, "delete status successfully");
            }
        });
        //create delete status data
        Map<String, Object> childUpdates = new HashMap<>();
        String nodePath  = String.format("/%s/%s",FirebaseConstant.DELETED_STATUS_NODE,statusModel.getStatusKeyId());
        childUpdates.put(nodePath, statusModel.toMap());
        fibDatabase.updateChildren(childUpdates);
    }
}
