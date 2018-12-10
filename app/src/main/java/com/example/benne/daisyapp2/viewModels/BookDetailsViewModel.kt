package com.example.benne.daisyapp2.viewModels

import android.arch.lifecycle.*
import android.support.v4.media.*
import android.util.Log
import com.example.benne.daisyapp2.*
import com.example.benne.daisyapp2.ui.bookList.*

/**
 * Created by benne on 10/01/2018.
 */
class BookDetailsViewModel (val mediaSessionConnection: MediaSessionConnection, mediaId: String)
    : ViewModel() {

    val bookSections: MutableLiveData<List<MediaBrowserCompat.MediaItem>>
            = MutableLiveData<List<MediaBrowserCompat.MediaItem>>()
                .also { it.value = listOf() }

    val playSectionCommand = SingleLiveEvent<MediaBrowserCompat.MediaItem>()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
            Log.d(TAG, "children loaded for parent $parentId items: ${children.count()}")
            bookSections.postValue(children)
        }
    }

    init {
        mediaSessionConnection.subscribe(mediaId, subscriptionCallback)
    }

    fun playSection(item: MediaBrowserCompat.MediaItem) {
        mediaSessionConnection.playMedia(item)
    }

    fun selectNavSection() {

    }

    class Factory(private val mediaSessionConnection: MediaSessionConnection,
                  private val mediaId: String
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BookDetailsViewModel(mediaSessionConnection, mediaId) as T
        }
    }

    companion object {
        val TAG: String = BookDetailsViewModel::class.java.simpleName
    }
}