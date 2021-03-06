package com.framgia.englishconversation.data.source.remote.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by toand on 5/13/2017.
 */

public interface TimelineDataSource {

    Observable<TimelineModel> createNewPost(TimelineModel timelineModel);

    Observable<List<TimelineModel>> getTimeline(TimelineModel lastTimeline);

    Observable<TimelineModel> updateTimeline(TimelineModel lastTimeline);

    Observable<List<TimelineModel>> getTimeline(TimelineModel lastTimeline, UserModel userModel);

    Observable<TimelineModel> updateTimeline(TimelineModel lastTimeline, UserModel userModel);

    void removeListener();
}
