package ua.tsisar.abetka;

import android.content.res.Resources;

public class AlphabetItem {
    private Resources resources;
    private int position;
    private int offset;

    public AlphabetItem(Resources resources, int position, int offset) {
        this.resources = resources;
        this.position = position % resources.obtainTypedArray(R.array.letters_array).length();
        this.offset = offset;
    }

    public String getLetter() {
        try {
            return resources.obtainTypedArray(R.array.letters_array).getString(position);
        } catch (ArrayIndexOutOfBoundsException e){
            return "";
        }
    }

    public int getColor(){
        try {
            return resources.obtainTypedArray(R.array.letters_color_array)
                    .getColor(position, 0xFF000000);
        } catch (ArrayIndexOutOfBoundsException e){
            return 0xFF000000;
        }
    }

    public String getLetterSmall() {
        try {
            return resources.obtainTypedArray(R.array.letters_small_array).getString(position);
        } catch (ArrayIndexOutOfBoundsException e){
            return "";
        }
    }

    public String getName() {
        try {
            return resources.obtainTypedArray(resources.obtainTypedArray(R.array.name_array)
                    .getResourceId(position, R.array.name_array_000)).getString(offset);
        } catch (ArrayIndexOutOfBoundsException e){
            return "";
        }
    }

    public int getIllustration() {
        try {
            return resources.obtainTypedArray(resources.obtainTypedArray(R.array.illustration_array)
                    .getResourceId(position, R.array.illustration_array_000))
                    .getResourceId(offset, R.mipmap.ab_001);
        } catch (ArrayIndexOutOfBoundsException e){
            return R.mipmap.ab_001;
        }
    }
}
