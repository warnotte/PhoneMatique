package org.apps.wax.WaxPhoneMartyrisator;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by user on 6/01/2016.
 */
public class ThreadSend extends Thread {

    MainActivity ui;

    volatile boolean running = true;

    public ThreadSend(MainActivity ui) {
        this.ui = ui;
    }

    public void run() {
        Log.d("doAction", "start");

        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

        /* CALL A NUMERO
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:043669621"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);*/


        SmsManager smsManager = SmsManager.getDefault();

        ui.progressBar.setMax(ui.amount);

        setText("Waiting " + ui.delayBefore + " milliseconds...\r\n");
        try {
            Thread.sleep(ui.delayBefore);
        } catch (InterruptedException e) {
            appendText("Canceled\r\n");
            enableSendButton();
            return;
        }

        appendText("Starting...\r\n");


        for (int i = 0; i < ui.amount; i++) {
            if (running == false) break;
            final int j = i;

            if (ui.makeBeep==true)
                toneG.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 25);
            if (ui.testMode==false) {
                if (ui.dummyMessageOrUserMessage==true) {
                    String msg = generate();
                    appendText("Message is "+msg+"...\r\n");
                    smsManager.sendTextMessage(ui.number, null, msg, null, null);
                }
                else {
                    String msg = ui.message;
                    appendText("Message is "+msg+"...\r\n");
                    smsManager.sendTextMessage(ui.number, null, msg, null, null);
                }
            }

            appendText("Send message " + (j + 1) + "/" + ui.amount + " to " + ui.number + "\r\n");
            setProgress(j + 1);

            try {
                Thread.sleep(ui.delayBetween);
            } catch (InterruptedException e) {

                appendText("Canceled\r\n");
                enableSendButton();

                return;
            }
            //SystemClock.sleep(1000);
        }

        appendText("Finished\r\n");
        enableSendButton();
        Log.d("doAction", "stop");
    }

    private void enableSendButton() {
        ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui.parameterPanel.setVisibility(View.VISIBLE);
                ui.button_send.setEnabled(true);
                ui.progressBar.setProgress(0);
                ui.paneau_progression.setVisibility(View.GONE);
                ui.scroll_view_log.fullScroll(View.FOCUS_UP);
            }
        });
    }

    public void setProgress(final int progress) {
        ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui.progressBar.setProgress(progress);
            }
        });
    }

    public void appendText(final String text) {
        ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui.textArea.append(text);
                ui.scroll_view_log.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void setText(final String text) {
        ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui.textArea.setText(text);
                ui.scroll_view_log.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    public String generate()
    {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
