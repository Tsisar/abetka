package ua.tsisar.abetka.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.tsisar.abetka.AlphabetItem;
import ua.tsisar.abetka.R;


public class PageFragment extends Fragment {
    private static final String POSITION = "position";
    private static final String OFFSET = "offset";
    private static final String FONT = "fonts/font.ttf";


    private int position;
    private int offset;

    private OnMapChangedListener listener;

    public interface OnMapChangedListener{
        void onMapChanged(int position, int offset);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapChangedListener) {
            this.listener = (OnMapChangedListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            position = getArguments().getInt(POSITION);
            offset = getArguments().getInt(OFFSET);
        }else {
            position = 0;
            offset = 0;
        }

        if(listener != null){
            listener.onMapChanged(position, offset);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), FONT);
        AlphabetItem alphabetItem = new AlphabetItem(getActivity().getResources(), position, offset);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Буква
        TextView letter = view.findViewById(R.id.letter_text_view);
        String letterString = alphabetItem.getLetter() + alphabetItem.getLetterSmall();
        letter.setText(letterString);
        letter.setTextColor(alphabetItem.getColor());
        letter.setTypeface(typeface);

        // Ілюстрація
        ImageView illustration = view.findViewById(R.id.illustration_image_view);
        illustration.setImageResource(alphabetItem.getIllustration());

        // Перша буква назви
        TextView firstLetter = view.findViewById(R.id.first_letter_text_view);
        firstLetter.setText(alphabetItem.getLetter());
        firstLetter.setTextColor(alphabetItem.getColor());
        firstLetter.setTypeface(typeface);

        // назва
        TextView tvName = view.findViewById(R.id.name_text_view);
        tvName.setText(alphabetItem.getName());

        return view;
    }
}