package com.dynamic.adapter;


import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dynamic.DynamicModule;
import com.dynamic.R;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.listeners.DMContentType;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.model.DMOtherProperty;
import com.dynamic.model.DMPadding;
import com.helper.callback.Response;
import com.helper.model.common.BaseTimeViewHolder;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class BaseDynamicChildAdapter<T1,T2> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final Response.OnClickListener<T2> clickListener;
    protected final List<T2> mList;
    protected final String imageUrl;
    protected final int itemType;
    protected final T1 category;
    private final Context context;
    private final DMOtherProperty otherProperty;
    private final boolean isPortrait;
    private boolean isMediumVideoPlaceholderQuality = false;

    public BaseDynamicChildAdapter(Context context, int itemType, T1 category, List<T2> mList, Response.OnClickListener<T2> clickListener) {
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
        this.context = context;
        this.itemType = itemType;
        this.category = category;
        this.mList = mList;
        this.clickListener = clickListener;
        this.otherProperty = getOtherProperty(category);
        this.isPortrait = otherProperty == null || otherProperty.isPortrait();
    }

    private DMOtherProperty getOtherProperty(T1 category) {
        if(category instanceof DMCategory){
            return ((DMCategory) category).getOtherPropertyModel();
        }else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    protected abstract RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType);

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DMCategoryType.TYPE_LIST:
            case DMCategoryType.TYPE_GRID_HORIZONTAL:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
            case DMCategoryType.TYPE_LIST_CARD:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_view, parent, false));
            case DMCategoryType.TYPE_GRID:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_grid_card_view, parent, false));
            case DMCategoryType.TYPE_GRID_CARD:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_grid_view, parent, false));
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_scroll_view, parent, false));
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_slider, parent, false));
            case DMCategoryType.TYPE_TITLE_ONLY:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_title_only, parent, false));
            case DMCategoryType.TYPE_TITLE_WITH_COUNT:
                return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_title_with_count, parent, false));
            case DMCategoryType.TYPE_VIDEO_PLAYLIST:
                return new VideoViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_video_play_list, parent, false));
            case DMCategoryType.TYPE_VIDEO_CHANNEL:
                return new VideoViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_video_channel, parent, false));
            default:
                return onCreateViewHolderDynamic(parent, viewType);
        }
    }

    protected abstract void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoViewHolder) {
            VideoViewHolder<T2> holder = (VideoViewHolder) viewHolder;
            holder.setData(mList.get(position), position);
        }else {
            onBindViewHolderDynamic(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    protected String getUrl(String appImage) {
        if (TextUtils.isEmpty(appImage) || BaseUtil.isValidUrl(appImage)) {
            return appImage;
        }
        return imageUrl + appImage;
    }

    public class CommonChildHolder<T> extends BaseTimeViewHolder implements View.OnClickListener {
        public TextView tvTitle, tvTitleTag, tvCreatedAt;
        public ImageView ivIcon;
        public View cardView;

        public CommonChildHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvTitle = v.findViewById(R.id.tv_title);
            tvTitleTag = v.findViewById(R.id.tv_title_tag);
            tvCreatedAt = v.findViewById(R.id.tv_created_at);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }

        public void setData(T mItem, int pos) {
            if(mItem instanceof DMContent) {
                DMContent item = ((DMContent) mItem);
                if (tvTitle != null) {
                    if (itemType != DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE) {
                        tvTitle.setText(item.getTitle());
                        tvTitle.setVisibility(View.VISIBLE);
                    } else {
                        tvTitle.setVisibility(View.GONE);
                    }
                }
                if (tvCreatedAt != null) {
                    if (!TextUtils.isEmpty(item.getCreatedAt())) {
                        if (item.getItemType() == DMContentType.TYPE_HTML_VIEW || item.getItemType() == DMContentType.TYPE_VIDEOS) {
                            tvCreatedAt.setText(getTimeInDaysAgoFormat(item.getCreatedAt()));
                            tvCreatedAt.setVisibility(View.VISIBLE);
                        } else {
                            tvCreatedAt.setVisibility(View.GONE);
                        }
                    } else {
                        tvCreatedAt.setVisibility(View.GONE);
                    }
                }
                if (tvTitleTag != null) {
                    tvTitleTag.setText(String.format(Locale.ENGLISH, "%d", pos + 1));
                    setColorFilter(tvTitleTag.getBackground(), getSequentialColor(pos));
                }
                if (ivIcon != null) {
                    String imagePath = getUrl(item.getImage());
                    int placeHolder = getPlaceHolder();
                    if (BaseUtil.isValidUrl(imagePath)) {
                        Picasso.get().load(imagePath)
                                .placeholder(placeHolder)
                                .into(ivIcon);
                    } else if (item.getItemType() == DMContentType.TYPE_VIDEOS) {
                        if (!TextUtils.isEmpty(item.getLink())) {
                            String videoPreviewUrl = getYoutubePlaceholderImage(getVideoIdFromUrl(item.getLink()));
                            Picasso.get().load(videoPreviewUrl)
                                    .placeholder(R.drawable.ic_yt_placeholder)
                                    .error(R.drawable.ic_yt_placeholder)
                                    .into(ivIcon);
                        } else {
                            ivIcon.setImageResource(placeHolder);
                        }
                    } else {
                        ivIcon.setImageResource(placeHolder);
                    }
                }
                applyStyle(item, pos);
            }
        }

        private void applyStyle(DMContent item, int pos) {
            if (otherProperty != null) {
                if (otherProperty.isRandomBGColor()) {
                    if(cardView != null){
                        setColorFilter(cardView.getBackground(), getSequentialColor(pos));
                    }
                }
                if (otherProperty.isRandomIconColor()) {
                    if(ivIcon != null){
                        setColorFilter(ivIcon, getSequentialColor(pos));
                    }
                }
                if (otherProperty.isRemoveCard()) {
                    if(cardView != null && cardView instanceof CardView) {
                        ((CardView) cardView).setCardBackgroundColor(Color.TRANSPARENT);
                        ((CardView) cardView).setCardElevation(0);
                    }
                }
                if(ivIcon != null){
                    ViewGroup.LayoutParams params = ivIcon.getLayoutParams();
                    if (otherProperty.getWidth() > 0) {
                        params.width = dpToPx(otherProperty.getWidth());
                    }
                    if (otherProperty.getHeight() > 0) {
                        params.height = dpToPx(otherProperty.getHeight());
                    }
                    DMPadding padding = otherProperty.getPadding();
                    if(padding != null) {
                        ivIcon.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
                    }
                    ivIcon.setLayoutParams(params);
                }
                if(tvTitle != null){
                    if (otherProperty.getTextSize() > 0) {
                        tvTitle.setTextSize(otherProperty.getTextSize());
                    }
                }
            }
        }
    }


    /**
     * @param <T> : DMContent
     */
    public class VideoViewHolder<T> extends CommonChildHolder<T> implements View.OnClickListener {
        public final TextView tvWatchTime;
        public final ProgressBar progressBar;

        public VideoViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvWatchTime = v.findViewById(R.id.watch_time);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        public void setData(T mItem, int pos) {
            super.setData(mItem, pos);
            if(mItem instanceof DMContent) {
                DMContent item = ((DMContent) mItem);
                if (!TextUtils.isEmpty(item.getLink())) {
                    String videoPreviewUrl = getYoutubePlaceholderImage(getVideoIdFromUrl(item.getLink()));
                    Picasso.get().load(videoPreviewUrl)
                            .placeholder(R.drawable.ic_yt_placeholder)
                            .error(R.drawable.ic_yt_placeholder)
                            .into(ivIcon);
                    ivIcon.setVisibility(View.VISIBLE);
                } else {
                    if (TextUtils.isEmpty(item.getImage())) {
                        ivIcon.setVisibility(View.GONE);
                    }
                }
                if (cardView != null && cardView instanceof CardView) {
                    ((CardView) cardView).setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), item.getVideoDuration() > 0 ? R.color.yt_color_video_watched : R.color.themeBackgroundCardColor));
                }
                if (item.getVideoDuration() > 0) {
                    progressBar.setMax(item.getVideoDuration());
                    progressBar.setProgress(item.getVideoTime());
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
//            if (item.getVideoTime() > 0 && !TextUtils.isEmpty(item.getVideoTimeFormatted())) {
//                viewHolder.tvWatchTime.setText("Watched: " + mList.get(i).getVideoTimeFormatted());
//                viewHolder.tvWatchTime.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.tvWatchTime.setVisibility(View.GONE);
//            }
            }
        }
    }

    /**
     * @apiNote image quality: default.jpg, mqdefault.jpg, hqdefault.jpg, maxresdefault.jpg
     */
    private String getYoutubePlaceholderImage(String videoId) {
        String quality = isMediumVideoPlaceholderQuality ? "mqdefault.jpg" : "maxresdefault.jpg";
        return "https://i.ytimg.com/vi/" + videoId + "/" + quality;
    }

    public String getVideoIdFromUrl(String lectureVideo) {
        try {
            if (!TextUtils.isEmpty(lectureVideo) && BaseUtil.isValidUrl(lectureVideo)) {
                return lectureVideo.substring(lectureVideo.lastIndexOf("/") + 1);
            }else {
                return lectureVideo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return lectureVideo;
        }
    }

    protected int getPlaceHolder() {
        switch (itemType) {
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER:
            case DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE:
                return R.drawable.ic_dm_placeholder_slider;
            case DMCategoryType.TYPE_HORIZONTAL_CARD_SCROLL:
                if(isPortrait){
                    return R.drawable.ic_dm_placeholder_slider_portrait;
                }else {
                    return R.drawable.ic_dm_placeholder_graphic;
                }
            default:
                return R.drawable.ic_dm_placeholder_icon;
        }
    }

    @SuppressWarnings("deprecation")
    public void setColorFilter(@NonNull Object drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(drawable instanceof ImageView) {
                ((ImageView)drawable).setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            }else if(drawable instanceof Drawable) {
                ((Drawable)drawable).setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            }
        } else {
            if(drawable instanceof ImageView) {
                ((ImageView)drawable).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }else if(drawable instanceof Drawable) {
                ((Drawable)drawable).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private int getSequentialColor(int position) {
        String[] colors = context.getResources().getStringArray(R.array.dynamic_colors);
        if(position % colors.length == 0) {
            return Color.parseColor(colors[0]);
        } else  {
            for (int i = 1; i < colors.length; i++){
                if(position == i || (position-i) % colors.length == 0) {
                    return Color.parseColor(colors[i]);
                }
            }
            return Color.parseColor(colors[0]);
        }
    }

    public int dpToPx(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public BaseDynamicChildAdapter<T1, T2> setMediumVideoPlaceholderQuality(boolean mediumVideoPlaceholderQuality) {
        isMediumVideoPlaceholderQuality = mediumVideoPlaceholderQuality;
        return this;
    }
}