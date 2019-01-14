package pro.axonomy.www.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 1/12/19.
 */
public class VoteFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout mSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vote, container, false);

        mSlider = view.findViewById(R.id.slider);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://img.aceport.com/4102152238648369.png");
        imageUrls.add("https://img.aceport.com/4720170772930343.png");

        for(String imageUrl : imageUrls){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .image(imageUrl)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mSlider.addSlider(textSliderView);
        }

        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // TODO if needed
    }
}
