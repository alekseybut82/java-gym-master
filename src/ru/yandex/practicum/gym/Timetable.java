package ru.yandex.practicum.gym;

import java.util.*;


public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        var dayOfWeekForTrain = trainingSession.getDayOfWeek();
        var timeOfDayForTrain = trainingSession.getTimeOfDay();

        timetable.computeIfAbsent(dayOfWeekForTrain, k -> new TreeMap<>())
            .computeIfAbsent(timeOfDayForTrain, k -> new ArrayList<>())
            .add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>>  getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> trainingSessionsForThisDay = timetable.get(dayOfWeek);
        return trainingSessionsForThisDay != null ? trainingSessionsForThisDay : new TreeMap<>();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> trainingSessionsForThisDay = timetable.get(dayOfWeek);
        if (trainingSessionsForThisDay == null) return Collections.emptyList();

        List<TrainingSession> trainingSessionsForThisTime = trainingSessionsForThisDay.get(timeOfDay);
        return trainingSessionsForThisTime != null ? trainingSessionsForThisTime : Collections.emptyList();
    }

    public List<Map.Entry<Coach, Integer>> getCountByCoaches() {
        Map<Coach, Integer> countByCoachTraining = new HashMap<>();
        for(Map<TimeOfDay, List<TrainingSession>> trainingSessionsForDay: timetable.values()) {
            for(List<TrainingSession> trainingSessionsForTime: trainingSessionsForDay.values()) {
                for(TrainingSession trainingSession: trainingSessionsForTime) {
                    Coach coach = trainingSession.getCoach();
                    countByCoachTraining.put(coach, countByCoachTraining.getOrDefault(coach,0) + 1);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> listOfCoachAndCountTraining = new ArrayList<>(countByCoachTraining.entrySet());

        Collections.sort(listOfCoachAndCountTraining, new Comparator<Map.Entry<Coach, Integer>>() {
            @Override
            public int compare(Map.Entry<Coach, Integer> e1, Map.Entry<Coach, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue()); // убывание
            }
        });

        return listOfCoachAndCountTraining;
    }
}
