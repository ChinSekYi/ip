package carly.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.IntStream;

import carly.exception.CarlyException;
import carly.exception.CarlyIncorrectIndexFormat;
import carly.exception.CarlyIndexOutOfBoundsException;
import carly.exception.CarlyMissingDateTimeException;
import carly.tasks.Deadline;
import carly.tasks.Event;
import carly.tasks.Task;
import carly.tasks.Todo;
import carly.ui.Ui;

/** Represents a list of tasks. Handle a collection of {@link Task} objects. */
public class TaskList {

    /** List of Task objects. */
    private final ArrayList<Task> taskList;

    /** TWO_INDENT used in displaying tasklist size. */
    private final String ONE_INDENT = "    ";

    /** TWO_INDENT used in displaying output messages. */
    private final String TWO_INDENT = "        ";

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Marks the specified task as done.
     *
     * @param taskNumString The task number to be marked as done.
     * @throws CarlyException If the task number format is incorrect or out of bounds.
     */
    public void mark(String taskNumString) throws CarlyException {
        Integer taskNum = null;
        try {
            taskNum = Integer.parseInt(taskNumString);
            Task t = this.get(taskNum - 1);
            Task updatedT = t.markAsDone();
            this.taskList.set(taskNum - 1, updatedT);
            String msg = "Okiee! I've marked this task as done:\n" + TWO_INDENT + t;
            Ui.printOutput(msg);
        } catch (NumberFormatException e) {
            throw new CarlyIncorrectIndexFormat();
        } catch (IndexOutOfBoundsException e) {
            throw new CarlyIndexOutOfBoundsException(taskNum, this.getSize());
        }
    }

    /**
     * Unmarks the specified task as not done yet.
     *
     * @param taskNumString The task number to be unmarked.
     * @throws CarlyException If the task number is out of bounds.
     */
    public void unmark(String taskNumString) throws CarlyException {
        Integer taskNum = null;
        try {
            taskNum = Integer.parseInt(taskNumString);
            Task t = this.get(taskNum - 1);
            Task updatedT = t.unmarkAsDone();
            this.taskList.set(taskNum - 1, updatedT);
            String msg = "Okiee! I've marked this task as not done yet:\n" + TWO_INDENT + t;
            Ui.printOutput(msg);
        } catch (IndexOutOfBoundsException e) {
            throw new CarlyIndexOutOfBoundsException(taskNum, this.getSize());
        }
    }

    /**
     * Deletes the specified task from the list.
     *
     * @param taskNumString The task number to be deleted.
     * @throws CarlyException If the task number format is incorrect or out of bounds.
     */
    public void delete(String taskNumString) throws CarlyException {
        Integer taskNum = null;
        try {
            taskNum = Integer.parseInt(taskNumString);
            Task t = this.get(taskNum - 1);
            this.taskList.remove(taskNum - 1);
            String msg = "Okay, I've removed this task:\n" + TWO_INDENT + t;
            Ui.printOutput(msg + "\n" + taskListSize());
        } catch (NumberFormatException e) {
            throw new CarlyIncorrectIndexFormat();
        } catch (IndexOutOfBoundsException e) {
            throw new CarlyIndexOutOfBoundsException(taskNum, this.getSize());
        }
    }

    public void find(String word) throws CarlyException {
        TaskList filteredList = new TaskList();
        for (Task t: this.taskList) {
            if (t.getDescription().contains(word)) {
                filteredList.taskList.add(t);
            }
        }
        filteredList.printTaskList("Here are the matching returns for your list:");
    }

    /**
     * Adds a new ToDo task to the list.
     *
     * @param taskDescription The description of the task.
     * @throws CarlyException If there are issues with the task description.
     */
    public void addToDo(String taskDescription) throws CarlyException {
        Todo t = new Todo(taskDescription);
        this.taskList.add(t);
        String msg = "Got it. I've added this task:\n" + TWO_INDENT + t;
        Ui.printOutput(msg + "\n" + taskListSize());
    }

    /**
     * Adds a new Deadline task to the list.
     *
     * @param taskDescription The description and due date of the task, formatted as "description /by dueDate".
     * @throws CarlyMissingDateTimeException If the task description or due date is missing.
     */
    public void addDeadLine(String taskDescription) throws CarlyException {
        try {
            String[] taskDueDate = taskDescription.split(" /by ");
            String task = taskDueDate[0];
            String dueDate = taskDueDate[1];

            Deadline t = new Deadline(task, dueDate);
            this.taskList.add(t);

            String msg = "Got it. I've added this task:\n" + TWO_INDENT + t;
            Ui.printOutput(msg + "\n" + taskListSize());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CarlyMissingDateTimeException("task description or \"/by\" command");
        }
    }

    /**
     * Adds a new Event task to the list.
     *
     * @param taskDescription The description, start time, and end time of the task.
     * @throws CarlyMissingDateTimeException If the task description, start time, or end time is missing.
     */
    public void addEvent(String taskDescription) throws CarlyMissingDateTimeException {
        try {
            String[] taskTimeParts = taskDescription.split(" /from ");
            String task = taskTimeParts[0];
            String timeParts = taskTimeParts[1];
            String[] startEndTimeParts = timeParts.split(" /to ");
            String startTime = startEndTimeParts[0];
            String endTime = startEndTimeParts[1];

            Event t = new Event(task, startTime, endTime);
            this.taskList.add(t);
            String msg = "Got it. I've added this task:\n" + TWO_INDENT + t;
            Ui.printOutput(msg + "\n" + taskListSize());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CarlyMissingDateTimeException("\"/from\" or \"/to\" command");
        }
    }

    /** Retrieves a task by index. */
    public Task get(Integer index) {
        return this.taskList.get(index);
    }

    /** Gets the number of tasks in the list. */
    public Integer getSize() {
        return this.taskList.size();
    }

    /** Prints the current size of the task list. */
    public String taskListSize() {
        return ONE_INDENT + "Now you have " + this.getSize() + " tasks in the list.";
    }

    /** Prints list for Command FIND*/
    public void printTaskList(String msg) {
        if (this.taskList.isEmpty()) {
            Ui.printOutput("Oh no. What you're looking for is not in the list :(");
        } else {
            Ui.printOutputTopLine(msg);
            IntStream.range(0, this.getSize())
                    .forEach(i -> Ui.printOutputNoLine(
                            MessageFormat.format("{0}.{1}", i + 1, this.get(i).toString())));
            Ui.printOutputBottomLine();

        }
    }

    /** Prints list for Command LIST*/
    public void printTaskList() {
        if (this.taskList.isEmpty()) {
            Ui.printOutput("There's nothing in your list yet.");
        } else {
            String msg = "Here are the tasks in your list:";
            this.printTaskList(msg);
        }
    }

    /** Generates a string representation of all tasks in the list to be saved in txt file. */
    public String getFormattedTaskList() {
        if (this.taskList.isEmpty()) {
            return "Nothing in your list";
        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < this.getSize(); i++) {
                Task task = this.get(i);
                String taskType = task instanceof Todo ? "T" : task instanceof Deadline ? "D" : "E";
                String isDone = task.getIsDone() ? "1" : "0";
                String taskDescription = task.getDescription();

                String details = "";
                if (task instanceof Deadline) {
                    details = " | " + ((Deadline) task).getDueDate();
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    details = " | " + event.getStartTime() + " to " + event.getEndTime();
                }

                sb.append(String.format("%s | %s | %s%s\n", taskType, isDone, taskDescription, details));
            }

            return sb.toString().trim();
        }
    }


}
