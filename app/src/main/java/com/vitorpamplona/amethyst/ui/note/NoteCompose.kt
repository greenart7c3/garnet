/**
 * Copyright (c) 2024 Vitor Pamplona
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.vitorpamplona.amethyst.ui.note

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.vitorpamplona.amethyst.R
import com.vitorpamplona.amethyst.model.Channel
import com.vitorpamplona.amethyst.model.FeatureSetType
import com.vitorpamplona.amethyst.model.Note
import com.vitorpamplona.amethyst.ui.components.GenericLoadable
import com.vitorpamplona.amethyst.ui.components.ObserveDisplayNip05Status
import com.vitorpamplona.amethyst.ui.components.RobohashFallbackAsyncImage
import com.vitorpamplona.amethyst.ui.layouts.GenericRepostLayout
import com.vitorpamplona.amethyst.ui.navigation.routeFor
import com.vitorpamplona.amethyst.ui.note.elements.BoostedMark
import com.vitorpamplona.amethyst.ui.note.elements.DisplayEditStatus
import com.vitorpamplona.amethyst.ui.note.elements.DisplayFollowingCommunityInPost
import com.vitorpamplona.amethyst.ui.note.elements.DisplayFollowingHashtagsInPost
import com.vitorpamplona.amethyst.ui.note.elements.DisplayLocation
import com.vitorpamplona.amethyst.ui.note.elements.DisplayOts
import com.vitorpamplona.amethyst.ui.note.elements.DisplayPoW
import com.vitorpamplona.amethyst.ui.note.elements.DisplayReward
import com.vitorpamplona.amethyst.ui.note.elements.DisplayZapSplits
import com.vitorpamplona.amethyst.ui.note.elements.MoreOptionsButton
import com.vitorpamplona.amethyst.ui.note.elements.Reward
import com.vitorpamplona.amethyst.ui.note.elements.ShowForkInformation
import com.vitorpamplona.amethyst.ui.note.elements.TimeAgo
import com.vitorpamplona.amethyst.ui.note.types.DisplayPeopleList
import com.vitorpamplona.amethyst.ui.note.types.DisplayRelaySet
import com.vitorpamplona.amethyst.ui.note.types.EditState
import com.vitorpamplona.amethyst.ui.note.types.FileHeaderDisplay
import com.vitorpamplona.amethyst.ui.note.types.FileStorageHeaderDisplay
import com.vitorpamplona.amethyst.ui.note.types.RenderAppDefinition
import com.vitorpamplona.amethyst.ui.note.types.RenderAudioHeader
import com.vitorpamplona.amethyst.ui.note.types.RenderAudioTrack
import com.vitorpamplona.amethyst.ui.note.types.RenderBadgeAward
import com.vitorpamplona.amethyst.ui.note.types.RenderClassifieds
import com.vitorpamplona.amethyst.ui.note.types.RenderEmojiPack
import com.vitorpamplona.amethyst.ui.note.types.RenderFhirResource
import com.vitorpamplona.amethyst.ui.note.types.RenderGitIssueEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderGitPatchEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderGitRepositoryEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderHighlight
import com.vitorpamplona.amethyst.ui.note.types.RenderLiveActivityEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderLongFormContent
import com.vitorpamplona.amethyst.ui.note.types.RenderPinListEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderPoll
import com.vitorpamplona.amethyst.ui.note.types.RenderPostApproval
import com.vitorpamplona.amethyst.ui.note.types.RenderPrivateMessage
import com.vitorpamplona.amethyst.ui.note.types.RenderReaction
import com.vitorpamplona.amethyst.ui.note.types.RenderReport
import com.vitorpamplona.amethyst.ui.note.types.RenderTextEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderTextModificationEvent
import com.vitorpamplona.amethyst.ui.note.types.RenderWikiContent
import com.vitorpamplona.amethyst.ui.note.types.VideoDisplay
import com.vitorpamplona.amethyst.ui.screen.loggedIn.AccountViewModel
import com.vitorpamplona.amethyst.ui.screen.loggedIn.ChannelHeader
import com.vitorpamplona.amethyst.ui.theme.DoubleHorzSpacer
import com.vitorpamplona.amethyst.ui.theme.DoubleVertSpacer
import com.vitorpamplona.amethyst.ui.theme.HalfDoubleVertSpacer
import com.vitorpamplona.amethyst.ui.theme.HalfPadding
import com.vitorpamplona.amethyst.ui.theme.Size25dp
import com.vitorpamplona.amethyst.ui.theme.Size30Modifier
import com.vitorpamplona.amethyst.ui.theme.Size34dp
import com.vitorpamplona.amethyst.ui.theme.Size55Modifier
import com.vitorpamplona.amethyst.ui.theme.Size55dp
import com.vitorpamplona.amethyst.ui.theme.StdHorzSpacer
import com.vitorpamplona.amethyst.ui.theme.StdVertSpacer
import com.vitorpamplona.amethyst.ui.theme.UserNameMaxRowHeight
import com.vitorpamplona.amethyst.ui.theme.UserNameRowHeight
import com.vitorpamplona.amethyst.ui.theme.WidthAuthorPictureModifier
import com.vitorpamplona.amethyst.ui.theme.boostedNoteModifier
import com.vitorpamplona.amethyst.ui.theme.channelNotePictureModifier
import com.vitorpamplona.amethyst.ui.theme.grayText
import com.vitorpamplona.amethyst.ui.theme.newItemBackgroundColor
import com.vitorpamplona.amethyst.ui.theme.normalNoteModifier
import com.vitorpamplona.amethyst.ui.theme.normalWithTopMarginNoteModifier
import com.vitorpamplona.amethyst.ui.theme.replyBackground
import com.vitorpamplona.amethyst.ui.theme.replyModifier
import com.vitorpamplona.quartz.events.AppDefinitionEvent
import com.vitorpamplona.quartz.events.AudioHeaderEvent
import com.vitorpamplona.quartz.events.AudioTrackEvent
import com.vitorpamplona.quartz.events.BadgeAwardEvent
import com.vitorpamplona.quartz.events.BaseTextNoteEvent
import com.vitorpamplona.quartz.events.ChannelCreateEvent
import com.vitorpamplona.quartz.events.ChannelMessageEvent
import com.vitorpamplona.quartz.events.ChannelMetadataEvent
import com.vitorpamplona.quartz.events.ClassifiedsEvent
import com.vitorpamplona.quartz.events.CommunityDefinitionEvent
import com.vitorpamplona.quartz.events.CommunityPostApprovalEvent
import com.vitorpamplona.quartz.events.EmojiPackEvent
import com.vitorpamplona.quartz.events.FhirResourceEvent
import com.vitorpamplona.quartz.events.FileHeaderEvent
import com.vitorpamplona.quartz.events.FileStorageHeaderEvent
import com.vitorpamplona.quartz.events.GenericRepostEvent
import com.vitorpamplona.quartz.events.GitIssueEvent
import com.vitorpamplona.quartz.events.GitPatchEvent
import com.vitorpamplona.quartz.events.GitRepositoryEvent
import com.vitorpamplona.quartz.events.HighlightEvent
import com.vitorpamplona.quartz.events.LiveActivitiesChatMessageEvent
import com.vitorpamplona.quartz.events.LiveActivitiesEvent
import com.vitorpamplona.quartz.events.LongTextNoteEvent
import com.vitorpamplona.quartz.events.PeopleListEvent
import com.vitorpamplona.quartz.events.PinListEvent
import com.vitorpamplona.quartz.events.PollNoteEvent
import com.vitorpamplona.quartz.events.PrivateDmEvent
import com.vitorpamplona.quartz.events.ReactionEvent
import com.vitorpamplona.quartz.events.RelaySetEvent
import com.vitorpamplona.quartz.events.ReportEvent
import com.vitorpamplona.quartz.events.RepostEvent
import com.vitorpamplona.quartz.events.TextNoteEvent
import com.vitorpamplona.quartz.events.TextNoteModificationEvent
import com.vitorpamplona.quartz.events.VideoHorizontalEvent
import com.vitorpamplona.quartz.events.VideoVerticalEvent
import com.vitorpamplona.quartz.events.WikiNoteEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun NoteCompose(
    baseNote: Note,
    routeForLastRead: String? = null,
    modifier: Modifier = Modifier,
    isBoostedNote: Boolean = false,
    isQuotedNote: Boolean = false,
    unPackReply: Boolean = true,
    makeItShort: Boolean = false,
    addMarginTop: Boolean = true,
    showHidden: Boolean = false,
    quotesLeft: Int,
    parentBackgroundColor: MutableState<Color>? = null,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    WatchNoteEvent(
        baseNote = baseNote,
        accountViewModel = accountViewModel,
        showDivider = !isBoostedNote && !isQuotedNote,
        modifier,
    ) {
        CheckHiddenNoteCompose(
            note = baseNote,
            modifier = modifier,
            showHidden = showHidden,
            showHiddenWarning = isQuotedNote || isBoostedNote,
            accountViewModel = accountViewModel,
            nav = nav,
        ) { canPreview ->
            NormalNote(
                baseNote = baseNote,
                routeForLastRead = routeForLastRead,
                modifier = modifier,
                isBoostedNote = isBoostedNote,
                isQuotedNote = isQuotedNote,
                unPackReply = unPackReply,
                makeItShort = makeItShort,
                addMarginTop = addMarginTop,
                canPreview = canPreview,
                quotesLeft = quotesLeft,
                parentBackgroundColor = parentBackgroundColor,
                accountViewModel = accountViewModel,
                nav = nav,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchNoteEvent(
    baseNote: Note,
    accountViewModel: AccountViewModel,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    onNoteEventFound: @Composable () -> Unit,
) {
    if (baseNote.event != null) {
        onNoteEventFound()
    } else {
        // avoid observing costs if already has an event.

        val hasEvent by baseNote.live().hasEvent.observeAsState(baseNote.event != null)
        Crossfade(targetState = hasEvent, label = "Event presence") {
            if (it) {
                onNoteEventFound()
            } else {
                LongPressToQuickAction(baseNote = baseNote, accountViewModel = accountViewModel) { showPopup ->
                    BlankNote(
                        remember {
                            modifier.combinedClickable(
                                onClick = {},
                                onLongClick = showPopup,
                            )
                        },
                        showDivider,
                    )
                }
            }
        }
    }
}

@Composable
fun CheckHiddenNoteCompose(
    note: Note,
    modifier: Modifier = Modifier,
    showHiddenWarning: Boolean,
    showHidden: Boolean = false,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    normalNote: @Composable (canPreview: Boolean) -> Unit,
) {
    if (showHidden) {
        // Ignores reports as well
        normalNote(true)
    } else {
        WatchIsHidden(note, showHiddenWarning, modifier, accountViewModel, nav) { canPreview ->
            normalNote(canPreview)
        }
    }
}

@Composable
fun WatchIsHidden(
    note: Note,
    showHiddenWarning: Boolean,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    normalNote: @Composable (canPreview: Boolean) -> Unit,
) {
    val isHiddenState by remember(note) {
        accountViewModel.account.liveHiddenUsers
            .map { note.isHiddenFor(it) }
            .distinctUntilChanged()
    }
        .observeAsState(accountViewModel.isNoteHidden(note))

    val showAnyway =
        remember {
            mutableStateOf(false)
        }

    Crossfade(targetState = isHiddenState, label = "CheckHiddenNoteCompose") { isHidden ->
        if (showAnyway.value) {
            normalNote(true)
        } else if (!isHidden) {
            LoadReportsNoteCompose(note, modifier, accountViewModel, nav) { canPreview ->
                normalNote(canPreview)
            }
        } else if (showHiddenWarning) {
            // if it is a quoted or boosted note, how the hidden warning.
            HiddenNoteByMe(
                isQuote = true,
                onClick = { showAnyway.value = true },
            )
        }
    }
}

@Composable
fun LoadReportsNoteCompose(
    note: Note,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    normalNote: @Composable (canPreview: Boolean) -> Unit,
) {
    var state by
        remember(note) {
            mutableStateOf(
                AccountViewModel.NoteComposeReportState(),
            )
        }

    WatchForReports(note, accountViewModel) { newState ->
        if (state != newState) {
            state = newState
        }
    }

    Crossfade(targetState = state, label = "LoadedNoteCompose") {
        RenderReportState(state = it, note = note, modifier = modifier, accountViewModel = accountViewModel, nav = nav) { canPreview ->
            normalNote(canPreview)
        }
    }
}

@Composable
fun RenderReportState(
    state: AccountViewModel.NoteComposeReportState,
    note: Note,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    normalNote: @Composable (canPreview: Boolean) -> Unit,
) {
    var showReportedNote by remember(note) { mutableStateOf(false) }

    Crossfade(targetState = !state.isAcceptable && !showReportedNote, label = "RenderReportState") { showHiddenNote ->
        if (showHiddenNote) {
            HiddenNote(
                state.relevantReports,
                state.isHiddenAuthor,
                accountViewModel,
                modifier,
                nav,
                onClick = { showReportedNote = true },
            )
        } else {
            val canPreview = (!state.isAcceptable && showReportedNote) || state.canPreview

            normalNote(canPreview)
        }
    }
}

@Composable
fun WatchForReports(
    note: Note,
    accountViewModel: AccountViewModel,
    onChange: (AccountViewModel.NoteComposeReportState) -> Unit,
) {
    val userFollowsState by accountViewModel.userFollows.observeAsState()
    val noteReportsState by note.live().reports.observeAsState()
    val userBlocks by accountViewModel.account.flowHiddenUsers.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = noteReportsState, key2 = userFollowsState, userBlocks) {
        accountViewModel.isNoteAcceptable(note, onChange)
    }
}

@Composable
fun NormalNote(
    baseNote: Note,
    routeForLastRead: String? = null,
    modifier: Modifier = Modifier,
    isBoostedNote: Boolean = false,
    isQuotedNote: Boolean = false,
    unPackReply: Boolean = true,
    makeItShort: Boolean = false,
    addMarginTop: Boolean = true,
    canPreview: Boolean = true,
    quotesLeft: Int,
    parentBackgroundColor: MutableState<Color>? = null,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    if (isQuotedNote || isBoostedNote) {
        when (baseNote.event) {
            is ChannelCreateEvent,
            is ChannelMetadataEvent,
            ->
                ChannelHeader(
                    channelNote = baseNote,
                    showVideo = !makeItShort,
                    showBottomDiviser = true,
                    sendToChannel = true,
                    accountViewModel = accountViewModel,
                    nav = nav,
                )
            else ->
                LongPressToQuickAction(baseNote = baseNote, accountViewModel = accountViewModel) {
                        showPopup,
                    ->
                    CheckNewAndRenderNote(
                        baseNote = baseNote,
                        routeForLastRead = routeForLastRead,
                        modifier = modifier,
                        isBoostedNote = isBoostedNote,
                        isQuotedNote = isQuotedNote,
                        unPackReply = unPackReply,
                        makeItShort = makeItShort,
                        addMarginTop = addMarginTop,
                        canPreview = canPreview,
                        quotesLeft = quotesLeft,
                        parentBackgroundColor = parentBackgroundColor,
                        accountViewModel = accountViewModel,
                        showPopup = showPopup,
                        nav = nav,
                    )
                }
        }
    } else {
        when (baseNote.event) {
            is ChannelCreateEvent,
            is ChannelMetadataEvent,
            ->
                ChannelHeader(
                    channelNote = baseNote,
                    showVideo = !makeItShort,
                    showBottomDiviser = true,
                    sendToChannel = true,
                    accountViewModel = accountViewModel,
                    nav = nav,
                )
            is FileHeaderEvent -> FileHeaderDisplay(baseNote, false, accountViewModel)
            is FileStorageHeaderEvent -> FileStorageHeaderDisplay(baseNote, false, accountViewModel)
            else ->
                LongPressToQuickAction(baseNote = baseNote, accountViewModel = accountViewModel) {
                        showPopup,
                    ->
                    CheckNewAndRenderNote(
                        baseNote = baseNote,
                        routeForLastRead = routeForLastRead,
                        modifier = modifier,
                        isBoostedNote = isBoostedNote,
                        isQuotedNote = isQuotedNote,
                        unPackReply = unPackReply,
                        makeItShort = makeItShort,
                        addMarginTop = addMarginTop,
                        canPreview = canPreview,
                        quotesLeft = quotesLeft,
                        parentBackgroundColor = parentBackgroundColor,
                        accountViewModel = accountViewModel,
                        showPopup = showPopup,
                        nav = nav,
                    )
                }
        }
    }
}

@Composable
fun calculateBackgroundColor(
    createdAt: Long?,
    routeForLastRead: String? = null,
    parentBackgroundColor: MutableState<Color>? = null,
    accountViewModel: AccountViewModel,
): MutableState<Color> {
    val defaultBackgroundColor = MaterialTheme.colorScheme.background
    val newItemColor = MaterialTheme.colorScheme.newItemBackgroundColor
    return remember(createdAt) {
        mutableStateOf<Color>(
            if (routeForLastRead != null) {
                val isNew = accountViewModel.loadAndMarkAsRead(routeForLastRead, createdAt)

                if (isNew) {
                    if (parentBackgroundColor != null) {
                        newItemColor.compositeOver(parentBackgroundColor.value)
                    } else {
                        newItemColor.compositeOver(defaultBackgroundColor)
                    }
                } else {
                    parentBackgroundColor?.value ?: defaultBackgroundColor
                }
            } else {
                parentBackgroundColor?.value ?: defaultBackgroundColor
            },
        )
    }
}

@Composable
private fun CheckNewAndRenderNote(
    baseNote: Note,
    routeForLastRead: String? = null,
    modifier: Modifier = Modifier,
    isBoostedNote: Boolean = false,
    isQuotedNote: Boolean = false,
    unPackReply: Boolean = true,
    makeItShort: Boolean = false,
    addMarginTop: Boolean = true,
    canPreview: Boolean = true,
    quotesLeft: Int,
    parentBackgroundColor: MutableState<Color>? = null,
    accountViewModel: AccountViewModel,
    showPopup: () -> Unit,
    nav: (String) -> Unit,
) {
    val backgroundColor =
        calculateBackgroundColor(
            baseNote.createdAt(),
            routeForLastRead,
            parentBackgroundColor,
            accountViewModel,
        )

    ClickableNote(
        baseNote = baseNote,
        backgroundColor = backgroundColor,
        modifier = modifier,
        accountViewModel = accountViewModel,
        showPopup = showPopup,
        nav = nav,
    ) {
        InnerNoteWithReactions(
            baseNote = baseNote,
            backgroundColor = backgroundColor,
            isBoostedNote = isBoostedNote,
            isQuotedNote = isQuotedNote,
            addMarginTop = addMarginTop,
            unPackReply = unPackReply,
            makeItShort = makeItShort,
            canPreview = canPreview,
            quotesLeft = quotesLeft,
            accountViewModel = accountViewModel,
            nav = nav,
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ClickableNote(
    baseNote: Note,
    modifier: Modifier,
    backgroundColor: MutableState<Color>,
    accountViewModel: AccountViewModel,
    showPopup: () -> Unit,
    nav: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val updatedModifier =
        remember(baseNote, backgroundColor.value) {
            modifier
                .combinedClickable(
                    onClick = {
                        scope.launch {
                            val redirectToNote =
                                if (baseNote.event is RepostEvent || baseNote.event is GenericRepostEvent) {
                                    baseNote.replyTo?.lastOrNull() ?: baseNote
                                } else {
                                    baseNote
                                }
                            routeFor(redirectToNote, accountViewModel.userProfile())?.let { nav(it) }
                        }
                    },
                    onLongClick = showPopup,
                )
                .background(backgroundColor.value)
        }

    Column(modifier = updatedModifier) { content() }
}

@Composable
fun InnerNoteWithReactions(
    baseNote: Note,
    backgroundColor: MutableState<Color>,
    isBoostedNote: Boolean,
    isQuotedNote: Boolean,
    addMarginTop: Boolean,
    unPackReply: Boolean,
    makeItShort: Boolean,
    canPreview: Boolean,
    quotesLeft: Int,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    val notBoostedNorQuote = !isBoostedNote && !isQuotedNote
    val editState = observeEdits(baseNote = baseNote, accountViewModel = accountViewModel)

    Row(
        modifier =
            if (!isBoostedNote && addMarginTop) {
                normalWithTopMarginNoteModifier
            } else if (!isBoostedNote) {
                normalNoteModifier
            } else {
                boostedNoteModifier
            },
    ) {
        if (notBoostedNorQuote) {
            Column(WidthAuthorPictureModifier) {
                AuthorAndRelayInformation(baseNote, accountViewModel, nav)
            }
            Spacer(modifier = DoubleHorzSpacer)
        }

        Column(Modifier.fillMaxWidth()) {
            val showSecondRow =
                baseNote.event !is RepostEvent && baseNote.event !is GenericRepostEvent &&
                    !isBoostedNote && !isQuotedNote && accountViewModel.settings.featureSet != FeatureSetType.SIMPLIFIED
            NoteBody(
                baseNote = baseNote,
                showAuthorPicture = isQuotedNote,
                unPackReply = unPackReply,
                makeItShort = makeItShort,
                canPreview = canPreview,
                showSecondRow = showSecondRow,
                quotesLeft = quotesLeft,
                backgroundColor = backgroundColor,
                editState = editState,
                accountViewModel = accountViewModel,
                nav = nav,
            )
        }
    }

    val isNotRepost = baseNote.event !is RepostEvent && baseNote.event !is GenericRepostEvent

    if (isNotRepost) {
        if (makeItShort) {
            if (isBoostedNote) {
            } else {
                Spacer(modifier = DoubleVertSpacer)
            }
        } else {
            ReactionsRow(
                baseNote = baseNote,
                showReactionDetail = notBoostedNorQuote,
                editState = editState,
                accountViewModel = accountViewModel,
                nav = nav,
            )
        }
    }
}

@Composable
fun NoteBody(
    baseNote: Note,
    showAuthorPicture: Boolean = false,
    unPackReply: Boolean = true,
    makeItShort: Boolean = false,
    canPreview: Boolean = true,
    showSecondRow: Boolean,
    quotesLeft: Int,
    backgroundColor: MutableState<Color>,
    editState: State<GenericLoadable<EditState>>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    FirstUserInfoRow(
        baseNote = baseNote,
        showAuthorPicture = showAuthorPicture,
        editState = editState,
        accountViewModel = accountViewModel,
        nav = nav,
    )

    if (showSecondRow) {
        SecondUserInfoRow(
            baseNote,
            accountViewModel,
            nav,
        )
    }

    if (baseNote.event !is RepostEvent && baseNote.event !is GenericRepostEvent) {
        Spacer(modifier = Modifier.height(3.dp))
    }

    if (!makeItShort) {
        ReplyRow(
            baseNote,
            unPackReply,
            backgroundColor,
            accountViewModel,
            nav,
        )
    }

    RenderNoteRow(
        baseNote = baseNote,
        backgroundColor = backgroundColor,
        makeItShort = makeItShort,
        canPreview = canPreview,
        editState = editState,
        quotesLeft = quotesLeft,
        accountViewModel = accountViewModel,
        nav = nav,
    )

    val noteEvent = baseNote.event
    val zapSplits = remember(noteEvent) { noteEvent?.hasZapSplitSetup() ?: false }
    if (zapSplits && noteEvent != null) {
        Spacer(modifier = HalfDoubleVertSpacer)
        DisplayZapSplits(noteEvent, false, accountViewModel, nav)
    }
}

@Composable
private fun RenderNoteRow(
    baseNote: Note,
    backgroundColor: MutableState<Color>,
    makeItShort: Boolean,
    canPreview: Boolean,
    quotesLeft: Int,
    editState: State<GenericLoadable<EditState>>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    val noteEvent = baseNote.event
    when (noteEvent) {
        is AppDefinitionEvent -> {
            RenderAppDefinition(baseNote, accountViewModel, nav)
        }
        is AudioTrackEvent -> {
            RenderAudioTrack(baseNote, accountViewModel, nav)
        }
        is AudioHeaderEvent -> {
            RenderAudioHeader(baseNote, accountViewModel, nav)
        }
        is ReactionEvent -> {
            RenderReaction(baseNote, quotesLeft, backgroundColor, accountViewModel, nav)
        }
        is RepostEvent -> {
            RenderRepost(baseNote, quotesLeft, backgroundColor, accountViewModel, nav)
        }
        is GenericRepostEvent -> {
            RenderRepost(baseNote, quotesLeft, backgroundColor, accountViewModel, nav)
        }
        is ReportEvent -> {
            RenderReport(baseNote, quotesLeft, backgroundColor, accountViewModel, nav)
        }
        is LongTextNoteEvent -> {
            RenderLongFormContent(baseNote, accountViewModel, nav)
        }
        is WikiNoteEvent -> {
            RenderWikiContent(baseNote, accountViewModel, nav)
        }
        is BadgeAwardEvent -> {
            RenderBadgeAward(baseNote, backgroundColor, accountViewModel, nav)
        }
        is FhirResourceEvent -> {
            RenderFhirResource(baseNote, accountViewModel, nav)
        }
        is PeopleListEvent -> {
            DisplayPeopleList(baseNote, backgroundColor, accountViewModel, nav)
        }
        is RelaySetEvent -> {
            DisplayRelaySet(baseNote, backgroundColor, accountViewModel, nav)
        }
        is PinListEvent -> {
            RenderPinListEvent(baseNote, backgroundColor, accountViewModel, nav)
        }
        is EmojiPackEvent -> {
            RenderEmojiPack(baseNote, true, backgroundColor, accountViewModel)
        }
        is LiveActivitiesEvent -> {
            RenderLiveActivityEvent(baseNote, accountViewModel, nav)
        }
        is GitRepositoryEvent -> {
            RenderGitRepositoryEvent(baseNote, accountViewModel, nav)
        }
        is GitPatchEvent -> {
            RenderGitPatchEvent(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is GitIssueEvent -> {
            RenderGitIssueEvent(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is PrivateDmEvent -> {
            RenderPrivateMessage(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is ClassifiedsEvent -> {
            RenderClassifieds(
                noteEvent,
                baseNote,
                accountViewModel,
                nav,
            )
        }
        is HighlightEvent -> {
            RenderHighlight(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is PollNoteEvent -> {
            RenderPoll(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is FileHeaderEvent -> {
            FileHeaderDisplay(baseNote, true, accountViewModel)
        }
        is VideoHorizontalEvent -> {
            VideoDisplay(baseNote, makeItShort, canPreview, backgroundColor, accountViewModel, nav)
        }
        is VideoVerticalEvent -> {
            VideoDisplay(baseNote, makeItShort, canPreview, backgroundColor, accountViewModel, nav)
        }
        is FileStorageHeaderEvent -> {
            FileStorageHeaderDisplay(baseNote, true, accountViewModel)
        }
        is CommunityPostApprovalEvent -> {
            RenderPostApproval(
                baseNote,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        is TextNoteModificationEvent -> {
            RenderTextModificationEvent(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                accountViewModel,
                nav,
            )
        }
        else -> {
            RenderTextEvent(
                baseNote,
                makeItShort,
                canPreview,
                quotesLeft,
                backgroundColor,
                editState,
                accountViewModel,
                nav,
            )
        }
    }
}

@Composable
fun RenderRepost(
    note: Note,
    quotesLeft: Int,
    backgroundColor: MutableState<Color>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    note.replyTo?.lastOrNull()?.let {
        NoteCompose(
            it,
            modifier = Modifier,
            isBoostedNote = true,
            unPackReply = false,
            quotesLeft = quotesLeft - 1,
            parentBackgroundColor = backgroundColor,
            accountViewModel = accountViewModel,
            nav = nav,
        )
    }
}

fun getGradient(backgroundColor: MutableState<Color>): Brush {
    return Brush.verticalGradient(
        colors =
            listOf(
                backgroundColor.value.copy(alpha = 0f),
                backgroundColor.value,
            ),
    )
}

@Composable
private fun ReplyRow(
    note: Note,
    unPackReply: Boolean,
    backgroundColor: MutableState<Color>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    val noteEvent = note.event

    val showReply by
        remember(note) {
            derivedStateOf {
                noteEvent is BaseTextNoteEvent && (note.replyTo != null || noteEvent.hasAnyTaggedUser())
            }
        }

    val showChannelInfo by
        remember(note) {
            derivedStateOf {
                if (noteEvent is ChannelMessageEvent || noteEvent is LiveActivitiesChatMessageEvent) {
                    note.channelHex()
                } else {
                    null
                }
            }
        }

    showChannelInfo?.let {
        ChannelHeader(
            channelHex = it,
            showVideo = false,
            showBottomDiviser = false,
            sendToChannel = true,
            modifier = MaterialTheme.colorScheme.replyModifier.padding(10.dp),
            accountViewModel = accountViewModel,
            nav = nav,
        )
    }

    if (showReply) {
        val replyingDirectlyTo =
            remember(note) {
                if (noteEvent is BaseTextNoteEvent) {
                    val replyingTo = noteEvent.replyingTo()
                    if (replyingTo != null) {
                        note.replyTo?.firstOrNull {
                            // important to test both ids in case it's a replaceable event.
                            it.idHex == replyingTo || it.event?.id() == replyingTo
                        }
                    } else {
                        note.replyTo?.lastOrNull { it.event?.kind() != CommunityDefinitionEvent.KIND }
                    }
                } else {
                    note.replyTo?.lastOrNull { it.event?.kind() != CommunityDefinitionEvent.KIND }
                }
            }
        if (replyingDirectlyTo != null && unPackReply) {
            ReplyNoteComposition(replyingDirectlyTo, backgroundColor, accountViewModel, nav)
            Spacer(modifier = StdVertSpacer)
        } else if (showChannelInfo != null) {
            val replies = remember { note.replyTo?.toImmutableList() }
            val mentions =
                remember {
                    (note.event as? BaseTextNoteEvent)?.mentions()?.toImmutableList() ?: persistentListOf()
                }

            ReplyInformationChannel(replies, mentions, accountViewModel, nav)
        }
    }
}

@Composable
private fun ReplyNoteComposition(
    replyingDirectlyTo: Note,
    backgroundColor: MutableState<Color>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    val defaultReplyBackground = MaterialTheme.colorScheme.replyBackground

    val replyBackgroundColor =
        remember {
            mutableStateOf(
                defaultReplyBackground.compositeOver(backgroundColor.value),
            )
        }

    NoteCompose(
        baseNote = replyingDirectlyTo,
        isQuotedNote = true,
        quotesLeft = 0,
        modifier = MaterialTheme.colorScheme.replyModifier,
        unPackReply = false,
        makeItShort = true,
        parentBackgroundColor = replyBackgroundColor,
        accountViewModel = accountViewModel,
        nav = nav,
    )
}

@Composable
fun SecondUserInfoRow(
    note: Note,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    val noteEvent = note.event ?: return
    val noteAuthor = note.author ?: return

    Row(
        verticalAlignment = CenterVertically,
        modifier = UserNameMaxRowHeight,
    ) {
        if (noteEvent is BaseTextNoteEvent && noteEvent.isAFork()) {
            ShowForkInformation(noteEvent, remember(noteEvent) { Modifier.weight(1f) }, accountViewModel, nav)
        } else {
            ObserveDisplayNip05Status(noteAuthor, remember(noteEvent) { Modifier.weight(1f) }, accountViewModel, nav)
        }

        val geo = remember(noteEvent) { noteEvent.getGeoHash() }
        if (geo != null) {
            Spacer(StdHorzSpacer)
            DisplayLocation(geo, nav)
        }

        val baseReward = remember(noteEvent) { noteEvent.getReward()?.let { Reward(it) } }
        if (baseReward != null) {
            Spacer(StdHorzSpacer)
            DisplayReward(baseReward, note, accountViewModel, nav)
        }

        val pow = remember(noteEvent) { noteEvent.getPoWRank() }
        if (pow > 20) {
            Spacer(StdHorzSpacer)
            DisplayPoW(pow)
        }

        DisplayOts(note, accountViewModel)
    }
}

@Composable
fun FirstUserInfoRow(
    baseNote: Note,
    showAuthorPicture: Boolean,
    editState: State<GenericLoadable<EditState>>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    Row(verticalAlignment = CenterVertically, modifier = UserNameRowHeight) {
        val isRepost = baseNote.event is RepostEvent || baseNote.event is GenericRepostEvent

        val isCommunityPost by
            remember(baseNote) {
                derivedStateOf {
                    baseNote.event?.isTaggedAddressableKind(CommunityDefinitionEvent.KIND) == true
                }
            }

        val textColor = if (isRepost) MaterialTheme.colorScheme.grayText else Color.Unspecified

        if (showAuthorPicture) {
            NoteAuthorPicture(baseNote, nav, accountViewModel, Size25dp)
            Spacer(HalfPadding)
            NoteUsernameDisplay(baseNote, Modifier.weight(1f), textColor = textColor)
        } else {
            NoteUsernameDisplay(baseNote, Modifier.weight(1f), textColor = textColor)
        }

        if (isRepost) {
            BoostedMark()
        } else if (isCommunityPost) {
            DisplayFollowingCommunityInPost(baseNote, accountViewModel, nav)
        } else {
            DisplayFollowingHashtagsInPost(baseNote, accountViewModel, nav)
        }

        if (editState.value is GenericLoadable.Loaded) {
            (editState.value as? GenericLoadable.Loaded<EditState>)?.loaded?.let {
                DisplayEditStatus(it)
            }
        }

        TimeAgo(baseNote)

        MoreOptionsButton(baseNote, editState, accountViewModel, nav)
    }
}

@Composable
fun observeEdits(
    baseNote: Note,
    accountViewModel: AccountViewModel,
): State<GenericLoadable<EditState>> {
    if (baseNote.event !is TextNoteEvent) {
        return remember { mutableStateOf(GenericLoadable.Empty<EditState>()) }
    }

    val editState =
        remember(baseNote.idHex) {
            val cached = accountViewModel.cachedModificationEventsForNote(baseNote)
            mutableStateOf(
                if (cached != null) {
                    if (cached.isEmpty()) {
                        GenericLoadable.Empty<EditState>()
                    } else {
                        val state = EditState()
                        state.updateModifications(cached)
                        GenericLoadable.Loaded<EditState>(state)
                    }
                } else {
                    GenericLoadable.Loading<EditState>()
                },
            )
        }

    val updatedNote by baseNote.live().innerModifications.observeAsState()

    LaunchedEffect(key1 = updatedNote) {
        updatedNote?.note?.let {
            accountViewModel.findModificationEventsForNote(it) { newModifications ->
                if (newModifications.isEmpty()) {
                    if (editState.value !is GenericLoadable.Empty) {
                        editState.value = GenericLoadable.Empty<EditState>()
                    }
                } else {
                    if (editState.value is GenericLoadable.Loaded) {
                        (editState.value as? GenericLoadable.Loaded<EditState>)?.loaded?.updateModifications(newModifications)
                    } else {
                        val state = EditState()
                        state.updateModifications(newModifications)
                        editState.value = GenericLoadable.Loaded(state)
                    }
                }
            }
        }
    }

    return editState
}

@Composable
private fun AuthorAndRelayInformation(
    baseNote: Note,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    // Draws the boosted picture outside the boosted card.
    Box(modifier = Size55Modifier, contentAlignment = Alignment.BottomEnd) {
        RenderAuthorImages(baseNote, nav, accountViewModel)
    }

    BadgeBox(baseNote, accountViewModel, nav)
}

@Composable
private fun BadgeBox(
    baseNote: Note,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    if (accountViewModel.settings.featureSet != FeatureSetType.SIMPLIFIED) {
        if (baseNote.event is RepostEvent || baseNote.event is GenericRepostEvent) {
            baseNote.replyTo?.lastOrNull()?.let { RelayBadges(it, accountViewModel, nav) }
        } else {
            RelayBadges(baseNote, accountViewModel, nav)
        }
    }
}

@Composable
private fun RenderAuthorImages(
    baseNote: Note,
    nav: (String) -> Unit,
    accountViewModel: AccountViewModel,
) {
    val isRepost = baseNote.event is RepostEvent || baseNote.event is GenericRepostEvent

    if (isRepost) {
        val baseRepost = baseNote.replyTo?.lastOrNull()
        if (baseRepost != null) {
            RepostNoteAuthorPicture(baseNote, baseRepost, accountViewModel, nav)
        } else {
            NoteAuthorPicture(baseNote, nav, accountViewModel, Size55dp)
        }
    } else {
        NoteAuthorPicture(baseNote, nav, accountViewModel, Size55dp)
    }

    if (baseNote.event is ChannelMessageEvent) {
        val baseChannelHex = remember(baseNote) { baseNote.channelHex() }
        if (baseChannelHex != null) {
            LoadChannel(baseChannelHex, accountViewModel) { channel ->
                ChannelNotePicture(
                    channel,
                    loadProfilePicture = accountViewModel.settings.showProfilePictures.value,
                )
            }
        }
    }
}

@Composable
private fun ChannelNotePicture(
    baseChannel: Channel,
    loadProfilePicture: Boolean,
) {
    val model by
        baseChannel.live.map { it.channel.profilePicture() }.distinctUntilChanged().observeAsState()

    Box(Size30Modifier) {
        RobohashFallbackAsyncImage(
            robot = baseChannel.idHex,
            model = model,
            contentDescription = stringResource(R.string.group_picture),
            modifier = MaterialTheme.colorScheme.channelNotePictureModifier,
            loadProfilePicture = loadProfilePicture,
        )
    }
}

@Composable
private fun RepostNoteAuthorPicture(
    baseNote: Note,
    baseRepost: Note,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
) {
    GenericRepostLayout(
        baseAuthorPicture = {
            NoteAuthorPicture(
                baseNote = baseNote,
                nav = nav,
                accountViewModel = accountViewModel,
                size = Size34dp,
            )
        },
        repostAuthorPicture = {
            NoteAuthorPicture(
                baseNote = baseRepost,
                nav = nav,
                accountViewModel = accountViewModel,
                size = Size34dp,
            )
        },
    )
}
