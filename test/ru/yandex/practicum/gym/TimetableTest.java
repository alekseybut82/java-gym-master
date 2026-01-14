package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimetableTest {

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(), 1);
        Assertions.assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size(), 0);
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(), 1);
        Assertions.assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size(), 0);
        Assertions.assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size(), 2);
        //приведение без проверки принадлежности к типу TreeMap
        Assertions.assertEquals(((TreeMap<TimeOfDay, List<TrainingSession>>) timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)).firstKey()
                                , new TimeOfDay(13, 0));
        Assertions.assertEquals(((TreeMap<TimeOfDay, List<TrainingSession>>) timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)).lastKey()
                                , new TimeOfDay(20, 0));


    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertEquals(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size(), 1);
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).isEmpty());
    }

    @Test
    public void testGetCountByCoachesWithSingleCoach() {

        Timetable timetable = new Timetable();

        TrainingSession trainingSession = new TrainingSession(
                new Group("Акробатика для детей", Age.CHILD, 60)
                , new Coach("Васильев", "Николай", "Сергеевич")
                , DayOfWeek.MONDAY
                , new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(trainingSession);
        List<Map.Entry<Coach, Integer>> listOfCoachAndCountTraining = timetable.getCountByCoaches();

        Assertions.assertEquals(listOfCoachAndCountTraining.size(), 1);
        Assertions.assertEquals(listOfCoachAndCountTraining.get(0).getValue(), 1);
        Assertions.assertEquals(listOfCoachAndCountTraining.get(0).getKey(), new Coach("Васильев", "Николай", "Сергеевич"));
    }

    @Test
    public void testGetCountByCoachesWithMultiCoach() {

        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Coach coach3 = new Coach("Егоров", "Егор", "Егорович");

        timetable.addNewTrainingSession(new TrainingSession(
                group
                , coach1
                , DayOfWeek.MONDAY
                , timeOfDay)
        );

        for(DayOfWeek dayOfWeek: List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)) {
            timetable.addNewTrainingSession(new TrainingSession(
                    group
                    , coach2
                    , dayOfWeek
                    , timeOfDay)
            );
        }

        for(DayOfWeek dayOfWeek: List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)) {
            timetable.addNewTrainingSession(new TrainingSession(
                    group
                    , coach3
                    , dayOfWeek
                    , timeOfDay)
            );
        }

        List<Map.Entry<Coach, Integer>> listOfCoachAndCountTraining = timetable.getCountByCoaches();

        Assertions.assertEquals(listOfCoachAndCountTraining.size(), 3);
        Assertions.assertEquals(listOfCoachAndCountTraining.get(0).getValue(), 3);
        Assertions.assertEquals(listOfCoachAndCountTraining.get(0).getKey(), coach3);
    }

}
