package com.example.synchronousmusicapp;

import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class YesMediaBrowser extends MediaBrowserService {

    private MediaSession mediaSession;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSession(this, "AutoTestMediaSession");
        mediaSession.setActive(true);
        MediaSession.Token token = mediaSession.getSessionToken();
        setSessionToken(token);
    }
    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        BrowserRoot root = new BrowserRoot("test.id", null);
        return root;
    }

    @Override
    public void onLoadChildren( String rootId, Result<List<MediaBrowser.MediaItem>> listResult) {
        List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();

        MediaDescription.Builder builder = new MediaDescription.Builder();
        builder.setDescription("Media Item DESC");
        builder.setTitle("Media Item");
        builder.setMediaId("media item 1");

        MediaBrowser.MediaItem mediaItem = new MediaBrowser.MediaItem(builder.build(), MediaBrowser.MediaItem.FLAG_PLAYABLE);
        mediaItems.add(mediaItem);
        listResult.sendResult(mediaItems);
    }
}
