package com.pyrokid.myapplication.modules.main;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;


@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface MainView extends MvpView {

}
