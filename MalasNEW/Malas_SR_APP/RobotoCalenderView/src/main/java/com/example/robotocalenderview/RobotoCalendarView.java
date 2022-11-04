
package com.example.robotocalenderview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * The roboto calendar view
 *
 * @author Marco Hernaiz Cao
 */
public class RobotoCalendarView extends LinearLayout {
    private static final String DAY_OF_THE_WEEK_TEXT = "dayOfTheWeekText";
    private static final String DAY_OF_THE_WEEK_LAYOUT = "dayOfTheWeekLayout";
    private static final String DAY_OF_THE_MONTH_LAYOUT = "dayOfTheMonthLayout";
    private static final String DAY_OF_THE_MONTH_TEXT = "dayOfTheMonthText";
    private static final String DAY_OF_THE_MONTH_BACKGROUND = "dayOfTheMonthBackground";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 = "dayOfTheMonthCircleImage1";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 = "dayOfTheMonthCircleImage2";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_3 = "dayOfTheMonthCircleImage3";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_4 = "dayOfTheMonthCircleImage4";


    private TextView dateTitle;
    private ImageView leftButton;
    private ImageView rightButton;
    private View rootView;
    private ViewGroup robotoCalendarMonthLayout;
    @NonNull
    private Calendar currentCalendar = Calendar.getInstance();
    @Nullable
    private Calendar lastSelectedDayCalendar;
    private boolean shortWeekDays = false;

    private RobotoCalendarListener robotoCalendarListener;

    public RobotoCalendarView(Context context) {
        super(context);
        init(null);
    }

    public RobotoCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RobotoCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RobotoCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        if (isInEditMode()) {
            return;
        }

        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflate.inflate(R.layout.roboto_calendar_view_layout, this, true);
        findViewsById(rootView);
        setUpEventListeners();

