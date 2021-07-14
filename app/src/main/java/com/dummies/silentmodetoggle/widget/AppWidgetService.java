package com.dummies.silentmodetoggle.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

import com.dummies.silentmodetoggle.R;
import com.dummies.silentmodetoggle.RingerHelper;
/*
The service that handles all your widget’s operations. The intent sent to the service
will tell it what you it want to do.
This service is an instance of IntentService. An IntentService is a convenient
way to handle things that need to be done on background threads. Whenever a new intent is
received, onHandleIntent executes in a background thread. This allows you to perform
whatever operations you want to in the background — no matter how long they might take —
without blocking the foreground UI thread (which would cause the app to hang).
 */

public class AppWidgetService extends IntentService {

    AudioManager audioManager;
    /*
    A flag that you set in your intent whenever you want to indicate that you want to
    toggle the phone’s silent setting.
     */
    private static String ACTION_DO_TOGGLE = "actionDoToggle";

    /*
    All IntentServices need to have a name. Ours is called
    AppWidgetService.
     */
    public AppWidgetService() {
        super("AppWidgetService");
    }

    @Override
    public void onCreate() {
    // Always call super.onCreate
        /*
        Just like in the activity, you’ll get a reference to Android’s AudioManager so
        you can use it to toggle our ringer.
         */
        super.onCreate();
        audioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        Checks the intent. If it says ACTION_DO_TOGGLE, then it toggles the phone’s
        Silent mode. If it doesn’t say ACTION_DO_TOGGLE, then this is just an update request, so
        it updates the UI.
         */
        if (intent != null && intent.getBooleanExtra(
                ACTION_DO_TOGGLE, false)) {
            RingerHelper.performToggle(audioManager);
            /*
            Gets a reference to Android’s AppWidgetManager, which is used to update the
            widget’s state.
             */
        } AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        /*
        Updates the widget’s UI. First, find the name for your widget, then ask the
        AppWidgetManager to update it using the views that you’ll construct in updateUi()
        in the next line.
         */
        ComponentName name =    new ComponentName(this, AppWidget.class);
        mgr.updateAppWidget(name, updateUi());
    }
    /*
        Returns the RemoteViews that is used to update the widget. Similar to
        updateUi() in MainActivity, but appropriate for use with widgets.
     */

    private RemoteViews updateUi() {
        /*
        Inflates the res/layout/app_widget.xml layout file into a RemoteViews
o         bject, which communicates with the widget.
         */
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.app_widget);
        /*
        Determines which image to use in the widget.
         */
        int phoneImage = RingerHelper.isPhoneSilent(audioManager)
                ? R.drawable.ringer_off : R.drawable.ringer_on;
        /*
        Sets the appropriate image.
         */
        remoteViews.setImageViewResource(R.id.phone_state, phoneImage);
        /*
        Creates an intent to toggle the phone’s state. This intent specifies
           ACTION_DO_TOGGLE=true, which you look for in onHandleIntent in if statement.
         */
        Intent intent = new Intent(this, AppWidgetService.class)
                .putExtra(ACTION_DO_TOGGLE, true);
        /*
        Wraps the intent in a PendingIntent, which gives someone in another process
        permission to send you an intent. In this case, the widget is actually running in another
        process (the device’s launcher process), so it must have a pending intent to communicate
        back into your service.
        You should specify FLAG_ONE_SHOT to this intent to ensure it is used only once.
         */
        PendingIntent pendingIntent =
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        /*
        Gets the layout for the app widget and attaches an on click listener to the button.
         */
        // changed from R.id.widget to this layout reference
        // maybe u could use R.id.icon_image that referring to the actual clickable image
        remoteViews.setOnClickPendingIntent(R.layout.app_widget, pendingIntent);
    return remoteViews;
    }
}