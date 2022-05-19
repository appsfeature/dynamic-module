package com.dynamic.listeners;

public interface DMCategoryType {
    int TYPE_LIST = 0;                          // list simple child card
    int TYPE_GRID = 1;                          // list simple child card
    int TYPE_GRID_HORIZONTAL = 3;               // list simple child card with horizontal align
    int TYPE_HORIZONTAL_CARD_SCROLL = 4;        // PlayStore like card manual slider.
    int TYPE_VIEWPAGER_AUTO_SLIDER = 5;         // Auto slider with full screen of width
    int TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE = 6;// Auto slider with full screen of width without title
    int TYPE_LIST_CARD = 7;                     // list card child simple
    int TYPE_GRID_CARD = 8;                     // list card child simple
    int TYPE_TITLE_ONLY = 9;                    // Showing title only
    int TYPE_TITLE_WITH_COUNT = 10;             // Showing title with count
    int TYPE_VIDEO_PLAYLIST = 11;               // Showing youtube videos list
    int TYPE_VIDEO_CHANNEL = 12;                // Showing youtube channel list
}
