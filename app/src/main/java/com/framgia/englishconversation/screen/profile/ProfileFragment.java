package com.framgia.englishconversation.screen.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.databinding.FragmentProfileBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

;

/**
 * Profile Screen.
 */
public class ProfileFragment extends BaseFragment {

    private ProfileContract.ViewModel mViewModel;

    public static ProfileFragment newInstance(UserModel userModel) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserModel userModel = getArguments().getParcelable(EXTRA_USER);
        mViewModel = new ProfileViewModel(new Navigator(getActivity()), getChildFragmentManager(),
                userModel);
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        ProfileContract.Presenter presenter = new ProfilePresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        FragmentProfileBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setViewModel((ProfileViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
