package com.ellen.baselibrary.mvp.fragment;

public class BaseFragmentPresenter<M extends BaseFragmentModel,V extends BaseFragmentView> {
    public M mFragmentModel;
    public V mFragmentView;
}
