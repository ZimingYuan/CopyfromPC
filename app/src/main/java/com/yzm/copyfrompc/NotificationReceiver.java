package com.yzm.copyfrompc;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationReceiver extends BroadcastReceiver {

    private ClipboardManager clipboard;
    private Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(intent.getStringExtra("address")).build();
        clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        mcontext = context;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("fuck", e.getMessage());
                Looper.prepare();
                Toast.makeText(mcontext, "Copy failed!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ClipData clip = ClipData.newPlainText("text", response.body().string());
                clipboard.setPrimaryClip(clip);
                Looper.prepare();
                Toast.makeText(mcontext, "Copy successfully!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}
