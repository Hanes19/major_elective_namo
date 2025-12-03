package com.elective.camera_ar_school_nav; // IMPORTANT: Use your actual package name

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// This line allows your Java code to send data back to Unity's C# environment
import com.unity3d.player.UnityPlayer;

public class UnityPlugin {
    private static final String TAG = "UnityPluginLog";
    private static final String GAME_OBJECT_NAME = "AndroidCallbackReceiver"; // The name of the GameObject in Unity to receive the message

    // --- Core Methods ---

    // 1. Method to show a simple Android Toast notification (No return value, uses Android context)
    public static void showToast(final String message) {
        // Get the current Activity to ensure UI operations run on the main thread
        final Activity currentActivity = UnityPlayer.currentActivity;

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Showing Toast: " + message);
                Toast.makeText(currentActivity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // 2. Method that returns a calculated integer value back to Unity (C#)
    public static int multiplyNumbers(int a, int b) {
        int result = a * b;
        Log.i(TAG, "Java calculated: " + a + " * " + b + " = " + result);
        return result;
    }

    // 3. Method to send a message back to a specific C# script in Unity
    public static void sendMessageToUnity(String javaMessage) {
        Log.i(TAG, "Sending message to Unity: " + javaMessage);

        // Use UnityPlayer.UnitySendMessage to call a method on a GameObject in the Unity scene.
        // Parameters: (GameObject Name, Method Name, Message String)
        UnityPlayer.UnitySendMessage(
                GAME_OBJECT_NAME,  // Name of the GameObject in Unity
                "ReceiveMessage",  // Name of the C# method to call
                "Data from Java: " + javaMessage
        );
    }
}