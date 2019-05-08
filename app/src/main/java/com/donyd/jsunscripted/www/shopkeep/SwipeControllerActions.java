package com.donyd.jsunscripted.www.shopkeep;

import java.util.HashMap;

// code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
public abstract class SwipeControllerActions {

    public void onLeftClicked(int position) {}

    public void onRightClicked(int position, HashMap<String, Float> hashList) {}

}