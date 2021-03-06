package com.example.benne.daisyapp2.playback

import android.support.v4.media.*
import android.support.v4.media.session.*
import com.example.benne.daisyapp2.data.*
import com.example.benne.daisyapp2.data.daisy202.*
import javax.inject.*

/**
 * Created by benne on 13/01/2018.
 */
class QueueManager @Inject constructor(
    private val mediaProvider: MediaProvider) {
    private var currentQueuedBook: String = ""
    private var currentIndex: Int = 0
    lateinit var metadataUpdateListener: MetadataUpdateListener
    var currentBook: DaisyBook? = null
    var currentNavElement: NavElement? = null
    var currentQueueMediaId: String = ""

    fun isSameBook(): Boolean {
        return false
    }

    fun setCurrentBook(bookMediaId: String) {
        currentBook = mediaProvider.books.find { it.toMediaId() == bookMediaId }
    }

    fun currentClip(): Pair<PlayableClip, MediaBrowserCompat.MediaItem>? {
        val books = mediaProvider.books

        // todo handle multiple matches
        val book = books.single {
            it.toMediaId() == currentQueueMediaId
                || it.navElements.any { it.toMediaId() == currentQueueMediaId }
        }

        // todo handle multiple matches
        val navElement = book.navElements.single { it.toMediaId() == currentQueueMediaId }

        currentBook = book
        currentNavElement = navElement
        val playableClip = mediaProvider.getPlayableClip(book.location, navElement)
        return Pair(playableClip!!, toMediaItem(navElement))
    }

    fun previousPlayableClip(): Pair<PlayableClip, MediaBrowserCompat.MediaItem>? {
        val book = currentBook

        val index = book?.navElements
            ?.indexOfFirst { it.toMediaId() == currentQueueMediaId }

        if (index == 0) {
            return null
        }

        // a next element is available
        for (i in (index!! - 1) downTo 0) {
            val nav = book.navElements[i]
            val playableClip = mediaProvider.getPlayableClip(book.location, nav)
            if (playableClip != null) {
                currentQueueMediaId = nav.toMediaId()
                currentNavElement = nav
                return Pair(playableClip, toMediaItem(nav))
            }
        }
        return null
    }

    fun nextPlayableClip(): Pair<PlayableClip, MediaBrowserCompat.MediaItem>? {
        val book = currentBook

        val index = book?.navElements
            ?.indexOfFirst { it.toMediaId() == currentQueueMediaId }

        // a next element is available
        for (i in index!! + 1 until book.navElements.count()) {
            val nav = book.navElements[i]
            val playableClip = mediaProvider.getPlayableClip(book.location, nav)
            if (playableClip != null) {
                currentQueueMediaId = nav.toMediaId()
                currentNavElement = nav

                return Pair(playableClip, toMediaItem(nav))
            }
        }
        return null
    }

//    fun updateMetadata() {
//        if (currentBook != null) {
//            val metadata = toMediaMetadata(currentBook!!, currentNavElement!!)
//            metadataUpdateListener.onMetadataChanged(metadata)
//        }
//    }

    interface MetadataUpdateListener {
        fun onMetadataChanged(metadata: MediaMetadataCompat?)
        fun onMetadataRetrieveError()
        fun onCurrentQueueIndexUpdated(queueIndex: Int)
        fun onQueueUpdated(title: String, newQueue: List<MediaSessionCompat.QueueItem>)
    }
}