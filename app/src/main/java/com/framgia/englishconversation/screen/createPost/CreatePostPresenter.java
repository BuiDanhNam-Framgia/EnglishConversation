package com.framgia.englishconversation.screen.createPost;

import android.text.TextUtils;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link CreatePostActivity}), retrieves the data and updates
 * the UI as required.
 */
final class CreatePostPresenter implements CreatePostContract.Presenter {

    private final CreatePostContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private TimelineRepository mTimelineRepository;
    private CompositeDisposable mDisposable;

    CreatePostPresenter(CreatePostContract.ViewModel viewModel,
            AuthenicationRepository authenicationRepository,
            TimelineRepository timelineRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mTimelineRepository = timelineRepository;
        mDisposable = new CompositeDisposable();
        getUser();
    }

    @Override
    public void getUser() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserFailed(msg);
            }
        });
    }

    @Override
    public void createPost(TimelineModel timelineModel) {
        if ((timelineModel.getContent() == null || TextUtils.isEmpty(
                timelineModel.getContent().trim()))
                && (timelineModel.getMedias() == null
                || timelineModel.getMedias().size() == 0)
                && timelineModel.getConversations() == null) {
            return;
        }
        mDisposable.add(mTimelineRepository.createNewPost(timelineModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TimelineModel>() {
                    @Override
                    public void onNext(@NonNull TimelineModel timelineModel) {
                        mViewModel.onCreatePostSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mViewModel.onCreatePostFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void onDestroy() {
        mDisposable.dispose();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
