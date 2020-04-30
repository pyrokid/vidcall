package com.pyrokid.myapplication.modules.call.incomingCall;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface IncomingCallView extends MvpView {
}
