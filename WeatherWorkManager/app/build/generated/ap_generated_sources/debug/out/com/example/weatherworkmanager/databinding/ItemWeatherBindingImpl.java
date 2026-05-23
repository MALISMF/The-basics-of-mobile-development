package com.example.weatherworkmanager.databinding;
import com.example.weatherworkmanager.R;
import com.example.weatherworkmanager.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemWeatherBindingImpl extends ItemWeatherBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.divider, 9);
    }
    // views
    @NonNull
    private final com.google.android.material.card.MaterialCardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemWeatherBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private ItemWeatherBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.view.View) bindings[9]
            , (android.widget.ProgressBar) bindings[7]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[6]
            );
        this.itemProgress.setTag(null);
        this.mboundView0 = (com.google.android.material.card.MaterialCardView) bindings[0];
        this.mboundView0.setTag(null);
        this.tvCity.setTag(null);
        this.tvDescription.setTag(null);
        this.tvError.setTag(null);
        this.tvFeelsLike.setTag(null);
        this.tvHumidity.setTag(null);
        this.tvTemp.setTag(null);
        this.tvWind.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.item == variableId) {
            setItem((com.example.weatherworkmanager.model.CityWeatherItem) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setItem(@Nullable com.example.weatherworkmanager.model.CityWeatherItem Item) {
        this.mItem = Item;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.item);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.example.weatherworkmanager.model.CityWeatherItem item = mItem;
        java.lang.String itemHumidityFormatted = null;
        int itemErrorJavaLangObjectNullAndroidViewViewVISIBLEAndroidViewViewGONE = 0;
        java.lang.String itemFeelsLikeFormatted = null;
        java.lang.String itemDescription = null;
        java.lang.String itemError = null;
        boolean itemErrorJavaLangObjectNull = false;
        java.lang.String itemTitleFormatted = null;
        java.lang.String itemTempFormatted = null;
        boolean itemLoading = false;
        java.lang.String itemWindFormatted = null;
        int itemLoadingAndroidViewViewVISIBLEAndroidViewViewGONE = 0;

        if ((dirtyFlags & 0x3L) != 0) {



                if (item != null) {
                    // read item.humidityFormatted
                    itemHumidityFormatted = item.getHumidityFormatted();
                    // read item.feelsLikeFormatted
                    itemFeelsLikeFormatted = item.getFeelsLikeFormatted();
                    // read item.description
                    itemDescription = item.getDescription();
                    // read item.error
                    itemError = item.getError();
                    // read item.titleFormatted
                    itemTitleFormatted = item.getTitleFormatted();
                    // read item.tempFormatted
                    itemTempFormatted = item.getTempFormatted();
                    // read item.loading
                    itemLoading = item.isLoading();
                    // read item.windFormatted
                    itemWindFormatted = item.getWindFormatted();
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(itemLoading) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read item.error != null
                itemErrorJavaLangObjectNull = (itemError) != (null);
                // read item.loading ? android.view.View.VISIBLE : android.view.View.GONE
                itemLoadingAndroidViewViewVISIBLEAndroidViewViewGONE = ((itemLoading) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            if((dirtyFlags & 0x3L) != 0) {
                if(itemErrorJavaLangObjectNull) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }


                // read item.error != null ? android.view.View.VISIBLE : android.view.View.GONE
                itemErrorJavaLangObjectNullAndroidViewViewVISIBLEAndroidViewViewGONE = ((itemErrorJavaLangObjectNull) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.itemProgress.setVisibility(itemLoadingAndroidViewViewVISIBLEAndroidViewViewGONE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvCity, itemTitleFormatted);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvDescription, itemDescription);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvError, itemError);
            this.tvError.setVisibility(itemErrorJavaLangObjectNullAndroidViewViewVISIBLEAndroidViewViewGONE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvFeelsLike, itemFeelsLikeFormatted);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvHumidity, itemHumidityFormatted);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTemp, itemTempFormatted);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvWind, itemWindFormatted);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): item
        flag 1 (0x2L): null
        flag 2 (0x3L): item.error != null ? android.view.View.VISIBLE : android.view.View.GONE
        flag 3 (0x4L): item.error != null ? android.view.View.VISIBLE : android.view.View.GONE
        flag 4 (0x5L): item.loading ? android.view.View.VISIBLE : android.view.View.GONE
        flag 5 (0x6L): item.loading ? android.view.View.VISIBLE : android.view.View.GONE
    flag mapping end*/
    //end
}