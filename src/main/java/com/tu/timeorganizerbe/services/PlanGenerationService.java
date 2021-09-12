package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.Activity;
import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.entities.User;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import com.tu.timeorganizerbe.models.PlanGenerationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanGenerationService {

    private final ActivityInstanceService activityInstanceService;
    private final UserService userService;
    private static final String TIME_PREFERENCE_CONCRETE = "Concrete Time";
    private static final String TIME_PREFERENCE_MORNING = "Morning";
    private static final String TIME_PREFERENCE_AFTERNOON = "Afternoon";
    private static final String TIME_PREFERENCE_EVENING = "Evening";
    private static final List<String> WEEK = Arrays.asList("Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday");

    private List<ActivityInstanceModel> failedInstances = new ArrayList<>();

    @Autowired
    public PlanGenerationService(ActivityInstanceService activityInstanceService, UserService userService) {
        this.activityInstanceService = activityInstanceService;
        this.userService = userService;
    }

    public void generateWeek(PlanGenerationModel planModel) {
        List<Activity> activities = planModel.getActivities();
        List<Activity> concreteDaysAndTime = this.getConcreteDaysAndTimeActivities(activities);
        List<Activity> concreteTime = this.getConcreteTimeActivities(activities);
        List<Activity> concreteDaysAndTimePreference = this.getConcreteDaysAndTimePreferenceActivities(activities);
        List<Activity> concreteDays = this.getConcreteDaysActivities(activities);
        List<Activity> timePreference = this.getTimePreferenceActivities(activities);
        List<Activity> noPreferences = this.getNoPreferencesActivities(activities);

        assert (activities.size() == concreteDaysAndTime.size() + concreteDaysAndTimePreference.size() +
                concreteDays.size() + concreteTime.size() + timePreference.size() + noPreferences.size());

        this.generateConcreteDaysAndTime(concreteDaysAndTime, planModel);
        this.generateConcreteTime(concreteTime, planModel);
        this.generateConcreteDaysAndTimePreference(concreteDaysAndTimePreference, planModel);
        this.generateConcreteDays(concreteDays, planModel);
        this.generateTimePref(timePreference, planModel);
        this.generateNoPref(noPreferences, planModel);
        this.generateFailed();
    }

    private void generateConcreteDaysAndTime(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            if (activity.getFrequency() <= activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getFrequency(); i++) {
                    this.createConcreteDayAndTimeInstance(planModel, activity, activity.getDayPreference().get(i));
                }
            } else if (activity.getFrequency() > activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getDayPreference().size(); i++) {
                    this.createConcreteDayAndTimeInstance(planModel, activity, activity.getDayPreference().get(i));
                }
                for (int i = 0; i < activity.getFrequency() - activity.getDayPreference().size(); i++) {
                    this.createConcreteTimeInstance(planModel, activity);
                }
            }
        });
    }

    private void generateConcreteDaysAndTimePreference(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            if (activity.getFrequency() <= activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getFrequency(); i++) {
                    this.createConcreteDayAndTimePrefInstance(planModel,
                            activity, activity.getDayPreference().get(i));
                }
            } else if (activity.getFrequency() > activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getDayPreference().size(); i++) {
                    this.createConcreteDayAndTimePrefInstance(planModel,
                            activity, activity.getDayPreference().get(i));
                }

                for (int i = 0; i < activity.getFrequency() - activity.getDayPreference().size(); i++) {
                    this.createTimePrefInstance(planModel, activity);
                }
            }
        });
    }

    private void generateConcreteTime(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            for (int i = 0; i < activity.getFrequency(); i++) {
                this.createConcreteTimeInstance(planModel, activity);
            }
        });
    }

    private void generateConcreteDays(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            if (activity.getFrequency() < activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getFrequency(); i++) {
                    this.createNoPrefInstanceInDays(planModel, activity, activity.getDayPreference());
                }
            } else if (activity.getFrequency() == activity.getDayPreference().size()) {
                for (int i = 0; i < activity.getFrequency(); i++) {
                    this.createConcreteDayInstance(planModel,
                            activity, activity.getDayPreference().get(i));
                }
            } else if (activity.getFrequency() > activity.getDayPreference().size()) {
                int numOfDayPref = activity.getDayPreference().size();
                int[] instancesPerDay = this.split(activity.getFrequency(),  numOfDayPref);
                for (int i = 0; i < numOfDayPref; i++) {
                    this.createConcreteDayMultipleInstances(planModel,
                            activity, activity.getDayPreference().get(i), instancesPerDay[i]);
                }
            }
        });
    }

    private void generateTimePref(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            for (int i = 0; i < activity.getFrequency(); i++) {
                this.createTimePrefInstance(planModel, activity);
            }
        });
    }

    private void generateNoPref(List<Activity> activities, PlanGenerationModel planModel) {
        activities.forEach((activity) -> {
            for (int i = 0; i < activity.getFrequency(); i++) {
                this.createNoPrefInstance(planModel, activity);
            }
        });
    }

    private void createConcreteDayAndTimeInstance(PlanGenerationModel planModel, Activity activity, String day) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        int dayOfMonth = this.getDayAndMonth(weekStart, day)[0];
        int month = this.getDayAndMonth(weekStart, day)[1];
        LocalDate startDate = LocalDate.of(yearStart, month, dayOfMonth);
        LocalTime startTime = activity.getConcreteTime();
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = start.plusMinutes(activity.getDuration());
        String title = activity.getIcon() + " " + activity.getName();

        ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end, activity.getColor().getSecondaryColor());
        this.activityInstanceService.createActivityInstance(model);
    }

    private void createConcreteDayAndTimePrefInstance(PlanGenerationModel planModel, Activity activity, String day) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        LocalDate startDate = LocalDate.of(yearStart,
                this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
        LocalTime startTime = this.getTimeBasedOnTimePreference(startDate, activity.getTimePreference(),
                activity.getDuration(), planModel.getUserId());

        if (startTime != null) {
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = start.plusMinutes(activity.getDuration());
            String title = activity.getIcon() + " " + activity.getName();

            ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                    activity.getColor().getSecondaryColor());
            this.activityInstanceService.createActivityInstance(model);
        } else {
            // Set at the same time as some other instance
            createConcreteDayAndTimePrefInstanceAlt(planModel, activity, day);
        }
    }

    private void createConcreteTimeInstance(PlanGenerationModel planModel, Activity activity) {
        LocalTime startTime = activity.getConcreteTime();
        LocalDate startDate = this.findPossibleStartDate(planModel, activity);

        if (startDate == null) {
            // Set at the same time as some other instance
            startDate = this.findRandomStartDate(planModel);
            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            LocalDateTime end = start.plusMinutes(activity.getDuration());
            String title = activity.getIcon() + " " + activity.getName();

            ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                    activity.getColor().getSecondaryColor());

            this.failedInstances.add(model);
        } else {
            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            LocalDateTime end = start.plusMinutes(activity.getDuration());
            String title = activity.getIcon() + " " + activity.getName();

            ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                    activity.getColor().getSecondaryColor());

            this.activityInstanceService.createActivityInstance(model);
        }
    }

    private void createTimePrefInstance(PlanGenerationModel planModel, Activity activity) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();

        Collections.shuffle(WEEK);
        for (String day : WEEK) {
            LocalDate proposedStartDate = LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1],
                    this.getDayAndMonth(weekStart, day)[0]);
            LocalTime proposedStartTime = this.getTimeBasedOnTimePreference(proposedStartDate, activity.getTimePreference(),
                    activity.getDuration(), planModel.getUserId());

            if (proposedStartTime != null) {
                LocalDateTime start = LocalDateTime.of(proposedStartDate, proposedStartTime);
                LocalDateTime end = start.plusMinutes(activity.getDuration());
                String title = activity.getIcon() + " " + activity.getName();

                ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                        activity.getColor().getSecondaryColor());
                this.activityInstanceService.createActivityInstance(model);
                return;
            }
        }
        // Set at the same time as some other instance
        Collections.shuffle(WEEK);
        this.createConcreteDayAndTimePrefInstanceAlt(planModel, activity, WEEK.get(0));
    }

    private void createConcreteDayInstance(PlanGenerationModel planModel, Activity activity, String day) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        LocalDate startDate = LocalDate.of(yearStart,
                this.getDayAndMonth(weekStart, day)[1],
                this.getDayAndMonth(weekStart, day)[0]);
        List<ActivityInstance> wholeDayInstances = this.activityInstanceService.findWholeDayInstances(startDate, planModel.getUserId())
                .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

        LocalTime startTime = LocalTime.of(5, 0);

        if (wholeDayInstances.size() > 0) {
            startTime = this.findPossibleStartTime(startDate, activity.getDuration(),
                    wholeDayInstances, planModel.getUserId());
        }

        if (startTime != null) {
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = start.plusMinutes(activity.getDuration());
            String title = activity.getIcon() + " " + activity.getName();

            ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                    activity.getColor().getSecondaryColor());
            this.activityInstanceService.createActivityInstance(model);
        } else {
            // Set at the same time as some other instance
            this.createConcreteDayInstanceAlt(planModel, activity, day);
        }
    }

    public void createConcreteDayMultipleInstances(PlanGenerationModel planModel, Activity activity,
                                                                          String day, int numOfInstances) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();

        LocalDate startDate = LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
        List<ActivityInstance> wholeDayInstances = this.activityInstanceService.findWholeDayInstances(startDate, planModel.getUserId())
                .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

        for (int i = 0; i < numOfInstances; i++) {
            LocalTime startTime = LocalTime.of(5, 0);
            if (wholeDayInstances.size() > 0) {
                startTime = this.findPossibleStartTime(startDate, activity.getDuration(),
                        wholeDayInstances, planModel.getUserId());
            }

            if (startTime != null) {
                LocalDateTime start = LocalDateTime.of(startDate, startTime);
                LocalDateTime end = start.plusMinutes(activity.getDuration());
                String title = activity.getIcon() + " " + activity.getName();

                ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(),
                        start, end, activity.getColor().getSecondaryColor());
                wholeDayInstances.add(this.activityInstanceService.createActivityInstance(model));
            } else {
                // Set at the same time as some other instance
                this.createConcreteDayInstanceAlt(planModel, activity, day);
            }
        }
    }

    private void createNoPrefInstance(PlanGenerationModel planModel, Activity activity) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();

        Collections.shuffle(WEEK);
        for (String day : WEEK) {
            LocalDate proposedStartDate = LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1],
                    this.getDayAndMonth(weekStart, day)[0]);
            List<ActivityInstance> dayInstances = this.activityInstanceService
                    .findWholeDayInstances(proposedStartDate, planModel.getUserId())
                    .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

            LocalTime proposedStartTime = LocalTime.of(5, 0);
            if (dayInstances.size() > 0) {
                proposedStartTime = this.findPossibleStartTime(proposedStartDate, activity.getDuration(),
                        dayInstances, planModel.getUserId());
            }

            if (proposedStartTime != null) {
                LocalDateTime start = LocalDateTime.of(proposedStartDate, proposedStartTime);
                LocalDateTime end = start.plusMinutes(activity.getDuration());
                String title = activity.getIcon() + " " + activity.getName();

                ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                        activity.getColor().getSecondaryColor());
                this.activityInstanceService.createActivityInstance(model);
                return;
            }
        }
        // Set at the same time as some other instance
        Collections.shuffle(WEEK);
        this.createConcreteDayInstanceAlt(planModel, activity, WEEK.get(0));
    }

    private void createNoPrefInstanceInDays(PlanGenerationModel planModel, Activity activity, List<String> days) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();

        Collections.shuffle(days);
        for (String day : days) {
            LocalDate proposedStartDate = LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1],
                    this.getDayAndMonth(weekStart, day)[0]);
            List<ActivityInstance> dayInstances = this.activityInstanceService
                    .findWholeDayInstances(proposedStartDate, planModel.getUserId())
                    .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

            LocalTime proposedStartTime = LocalTime.of(5, 0);
            if (dayInstances.size() > 0) {
                proposedStartTime = this.findPossibleStartTime(proposedStartDate, activity.getDuration(),
                        dayInstances, planModel.getUserId());
            }

            if (proposedStartTime != null) {
                LocalDateTime start = LocalDateTime.of(proposedStartDate, proposedStartTime);
                LocalDateTime end = start.plusMinutes(activity.getDuration());
                String title = activity.getIcon() + " " + activity.getName();

                ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                        activity.getColor().getSecondaryColor());
                this.activityInstanceService.createActivityInstance(model);
                return;
            }
        }
        // Set at the same time as some other instance
        Collections.shuffle(days);
        this.createConcreteDayInstanceAlt(planModel, activity, days.get(0));
    }

    private int[] getDayAndMonth(LocalDateTime weekStart, String day) {
        LocalDate endDate = weekStart.plusDays(7).toLocalDate();

        for (LocalDate date = weekStart.toLocalDate(); date.isBefore(endDate); date = date.plusDays(1)) {
            String current = date.getDayOfWeek().toString();
            if (current.equalsIgnoreCase(day)) {
                return new int[]{date.getDayOfMonth(), date.getMonthValue()};
            }
        }

        throw new Error("No such day " + day);
    }

    private LocalTime getTimeBasedOnTimePreference(LocalDate startDate, List<String> timePreferences, int duration, Integer userId) {
        for (String timePref: timePreferences) {
            if (TIME_PREFERENCE_MORNING.equalsIgnoreCase(timePref)) {
                LocalTime startTime = this.getMorningPrefTime(startDate, duration, userId);
                if (startTime != null) {
                    return startTime;
                }
            } else if (TIME_PREFERENCE_AFTERNOON.equalsIgnoreCase(timePref)) {
                LocalTime startTime = this.getAfternoonPrefTime(startDate, duration, userId);
                if (startTime != null) {
                    return startTime;
                }
            } else if (TIME_PREFERENCE_EVENING.equalsIgnoreCase(timePref)) {
                LocalTime startTime = this.getEveningPrefTime(startDate, duration, userId);
                if (startTime != null) {
                    return startTime;
                }
            }
        }
        return null;
    }

    private LocalTime getMorningPrefTime(LocalDate startDate, int duration, Integer userId) {
        List<ActivityInstance> morningInstances = this.activityInstanceService.findMorningInstances(startDate, userId)
                .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

        if (morningInstances.size() > 0) {
            return this.findPossibleStartTime(startDate, duration, morningInstances, userId);
        } else {
            return this.findPossibleStartTimeWhen(startDate, duration, userId, "Morning");
        }
    }

    private LocalTime getAfternoonPrefTime(LocalDate startDate, int duration, Integer userId) {
        List<ActivityInstance> afternoonInstances = this.activityInstanceService.findAfternoonInstances(startDate, userId)
                .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());
        if (afternoonInstances.size() > 0) {
            return this.findPossibleStartTime(startDate, duration, afternoonInstances, userId);
        } else {
            return this.findPossibleStartTimeWhen(startDate, duration, userId, "Afternoon");
        }
    }

    private LocalTime getEveningPrefTime(LocalDate startDate, int duration, Integer userId) {
        List<ActivityInstance> eveningInstances = this.activityInstanceService.findEveningInstances(startDate, userId)
                .stream().sorted(Comparator.comparing(ActivityInstance::getEnd).reversed()).collect(Collectors.toList());

        if (eveningInstances.size() > 0) {
            return this.findPossibleStartTime(startDate, duration, eveningInstances, userId);
        } else {
            return this.findPossibleStartTimeWhen(startDate, duration, userId, "Evening");
        }
    }

    private LocalTime findPossibleStartTime(LocalDate startDate, int duration,
                                            List<ActivityInstance> instances, Integer userId) {
        for (ActivityInstance currentInstance: instances) {
            LocalTime currentEndTime = currentInstance.getEnd().toLocalTime();
            LocalTime proposedStartTime = currentEndTime.plusMinutes(1);
            LocalDateTime proposedStart = LocalDateTime.of(startDate, proposedStartTime);
            LocalDateTime proposedEnd = proposedStart.plusMinutes(duration);
            List<ActivityInstance> proposedIntervalInstances = this.activityInstanceService
                    .findInstancesBetweenDates(proposedStart, proposedEnd, userId);

            if (proposedIntervalInstances.size() == 0) {
                return proposedStartTime;
            }
        }
        return null;
    }

    private LocalDate findPossibleStartDate(PlanGenerationModel planModel, Activity activity) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        LocalTime startTime = activity.getConcreteTime();

        Collections.shuffle(WEEK);
        for (String day : WEEK) {
            LocalDate proposedStartDate = LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
            LocalDateTime proposedStart = LocalDateTime.of(proposedStartDate, startTime);
            LocalDateTime proposedEnd = proposedStart.plusMinutes(activity.getDuration());
            List<ActivityInstance> proposedIntervalInstances = this.activityInstanceService
                    .findInstancesBetweenDates(proposedStart, proposedEnd, planModel.getUserId());


            if (proposedIntervalInstances.size() == 0) {
                return proposedStartDate;
            }
        }

        return null;
    }

    private LocalTime findPossibleStartTimeWhen(LocalDate startDate, int duration, Integer userId, String when) {
        LocalTime proposedStartTime = LocalTime.of(5, 0);
        User user = this.userService.findUser(userId);
        if ("Morning".equalsIgnoreCase(when)) {
            int morningStartHour = 5;
            if (user.getDayStartHour() > morningStartHour) {
                morningStartHour = user.getDayStartHour();
            }
            proposedStartTime = LocalTime.of(morningStartHour, 0);
        } else if ("Afternoon".equalsIgnoreCase(when)) {
            proposedStartTime = LocalTime.of(12, 0);
        } else if ("Evening".equalsIgnoreCase(when)) {
            proposedStartTime = LocalTime.of(17, 1);
        }

        LocalDateTime proposedStart = LocalDateTime.of(startDate, proposedStartTime);
        LocalDateTime proposedEnd = proposedStart.plusMinutes(duration);
        List<ActivityInstance> proposedIntervalInstances = this.activityInstanceService
                .findInstancesBetweenDates(proposedStart, proposedEnd, userId);

        if (proposedIntervalInstances.size() == 0) {
            return proposedStartTime;
        }
        return null;
    }

    private void createConcreteDayAndTimePrefInstanceAlt(PlanGenerationModel planModel, Activity activity, String day) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        LocalDate startDate = LocalDate.of(yearStart,
                this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
        LocalTime startTime = LocalTime.of(5, 0);
        User user = this.userService.findUser(planModel.getUserId());
        if (activity.getTimePreference().contains("Morning")) {
            int morningStartHour = 5;
            if (user.getDayStartHour() > morningStartHour) {
                morningStartHour = user.getDayStartHour();
            }
            startTime = LocalTime.of(morningStartHour, 0);
        } else if (activity.getTimePreference().contains("Afternoon")) {
            startTime = LocalTime.of(12, 0);
        } else if (activity.getTimePreference().contains("Evening")) {
            startTime = LocalTime.of(17, 1);
        }

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = start.plusMinutes(activity.getDuration());
        String title = activity.getIcon() + " " + activity.getName();

        ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                activity.getColor().getSecondaryColor());
        this.failedInstances.add(model);
    }

    private void createConcreteDayInstanceAlt(PlanGenerationModel planModel, Activity activity, String day) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        LocalDate startDate = LocalDate.of(yearStart,
                this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
        LocalTime startTime = LocalTime.of(0, 0);
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = start.plusMinutes(activity.getDuration());
        String title = activity.getIcon() + " " + activity.getName();

        ActivityInstanceModel model = new ActivityInstanceModel(title, planModel.getUserId(), start, end,
                activity.getColor().getSecondaryColor());
        this.failedInstances.add(model);
    }

    private void generateFailed() {
        for (ActivityInstanceModel model : this.failedInstances) {
            this.activityInstanceService.createActivityInstance(model);
        }
        this.failedInstances = new ArrayList<>();
    }

    private LocalDate findRandomStartDate(PlanGenerationModel planModel) {
        LocalDateTime weekStart = planModel.getStart();
        int yearStart = weekStart.getYear();
        Collections.shuffle(WEEK);
        String day = WEEK.get(0);
        return LocalDate.of(yearStart, this.getDayAndMonth(weekStart, day)[1], this.getDayAndMonth(weekStart, day)[0]);
    }


    private List<Activity> getConcreteDaysAndTimeActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getDayPreference().size() != 0 &&
                a.getConcreteTime() != null)
                .collect(Collectors.toList());
    }

    private List<Activity> getConcreteDaysAndTimePreferenceActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getDayPreference().size() != 0 &&
                a.getTimePreference().size() != 0 &&
                !a.getTimePreference().contains(TIME_PREFERENCE_CONCRETE))
                .collect(Collectors.toList());
    }

    private List<Activity> getConcreteDaysActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getDayPreference().size() != 0 &&
                a.getTimePreference().size() == 0)
                .collect(Collectors.toList());
    }

    private List<Activity> getConcreteTimeActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getConcreteTime() != null &&
                a.getDayPreference().size() == 0)
                .collect(Collectors.toList());
    }

    private List<Activity> getTimePreferenceActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getTimePreference().size() != 0 &&
                !a.getTimePreference().contains(TIME_PREFERENCE_CONCRETE) &&
                a.getDayPreference().size() == 0)
                .collect(Collectors.toList());
    }

    private List<Activity> getNoPreferencesActivities(List<Activity> activities) {
        return activities.stream().filter((a) ->
                a.getDayPreference().size() == 0 &&
                a.getTimePreference().size() == 0)
                .collect(Collectors.toList());
    }

    private int[] split(int x, int n) {
        int[] parts = new int[n];
        if (x < n) {
            throw new Error("x can't be split into n parts");
        } else if (x % n == 0) {
            for (int i=0; i<n; i++) {
                parts[i] = (x/n);
            }
        } else {
            int zp = n - (x % n);
            int pp = x/n;
            for (int i=0; i<n; i++) {
                if (i >= zp) {
                    parts[i] = (pp + 1);
                } else {
                    parts[i] = (pp);
                }
            }
        }
        return parts;
    }

}
