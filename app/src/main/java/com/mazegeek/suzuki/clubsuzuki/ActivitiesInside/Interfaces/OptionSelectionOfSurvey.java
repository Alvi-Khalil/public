package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Options;

import java.util.List;

public interface OptionSelectionOfSurvey {
    void onClickOption(String value);
    void onClickMultipleOption(List<Options> arrayListOfOptions);
}