        currentCalendar = Calendar.getInstance();
        setDate(currentCalendar.getTime());


        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    private void findViewsById(View view) {
        robotoCalendarMonthLayout = view.findViewById(R.id.robotoCalendarDateTitleContainer);
        leftButton = view.findViewById(R.id.leftButton);
        rightButton = view.findViewById(R.id.rightButton);
        dateTitle = view.findViewById(R.id.monthText);

        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 42; i++) {

            int weekIndex = (i % 7) + 1;
            ViewGroup dayOfTheWeekLayout = view.findViewWithTag(DAY_OF_THE_WEEK_LAYOUT + weekIndex);

            // Create day of the month
            View dayOfTheMonthLayout = inflate.inflate(R.layout.roboto_calendar_day_of_the_month_layout, null);
            View dayOfTheMonthText = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_TEXT);
            View dayOfTheMonthBackground = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_BACKGROUND);
            View dayOfTheMonthCircleImage1 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1);
            View dayOfTheMonthCircleImage2 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2);
            View dayOfTheMonthCircleImage3 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_3);
            View dayOfTheMonthCircleImage4 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_4);

            // Set tags to identify them
            int viewIndex = i + 1;
            dayOfTheMonthLayout.setTag(DAY_OF_THE_MONTH_LAYOUT + viewIndex);
            dayOfTheMonthText.setTag(DAY_OF_THE_MONTH_TEXT + viewIndex);
            dayOfTheMonthBackground.setTag(DAY_OF_THE_MONTH_BACKGROUND + viewIndex);
            dayOfTheMonthCircleImage1.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 + viewIndex);
            dayOfTheMonthCircleImage2.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 + viewIndex);
            dayOfTheMonthCircleImage3.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_3 + viewIndex);
            dayOfTheMonthCircleImage4.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_4 + viewIndex);

            dayOfTheWeekLayout.addView(dayOfTheMonthLayout);
        }

    }

    public void setDate(@NonNull Date date) {
        currentCalendar.setTime(date);
        updateView();
    }

    private void updateView() {
        setUpMonthLayout();
        setUpWeekDaysLayout();
        setUpDaysOfMonthLayout();
        setUpDaysInCalendar();
        markDayAsCurrentDay();
    }

    /**
     * Write Day of the Week on view
     */
    private void setUpWeekDaysLayout() {
        TextView dayOfWeek;
        String dayOfTheWeekString;
        String[] weekDaysArray = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
        int length = weekDaysArray.length;
        for (int i = 1; i < length; i++) {
            dayOfWeek = rootView.findViewWithTag(DAY_OF_THE_WEEK_TEXT + getWeekIndex(i, currentCalendar));
            dayOfTheWeekString = weekDaysArray[i];
            if (shortWeekDays) {
                dayOfTheWeekString = checkSpecificLocales(dayOfTheWeekString, i);
            } else {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase() + dayOfTheWeekString.substring(1, 3);
            }
            dayOfWeek.setText(dayOfTheWeekString);
        }
    }

    private static int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private static String checkSpecificLocales(String dayOfTheWeekString, int i) {
        // Set Wednesday as "X" in Spanish Locale.getDefault()
        if (i == 4 && "ES".equals(Locale.getDefault().getCountry())) {
            dayOfTheWeekString = "X";
        } else {
            dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();
        }
        return dayOfTheWeekString;
    }

    /**
     * Write Current Month Name In Title
     */

    private void setUpMonthLayout() {
        String dateText = new DateFormatSymbols(Locale.getDefault()).getMonths()[currentCalendar.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        dateTitle.setText(String.format("%s %s", dateText, currentCalendar.get(Calendar.YEAR)));
    }

    /**
     * Formating month days
     */

    private void setUpDaysOfMonthLayout() {

        TextView dayOfTheMonthText;
        View circleImage1;
        View circleImage2;
        View circleImage3;
        View circleImage4;

        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthBackground;

        for (int i = 1; i < 43; i++) {

            dayOfTheMonthContainer = rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i);
            dayOfTheMonthBackground = rootView.findViewWithTag(DAY_OF_THE_MONTH_BACKGROUND + i);
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i);
            circleImage1 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 + i);
            circleImage2 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 + i);
            circleImage3 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_3 + i);
            circleImage4 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_4 + i);

            dayOfTheMonthText.setVisibility(View.INVISIBLE);
            circleImage1.setVisibility(View.GONE);
            circleImage2.setVisibility(View.GONE);
            circleImage3.setVisibility(View.GONE);
            circleImage4.setVisibility(View.GONE);

            // Apply styles
            dayOfTheMonthText.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthText.setTypeface(null, Typeface.NORMAL);
            dayOfTheMonthText.setTextColor(ContextCompat.getColor(getContext(), R.color.roboto_calendar_day_of_the_month_font));
            dayOfTheMonthContainer.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthContainer.setOnClickListener(null);
            dayOfTheMonthBackground.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void setUpDaysInCalendar() {

        Calendar auxCalendar = Calendar.getInstance(Locale.getDefault());
        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
        TextView dayOfTheMonthText;
        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthLayout;

        // Calculate dayOfTheMonthIndex
        int dayOfTheMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfTheMonthIndex++) {
            dayOfTheMonthContainer = rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + dayOfTheMonthIndex);
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + dayOfTheMonthIndex);
            if (dayOfTheMonthText == null) {
                break;
            }
            dayOfTheMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfTheMonthContainer.setOnLongClickListener(onDayOfMonthLongClickListener);
            dayOfTheMonthText.setVisibility(View.VISIBLE);
            dayOfTheMonthText.setText(String.valueOf(i));

        }

        for (int i = 36; i < 43; i++) {
            dayOfTheMonthText = rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i);
            dayOfTheMonthLayout = rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i);
            if (dayOfTheMonthText.getVisibility() == INVISIBLE) {
                dayOfTheMonthLayout.setVisibility(GONE);
            } else {
                dayOfTheMonthLayout.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Mark Current Day
     */

    private void markDayAsCurrentDay() {
        // If it's the current month, mark current day
        Calendar nowCalendar = Calendar.getInstance();
        if (nowCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(nowCalendar.getTime());

            ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(currentCalendar);
            dayOfTheMonthBackground.setBackgroundResource(R.drawable.ring);
        }
    }

    private ViewGroup getDayOfMonthBackground(Calendar currentCalendar) {
        return (ViewGroup) getView(DAY_OF_THE_MONTH_BACKGROUND, currentCalendar);
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        return rootView.findViewWithTag(key + index);
    }

    private static int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private static int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {

            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    /**
     * Overriding On Long Click Listener
     */


    OnLongClickListener onDayOfMonthLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            // Extract day selected
            ViewGroup dayOfTheMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfTheMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_THE_MONTH_LAYOUT.length());
            TextView dayOfTheMonthText = view.findViewWithTag(DAY_OF_THE_MONTH_TEXT + tagId);

            // Extract the day from the text
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfTheMonthText.getText().toString()));

            markDayAsSelectedDay(calendar.getTime(), view);

            // Fire event
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDayLongClick(calendar.getTime());
            }
            return true;
        }
    };
    /**
     * Overriding Onclick Listener
     */
    private final OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            // Extract day selected
            ViewGroup dayOfTheMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfTheMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_THE_MONTH_LAYOUT.length());
            TextView dayOfTheMonthText = view.findViewWithTag(DAY_OF_THE_MONTH_TEXT + tagId);

            // Extract the day from the text
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfTheMonthText.getText().toString()));

            markDayAsSelectedDay(calendar.getTime(),view);
            // Fire event
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDayClick(calendar.getTime(),view);
            }
        }
    };

    /** mark day as  Selected Day */

    public void markDayAsSelectedDay(@NonNull Date date, View view) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Clear previous current day mark
        clearSelectedDay();

        // Store current values as last values
        lastSelectedDayCalendar = calendar;

        // Mark current day as selected
        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);

        TextView dayOfTheMonth = getDayOfMonthText(calendar);
        dayOfTheMonth.setTextColor(ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        Drawable buttonDrawable = dayOfTheMonthBackground.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable,getResources().getColor( R.color.roboto_calendar_selected_day_background));
        dayOfTheMonthBackground.setBackground(buttonDrawable);

        ImageView circleImage1 = getCircleImage1(calendar);
        ImageView circleImage2 = getCircleImage2(calendar);
        if (circleImage1.getVisibility() == VISIBLE) {
            DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        }

        if (circleImage2.getVisibility() == VISIBLE) {
            DrawableCompat.setTint(circleImage2.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        }
    }
    public void markWeekOff(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        TextView dayOfTheMonth = getDayOfMonthText(calendar);
        dayOfTheMonth.setTextColor(ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_1));
    }

    public void markHoliday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Mark current day as selected
        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);
        Drawable buttonDrawable = dayOfTheMonthBackground.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable,getResources().getColor( R.color.roboto_calendar_circle_3));
        dayOfTheMonthBackground.setBackground(buttonDrawable); }

    public void markAttendance(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        // Mark current day as selected
        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);
        Drawable buttonDrawable = dayOfTheMonthBackground.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable,getResources().getColor( R.color.roboto_calendar_circle_2));
        dayOfTheMonthBackground.setBackground(buttonDrawable);
    }

    public void markLeave(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        // Mark current day as selected
        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);
        Drawable buttonDrawable = dayOfTheMonthBackground.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable,getResources().getColor(R.color.roboto_calendar_circle_5));
        dayOfTheMonthBackground.setBackground(buttonDrawable);
    }


    public void markAbsent(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        // Mark current day as selected
        ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(calendar);
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle);
        Drawable buttonDrawable = dayOfTheMonthBackground.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, Color.RED);
        dayOfTheMonthBackground.setBackground(buttonDrawable);
    }

    private TextView getDayOfMonthText(Calendar currentCalendar) {
        return (TextView) getView(DAY_OF_THE_MONTH_TEXT, currentCalendar);
    }

    private ImageView getCircleImage1(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1, currentCalendar);
    }

    private ImageView getCircleImage2(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2, currentCalendar);
    }

    private ImageView getCircleImage3(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_3, currentCalendar);
    }

    private ImageView getCircleImage4(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_4, currentCalendar);
    }

    /**
     * Clear Selected Day
     */

    public void clearSelectedDay() {
        if (lastSelectedDayCalendar != null) {
            ViewGroup dayOfTheMonthBackground = getDayOfMonthBackground(lastSelectedDayCalendar);

            // If it's today, keep the current day style
            Calendar nowCalendar = Calendar.getInstance();
            if (nowCalendar.get(Calendar.YEAR) == lastSelectedDayCalendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.DAY_OF_YEAR) == lastSelectedDayCalendar.get(Calendar.DAY_OF_YEAR)) {
                dayOfTheMonthBackground.setBackgroundResource(R.drawable.ring);
            } else {
                dayOfTheMonthBackground.setBackgroundResource(android.R.color.transparent);
            }

            TextView dayOfTheMonth = getDayOfMonthText(lastSelectedDayCalendar);
            dayOfTheMonth.setTextColor(ContextCompat.getColor(getContext(), R.color.roboto_calendar_day_of_the_month_font));

            ImageView circleImage1 = getCircleImage1(lastSelectedDayCalendar);
            ImageView circleImage2 = getCircleImage2(lastSelectedDayCalendar);
            if (circleImage1.getVisibility() == VISIBLE) {
                DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_1));
            }

            if (circleImage2.getVisibility() == VISIBLE) {
                DrawableCompat.setTint(circleImage2.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_2));
            }
        }
    }

    /**
     * Event Listener For Month Change
     */

    private void setUpEventListeners() {

        leftButton.setOnClickListener(view -> {
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            }

            // Decrease month
            currentCalendar.add(Calendar.MONTH, -1);
            lastSelectedDayCalendar = null;
            RobotoCalendarView.this.updateView();
            robotoCalendarListener.onLeftButtonClick();
        });

        rightButton.setOnClickListener(view -> {
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            }

            // Increase month
            currentCalendar.add(Calendar.MONTH, 1);
            lastSelectedDayCalendar = null;
            RobotoCalendarView.this.updateView();
            robotoCalendarListener.onRightButtonClick();
        });
    }

    public void setRobotoCalendarListener(RobotoCalendarListener robotoCalendarListener) {
        this.robotoCalendarListener = robotoCalendarListener;
    }

    public void showDateTitle(boolean show) {
        if (show) {
            robotoCalendarMonthLayout.setVisibility(VISIBLE);
        } else {
            robotoCalendarMonthLayout.setVisibility(GONE);
        }
    }

    public void setShortWeekDays(boolean shortWeekDays) {
        this.shortWeekDays = shortWeekDays;
    }

    public void markCircleImage1(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ImageView circleImage1 = getCircleImage1(calendar);
        circleImage1.setVisibility(View.VISIBLE);
        if (lastSelectedDayCalendar != null && areInTheSameDay(calendar, lastSelectedDayCalendar)) {
            DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        } else {
            DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_1));
        }
    }

    public void markCircleImage2(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ImageView circleImage2 = getCircleImage2(calendar);
        circleImage2.setVisibility(View.VISIBLE);
        if (lastSelectedDayCalendar != null && areInTheSameDay(calendar, lastSelectedDayCalendar)) {
            DrawableCompat.setTint(circleImage2.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        } else {
            DrawableCompat.setTint(circleImage2.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_2));
        }
    }


    public void markCircleImage3(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ImageView circleImage3 = getCircleImage3(calendar);
        circleImage3.setVisibility(View.VISIBLE);
        if (lastSelectedDayCalendar != null && areInTheSameDay(calendar, lastSelectedDayCalendar)) {
            DrawableCompat.setTint(circleImage3.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        } else {
            DrawableCompat.setTint(circleImage3.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_3));
        }
    }


    public void markCircleImage4(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ImageView circleImage4 = getCircleImage4(calendar);
        circleImage4.setVisibility(View.VISIBLE);
        if (lastSelectedDayCalendar != null && areInTheSameDay(calendar, lastSelectedDayCalendar)) {
            DrawableCompat.setTint(circleImage4.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_selected_day_font));
        } else {
            DrawableCompat.setTint(circleImage4.getDrawable(), ContextCompat.getColor(getContext(), R.color.roboto_calendar_circle_4));
        }
    }

    private static boolean areInTheSameDay(@NonNull Calendar calendarOne, @NonNull Calendar calendarTwo) {
        return calendarOne.get(Calendar.YEAR) == calendarTwo.get(Calendar.YEAR) && calendarOne.get(Calendar.DAY_OF_YEAR) == calendarTwo.get(Calendar.DAY_OF_YEAR);
    }

    public Calendar currentmonth() {
        return currentCalendar;
    }

    /**
     * Listener Interface
     */
    public interface RobotoCalendarListener {

        void onDayClick(Date date, View view);

        void onDayLongClick(Date date);

        void onRightButtonClick();

        void onLeftButtonClick();

    }

}


